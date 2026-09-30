package co.secretonline.tinyflowers.block;

import co.secretonline.tinyflowers.block.entity.TinyFlowerPotBlockEntity;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.helper.TransformHelper;
import co.secretonline.tinyflowers.mixin.block.FlowerPotBlockAccessor;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TinyFlowerPotBlock extends BaseEntityBlock {
	public static final MapCodec<TinyFlowerPotBlock> CODEC = simpleCodec(TinyFlowerPotBlock::new);
	private static final VoxelShape SHAPE = Shapes.box(0.3125, 0, 0.3125, 0.6875, 0.375, 0.6875);

	protected TinyFlowerPotBlock(Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
		return new TinyFlowerPotBlockEntity(blockPos, blockState);
	}

	@Override
	protected @NotNull VoxelShape getShape(final @NotNull BlockState state, final @NotNull BlockGetter level, final @NotNull BlockPos pos, final @NotNull CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected @NotNull ItemInteractionResult useItemOn(
		final ItemStack itemStack,
		final @NotNull BlockState state,
		final @NotNull Level level,
		final @NotNull BlockPos pos,
		final @NotNull Player player,
		final @NotNull InteractionHand hand,
		final @NotNull BlockHitResult hitResult
	) {
		// This needs to handle replacement with a normal item transforming back into a real flower pot, as well
		// as Tiny Flowers which will swap it out.
		BlockState newContents;

		// Try normal flower pot contents first, with the map that Vanilla builds during block registration.
		Map<Block,Block> pottedByContent = FlowerPotBlockAccessor.tinyFlowers$getPottedByContent();
		newContents = (itemStack.getItem() instanceof BlockItem blockItem
			? pottedByContent.getOrDefault(blockItem.getBlock(), Blocks.AIR)
			: Blocks.AIR)
			.defaultBlockState();

		if (newContents.isAir()) {
			// No match in vanilla, check Tiny Flowers
			TinyFlowerData flowerData = TinyFlowerData.findByItemStack(level.registryAccess(), itemStack);
			if (flowerData == null || !flowerData.canBePotted()) {
				// No match for tiny flowers either, do vanilla's fallback.
				return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
			}

			if (!(level.getBlockEntity(pos) instanceof TinyFlowerPotBlockEntity potBlockEntity)) {
				return ItemInteractionResult.CONSUME;
			}

			if (potBlockEntity.getFlower() != null) {
				// Pot already has a flower, so do nothing
				return ItemInteractionResult.CONSUME;
			}

			potBlockEntity.setFlower(flowerData.id());
		} else {
			if (!(level.getBlockEntity(pos) instanceof TinyFlowerPotBlockEntity potBlockEntity)) {
				return ItemInteractionResult.CONSUME;
			}

			if (potBlockEntity.getFlower() != null) {
				// Pot already has a flower, so do nothing
				return ItemInteractionResult.CONSUME;
			}

			// Matched an actual potted plant type, ensu
			level.setBlockAndUpdate(pos, newContents);
		}

		level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
		player.awardStat(Stats.POT_FLOWER);
		itemStack.consume(1, player);
		return ItemInteractionResult.SUCCESS;
	}

	@Override
	protected @NotNull InteractionResult useWithoutItem(
		final @NotNull BlockState state, final Level level, final @NotNull BlockPos pos, final @NotNull Player player, final @NotNull BlockHitResult hitResult
	) {
		if (!(level.getBlockEntity(pos) instanceof TinyFlowerPotBlockEntity potBlockEntity)) {
			return InteractionResult.CONSUME;
		}

		ResourceLocation flowerId = potBlockEntity.getFlower();
		if (flowerId == null) {
			// No flower in pot, so do nothing
			return InteractionResult.CONSUME;
		}

		TinyFlowerData flowerData = TinyFlowerData.findById(level.registryAccess(), flowerId);
		if (flowerData == null) {
			// Unregistered flower in pot, so do nothing
			return InteractionResult.CONSUME;
		}

		ItemStack plant = flowerData.getItemStack(1);
		if (!player.addItem(plant)) {
			player.drop(plant, false);
		}

		// Revert to default flower pot when item is removed
		level.setBlockAndUpdate(pos, Blocks.FLOWER_POT.defaultBlockState());
		level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
		return InteractionResult.SUCCESS;
	}

	@Override
	public @NotNull ItemStack getCloneItemStack(LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state) {
		if (!(level.getBlockEntity(pos) instanceof TinyFlowerPotBlockEntity potBlockEntity)) {
			return new ItemStack(Blocks.FLOWER_POT);
		}

		ResourceLocation flowerId = potBlockEntity.getFlower();
		if (flowerId == null) {
			return new ItemStack(Blocks.FLOWER_POT);
		}

		TinyFlowerData flowerData = TinyFlowerData.findById(level.registryAccess(), flowerId);
		if (flowerData == null) {
			return new ItemStack(Blocks.FLOWER_POT);
		}

		return flowerData.getItemStack(1);
	}

	@Override
	protected @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
		return direction == Direction.DOWN && !state.canSurvive(level, pos)
			? Blocks.AIR.defaultBlockState()
			: super.updateShape(state, direction, neighborState, level, pos, neighborPos);
	}

	@Override
	protected boolean isPathfindable(final @NotNull BlockState state, final @NotNull PathComputationType type) {
		return false;
	}

	@Override
	protected void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
		TransformHelper.doTransformTick(state, level, pos, random, true, false);

		super.randomTick(state, level, pos, random);
	}

	@Override
	protected @NotNull List<ItemStack> getDrops(@NotNull BlockState blockState, LootParams.Builder builder) {
		BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
		if (!(blockEntity instanceof TinyFlowerPotBlockEntity potBlockEntity)) {
			// If there's no block entity, fall back to default (probably nothing)
			return super.getDrops(blockState, builder);
		}

		ResourceLocation flowerId = potBlockEntity.getFlower();
		RegistryAccess registryAccess = builder.getLevel().registryAccess();

		List<ItemStack> itemStacks = new ArrayList<>();

		TinyFlowerData flowerData = TinyFlowerData.findById(registryAccess, flowerId);
		if (flowerData != null) {
			itemStacks.add(flowerData.getItemStack(1));
		}

		return itemStacks;
	}

	@Override
	protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

}
