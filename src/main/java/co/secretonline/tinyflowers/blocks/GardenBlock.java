package co.secretonline.tinyflowers.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SegmentableBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import com.mojang.serialization.MapCodec;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.components.ModComponents;
import co.secretonline.tinyflowers.components.TinyFlowersComponent;
import co.secretonline.tinyflowers.helper.EyeblossomHelper;

public class GardenBlock extends VegetationBlock implements BonemealableBlock {
	public static final MapCodec<GardenBlock> CODEC = simpleCodec(GardenBlock::new);

	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
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
					return Block.box(0.0, 0.0, 0.0, 16.0, 3.0, 16.0);
				}

				VoxelShape[] voxelShapes = new VoxelShape[] {
						Block.box(8.0, 0.0, 8.0, 16.0, 3.0, 16.0),
						Block.box(8.0, 0.0, 0.0, 16.0, 3.0, 8.0),
						Block.box(0.0, 0.0, 0.0, 8.0, 3.0, 8.0),
						Block.box(0.0, 0.0, 8.0, 8.0, 3.0, 16.0)
				};
				VoxelShape voxelShape = Shapes.empty();

				for (int i = 0; i < FLOWER_VARIANT_PROPERTIES.length; i++) {
					if ((bitmap & (1 << i)) > 0) {
						int j = Math.floorMod(i - facing.get2DDataValue(), 4);
						voxelShape = Shapes.or(voxelShape, voxelShapes[j]);
					}
				}

				return voxelShape.singleEncompassing();
			}));

	public GardenBlock(BlockBehaviour.Properties settings) {
		super(settings);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(FACING, Direction.NORTH)
				.setValue(FLOWER_VARIANT_1, FlowerVariant.EMPTY)
				.setValue(FLOWER_VARIANT_2, FlowerVariant.EMPTY)
				.setValue(FLOWER_VARIANT_3, FlowerVariant.EMPTY)
				.setValue(FLOWER_VARIANT_4, FlowerVariant.EMPTY));
	}

	@Override
	public MapCodec<GardenBlock> codec() {
		return CODEC;
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
		return !context.isSecondaryUseActive() && context.getItemInHand().is(ModItemTags.TINY_FLOWERS)
				&& hasFreeSpace(state)
						? true
						: super.canBeReplaced(state, context);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return (VoxelShape) FACING_AND_AMOUNT_TO_SHAPE.apply((Direction) state.getValue(FACING), getFlowerBitmap(state));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState blockState = ctx.getLevel().getBlockState(ctx.getClickedPos());

		ItemStack stack = ctx.getItemInHand();
		Item item = stack.getItem();
		FlowerVariant flowerVariant = FlowerVariant.fromItem(item);
		if (flowerVariant.isEmpty()) {
			// The item being placed down is not a flower variant in the enum, but is a
			// GardenBlock block item.
			// Currently, the only known case for this is a pre-formed garden item.
			if (!blockState.is(this) && Block.byItem(item) == this) {
				// The item is a GardenBlock block item, but not any of the variants.
				// At this point we assume that the item is a pre-formed garden item.

				// Newer items (since v1.2.0 of this mod) have a TinyFlowersComponent that
				// stores which flowers were picked. Older items (before v1.2.0) set this
				// information on the BlockStateComponent instead. We need to check
				// both of these in case the item was creted before the new component was
				// added to the mod.
				if (stack.has(ModComponents.TINY_FLOWERS_COMPONENT_TYPE)) {
					// The item has a TinyFlowersComponent, so we can use that to get the
					// flower variants.
					TinyFlowersComponent tinyFlowersComponent = stack.get(
							ModComponents.TINY_FLOWERS_COMPONENT_TYPE);

					return this.defaultBlockState()
							.setValue(FACING, ctx.getHorizontalDirection().getOpposite())
							.setValue(FLOWER_VARIANT_1, tinyFlowersComponent.flower1())
							.setValue(FLOWER_VARIANT_2, tinyFlowersComponent.flower2())
							.setValue(FLOWER_VARIANT_3, tinyFlowersComponent.flower3())
							.setValue(FLOWER_VARIANT_4, tinyFlowersComponent.flower4());
				} else if (stack.has(DataComponents.BLOCK_STATE)) {
					BlockItemStateProperties blockStateComponent = stack.get(
							DataComponents.BLOCK_STATE);

					BlockState newBlockState = this.defaultBlockState()
							.setValue(FACING, ctx.getHorizontalDirection().getOpposite());

					for (EnumProperty<FlowerVariant> property : FLOWER_VARIANT_PROPERTIES) {
						FlowerVariant variant = blockStateComponent.get(property);
						variant = variant == null ? FlowerVariant.EMPTY : variant;

						newBlockState = newBlockState.setValue(property, variant);
					}

					return newBlockState;
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

		if (blockState.is(this)) {
			// Placing a tiny flower on a garden block.
			return addFlowerToBlockState(blockState, flowerVariant);
		} else if (blockState.getBlock() instanceof SegmentableBlock) {
			// Placing a tiny flower on a segmented block.
			// We need to convert the segmented block to a garden block
			// and then add the flower variant to it.
			BlockState baseState = getStateFromSegmented(blockState);

			// Add the new type in now that we've converted the block.
			return addFlowerToBlockState(baseState, flowerVariant);
		} else {
			// Item is a valid tiny flower block item, but there's no block yet.
			// Place a new garden with the flower variant.
			return this.defaultBlockState()
					.setValue(FACING, ctx.getHorizontalDirection().getOpposite())
					.setValue(FLOWER_VARIANT_1, flowerVariant);
		}
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING, FLOWER_VARIANT_1, FLOWER_VARIANT_2, FLOWER_VARIANT_3, FLOWER_VARIANT_4);
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
		List<FlowerVariant> flowers = getFlowers(state);
		if (flowers.isEmpty()) {
			TinyFlowers.LOGGER.warn("Tried to grow empty space in garden block");
			return;
		}

		FlowerVariant flowerVariant = Util.getRandom(flowers, random);

		if (hasFreeSpace(state)) {
			// Add flower to gerden
			world.setBlock(
					pos,
					addFlowerToBlockState(state, flowerVariant),
					Block.UPDATE_CLIENTS);
		} else {
			// Drop an item based on the variants in the garden. At this stage we can assume
			// that the garden is full.
			popResource(world, pos, new ItemStack(flowerVariant));
		}
	}

	@Override
	protected boolean isRandomlyTicking(BlockState state) {
		// Block should receive ticks if there is an eyeblossom present.
		for (EnumProperty<FlowerVariant> property : FLOWER_VARIANT_PROPERTIES) {
			FlowerVariant variant = state.getValue(property);
			if (variant == FlowerVariant.OPEN_EYEBLOSSOM || variant == FlowerVariant.CLOSED_EYEBLOSSOM) {
				return true;
			}
		}

		return super.isRandomlyTicking(state);
	}

	@Override
	protected void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (doEyeblossomTick(state, world, pos, random)) {
			EyeblossomHelper.playSound(world, pos, world.isBrightOutside(), true);
		}

		super.randomTick(state, world, pos, random);
	}

	@Override
	protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (doEyeblossomTick(state, world, pos, random)) {
			EyeblossomHelper.playSound(world, pos, world.isBrightOutside(), false);
		}

		super.tick(state, world, pos, random);
	}

	private static boolean doEyeblossomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (!world.dimensionType().natural()) {
			return false;
		}

		boolean isDay = world.isBrightOutside();
		FlowerVariant correctVariant = EyeblossomHelper.getFlowerVariant(isDay);
		FlowerVariant incorrectVariant = EyeblossomHelper.getFlowerVariant(!isDay);

		BlockState currentState = state;
		boolean didChange = false;
		for (EnumProperty<FlowerVariant> property : GardenBlock.FLOWER_VARIANT_PROPERTIES) {
			FlowerVariant variant = currentState.getValue(property);
			if (variant == incorrectVariant) {
				currentState = currentState.setValue(property, correctVariant);
				didChange = true;
			}
		}

		if (didChange) {
			world.setBlock(pos, currentState, Block.UPDATE_CLIENTS);
			world.gameEvent(GameEvent.BLOCK_CHANGE, pos, Context.of(state));

			EyeblossomHelper.getState(isDay).spawnTransformParticle(world, pos, random);

			EyeblossomHelper.notifyNearbyEyeblossoms(state, world, pos, random);
		}

		return didChange;
	}

	@Override
	protected ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData) {
		if (includeData) {
			TinyFlowersComponent tinyFlowersComponent = TinyFlowersComponent.of(
					state.getValue(FLOWER_VARIANT_1),
					state.getValue(FLOWER_VARIANT_2),
					state.getValue(FLOWER_VARIANT_3),
					state.getValue(FLOWER_VARIANT_4));

			ItemStack stack = new ItemStack(this.asItem());
			stack.set(ModComponents.TINY_FLOWERS_COMPONENT_TYPE, tinyFlowersComponent);

			return stack;
		}

		for (EnumProperty<FlowerVariant> property : FLOWER_VARIANT_PROPERTIES) {
			FlowerVariant variant = state.getValue(property);
			if (!variant.isEmpty()) {
				return new ItemStack(variant);
			}
		}

		return ItemStack.EMPTY;
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
			if (!state.getValue(property).isEmpty()) {
				numFlowers++;
			}
		}

		return numFlowers;
	}

	public static List<FlowerVariant> getFlowers(BlockState state) {
		List<FlowerVariant> flowers = new ArrayList<>(GardenBlock.FLOWER_VARIANT_PROPERTIES.length);

		for (EnumProperty<FlowerVariant> property : GardenBlock.FLOWER_VARIANT_PROPERTIES) {
			FlowerVariant variant = state.getValue(property);

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
			if (state.getValue(property).isEmpty()) {
				continue;
			}

			bitmap = bitmap | (1 << i);
		}

		return bitmap;
	}

	public static BlockState addFlowerToBlockState(BlockState state, FlowerVariant flowerVariant) {
		for (EnumProperty<FlowerVariant> property : FLOWER_VARIANT_PROPERTIES) {
			if (state.getValue(property).isEmpty()) {
				return state.setValue(property, flowerVariant);
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

		if (block instanceof SegmentableBlock segmentedBlock) {
			// This exists because FlowerbedBlocks had their own property before Segmented
			// existed.
			IntegerProperty amountProperty = segmentedBlock.getSegmentAmountProperty();
			int prevNumFlowers = blockState.getValue(amountProperty);

			BlockState baseState = this.defaultBlockState().setValue(FACING, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING))
					.setValue(FLOWER_VARIANT_1, prevNumFlowers >= 1 ? existingVariant : FlowerVariant.EMPTY)
					.setValue(FLOWER_VARIANT_2, prevNumFlowers >= 2 ? existingVariant : FlowerVariant.EMPTY)
					.setValue(FLOWER_VARIANT_3, prevNumFlowers >= 3 ? existingVariant : FlowerVariant.EMPTY)
					.setValue(FLOWER_VARIANT_4, prevNumFlowers >= 4 ? existingVariant : FlowerVariant.EMPTY);
			return baseState;

		} else {
			throw new IllegalStateException("Block is not a segmented block");
		}
	}
}
