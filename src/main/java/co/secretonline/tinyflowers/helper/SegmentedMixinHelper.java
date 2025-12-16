package co.secretonline.tinyflowers.helper;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.blocks.FlowerVariant;
// import co.secretonline.tinyflowers.blocks.GardenBlock;
// import co.secretonline.tinyflowers.blocks.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SegmentableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

/**
 * This class contains common logic for mixins targeting Segmented blocks.
 *
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

		FlowerVariant flowerVariant = FlowerVariant.fromItem(stack.getItem());
		if (flowerVariant.isEmpty()) {
			// The item is not a valid flower variant, so we don't need to do anything.
			return;
		}

		info.setReturnValue(true);
	}

	public static void getPlacementState(BlockPlaceContext context, Block blockBeingUsed, IntegerProperty amountProperty,
			EnumProperty<Direction> directionProperty, CallbackInfoReturnable<BlockState> info) {
		// We need to build a new BlockState if a Segmented block item is being placed
		// inside a GardenBlock.
		BlockState blockState = context.getLevel().getBlockState(context.getClickedPos());
		Block currentBlock = blockState.getBlock();

		// Early exit if the block being placed is the same as the current block.
		// This falls back to the original implementation.
		if (currentBlock.equals(blockBeingUsed)) {
			return;
		}

		// // If the current block is not currently a garden but is able to be converted
		// to
		// // a FlowerVariant, then we need to convert the current blockstate to a
		// // GardenBlock to before continuing.
		// if (!(currentBlock instanceof GardenBlock)) {
		// FlowerVariant currentBlockFlowerVariant =
		// FlowerVariant.fromItem(currentBlock.asItem());
		// if (!currentBlockFlowerVariant.isEmpty()) {
		// try {
		// blockState = ((GardenBlock)
		// ModBlocks.TINY_GARDEN).getStateFromSegmented(blockState);
		// currentBlock = blockState.getBlock();
		// } catch (IllegalStateException e) {
		// // This is expected to occur only if there are new Segmented blocks that
		// don't
		// // have tiny flowers. If the base game ever ends up doing this, then it's
		// // probably woth handling this better. For now just spitting out a warning
		// isn't
		// // the worst thing.
		// TinyFlowers.LOGGER.warn("Failed to convert blockstate to garden block.
		// Ignoring", e);
		// }
		// }
		// }

		// if (currentBlock instanceof GardenBlock) {
		// if (!GardenBlock.hasFreeSpace(blockState)) {
		// // Can't add flower, so don't replace blockstate.
		// // This case shouldn't ever be hit, as GardenBlock should have prevented
		// // replacement.
		// info.setReturnValue(blockState);
		// return;
		// }

		// // There's space in the garden, so add a flower.
		// FlowerVariant flowerVariant = FlowerVariant.fromItem(blockBeingUsed);
		// if (flowerVariant.isEmpty()) {
		// // Is this the correct thing to do?
		// // Do we need to do anything to prevent the item from being consumed?
		// info.setReturnValue(blockState);
		// return;
		// }

		// BlockState newState = GardenBlock.addFlowerToBlockState(blockState,
		// flowerVariant);
		// info.setReturnValue(newState);
		// }
	}
}
