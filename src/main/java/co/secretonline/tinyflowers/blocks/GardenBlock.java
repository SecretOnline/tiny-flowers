package co.secretonline.tinyflowers.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import com.mojang.serialization.MapCodec;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.components.ModComponents;
import co.secretonline.tinyflowers.components.TinyFlowersComponent;
import co.secretonline.tinyflowers.helper.EyeblossomHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.Segmented;
import net.minecraft.block.ShapeContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.GameEvent.Emitter;

public class GardenBlock extends PlantBlock implements Fertilizable {
	public static final MapCodec<GardenBlock> CODEC = createCodec(GardenBlock::new);

	public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
	public static final EnumProperty<FlowerVariant> FLOWER_VARIANT_1 = ModBlockProperties.FLOWER_VARIANT_1;
	public static final EnumProperty<FlowerVariant> FLOWER_VARIANT_2 = ModBlockProperties.FLOWER_VARIANT_2;
	public static final EnumProperty<FlowerVariant> FLOWER_VARIANT_3 = ModBlockProperties.FLOWER_VARIANT_3;
	public static final EnumProperty<FlowerVariant> FLOWER_VARIANT_4 = ModBlockProperties.FLOWER_VARIANT_4;

	@SuppressWarnings("unchecked")
	public static final EnumProperty<FlowerVariant>[] FLOWER_VARIANT_PROPERTIES = new EnumProperty[] {
			FLOWER_VARIANT_1, FLOWER_VARIANT_2, FLOWER_VARIANT_3, FLOWER_VARIANT_4 };

