package co.secretonline.tinyflowers.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import com.mojang.serialization.MapCodec;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.components.ModComponents;
import co.secretonline.tinyflowers.components.TinyFlowersComponent;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.FlowerbedBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.item.Item;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

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
						int j = Math.floorMod(i - facing.getHorizontal(), 4);
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

					return this.getDefaultState()
							.with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
							.with(FLOWER_VARIANT_1, tinyFlowersComponent.flower1())
							.with(FLOWER_VARIANT_2, tinyFlowersComponent.flower2())
							.with(FLOWER_VARIANT_3, tinyFlowersComponent.flower3())
							.with(FLOWER_VARIANT_4, tinyFlowersComponent.flower4());
				} else if (stack.contains(DataComponentTypes.BLOCK_STATE)) {
					BlockStateComponent blockStateComponent = stack.get(
							DataComponentTypes.BLOCK_STATE);

					BlockState newBlockState = this.getDefaultState()
							.with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());

					for (EnumProperty<FlowerVariant> property : FLOWER_VARIANT_PROPERTIES) {
						FlowerVariant variant = blockStateComponent.getValue(property);
						variant = variant == null ? FlowerVariant.EMPTY : variant;

						newBlockState = newBlockState.with(property, variant);
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

		if (blockState.isOf(this)) {
			// Placing a tiny flower on a garden block.
			return addFlowerToBlockState(blockState, flowerVariant);
		} else if (blockState.getBlock() instanceof FlowerbedBlock) {
			// Placing a tiny flower on a flowerbed block.
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
	public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
		// if (includeData) {
		// BlockStateComponent blockStateComponent = BlockStateComponent.DEFAULT
		// .with(FLOWER_VARIANT_1, state.get(FLOWER_VARIANT_1))
		// .with(FLOWER_VARIANT_2, state.get(FLOWER_VARIANT_2))
		// .with(FLOWER_VARIANT_3, state.get(FLOWER_VARIANT_3))
		// .with(FLOWER_VARIANT_4, state.get(FLOWER_VARIANT_4));

		// ItemStack stack = new ItemStack(this.asItem());
		// stack.set(DataComponentTypes.BLOCK_STATE, blockStateComponent);

		for (EnumProperty<FlowerVariant> property : FLOWER_VARIANT_PROPERTIES) {
			FlowerVariant variant = state.get(property);
			if (!variant.isEmpty()) {
				return new ItemStack(variant);
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType options) {
		super.appendTooltip(stack, context, tooltip, options);

		TinyFlowersComponent tinyFlowersComponent = stack.get(ModComponents.TINY_FLOWERS_COMPONENT_TYPE);

		if (tinyFlowersComponent == null || tinyFlowersComponent.isEmpty()) {
			// Since it's possible that garden items were created before this component was
			// added to the mod, we also need to check for variants in the block state.
			BlockStateComponent itemBlockState = stack.get(DataComponentTypes.BLOCK_STATE);
			if (itemBlockState != null) {
				for (EnumProperty<FlowerVariant> property : GardenBlock.FLOWER_VARIANT_PROPERTIES) {
					FlowerVariant variant = itemBlockState.getValue(property);
					variant = variant == null ? FlowerVariant.EMPTY : variant;

					MutableText text = Text.translatable(variant.getTranslationKey());
					if (variant.isEmpty()) {
						text.formatted(Formatting.GRAY);
					}

					tooltip.add(text);
				}
			}

			return;
		}

		FlowerVariant flower1 = tinyFlowersComponent.flower1();
		FlowerVariant flower2 = tinyFlowersComponent.flower2();
		FlowerVariant flower3 = tinyFlowersComponent.flower3();
		FlowerVariant flower4 = tinyFlowersComponent.flower4();

		FlowerVariant[] flowers = { flower1, flower2, flower3, flower4 };
		for (int i = 0; i < flowers.length; i++) {
			FlowerVariant variant = flowers[i];

			MutableText text = Text.translatable(variant.getTranslationKey());
			if (variant.isEmpty()) {
				text.formatted(Formatting.GRAY);
			}

			tooltip.add(text);
		}
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

		if (block instanceof FlowerbedBlock) {
			// This exists because FlowerbedBlocks had their own property before Segmented
			// existed.
			IntProperty amountProperty = FlowerbedBlock.FLOWER_AMOUNT;
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
