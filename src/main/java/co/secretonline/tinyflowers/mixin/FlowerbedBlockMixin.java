package co.secretonline.tinyflowers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.serialization.MapCodec;

import co.secretonline.tinyflowers.blocks.FlowerVariant;
import co.secretonline.tinyflowers.blocks.GardenBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerbedBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;

@Mixin(FlowerbedBlock.class)
public class FlowerbedBlockMixin extends PlantBlock {
	@Inject(method = "canReplace", at = @At("HEAD"), cancellable = true)
	public void injectCanReplace(BlockState state, ItemPlacementContext context, CallbackInfoReturnable<Boolean> info) {
		if (!context.shouldCancelInteraction() && state.get(FlowerbedBlock.FLOWER_AMOUNT) < 4) {
			// If theplacement item is the same as the current block, then we don't want to
			// overwrite it. This keeps more vanilla blocks in the world, which I think is
			// an admirable goal.
			if (context.getStack().isOf(this.asItem())) {
				return;
			}

			Item item = context.getStack().getItem();
			FlowerVariant flowerVariant = FlowerVariant.fromItem(item);
			if (flowerVariant.isEmpty()) {
				// The item is not a valid flower variant, so we don't need to do anything.
				return;
			}

			info.setReturnValue(true);
		}
	}

	@Inject(method = "getPlacementState(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/block/BlockState;", at = @At("RETURN"), cancellable = true)
	public void injectGetPlacementState(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> info) {
		// We need to add some extra behaviour to FlowerbedBlock to prevent it from
		// deleting GardenBlocks when trying to add a Flowerbed flower.
		BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos());
		Block currentBlock = blockState.getBlock();
		if (currentBlock instanceof GardenBlock) {
			// Existing block is a garden block, try add current flower to it.

			if (!GardenBlock.hasFreeSpace(blockState)) {
				// Can't add flower, so don't replace blockstate.
				// This case shouldn't ever be hit, as GardenBlock should have prevented
				// replacement.
				info.setReturnValue(blockState);
				return;
			}

			// There's space in the garden, so add a flower.
			// That double cast is nasty.
			FlowerVariant flowerVariant = FlowerVariant.fromItem((FlowerbedBlock) (Object) this);
			if (flowerVariant.isEmpty()) {
				// Is this the correct thing to do?
				// Do we need to do anything to prevent the item from being consumed?
				info.setReturnValue(blockState);
				return;
			}

			BlockState newState = GardenBlock.addFlowerToBlockState(blockState, flowerVariant);
			info.setReturnValue(newState);
		}
	}

	public FlowerbedBlockMixin() {
		super(null);
	}

	@Override
	public MapCodec<? extends PlantBlock> getCodec() {
		return null;
	}
}
