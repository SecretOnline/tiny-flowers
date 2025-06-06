package co.secretonline.tinyflowers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import co.secretonline.tinyflowers.helper.SegmentedMixinHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerbedBlock;
import net.minecraft.item.ItemPlacementContext;

@Mixin(FlowerbedBlock.class)
public class FlowerbedBlockMixin {
	@Inject(method = "canReplace", at = @At("HEAD"), cancellable = true)
	public void injectCanReplace(BlockState state, ItemPlacementContext context, CallbackInfoReturnable<Boolean> info) {
		// Delegate to helper.
		SegmentedMixinHelper.shouldAddSegment(state, context, FlowerbedBlock.FLOWER_AMOUNT, info);
	}

	@Inject(method = "getPlacementState", at = @At("RETURN"), cancellable = true)
	public void injectGetPlacementState(ItemPlacementContext context, CallbackInfoReturnable<BlockState> info) {
		FlowerbedBlock that = (FlowerbedBlock) (Object) this;

		SegmentedMixinHelper.getPlacementState(
				context,
				that,
				that.getAmountProperty(),
				FlowerbedBlock.HORIZONTAL_FACING,
				info);
	}
}
