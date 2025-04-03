package co.secretonline.tinyflowers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import co.secretonline.tinyflowers.helper.SegmentedMixinHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeafLitterBlock;
import net.minecraft.item.ItemPlacementContext;

@Mixin(LeafLitterBlock.class)
public class LeafLitterBlockMixin {
	@Inject(method = "canReplace", at = @At("HEAD"), cancellable = true)
	public void injectCanReplace(BlockState state, ItemPlacementContext context, CallbackInfoReturnable<Boolean> info) {
		LeafLitterBlock that = (LeafLitterBlock) (Object) this;

		SegmentedMixinHelper.shouldAddSegment(state, context, that.getAmountProperty(), info);
	}

	@Inject(method = "getPlacementState", at = @At("RETURN"), cancellable = true)
	public void injectGetPlacementState(ItemPlacementContext context, CallbackInfoReturnable<BlockState> info) {
		LeafLitterBlock that = (LeafLitterBlock) (Object) this;

		SegmentedMixinHelper.getPlacementState(
				context,
				that,
				that.getAmountProperty(),
				LeafLitterBlock.HORIZONTAL_FACING,
				info);
	}
}
