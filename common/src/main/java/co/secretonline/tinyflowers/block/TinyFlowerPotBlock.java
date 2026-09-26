package co.secretonline.tinyflowers.block;

import co.secretonline.tinyflowers.block.entity.TinyFlowerPotBlockEntity;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.helper.TransformHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Prediction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TriState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.attribute.EnvironmentAttributes;
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
import net.minecraft.world.level.block.EyeblossomBlock;
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
	protected VoxelShape getShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected InteractionResult useItemOn(
		final ItemStack itemStack,
		final BlockState state,
		final Level level,
		final BlockPos pos,
		final Player player,
		final InteractionHand hand,
		final BlockHitResult hitResult
	) {
		BlockState newContents = (itemStack.getItem() instanceof BlockItem blockItem
			? (Block)POTTED_BY_CONTENT.getOrDefault(blockItem.getBlock(), Blocks.AIR)
			: Blocks.AIR)
			.defaultBlockState();
		if (newContents.isAir()) {
			return InteractionResult.TRY_WITH_EMPTY_HAND;
		}

		if (!this.isEmpty()) {
			return InteractionResult.CONSUME;
		}

		level.setBlockAndUpdate(pos, newContents);
		level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
		player.awardStat(Stats.POT_FLOWER);
		itemStack.consume(1, player);
		return InteractionResult.SUCCESS;
	}

	@Override
	protected InteractionResult useWithoutItem(
		final BlockState state, final Level level, final BlockPos pos, final Player player, final BlockHitResult hitResult
	) {
		if (this.isEmpty()) {
			return InteractionResult.CONSUME;
		}

		ItemStack plant = new ItemStack(this.potted);
		if (!player.addItem(plant)) {
			player.drop(plant, false, Prediction.PREDICTED);
		}

		level.setBlockAndUpdate(pos, Blocks.FLOWER_POT.defaultBlockState());
		level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
		return InteractionResult.SUCCESS;
	}

	@Override
	protected ItemStack getCloneItemStack(final LevelReader level, final BlockPos pos, final BlockState state, final boolean includeData) {
		return this.isEmpty() ? super.getCloneItemStack(level, pos, state, includeData) : new ItemStack(this.potted);
	}

	private boolean isEmpty() {
		return this.potted == Blocks.AIR;
	}

	@Override
	protected BlockState updateShape(
		final BlockState state,
		final LevelReader level,
		final ScheduledTickAccess ticks,
		final BlockPos pos,
		final Direction directionToNeighbour,
		final BlockPos neighbourPos,
		final BlockState neighbourState,
		final RandomSource random
	) {
		return directionToNeighbour == Direction.DOWN && !state.canSurvive(level, pos)
			? Blocks.AIR.defaultBlockState()
			: super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
	}

	@Override
	protected boolean isPathfindable(final BlockState state, final PathComputationType type) {
		return false;
	}

	@Override
	protected boolean isRandomlyTicking(final BlockState state) {

		return state.is(Blocks.POTTED_OPEN_EYEBLOSSOM) || state.is(Blocks.POTTED_CLOSED_EYEBLOSSOM);
	}

	@Override
	protected void randomTick(final BlockState state, final ServerLevel level, final BlockPos pos, final RandomSource random) {
		TransformHelper.doTransformTick(state, level, pos, random, true);

		super.randomTick(state, level, pos, random);
	}

	@Override
	protected void tick(@NonNull BlockState state, @NonNull ServerLevel world, @NonNull BlockPos pos, @NonNull RandomSource random) {
		TransformHelper.doTransformTick(state, world, pos, random, false);

		super.tick(state, world, pos, random);
	}

	public BlockState opposite(final BlockState state) {
		if (state.is(Blocks.POTTED_OPEN_EYEBLOSSOM)) {
			return Blocks.POTTED_CLOSED_EYEBLOSSOM.defaultBlockState();
		} else {
			return state.is(Blocks.POTTED_CLOSED_EYEBLOSSOM) ? Blocks.POTTED_OPEN_EYEBLOSSOM.defaultBlockState() : state;
		}
	}

	@Override
	protected @NonNull List<ItemStack> getDrops(@NonNull BlockState blockState, LootParams.Builder builder) {
		BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
		if (!(blockEntity instanceof TinyFlowerPotBlockEntity potBlockEntity)) {
			// If there's no block entity, fall back to default (probably nothing)
			return super.getDrops(blockState, builder);
		}

		List<Identifier> flowerIds = gardenBlockEntity.getFlowers();
		RegistryAccess registryAccess = builder.getLevel().registryAccess();

		List<ItemStack> itemStacks = new ArrayList<>();
		for (Identifier flowerId : flowerIds) {
			TinyFlowerData flowerData = TinyFlowerData.findById(registryAccess, flowerId);
			if (flowerData != null) {
				itemStacks.add(flowerData.getItemStack(1));
			}
		}

		return itemStacks;
	}
}
