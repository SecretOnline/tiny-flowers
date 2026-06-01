package co.secretonline.tinyflowers.mixin.color;

import co.secretonline.tinyflowers.block.ModBlocks;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.color.block.BlockTintSources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.color.block.BlockColors;

import java.util.List;

@Mixin(BlockColors.class)
public class BlockColorsMixin {
	@Unique
	private static final BlockTintSource BLANK_LAYER = BlockTintSources.constant(-1);

	@Inject(method = "createDefault", at = @At("RETURN"))
	private static void injectCreateDefault(CallbackInfoReturnable<BlockColors> cir, @Local(name = "colors") BlockColors colors) {
		colors.register(List.of(BLANK_LAYER, BlockTintSources.grass(), BlockTintSources.dryFoliage()), ModBlocks.TINY_GARDEN_BLOCK.get());
	}
}
