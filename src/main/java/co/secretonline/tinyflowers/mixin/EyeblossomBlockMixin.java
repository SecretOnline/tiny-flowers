package co.secretonline.tinyflowers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import co.secretonline.tinyflowers.helper.EyeblossomHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.EyeblossomBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

@Mixin(EyeblossomBlock.class)
public class EyeblossomBlockMixin {
	@Inject(method = "updateStateAndNotifyOthers", at = @At("RETURN"), cancellable = true)
	public void returnUpdateStateAndNotifyOthers(BlockState state, ServerWorld world, BlockPos pos, Random random,
			CallbackInfoReturnable<Boolean> info) {
		// If no changes are being made, then there's no need to proceed.
		if (!info.getReturnValue()) {
			return;
		}

		EyeblossomHelper.notifyNearbyEyeblossoms(state, world, pos, random);
	}
}