	private static final BiFunction<Direction, Integer, VoxelShape> FACING_AND_AMOUNT_TO_SHAPE = Util.memoize(
			(BiFunction<Direction, Integer, VoxelShape>) ((facing, bitmap) -> {
				if (bitmap == 0) {
					return Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 3.0, 16.0);
				}

				VoxelShape[] voxelShapes = new VoxelShape[] {
						Block.createCuboidShape(8.0, 0.0, 8.0, 16.0, 3.0, 16.0),
						Block.createCuboidShape(8.0, 0.0, 0.0, 16.0, 3.0, 8.0),
						Block.createCuboidShape(0.0, 0.0, 0.0, 8.0, 3.0, 8.0),
						Block.createCuboidShape(0.0, 0.0, 8.0, 8.0, 3.0, 16.0)
				};
				VoxelShape voxelShape = VoxelShapes.empty();

				for (int i = 0; i < FLOWER_VARIANT_PROPERTIES.length; i++) {
					if ((bitmap & (1 << i)) > 0) {
						int j = Math.floorMod(i - facing.getHorizontalQuarterTurns(), 4);
						voxelShape = VoxelShapes.union(voxelShape, voxelShapes[j]);
					}
				}

				return voxelShape.asCuboid();
			}));

	public GardenBlock(AbstractBlock.Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState()
				.with(FACING, Direction.NORTH)
				.with(FLOWER_VARIANT_1, FlowerVariant.EMPTY)
				.with(FLOWER_VARIANT_2, FlowerVariant.EMPTY)
				.with(FLOWER_VARIANT_3, FlowerVariant.EMPTY)
				.with(FLOWER_VARIANT_4, FlowerVariant.EMPTY));
	}

	@Override
	public MapCodec<GardenBlock> getCodec() {
		return CODEC;
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}

	@Override
	public boolean canReplace(BlockState state, ItemPlacementContext context) {
		return !context.shouldCancelInteraction() && context.getStack().isIn(ModItemTags.TINY_FLOWERS)
				&& hasFreeSpace(state)
						? true
						: super.canReplace(state, context);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return (VoxelShape) FACING_AND_AMOUNT_TO_SHAPE.apply((Direction) state.get(FACING), getFlowerBitmap(state));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos());

		ItemStack stack = ctx.getStack();
		Item item = stack.getItem();
		FlowerVariant flowerVariant = FlowerVariant.fromItem(item);
		if (flowerVariant.isEmpty()) {
			// The item being placed down is not a flower variant in the enum, but is a
			// GardenBlock block item.
			// Currently, the only known case for this is a pre-formed garden item.
			if (!blockState.isOf(this) && Block.getBlockFromItem(item) == this) {
				// The item is a GardenBlock block item, but not any of the variants.
				// At this point we assume that the item is a pre-formed garden item.

				// Newer items (since v1.2.0 of this mod) have a TinyFlowersComponent that
				// stores which flowers were picked. Older items (before v1.2.0) set this
				// information on the BlockStateComponent instead. We need to check
				// both of these in case the item was creted before the new component was
				// added to the mod.
				if (stack.contains(ModComponents.TINY_FLOWERS_COMPONENT_TYPE)) {
					// The item has a TinyFlowersComponent, so we can use that to get the
					// flower variants.
					TinyFlowersComponent tinyFlowersComponent = stack.get(
							ModComponents.TINY_FLOWERS_COMPONENT_TYPE);

					List<FlowerVariant> flowers = tinyFlowersComponent.flowers();

					// Garden items always have a default component value, so we need to check if
					// there's an older block state component with data.
					if (flowers.isEmpty() && stack.contains(DataComponentTypes.BLOCK_STATE)) {
						return createBlockStateFromBlockStateComponent(ctx, stack.get(DataComponentTypes.BLOCK_STATE));
					}

					return createBlockStateFromTinyFlowersComponent(ctx, tinyFlowersComponent);
				} else if (stack.contains(DataComponentTypes.BLOCK_STATE)) {
					return createBlockStateFromBlockStateComponent(ctx, stack.get(DataComponentTypes.BLOCK_STATE));
				} else {
					// Neither of the components are present, so do nothing.
					return blockState;
				}
			}

			// The item is not a flower variant in the enum, and is not a GardenBlock block
			// item, so there must be something weird going on. For now I've decided to do
			// nothing, and just keep the current block state. It might consume an item, but
			// that's better than a crash.
			return blockState;
		}

		if (blockState.isOf(this)) {
			// Placing a tiny flower on a garden block.
			return addFlowerToBlockState(blockState, flowerVariant);
		} else if (blockState.getBlock() instanceof Segmented) {
			// Placing a tiny flower on a segmented block.
			// We need to convert the segmented block to a garden block
			// and then add the flower variant to it.
			BlockState baseState = getStateFromSegmented(blockState);

			// Add the new type in now that we've converted the block.
			return addFlowerToBlockState(baseState, flowerVariant);
		} else {
			// Item is a valid tiny flower block item, but there's no block yet.
			// Place a new garden with the flower variant.
			return this.getDefaultState()
					.with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
					.with(FLOWER_VARIANT_1, flowerVariant);
		}
	}

	private BlockState createBlockStateFromTinyFlowersComponent(ItemPlacementContext ctx,
			TinyFlowersComponent tinyFlowersComponent) {
		BlockState blockState = this.getDefaultState()
				.with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());

		List<FlowerVariant> flowers = tinyFlowersComponent.flowers();
		for (int i = 0; i < FLOWER_VARIANT_PROPERTIES.length; i++) {
			FlowerVariant variant = flowers.size() > i
					? flowers.get(i)
					: FlowerVariant.EMPTY;

			blockState = blockState.with(FLOWER_VARIANT_PROPERTIES[i], variant);
		}

		return blockState;
	}

	private BlockState createBlockStateFromBlockStateComponent(ItemPlacementContext ctx,
			BlockStateComponent itemBlockState) {
		BlockState blockState = this.getDefaultState()
				.with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());

		for (EnumProperty<FlowerVariant> property : FLOWER_VARIANT_PROPERTIES) {
			FlowerVariant variant = itemBlockState.getValue(property);
			variant = variant == null ? FlowerVariant.EMPTY : variant;

			blockState = blockState.with(property, variant);
		}

		return blockState;
	}

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(FACING, FLOWER_VARIANT_1, FLOWER_VARIANT_2, FLOWER_VARIANT_3, FLOWER_VARIANT_4);
	}

	@Override
	public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		List<FlowerVariant> flowers = getFlowers(state);
		if (flowers.isEmpty()) {
			TinyFlowers.LOGGER.warn("Tried to grow empty space in garden block");
			return;
		}

		FlowerVariant flowerVariant = Util.getRandom(flowers, random);

		if (hasFreeSpace(state)) {
			// Add flower to gerden
			world.setBlockState(
					pos,
					addFlowerToBlockState(state, flowerVariant),
					Block.NOTIFY_LISTENERS);
		} else {
			// Drop an item based on the variants in the garden. At this stage we can assume
			// that the garden is full.
			dropStack(world, pos, new ItemStack(flowerVariant));
		}
	}

	@Override
	protected boolean hasRandomTicks(BlockState state) {
		// Block should receive ticks if there is an eyeblossom present.
		for (EnumProperty<FlowerVariant> property : FLOWER_VARIANT_PROPERTIES) {
			FlowerVariant variant = state.get(property);
			if (variant == FlowerVariant.OPEN_EYEBLOSSOM || variant == FlowerVariant.CLOSED_EYEBLOSSOM) {
				return true;
			}
		}

		return super.hasRandomTicks(state);
	}

	@Override
	protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (doEyeblossomTick(state, world, pos, random)) {
			EyeblossomHelper.playSound(world, pos, world.isDay(), true);
		}

		super.randomTick(state, world, pos, random);
	}

	@Override
	protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (doEyeblossomTick(state, world, pos, random)) {
			EyeblossomHelper.playSound(world, pos, world.isDay(), false);
		}

		super.scheduledTick(state, world, pos, random);
	}

	private static boolean doEyeblossomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!world.getDimension().natural()) {
			return false;
		}

		boolean isDay = world.isDay();
		FlowerVariant correctVariant = EyeblossomHelper.getFlowerVariant(isDay);
		FlowerVariant incorrectVariant = EyeblossomHelper.getFlowerVariant(!isDay);

		BlockState currentState = state;
		boolean didChange = false;
		for (EnumProperty<FlowerVariant> property : GardenBlock.FLOWER_VARIANT_PROPERTIES) {
			FlowerVariant variant = currentState.get(property);
			if (variant == incorrectVariant) {
				currentState = currentState.with(property, correctVariant);
				didChange = true;
			}
		}

		if (didChange) {
			world.setBlockState(pos, currentState, Block.NOTIFY_LISTENERS);
			world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, Emitter.of(state));

			EyeblossomHelper.getState(isDay).spawnTrailParticle(world, pos, random);

			EyeblossomHelper.notifyNearbyEyeblossoms(state, world, pos, random);
		}

		return didChange;
	}

	@Override
	protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
		if (includeData) {
			TinyFlowersComponent tinyFlowersComponent = TinyFlowersComponent.of(List.of(
					state.get(FLOWER_VARIANT_1),
					state.get(FLOWER_VARIANT_2),
					state.get(FLOWER_VARIANT_3),
					state.get(FLOWER_VARIANT_4)));

			ItemStack stack = new ItemStack(this.asItem());
			stack.set(ModComponents.TINY_FLOWERS_COMPONENT_TYPE, tinyFlowersComponent);

			return stack;
		}

		FlowerVariant firstVariant = state.get(FLOWER_VARIANT_1);
		if (firstVariant.isEmpty()) {
			TinyFlowers.LOGGER.warn("Tried to pick flower from empty garden");
			return ItemStack.EMPTY;
		}

		return new ItemStack(firstVariant);
	}

	public static boolean hasFreeSpace(BlockState state) {
		return getNumFlowers(state) < FLOWER_VARIANT_PROPERTIES.length;
	}

	public static boolean isEmpty(BlockState state) {
		return getNumFlowers(state) == 0;
	}

	private static int getNumFlowers(BlockState state) {
		int numFlowers = 0;
		for (EnumProperty<FlowerVariant> property : FLOWER_VARIANT_PROPERTIES) {
			if (!state.get(property).isEmpty()) {
				numFlowers++;
			}
		}

		return numFlowers;
	}

	public static List<FlowerVariant> getFlowers(BlockState state) {
		List<FlowerVariant> flowers = new ArrayList<>(GardenBlock.FLOWER_VARIANT_PROPERTIES.length);

		for (EnumProperty<FlowerVariant> property : GardenBlock.FLOWER_VARIANT_PROPERTIES) {
			FlowerVariant variant = state.get(property);

			if (!variant.isEmpty()) {
				flowers.add(variant);
			}
		}

		return flowers;
	}

	/**
	 * Since there can be "holes" in the variants, this creates a tiny bitmap of
	 * which positions has flowers. The reason this is useful is for the memoisation
	 * during hitbox creation, as keeping the number of cache entries down for that
	 * is important.
	 */
	private static int getFlowerBitmap(BlockState state) {
		int bitmap = 0;
		for (int i = 0; i < FLOWER_VARIANT_PROPERTIES.length; i++) {
			EnumProperty<FlowerVariant> property = FLOWER_VARIANT_PROPERTIES[i];
			if (state.get(property).isEmpty()) {
				continue;
			}

			bitmap = bitmap | (1 << i);
		}

		return bitmap;
	}

	public static BlockState addFlowerToBlockState(BlockState state, FlowerVariant flowerVariant) {
		for (EnumProperty<FlowerVariant> property : FLOWER_VARIANT_PROPERTIES) {
			if (state.get(property).isEmpty()) {
				return state.with(property, flowerVariant);
			}
		}

		return state;
	}

	public BlockState getStateFromSegmented(BlockState blockState) {
		// Convert Segmented (e.g. Pink petals, Leaf litter) to GardenBlock
		Block block = blockState.getBlock();
		FlowerVariant existingVariant = FlowerVariant.fromItem(block);
		if (existingVariant.isEmpty()) {
			// Invalid state
			throw new IllegalStateException("Segmented block has no valid flower variant");
		}

		if (block instanceof Segmented segmentedBlock) {
			// This exists because FlowerbedBlocks had their own property before Segmented
			// existed.
			IntProperty amountProperty = segmentedBlock.getAmountProperty();
			int prevNumFlowers = blockState.get(amountProperty);

			BlockState baseState = this.getDefaultState().with(FACING, blockState.get(Properties.HORIZONTAL_FACING))
					.with(FLOWER_VARIANT_1, prevNumFlowers >= 1 ? existingVariant : FlowerVariant.EMPTY)
					.with(FLOWER_VARIANT_2, prevNumFlowers >= 2 ? existingVariant : FlowerVariant.EMPTY)
					.with(FLOWER_VARIANT_3, prevNumFlowers >= 3 ? existingVariant : FlowerVariant.EMPTY)
					.with(FLOWER_VARIANT_4, prevNumFlowers >= 4 ? existingVariant : FlowerVariant.EMPTY);
			return baseState;

		} else {
			throw new IllegalStateException("Block is not a segmented block");
		}
	}
}
