package co.secretonline.tinyflowers.mixin.block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import co.secretonline.tinyflowers.helper.SegmentedMixinHelper;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.FlowerBedBlock;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(FlowerBedBlock.class)
public class FlowerbedBlockMixin {
	@Inject(method = "canBeReplaced(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/item/context/BlockPlaceContext;)Z", at = @At("HEAD"), cancellable = true)
	public void injectCanReplace(BlockState state, BlockPlaceContext context, CallbackInfoReturnable<Boolean> info) {
		// Delegate to helper.
		SegmentedMixinHelper.shouldAddSegment(state, context, FlowerBedBlock.AMOUNT, info);
	}

	@Inject(method = "getStateForPlacement", at = @At("RETURN"), cancellable = true)
	public void injectGetPlacementState(BlockPlaceContext context, CallbackInfoReturnable<BlockState> info) {
		FlowerBedBlock that = (FlowerBedBlock) (Object) this;

		SegmentedMixinHelper.getPlacementState(
				context,
				that,
				that.getSegmentAmountProperty(),
				FlowerBedBlock.FACING,
				info);
	}
}
