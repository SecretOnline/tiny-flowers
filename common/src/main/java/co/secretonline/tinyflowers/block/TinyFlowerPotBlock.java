package co.secretonline.tinyflowers.block;

import co.secretonline.tinyflowers.block.entity.TinyFlowerPotBlockEntity;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.helper.TransformHelper;
import co.secretonline.tinyflowers.mixin.block.FlowerPotBlockAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.Prediction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
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
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TinyFlowerPotBlock extends BaseEntityBlock {
	private static final VoxelShape SHAPE = Block.column(6.0, 0.0, 6.0);

	protected TinyFlowerPotBlock(Properties properties) {
		super(properties);
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos blockPos, @NonNull BlockState blockState) {
		return new TinyFlowerPotBlockEntity(blockPos, blockState);
	}

	@Override
	protected @NonNull VoxelShape getShape(final @NonNull BlockState state, final @NonNull BlockGetter level, final @NonNull BlockPos pos, final @NonNull CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected @NonNull InteractionResult useItemOn(
		final ItemStack itemStack,
		final @NonNull BlockState state,
		final @NonNull Level level,
		final @NonNull BlockPos pos,
		final @NonNull Player player,
		final @NonNull InteractionHand hand,
		final @NonNull BlockHitResult hitResult
	) {
		// This needs to handle replacement with a normal item transforming back into a real flower pot, as well
		// as Tiny Flowers which will swap it out.
		BlockState newContents;

		// Try normal flower pot contents first, with the map that Vanilla builds during block registration.
		Map<Block,Block> pottedByContent = FlowerPotBlockAccessor.getPottedByContent();
		newContents = (itemStack.getItem() instanceof BlockItem blockItem
			? pottedByContent.getOrDefault(blockItem.getBlock(), Blocks.AIR)
			: Blocks.AIR)
			.defaultBlockState();

		if (newContents.isAir()) {
			// No match in vanilla, check Tiny Flowers
			TinyFlowerData flowerData = TinyFlowerData.findByItemStack(level.registryAccess(), itemStack);
			if (flowerData == null || !flowerData.canBePotted()) {
				// No match for tiny flowers either, do vanilla's fallback.
				return InteractionResult.TRY_WITH_EMPTY_HAND;
			}

			if (!(level.getBlockEntity(pos) instanceof TinyFlowerPotBlockEntity potBlockEntity)) {
				return InteractionResult.CONSUME;
			}

			potBlockEntity.setFlower(flowerData.id());
		} else {
			// Matched an actual potted plant type, replace block with that.
			level.setBlockAndUpdate(pos, newContents);
		}

		level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
		player.awardStat(Stats.POT_FLOWER);
		itemStack.consume(1, player);
		return InteractionResult.SUCCESS;
	}

	@Override
	protected @NonNull InteractionResult useWithoutItem(
		final @NonNull BlockState state, final @NonNull Level level, final @NonNull BlockPos pos, final @NonNull Player player, final @NonNull BlockHitResult hitResult
	) {
		if (!(level.getBlockEntity(pos) instanceof TinyFlowerPotBlockEntity potBlockEntity)) {
			return InteractionResult.CONSUME;
		}

		Identifier flowerId = potBlockEntity.getFlower();
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
			player.drop(plant, false, Prediction.PREDICTED);
		}

		// Revert to default flower pot when item is removed
		level.setBlockAndUpdate(pos, Blocks.FLOWER_POT.defaultBlockState());
		level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
		return InteractionResult.SUCCESS;
	}

	@Override
	protected @NonNull ItemStack getCloneItemStack(final @NonNull LevelReader level, final @NonNull BlockPos pos, final @NonNull BlockState state, final boolean includeData) {
		if (!(level.getBlockEntity(pos) instanceof TinyFlowerPotBlockEntity potBlockEntity)) {
			return new ItemStack(Blocks.FLOWER_POT);
		}

		Identifier flowerId = potBlockEntity.getFlower();
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
	protected @NonNull BlockState updateShape(
		final @NonNull BlockState state,
		final @NonNull LevelReader level,
		final @NonNull ScheduledTickAccess ticks,
		final @NonNull BlockPos pos,
		final @NonNull Direction directionToNeighbour,
		final @NonNull BlockPos neighbourPos,
		final @NonNull BlockState neighbourState,
		final @NonNull RandomSource random
	) {
		return directionToNeighbour == Direction.DOWN && !state.canSurvive(level, pos)
			? Blocks.AIR.defaultBlockState()
			: super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
	}

	@Override
	protected boolean isPathfindable(final @NonNull BlockState state, final @NonNull PathComputationType type) {
		return false;
	}

	@Override
	protected void randomTick(final @NonNull BlockState state, final @NonNull ServerLevel level, final @NonNull BlockPos pos, final @NonNull RandomSource random) {
		TransformHelper.doTransformTick(state, level, pos, random, true, false);

		super.randomTick(state, level, pos, random);
	}

	@Override
	protected @NonNull List<ItemStack> getDrops(@NonNull BlockState blockState, LootParams.Builder builder) {
		BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
		if (!(blockEntity instanceof TinyFlowerPotBlockEntity potBlockEntity)) {
			// If there's no block entity, fall back to default (probably nothing)
			return super.getDrops(blockState, builder);
		}

		Identifier flowerId = potBlockEntity.getFlower();
		RegistryAccess registryAccess = builder.getLevel().registryAccess();

		List<ItemStack> itemStacks = new ArrayList<>();

		TinyFlowerData flowerData = TinyFlowerData.findById(registryAccess, flowerId);
		if (flowerData != null) {
			itemStacks.add(flowerData.getItemStack(1));
		}

		return itemStacks;
	}
}
