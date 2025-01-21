package co.secretonline.tinyflowers.blocks;

import java.util.function.BiFunction;

import com.mojang.serialization.MapCodec;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
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
			(BiFunction<Direction, Integer, VoxelShape>) ((facing, flowerAmount) -> {
				VoxelShape[] voxelShapes = new VoxelShape[] {
						Block.createCuboidShape(8.0, 0.0, 8.0, 16.0, 3.0, 16.0),
						Block.createCuboidShape(8.0, 0.0, 0.0, 16.0, 3.0, 8.0),
						Block.createCuboidShape(0.0, 0.0, 0.0, 8.0, 3.0, 8.0),
						Block.createCuboidShape(0.0, 0.0, 8.0, 8.0, 3.0, 16.0)
				};
				VoxelShape voxelShape = VoxelShapes.empty();

				for (int i = 0; i < flowerAmount; i++) {
					int j = Math.floorMod(i - facing.getHorizontalQuarterTurns(), 4);
					voxelShape = VoxelShapes.union(voxelShape, voxelShapes[j]);
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
				&& this.hasFreeSpace(state)
						? true
						: super.canReplace(state, context);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return (VoxelShape) FACING_AND_AMOUNT_TO_SHAPE.apply((Direction) state.get(FACING), this.getNumFlowers(state));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos());

		Item item = ctx.getStack().getItem();
		FlowerVariant flowerVariant = FlowerVariant.fromItem(item);
		if (flowerVariant == FlowerVariant.EMPTY) {
			// Is this the correct thing to do?
			return blockState;
		}

		if (blockState.isOf(this)) {
			// Add flower
			int numFlowers = this.getNumFlowers(blockState);
			switch (numFlowers) {
				case 1:
					return blockState.with(FLOWER_VARIANT_2, flowerVariant);
				case 2:
					return blockState.with(FLOWER_VARIANT_3, flowerVariant);
				case 3:
					return blockState.with(FLOWER_VARIANT_4, flowerVariant);
				default:
					// Is this the correct thing to do?
					return blockState;
			}
		} else {
			return this.getDefaultState()
					.with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
					.with(FLOWER_VARIANT_1, FlowerVariant.fromItem(ctx.getStack().getItem()));
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
		int numFlowers = this.getNumFlowers(state);
		int randomPosition = random.nextInt(numFlowers);

		Identifier itemId = state.get(FLOWER_VARIANT_PROPERTIES[randomPosition]).identifier;
		Item item = Registries.ITEM.get(itemId);

		if (this.hasFreeSpace(state)) {
			// Add flower to gerden based on existing flower variants
			FlowerVariant flowerVariant = FlowerVariant.fromItem(item);
			if (flowerVariant == FlowerVariant.EMPTY) {
				// Is this the correct thing to do?
				return;
			}

			world.setBlockState(
					pos,
					state.with(FLOWER_VARIANT_PROPERTIES[this.getNumFlowers(state)], flowerVariant),
					Block.NOTIFY_LISTENERS);
		} else {
			// Drop an item based on the variants in the garden. At this stage we can assume
			// that the garden is full.
			dropStack(world, pos, new ItemStack(item));
		}
	}

	private boolean hasFreeSpace(BlockState state) {
		return state.get(FLOWER_VARIANT_4).equals(FlowerVariant.EMPTY);
	}

	private int getNumFlowers(BlockState state) {
		if (!state.get(FLOWER_VARIANT_4).equals(FlowerVariant.EMPTY)) {
			return 4;
		} else if (!state.get(FLOWER_VARIANT_3).equals(FlowerVariant.EMPTY)) {
			return 3;
		} else if (!state.get(FLOWER_VARIANT_2).equals(FlowerVariant.EMPTY)) {
			return 2;
		} else {
			return 1;
		}
	}
}
