package co.secretonline.tinyflowers.helper;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.blocks.ModBlocks;
import co.secretonline.tinyflowers.blocks.TinyGardenBlockEntity;
import co.secretonline.tinyflowers.mixin.EyeblossomStateAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EyeblossomBlock;
import net.minecraft.world.level.block.state.BlockState;

public class EyeblossomHelper {
	private static final Identifier OPEN_EYEBLOSSOM = TinyFlowers.id("tiny_open_eyeblossom");
	private static final Identifier CLOSED_EYEBLOSSOM = TinyFlowers.id("tiny_closed_eyeblossom");

	public static Identifier getIdentifierForDay(boolean isDay) {
		return isDay
				? CLOSED_EYEBLOSSOM
				: OPEN_EYEBLOSSOM;
	}

	public static Block getBlock(boolean isDay) {
		return isDay
				? Blocks.CLOSED_EYEBLOSSOM
				: Blocks.OPEN_EYEBLOSSOM;
	}

	public static EyeblossomBlock.Type getState(boolean isDay) {
		return isDay
				? EyeblossomBlock.Type.CLOSED
				: EyeblossomBlock.Type.OPEN;
	}

	public static void notifyNearbyEyeblossoms(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		boolean isDay = world.isBrightOutside();

		Identifier incorrectId = getIdentifierForDay(!isDay);
		Block incorrectEyeblossom = getBlock(!isDay);

		// This is to detect whether the central block of the area is a real Eyeblossom
		// flower.
		// This method gets called in two places:
		// 1. In GardenBlock, where we want to notify Eyeblossoms and gardens.
		// 2. In EyeblossomBlockMixin, which injects after Eyeblossoms are already
		// notified so we don't want to double up the scheduled ticks.
		boolean blockIsEyeblossom = state.is(Blocks.CLOSED_EYEBLOSSOM) || state.is(Blocks.OPEN_EYEBLOSSOM);

		BlockPos.betweenClosed(pos.offset(-3, -2, -3), pos.offset(3, 2, 3)).forEach(otherPos -> {
			BlockState nearbyBlockState = world.getBlockState(otherPos);

			// Eyeblossoms
			if (!blockIsEyeblossom && nearbyBlockState.is(incorrectEyeblossom)) {
				scheduleBlockTick(world, pos, otherPos, incorrectEyeblossom, random);
				return;
			}

			// Gardens
			if (nearbyBlockState.is(ModBlocks.TINY_GARDEN_BLOCK)) {

				if (!(world.getBlockEntity(pos) instanceof TinyGardenBlockEntity gardenBlockEntity)) {
					// If there's no block entity, don't do anything
					return;
				}

				// Tiny Gardens should also recieve updates if they have eyeblossoms.
				for (Identifier gardenId : gardenBlockEntity.getFlowers()) {
					if (gardenId.equals(incorrectId)) {
						scheduleBlockTick(world, pos, otherPos, ModBlocks.TINY_GARDEN_BLOCK, random);
						return;
					}
				}
			}
		});
	}

	private static void scheduleBlockTick(ServerLevel world, BlockPos centerPos, BlockPos otherPos, Block block,
			RandomSource random) {
		double distance = Math.sqrt(centerPos.distSqr(otherPos));
		int numTicks = random.nextIntBetweenInclusive((int) (distance * 5.0), (int) (distance *
				10.0));

		world.scheduleTick(otherPos, block, numTicks);
	}

	public static void playSound(ServerLevel world, BlockPos pos, boolean isDay, boolean isLong) {
		EyeblossomBlock.Type state = getState(isDay);

		if (isLong) {
			world.playSound(null, pos, state.longSwitchSound(),
					SoundSource.BLOCKS, 1.0F, 1.0F);
		} else {
			world.playSound(null, pos, ((EyeblossomStateAccessor) ((Object) (state))).getShortSwitchSound(),
					SoundSource.BLOCKS, 1.0F, 1.0F);
		}
	}
}
