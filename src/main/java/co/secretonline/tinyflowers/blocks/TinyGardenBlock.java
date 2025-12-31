package co.secretonline.tinyflowers.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.components.GardenContentsComponent;
import co.secretonline.tinyflowers.components.ModComponents;
import co.secretonline.tinyflowers.components.TinyFlowerComponent;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.helper.EyeblossomHelper;
import co.secretonline.tinyflowers.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SegmentableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TinyGardenBlock extends BaseEntityBlock implements BonemealableBlock {
	public static final MapCodec<TinyGardenBlock> CODEC = simpleCodec(TinyGardenBlock::new);
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

	private static final BiFunction<Direction, Integer, VoxelShape> FACING_AND_AMOUNT_TO_SHAPE = Util.memoize(
			(BiFunction<Direction, Integer, VoxelShape>) ((facing, bitmap) -> {
				if (bitmap == 0) {
					return Block.box(0.0, 0.0, 0.0, 16.0, 3.0, 16.0);
				}

				VoxelShape[] voxelShapes = new VoxelShape[] {
						Block.box(8.0, 0.0, 8.0, 16.0, 3.0, 16.0),
						Block.box(8.0, 0.0, 0.0, 16.0, 3.0, 8.0),
						Block.box(0.0, 0.0, 0.0, 8.0, 3.0, 8.0),
						Block.box(0.0, 0.0, 8.0, 8.0, 3.0, 16.0)
				};
				VoxelShape voxelShape = Shapes.empty();

				for (int i = 0; i < TinyGardenBlockEntity.NUM_SLOTS; i++) {
					if ((bitmap & (1 << i)) > 0) {
						int j = Math.floorMod(i - facing.get2DDataValue(), 4);
						voxelShape = Shapes.or(voxelShape, voxelShapes[j]);
					}
				}

				return voxelShape.singleEncompassing();
			}));

	public TinyGardenBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(FACING, Direction.NORTH));
	}

	@Override
	protected boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
		if (!(levelReader.getBlockEntity(blockPos) instanceof TinyGardenBlockEntity gardenBlockEntity)) {
			// If there's no block entity at this position, that means we're in the middle
			// of placing a block here. Let it pass for now, there will be another check
			// later.
			return true;
		}

		Block belowBlock = levelReader.getBlockState(blockPos.below()).getBlock();
		return gardenBlockEntity.canSurviveOn(belowBlock, levelReader.registryAccess());
	}

	@Override
	protected BlockState updateShape(
			BlockState blockState,
			LevelReader levelReader,
			ScheduledTickAccess scheduledTickAccess,
			BlockPos blockPos,
			Direction direction,
			BlockPos blockPos2,
			BlockState blockState2,
			RandomSource randomSource) {
		return !blockState.canSurvive(levelReader, blockPos)
				? Blocks.AIR.defaultBlockState()
				: super.updateShape(blockState, levelReader, scheduledTickAccess, blockPos, direction, blockPos2, blockState2,
						randomSource);
	}

	@Override
	protected List<ItemStack> getDrops(BlockState blockState, Builder builder) {
		BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
		if (!(blockEntity instanceof TinyGardenBlockEntity gardenBlockEntity)) {
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

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
		return !context.isSecondaryUseActive()
				&& (TinyFlowerData.findByItemStack(context.getLevel().registryAccess(), context.getItemInHand()) != null)
				&& hasFreeSpace(context.getLevel(), context.getClickedPos())
						? true
						: super.canBeReplaced(state, context);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return (VoxelShape) FACING_AND_AMOUNT_TO_SHAPE.apply((Direction) state.getValue(FACING),
				getFlowerBitmap(world, pos));
	}

	@Override
	public @org.jspecify.annotations.Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
		Level level = blockPlaceContext.getLevel();
		BlockPos blockPos = blockPlaceContext.getClickedPos();

		BlockState blockState = level.getBlockState(blockPos);

		Block supportingBlock = level.getBlockState(blockPos.below()).getBlock();

		ItemStack stack = blockPlaceContext.getItemInHand();

		TinyFlowerData flowerData = TinyFlowerData.findByItemStack(level.registryAccess(), stack);
		if (flowerData == null) {
			// The item being placed down is a TinyGardenBlock block item, but doesn't have
			// the tiny_flower component, which happens either if the item doesn't have any
			// components (unusual) or if it has the garden_contents component (normal). If
			// it's the latter, then the Block Entity will handle this so we just have to
			// set the direction. If it's the former, then don't do anything.
			GardenContentsComponent gardenContents = stack.getOrDefault(ModComponents.GARDEN_CONTENTS, null);
			if (gardenContents == null) {
				return blockState;
			}

			if (!gardenContents.canSurviveOn(supportingBlock, level.registryAccess())) {
				return blockState;
			}

			return this.defaultBlockState()
					.setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
		}

		// Ensure the tiny flower type we're placing can be placed on top of the
		// supporting block.
		if (!flowerData.canSurviveOn(supportingBlock)) {
			return blockState;
		}

		if (blockState.is(this)) {
			// Placing a tiny flower on a garden block.
			if (!(level.getBlockEntity(blockPos) instanceof TinyGardenBlockEntity gardenBlockEntity)) {
				// If there's no block entity, don't do anything
				return blockState;
			}

			gardenBlockEntity.addFlower(flowerData.id());

			// Consume item, play sound, and send game event.
			Player player = blockPlaceContext.getPlayer();
			SoundType soundType = blockState.getSoundType();
			level.playSound(player, blockPos, soundType.getPlaceSound(), SoundSource.BLOCKS,
					(soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
			level.gameEvent(GameEvent.BLOCK_PLACE, blockPos, GameEvent.Context.of(player, blockState));
			stack.consume(1, player);

			return blockState;
		} else if (blockState.getBlock() instanceof SegmentableBlock) {
			// Placing a tiny flower on a segmented block.
			// We need to convert the segmented block to a garden block
			// and then add the flower variant to it.
			BlockState newBlockState = ((TinyGardenBlock) ModBlocks.TINY_GARDEN_BLOCK).defaultBlockState()
					.setValue(TinyGardenBlock.FACING, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING));

			TinyFlowerData originalSegmentedData = TinyFlowerData.findByOriginalBlock(level.registryAccess(),
					blockState.getBlock());
			if (originalSegmentedData == null) {
				// The previous block was segmentable, but doesn't have a tiny flower variant
				// registered.
				return blockState;
			}
			if (!originalSegmentedData.canSurviveOn(supportingBlock)) {
				// This only happens if the original segmentable block was on a block that the
				// tiny flower doesn't support.
				return blockState;
			}

			// Since we also need to update the entity, try to update the world now.
			level.setBlockAndUpdate(blockPos, newBlockState);
			if (!(level.getBlockEntity(blockPos) instanceof TinyGardenBlockEntity gardenBlockEntity)) {
				// If there's no block entity, try undo the change
				level.setBlockAndUpdate(blockPos, blockState);
				return blockState;
			}

			gardenBlockEntity.setFromPreviousBlockState(level.registryAccess(), blockState);
			gardenBlockEntity.addFlower(flowerData.id());

			// Consume item, play sound, and send game event.
			Player player = blockPlaceContext.getPlayer();
			SoundType soundType = blockState.getSoundType();
			level.playSound(player, blockPos, soundType.getPlaceSound(), SoundSource.BLOCKS,
					(soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
			level.gameEvent(GameEvent.BLOCK_PLACE, blockPos, GameEvent.Context.of(player, blockState));
			stack.consume(1, player);

			return newBlockState;
		} else {
			// Item is a valid tiny flower block item, but there's no block yet.
			// Place a new garden with the flower variant.
			return this.defaultBlockState()
					.setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
		}
	}

	@Override
	protected void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		EyeblossomHelper.doEyeblossomTick(state, world, pos, random, true);

		super.randomTick(state, world, pos, random);
	}

	@Override
	protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		EyeblossomHelper.doEyeblossomTick(state, world, pos, random, false);

		super.tick(state, world, pos, random);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos pos, BlockState blockState) {
		return level.getBlockEntity(pos) instanceof TinyGardenBlockEntity;
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState blockState) {
		return level.getBlockEntity(pos) instanceof TinyGardenBlockEntity;
	}

	@Override
	public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos pos,
			BlockState blockState) {
		if (!(serverLevel.getBlockEntity(pos) instanceof TinyGardenBlockEntity gardenBlockEntity)) {
			return;
		}

		List<Identifier> flowers = gardenBlockEntity.getFlowers();
		if (flowers.isEmpty()) {
			TinyFlowers.LOGGER.warn("Tried to grow empty space in garden block");
			return;
		}

		Identifier randomId = Util.getRandom(flowers, randomSource);

		// Try add flow to garden, otherwise pop an item out.
		if (!gardenBlockEntity.addFlower(randomId)) {
			// Drop an item based on the variants in the garden. At this stage we can assume
			// that the garden is full.
			ItemStack stack = new ItemStack(
					BuiltInRegistries.ITEM.wrapAsHolder(ModItems.TINY_FLOWER_ITEM),
					4,
					DataComponentPatch.builder()
							.set(ModComponents.TINY_FLOWER, new TinyFlowerComponent(randomId))
							.build());

			popResource(serverLevel, pos, stack);
		}
	}

	protected boolean propagatesSkylightDown(BlockState blockState) {
		return blockState.getFluidState().isEmpty();
	}

	protected boolean isPathfindable(BlockState blockState, PathComputationType pathComputationType) {
		return pathComputationType == PathComputationType.AIR && !this.hasCollision ? true
				: super.isPathfindable(blockState, pathComputationType);
	}

	@Override
	protected ItemStack getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState,
			boolean includeData) {
		if (includeData) {
			return super.getCloneItemStack(levelReader, blockPos, blockState, includeData);
		}

		if (!(levelReader.getBlockEntity(blockPos) instanceof TinyGardenBlockEntity gardenBlockEntity)) {
			// If there's no block entity, don't pick anything.
			return ItemStack.EMPTY;
		}

		List<Identifier> flowers = gardenBlockEntity.getFlowers();
		for (Identifier id : flowers) {
			TinyFlowerData flowerData = TinyFlowerData.findById(levelReader.registryAccess(), id);
			if (flowerData != null) {
				return flowerData.getItemStack(1);
			}
		}

		return ItemStack.EMPTY;
	}

	private static boolean hasFreeSpace(BlockGetter world, BlockPos pos) {
		if (!(world.getBlockEntity(pos) instanceof TinyGardenBlockEntity gardenBlockEntity)) {
			// If there's no block entity, try prevent anything from trying to write to it
			return false;
		}

		return gardenBlockEntity.getFlower(1) == null ||
				gardenBlockEntity.getFlower(2) == null ||
				gardenBlockEntity.getFlower(3) == null ||
				gardenBlockEntity.getFlower(4) == null;
	}

	/**
	 * Since there can be "holes" in the variants, this creates a tiny bitmap of
	 * which positions has flowers. This is useful is for the memoisation during
	 * hitbox creation, as keeping the number of cache entries down for that
	 * is important.
	 */
	private static int getFlowerBitmap(BlockGetter world, BlockPos pos) {
		if (!(world.getBlockEntity(pos) instanceof TinyGardenBlockEntity gardenBlockEntity)) {
			return -1;
		}

		int bitmap = (gardenBlockEntity.getFlower(1) != null ? 1 : 0) +
				(gardenBlockEntity.getFlower(2) != null ? 2 : 0) +
				(gardenBlockEntity.getFlower(3) != null ? 4 : 0) +
				(gardenBlockEntity.getFlower(4) != null ? 8 : 0);

		return bitmap;
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TinyGardenBlockEntity(pos, state);
	}

}
