package co.secretonline.tinyflowers.blocks;

import java.util.List;
import java.util.function.BiFunction;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.components.ModComponents;
import co.secretonline.tinyflowers.components.TinyFlowerComponent;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
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
