package co.secretonline.tinyflowers.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import co.secretonline.tinyflowers.TinyFlowersClient;
import co.secretonline.tinyflowers.blocks.FlowerVariant;
import co.secretonline.tinyflowers.blocks.GardenBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;

@Mixin(BlockModels.class)
public class BlockModelsMixin {
	@Inject(method = "getModelParticleSprite", at = @At("HEAD"), cancellable = true)
	private void randomizeGardenModelParticleSprite(BlockState state, CallbackInfoReturnable<Sprite> info) {
		if (state.getBlock() instanceof GardenBlock) {
			// Select a random flower variant to render as the particle
			List<FlowerVariant> flowers = GardenBlock.getFlowers(state);
			FlowerVariant variant = Util.getRandom(flowers, TinyFlowersClient.RANDOM);

			MinecraftClient client = MinecraftClient.getInstance();
			ItemStack stack = new ItemStack(variant.asItem());

			client.getItemModelManager().update(TinyFlowersClient.ITEM_RENDER_STATE, stack, ItemDisplayContext.GROUND, client.world, null, 0);
			info.setReturnValue(TinyFlowersClient.ITEM_RENDER_STATE.getParticleSprite(TinyFlowersClient.RANDOM));
		}
	}
}
