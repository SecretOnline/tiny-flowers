package co.secretonline.tinyflowers.helper;

import java.util.ArrayList;
import java.util.List;

import co.secretonline.tinyflowers.data.TinyFlowerHolder;
import net.minecraft.Util;
import org.jetbrains.annotations.Nullable;

import co.secretonline.tinyflowers.block.ModBlocks;
import co.secretonline.tinyflowers.block.entity.TinyGardenBlockEntity;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.data.behavior.Behavior;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class TransformHelper {
	public static boolean doTransformTick(BlockState currentState, ServerLevel level, BlockPos pos, RandomSource random,
																				boolean isRandomTick, boolean shouldNotifyNearby) {
		if (level != level.getServer().overworld()) {
			return false;
		}

		boolean didChange = false;

		if (!(level.getBlockEntity(pos) instanceof TinyFlowerHolder flowerHolder)) {
			// If there's no block entity, don't do anything
			return false;
		}

		List<Behavior> featuresWithWorldEffect = new ArrayList<>();

		int size = flowerHolder.getSize();
		for (int i = 0; i < size; i++) {
			@Nullable
			ResourceLocation flowerId = flowerHolder.getFlower(i);
			if (flowerId == null) {
				continue;
			}

			@Nullable
			TinyFlowerData flowerData = TinyFlowerData.findById(level.registryAccess(), flowerId);
			if (flowerData == null) {
				continue;
			}

			for (Behavior behavior : flowerData.behaviors()) {
				if (behavior.shouldActivate(flowerHolder, i, currentState, level, pos, random)) {
					didChange = true;
					behavior.onActivate(flowerHolder, i, currentState, level, pos, random);

					if (behavior.hasWorldEffect()) {
						featuresWithWorldEffect.add(behavior);
					}
				}
			}
		}

		if (didChange) {
			level.setBlock(pos, currentState, Block.UPDATE_CLIENTS);
			level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(currentState));

			if (shouldNotifyNearby) {
				TransformHelper.notifyNearbyBlocks(currentState, level, pos, random);
			}

			if (!featuresWithWorldEffect.isEmpty()) {
				Behavior randomChange = Util.getRandom(featuresWithWorldEffect, random);
				randomChange.doWorldEffect(level, pos, random, isRandomTick);
			}
		}

		return didChange;
	}

	public static void notifyNearbyBlocks(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (level != level.getServer().overworld()) {
			return;
		}

		BlockPos.betweenClosed(pos.offset(-3, -2, -3), pos.offset(3, 2, 3)).forEach(otherPos -> {
			BlockState nearbyBlockState = level.getBlockState(otherPos);

			// Gardens
			if (nearbyBlockState.is(ModBlocks.TINY_GARDEN_BLOCK.get())) {

				if (!(level.getBlockEntity(otherPos) instanceof TinyGardenBlockEntity gardenBlockEntity)) {
					// If there's no block entity, don't do anything
					return;
				}

				// Tiny Gardens should also receive updates if they have any flowers that can activate.
				boolean didNotify = false;
				for (int i = 0; i < gardenBlockEntity.getSize(); i++) {
					@Nullable
					ResourceLocation flowerId = gardenBlockEntity.getFlower(i);
					if (flowerId == null) {
						continue;
					}

					@Nullable
					TinyFlowerData flowerData = TinyFlowerData.findById(level.registryAccess(), flowerId);
					if (flowerData == null) {
						continue;
					}

					for (Behavior feature : flowerData.behaviors()) {
						if (feature.shouldActivate(gardenBlockEntity, i, state, level, pos, random)) {
							scheduleBlockTick(level, pos, otherPos, ModBlocks.TINY_GARDEN_BLOCK.get(), random);
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
