package co.secretonline.tinyflowers.block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import co.secretonline.tinyflowers.block.entity.TinyGardenBlockEntity;
import co.secretonline.tinyflowers.helper.TransformHelper;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.item.component.GardenContentsComponent;
import co.secretonline.tinyflowers.item.component.ModComponents;
import co.secretonline.tinyflowers.item.component.TinyFlowerComponent;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.Util;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TinyGardenBlock extends BaseEntityBlock implements BonemealableBlock {
	public static final MapCodec<TinyGardenBlock> CODEC = simpleCodec(TinyGardenBlock::new);
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

	private static final BiFunction<Direction, Integer, VoxelShape> FACING_AND_AMOUNT_TO_SHAPE = Util.memoize(
		(facing, bitmap) -> {
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

			for (int i = 0; i < TinyGardenBlockEntity.NUM_TINY_FLOWER_SLOTS; i++) {
				if ((bitmap & (1 << i)) > 0) {
					int j = Math.floorMod(i - facing.get2DDataValue(), 4);
					voxelShape = Shapes.or(voxelShape, voxelShapes[j]);
				}
			}

			return voxelShape.singleEncompassing();
		});

	public TinyGardenBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(FACING, Direction.NORTH));
	}

	@Override
	protected boolean canSurvive(@NotNull BlockState blockState, LevelReader levelReader, @NotNull BlockPos blockPos) {
		if (!(levelReader.getBlockEntity(blockPos) instanceof TinyGardenBlockEntity gardenBlockEntity)) {
			// If there's no block entity at this position, that means we're in the middle
			// of placing a block here. Let it pass for now, there will be another check
			// later.
			return true;
		}

		BlockPos supportingPos = blockPos.below();
		return gardenBlockEntity.canSurviveOn(levelReader, supportingPos);
	}

	@Override
	protected @NotNull BlockState updateShape(BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
		return !state.canSurvive(level, pos)
			? Blocks.AIR.defaultBlockState()
			: super.updateShape(state, direction, neighborState, level, pos, neighborPos);
	}

	@Override
	protected @NotNull List<ItemStack> getDrops(@NotNull BlockState blockState, Builder builder) {
		BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
		if (!(blockEntity instanceof TinyGardenBlockEntity gardenBlockEntity)) {
			// If there's no block entity, fall back to default (probably nothing)
			return super.getDrops(blockState, builder);
		}

		List<ResourceLocation> flowerIds = gardenBlockEntity.getFlowers();
		RegistryAccess registryAccess = builder.getLevel().registryAccess();

		List<ItemStack> itemStacks = new ArrayList<>();
		for (ResourceLocation flowerId : flowerIds) {
			TinyFlowerData flowerData = TinyFlowerData.findById(registryAccess, flowerId);
			if (flowerData != null) {
				itemStacks.add(flowerData.getItemStack(1));
			}
		}

		return itemStacks;
	}

	@Override
	public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	public boolean canBeReplaced(@NotNull BlockState state, BlockPlaceContext context) {
		return !context.isSecondaryUseActive()
			&& (TinyFlowerData.findByItemStack(context.getLevel().registryAccess(), context.getItemInHand()) != null)
			&& hasFreeSpace(context.getLevel(), context.getClickedPos()) || super.canBeReplaced(state, context);
	}

	@Override
	public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		return FACING_AND_AMOUNT_TO_SHAPE.apply(state.getValue(FACING),
				getFlowerBitmap(world, pos));
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
		Level level = blockPlaceContext.getLevel();
		RegistryAccess registryAccess = level.registryAccess();
		BlockPos blockPos = blockPlaceContext.getClickedPos();

		BlockState blockState = level.getBlockState(blockPos);

		BlockPos supportingPos = blockPos.below();

		ItemStack stack = blockPlaceContext.getItemInHand();

		TinyFlowerData flowerData = TinyFlowerData.findByItemStack(registryAccess, stack);
		if (flowerData == null) {
			// The item being placed down is a TinyGardenBlock block item, but doesn't have
			// the tiny_flower component, which happens either if the item doesn't have any
			// components (unusual) or if it has the garden_contents component (normal). If
			// it's the latter, then the Block Entity will handle this so we just have to
			// set the direction. If it's the former, then don't do anything.
			GardenContentsComponent gardenContents = stack.get(ModComponents.GARDEN_CONTENTS.get());
			if (gardenContents == null) {
				return blockState;
			}

			if (!gardenContents.canSurviveOn(level, supportingPos)) {
				return blockState;
			}

			return this.defaultBlockState()
					.setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
		}

		// Ensure the tiny flower type we're placing can be placed on top of the
		// supporting block.
		if (!flowerData.canSurviveOn(level, supportingPos)) {
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
		}

		Block currentBlock = blockState.getBlock();
		if (currentBlock instanceof PinkPetalsBlock segmentableBlock) {
			// Placing a tiny flower on a segmented block.
			// Don't do anything if the segmented block is already full.
			IntegerProperty amountProperty = PinkPetalsBlock.AMOUNT;
			int currentAmount = blockState.getValue(amountProperty);
			if (currentAmount >= PinkPetalsBlock.MAX_FLOWERS) {
				return blockState;
			}

			// We need to convert the segmented block to a garden block
			// and then add the flower variant to it.
			BlockState newBlockState = ModBlocks.TINY_GARDEN_BLOCK.get().defaultBlockState()
					.setValue(TinyGardenBlock.FACING, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING));

			TinyFlowerData originalSegmentedData = TinyFlowerData.findByOriginalBlock(registryAccess, currentBlock);
			if (originalSegmentedData == null) {
				// The previous block was segmentable, but doesn't have a tiny flower variant
				// registered.
				return blockState;
			}
			if (!originalSegmentedData.canSurviveOn(level, supportingPos)) {
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

			gardenBlockEntity.setFromPreviousBlockState(registryAccess, blockState);
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
	protected void randomTick(@NotNull BlockState state, @NotNull ServerLevel world, @NotNull BlockPos pos, @NotNull RandomSource random) {
		TransformHelper.doTransformTick(state, world, pos, random, true, true);

		super.randomTick(state, world, pos, random);
	}

	@Override
	protected void tick(@NotNull BlockState state, @NotNull ServerLevel world, @NotNull BlockPos pos, @NotNull RandomSource random) {
		TransformHelper.doTransformTick(state, world, pos, random, false, true);

		super.tick(state, world, pos, random);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public boolean isBonemealSuccess(Level level, @NotNull RandomSource randomSource, @NotNull BlockPos pos, @NotNull BlockState blockState) {
		return level.getBlockEntity(pos) instanceof TinyGardenBlockEntity;
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader level, @NotNull BlockPos pos, @NotNull BlockState blockState) {
		return level.getBlockEntity(pos) instanceof TinyGardenBlockEntity;
	}

	@Override
	public void performBonemeal(ServerLevel serverLevel, @NotNull RandomSource randomSource, @NotNull BlockPos pos,
															@NotNull BlockState blockState) {
		if (!(serverLevel.getBlockEntity(pos) instanceof TinyGardenBlockEntity gardenBlockEntity)) {
			return;
		}

		List<ResourceLocation> flowers = gardenBlockEntity.getFlowers();
		if (flowers.isEmpty()) {
			TinyFlowers.LOGGER.warn("Tried to grow empty space in garden block");
			return;
		}

		ResourceLocation randomId = Util.getRandom(flowers, randomSource);

		// Try to add flower to garden, otherwise pop an item out.
		if (!gardenBlockEntity.addFlower(randomId)) {
			// Drop an item based on the variants in the garden. At this stage we can assume
			// that the garden is full.
			ItemStack stack = new ItemStack(
				BuiltInRegistries.ITEM.wrapAsHolder(ModItems.TINY_FLOWER_ITEM.get()),
				4,
				DataComponentPatch.builder()
					.set(ModComponents.TINY_FLOWER.get(), new TinyFlowerComponent(randomId))
					.build());

			popResource(serverLevel, pos, stack);
		}
	}

	protected boolean propagatesSkylightDown(BlockState blockState) {
		return blockState.getFluidState().isEmpty();
	}

	protected boolean isPathfindable(@NotNull BlockState blockState, @NotNull PathComputationType pathComputationType) {
		return pathComputationType == PathComputationType.AIR && !this.hasCollision || super.isPathfindable(blockState, pathComputationType);
	}

	@Override
	public @NotNull ItemStack getCloneItemStack(LevelReader levelReader, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
		if (!(levelReader.getBlockEntity(blockPos) instanceof TinyGardenBlockEntity gardenBlockEntity)) {
			// If there's no block entity, don't pick anything.
			return ItemStack.EMPTY;
		}

		List<ResourceLocation> flowers = gardenBlockEntity.getFlowers();
		for (ResourceLocation id : flowers) {
			TinyFlowerData flowerData = TinyFlowerData.findById(levelReader.registryAccess(), id);
			if (flowerData != null) {
				return flowerData.getItemStack(1);
			}
		}

		return ItemStack.EMPTY;
	}

	private static boolean hasFreeSpace(BlockGetter world, BlockPos pos) {
		if (!(world.getBlockEntity(pos) instanceof TinyGardenBlockEntity gardenBlockEntity)) {
			// If there's no block entity, try to prevent anything from trying to write to it
			return false;
		}

		return !gardenBlockEntity.isFull();
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

		return (gardenBlockEntity.getFlower(0) != null ? 1 : 0) +
				(gardenBlockEntity.getFlower(1) != null ? 2 : 0) +
				(gardenBlockEntity.getFlower(2) != null ? 4 : 0) +
				(gardenBlockEntity.getFlower(3) != null ? 8 : 0);
	}

	@Override
	protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}


	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new TinyGardenBlockEntity(pos, state);
	}
}
