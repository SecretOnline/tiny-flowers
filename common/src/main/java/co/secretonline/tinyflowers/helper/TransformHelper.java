package co.secretonline.tinyflowers.helper;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import co.secretonline.tinyflowers.block.ModBlocks;
import co.secretonline.tinyflowers.block.entity.TinyGardenBlockEntity;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.data.behavior.Behavior;
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

public class TransformHelper {

	public static boolean doTransformTick(BlockState currentState, ServerLevel world, BlockPos pos, RandomSource random,
																				boolean isRandomTick) {
		TriState openTriState = world.environmentAttributes().getValue(EnvironmentAttributes.EYEBLOSSOM_OPEN, pos);
		if (openTriState == TriState.DEFAULT) {
			return false;
		}

		boolean didChange = false;

		if (!(world.getBlockEntity(pos) instanceof TinyGardenBlockEntity gardenBlockEntity)) {
			// If there's no block entity, don't do anything
			return false;
		}

		List<Behavior> featuresWithWorldEffect = new ArrayList<>();
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

			for (Behavior feature : flowerData.behaviors()) {
				if (feature.shouldActivateFeature(gardenBlockEntity, i, currentState, world, pos, random)) {
					didChange = true;
					feature.onActivateFeature(gardenBlockEntity, i, currentState, world, pos, random);

					if (feature.hasWorldEffect()) {
						featuresWithWorldEffect.add(feature);
					}
				}
			}
		}

		if (didChange) {
			world.setBlock(pos, currentState, Block.UPDATE_CLIENTS);
			world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(currentState));

			TransformHelper.notifyNearbyBlocks(currentState, world, pos, random);

			if (!featuresWithWorldEffect.isEmpty()) {
				Behavior randomChange = Util.getRandom(featuresWithWorldEffect, random);
				randomChange.doWorldEffect(world, pos, random, isRandomTick);
			}
		}

		return didChange;
	}

	public static void notifyNearbyBlocks(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
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
			if (nearbyBlockState.is(ModBlocks.TINY_GARDEN_BLOCK.get())) {

				if (!(world.getBlockEntity(otherPos) instanceof TinyGardenBlockEntity gardenBlockEntity)) {
					// If there's no block entity, don't do anything
					return;
				}

				// Tiny Gardens should also recieve updates if they have eyeblossoms.
				boolean didNotify = false;
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

					for (Behavior feature : flowerData.behaviors()) {
						if (feature.shouldActivateFeature(gardenBlockEntity, i, state, world, pos, random)) {
							scheduleBlockTick(world, pos, otherPos, ModBlocks.TINY_GARDEN_BLOCK.get(), random);
							didNotify = true;
							break;
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
