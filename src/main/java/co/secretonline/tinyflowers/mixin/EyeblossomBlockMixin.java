package co.secretonline.tinyflowers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import co.secretonline.tinyflowers.helper.TransformHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.EyeblossomBlock;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(EyeblossomBlock.class)
public class EyeblossomBlockMixin {
	@Inject(method = "tryChangingState", at = @At("RETURN"), cancellable = true)
	public void returnUpdateStateAndNotifyOthers(BlockState state, ServerLevel world, BlockPos pos, RandomSource random,
			CallbackInfoReturnable<Boolean> info) {
		// If no changes are being made, then there's no need to proceed.
		if (!info.getReturnValue()) {
			return;
		}

		TransformHelper.notifyNearbyBlocks(state, world, pos, random);
	}
}
