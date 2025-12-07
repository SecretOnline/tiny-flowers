package co.secretonline.tinyflowers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import co.secretonline.tinyflowers.helper.SegmentedMixinHelper;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.LeafLitterBlock;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(LeafLitterBlock.class)
public class LeafLitterBlockMixin {
	@Inject(method = "canBeReplaced(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/item/context/BlockPlaceContext;)Z", at = @At("HEAD"), cancellable = true)
	public void injectCanReplace(BlockState state, BlockPlaceContext context, CallbackInfoReturnable<Boolean> info) {
		LeafLitterBlock that = (LeafLitterBlock) (Object) this;

		SegmentedMixinHelper.shouldAddSegment(state, context, that.getSegmentAmountProperty(), info);
	}

	@Inject(method = "getStateForPlacement", at = @At("RETURN"), cancellable = true)
	public void injectGetPlacementState(BlockPlaceContext context, CallbackInfoReturnable<BlockState> info) {
		LeafLitterBlock that = (LeafLitterBlock) (Object) this;

		SegmentedMixinHelper.getPlacementState(
				context,
				that,
				that.getSegmentAmountProperty(),
				LeafLitterBlock.FACING,
				info);
	}
}
