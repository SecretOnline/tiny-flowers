package co.secretonline.tinyflowers.helper;

import co.secretonline.tinyflowers.blocks.FlowerVariant;
import co.secretonline.tinyflowers.blocks.GardenBlock;
import co.secretonline.tinyflowers.blocks.ModBlocks;
import co.secretonline.tinyflowers.mixin.EyeblossomStateAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EyeblossomBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class EyeblossomHelper {
	public static FlowerVariant getFlowerVariant(boolean isDay) {
		return isDay
				? FlowerVariant.CLOSED_EYEBLOSSOM
				: FlowerVariant.OPEN_EYEBLOSSOM;
	}

	public static Block getBlock(boolean isDay) {
		return isDay
				? Blocks.CLOSED_EYEBLOSSOM
				: Blocks.OPEN_EYEBLOSSOM;
	}

	public static EyeblossomBlock.EyeblossomState getState(boolean isDay) {
		return isDay
				? EyeblossomBlock.EyeblossomState.CLOSED
				: EyeblossomBlock.EyeblossomState.OPEN;
	}

	public static void notifyNearbyEyeblossoms(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		boolean isDay = world.isDay();

		FlowerVariant incorrectFlowerVariant = getFlowerVariant(!isDay);
		Block incorrectEyeblossom = getBlock(!isDay);

		// This is to detect whether the central block of the area is a real Eyeblossom
		// flower.
		// This method gets called in two places:
		// 1. In GardenBlock, where we want to notify Eyeblossoms and gardens.
		// 2. In EyeblossomBlockMixin, which injects after Eyeblossoms are already
		// notified so we don't want to double up the scheduled ticks.
		boolean blockIsEyeblossom = state.isOf(Blocks.CLOSED_EYEBLOSSOM) || state.isOf(Blocks.OPEN_EYEBLOSSOM);

		BlockPos.iterate(pos.add(-3, -2, -3), pos.add(3, 2, 3)).forEach(otherPos -> {
			BlockState nearbyBlockState = world.getBlockState(otherPos);

			// Eyeblossoms
			if (!blockIsEyeblossom && nearbyBlockState.isOf(incorrectEyeblossom)) {
				scheduleBlockTick(world, pos, otherPos, incorrectEyeblossom, random);
				return;
			}

			// Gardens
			if (nearbyBlockState.isOf(ModBlocks.TINY_GARDEN)) {
				// Tiny Gardens should also recieve updates if they have eyeblossoms.
				for (EnumProperty<FlowerVariant> property : GardenBlock.FLOWER_VARIANT_PROPERTIES) {
					FlowerVariant variant = nearbyBlockState.get(property);
					if (variant == incorrectFlowerVariant) {
						scheduleBlockTick(world, pos, otherPos, ModBlocks.TINY_GARDEN, random);
						return;
					}
				}
			}
		});
	}

	private static void scheduleBlockTick(ServerWorld world, BlockPos centerPos, BlockPos otherPos, Block block,
			Random random) {
		double distance = Math.sqrt(centerPos.getSquaredDistance(otherPos));
		int numTicks = random.nextBetween((int) (distance * 5.0), (int) (distance *
				10.0));

		world.scheduleBlockTick(otherPos, block, numTicks);
	}

	public static void playSound(ServerWorld world, BlockPos pos, boolean isDay, boolean isLong) {
		EyeblossomBlock.EyeblossomState state = getState(isDay);

		if (isLong) {
			world.playSound(null, pos, state.getLongSound(),
					SoundCategory.BLOCKS, 1.0F, 1.0F);
		} else {
			world.playSound(null, pos, ((EyeblossomStateAccessor) ((Object) (state))).getSound(),
					SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
	}
}
