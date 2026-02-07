package co.secretonline.tinyflowers.helper;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import co.secretonline.tinyflowers.blocks.ModBlocks;
import co.secretonline.tinyflowers.blocks.TinyGardenBlockEntity;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.data.special.TransformDayNightSpecialFeature;
import co.secretonline.tinyflowers.data.special.SpecialFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TriState;
import net.minecraft.util.Util;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class EyeblossomHelper {

	public static boolean doEyeblossomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random,
			boolean isRandomTick) {
		TriState openTriState = world.environmentAttributes().getValue(EnvironmentAttributes.EYEBLOSSOM_OPEN, pos);
		if (openTriState == TriState.DEFAULT) {
			return false;
		}

		BlockState currentState = state;
		boolean didChange = false;

		if (!(world.getBlockEntity(pos) instanceof TinyGardenBlockEntity gardenBlockEntity)) {
			// If there's no block entity, don't do anything
			return false;
		}

		List<TransformDayNightSpecialFeature> allChanges = new ArrayList<>();

		for (int i = 1; i <= 4; i++) {
			@Nullable
			Identifier flowerId = gardenBlockEntity.getFlower(i);
			if (flowerId == null) {
				continue;
			}

			@Nullable
			TinyFlowerData flowerData = TinyFlowerData.findById(world.registryAccess(), flowerId);
			if (flowerData == null) {
				continue;
			}

			for (SpecialFeature feature : flowerData.specialFeatures()) {
				if (feature instanceof TransformDayNightSpecialFeature eyeblossomFeature) {
					if (eyeblossomFeature.when().shouldChange(openTriState)) {
						gardenBlockEntity.setFlower(i, eyeblossomFeature.turnsInto());
						allChanges.add(eyeblossomFeature);

						// Stop processing special features.
						break;
					}
				}
			}
		}

		if (allChanges.size() > 0) {
			world.setBlock(pos, currentState, Block.UPDATE_CLIENTS);
			world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));

			EyeblossomHelper.notifyNearbyEyeblossoms(state, world, pos, random);

			TransformDayNightSpecialFeature randomChange = Util.getRandom(allChanges, random);
			randomChange.spawnTransformParticle(world, pos, random);
			randomChange.playSound(world, pos, isRandomTick);
		}

		return didChange;
	}

	public static void notifyNearbyEyeblossoms(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		TriState openTriState = world.environmentAttributes().getValue(EnvironmentAttributes.EYEBLOSSOM_OPEN, pos);
		if (openTriState == TriState.DEFAULT) {
			return;
		}

		// This is to detect whether the block requesting this notification is an actual
		// Eyeblossom. Actual Eyeblossoms will have already done this loop for
		// Eyeblossom blocks, so if that's the case then we only need to notify gardens.
		// If the origin was not an Eyeblossom, then we need to notify both Eyeblossoms
		// and Tiny Gardens.
		boolean originIsEyeblossom = state.is(Blocks.CLOSED_EYEBLOSSOM) || state.is(Blocks.OPEN_EYEBLOSSOM);

		// Calculate what the wrong eyeblossom is so we don't have to do it every time
		// in the loop.
		Block incorrectEyeblossom = openTriState.toBoolean(true) ? Blocks.CLOSED_EYEBLOSSOM : Blocks.OPEN_EYEBLOSSOM;

		BlockPos.betweenClosed(pos.offset(-3, -2, -3), pos.offset(3, 2, 3)).forEach(otherPos -> {
			BlockState nearbyBlockState = world.getBlockState(otherPos);

			// Update Eyeblossoms if the source was not an eyeblossom.
			if (nearbyBlockState.is(incorrectEyeblossom) && !originIsEyeblossom) {
				scheduleBlockTick(world, pos, otherPos, incorrectEyeblossom, random);
				return;
			}

			// Gardens
			if (nearbyBlockState.is(ModBlocks.TINY_GARDEN_BLOCK)) {

				if (!(world.getBlockEntity(otherPos) instanceof TinyGardenBlockEntity gardenBlockEntity)) {
					// If there's no block entity, don't do anything
					return;
				}

				// Tiny Gardens should also recieve updates if they have eyeblossoms.
				boolean didNotify = false;
				for (Identifier flowerId : gardenBlockEntity.getFlowers()) {
					@Nullable
					TinyFlowerData flowerData = TinyFlowerData.findById(world.registryAccess(), flowerId);
					if (flowerData == null) {
						continue;
					}

					for (SpecialFeature feature : flowerData.specialFeatures()) {
						if (feature instanceof TransformDayNightSpecialFeature eyeblossomFeature) {
							if (eyeblossomFeature.when().shouldChange(openTriState)) {
								scheduleBlockTick(world, pos, otherPos, ModBlocks.TINY_GARDEN_BLOCK, random);
								didNotify = true;
								break;
							}
						}
					}
					if (didNotify) {
						break;
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
}
