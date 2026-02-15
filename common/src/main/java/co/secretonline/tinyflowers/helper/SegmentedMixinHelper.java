package co.secretonline.tinyflowers.helper;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.block.ModBlocks;
import co.secretonline.tinyflowers.block.TinyGardenBlock;
import co.secretonline.tinyflowers.block.entity.TinyGardenBlockEntity;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SegmentableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * This class contains common logic for mixins targeting Segmented blocks.
 * <p>
 * As Segmented is an interface, we can't use a mixin to inject into it
 * directly. Instead, we need to inject into all classes that implement
 * Segmented and call the methods in this class.
 */
public class SegmentedMixinHelper {
	public static void shouldAddSegment(BlockState state, BlockPlaceContext context,
																			IntegerProperty property, CallbackInfoReturnable<Boolean> info) {
		// Early exit for cases where no additional items should be placed.
		if (context.isSecondaryUseActive() || state.getValue(property) >= SegmentableBlock.MAX_SEGMENT) {
			return;
		}

		ItemStack stack = context.getItemInHand();

		// If the placement item is the same as the current block, then we don't want to
		// overwrite it. This keeps more vanilla blocks in the world, which I think is
		// an admirable goal.
		if (stack.is(state.getBlock().asItem())) {
			return;
		}

		RegistryAccess registryAccess = context.getLevel().registryAccess();

		TinyFlowerData flowerData = TinyFlowerData.findByItemStack(registryAccess, stack);
		if (flowerData == null) {
			// The item is not a valid flower variant, so we don't need to do anything.
			return;
		}

		BlockPos supportingPos = context.getClickedPos().below();
		if (!flowerData.canSurviveOn(context.getLevel(), supportingPos)) {
			return;
		}

		info.setReturnValue(true);
	}

	public static void getPlacementState(BlockPlaceContext context, Block blockBeingUsed, IntegerProperty amountProperty,
																			 EnumProperty<Direction> directionProperty, CallbackInfoReturnable<BlockState> info) {
		Level level = context.getLevel();
		RegistryAccess registryAccess = level.registryAccess();
		BlockPos blockPos = context.getClickedPos();

		// We need to build a new BlockState if a Segmented block item is being placed
		// inside a TinyGardenBlock.
		BlockState blockState = level.getBlockState(blockPos);
		Block currentBlock = blockState.getBlock();
		BlockPos supportingPos = context.getClickedPos().below();

		// Early exit if the block being placed is the same as the current block.
		// This falls back to the original implementation.
		if (currentBlock.equals(blockBeingUsed)) {
			return;
		}

		// If the current block is not currently a garden but is able to be converted to
		// a tiny flower, then we need to convert the current blockstate to a
		// TinyGardenBlock to before continuing.
		if (!(currentBlock instanceof TinyGardenBlock)) {
			TinyFlowerData flowerData = TinyFlowerData.findByOriginalBlock(registryAccess, currentBlock);
			if (flowerData != null) {
				if (!flowerData.canSurviveOn(level, supportingPos)) {
					return;
				}

				try {
					BlockState prevBockState = blockState;
					blockState = ModBlocks.TINY_GARDEN_BLOCK.get().defaultBlockState()
						.setValue(TinyGardenBlock.FACING, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING));

					// Since we also need to update the entity, try to update the world now.
					level.setBlockAndUpdate(blockPos, blockState);

					if (!(level.getBlockEntity(blockPos) instanceof TinyGardenBlockEntity gardenBlockEntity)) {
						// If there's no block entity, try undo the change
						level.setBlockAndUpdate(blockPos, prevBockState);
						return;
					}

					gardenBlockEntity.setFromPreviousBlockState(registryAccess, prevBockState);

					currentBlock = blockState.getBlock();
				} catch (IllegalStateException e) {
					// This is expected to occur only if there are new Segmented blocks that don't
					// have tiny flowers. If the base game ever ends up doing this, then it's
					// probably woth handling this better. For now just spitting out a warning isn't
					// the worst thing.
					TinyFlowers.LOGGER.warn("Failed to convert blockstate to garden block. Ignoring", e);
				}
			}
		}

		if (currentBlock instanceof TinyGardenBlock) {
			if (!(level.getBlockEntity(blockPos) instanceof TinyGardenBlockEntity gardenBlockEntity)) {
				return;
			}
			if (gardenBlockEntity.isFull()) {
				// Can't add flower, so don't replace blockstate.
				// This case shouldn't ever be hit, as TinyGardenBlock should have prevented
				// replacement.
				info.setReturnValue(blockState);
				return;
			}

			// There's space in the garden, so add a flower.
			TinyFlowerData flowerData = TinyFlowerData.findByOriginalBlock(registryAccess, blockBeingUsed);
			if (flowerData == null) {
				info.setReturnValue(blockState);
				return;
			}
			if (!flowerData.canSurviveOn(level, supportingPos)) {
				info.setReturnValue(blockState);
				return;
			}

			gardenBlockEntity.addFlower(flowerData.id());

			// Consume item, play sound, and send game event.
			Player player = context.getPlayer();
			SoundType soundType = blockState.getSoundType();
			level.playSound(player, blockPos, soundType.getPlaceSound(), SoundSource.BLOCKS,
				(soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
			level.gameEvent(GameEvent.BLOCK_PLACE, blockPos, GameEvent.Context.of(player, blockState));
			context.getItemInHand().consume(1, player);

			info.setReturnValue(blockState);
		}
	}
}
