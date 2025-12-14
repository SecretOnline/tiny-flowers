package co.secretonline.tinyflowers.mixin;

import java.util.List;
import net.minecraft.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import co.secretonline.tinyflowers.TinyFlowersClient;
import co.secretonline.tinyflowers.blocks.FlowerVariant;
import co.secretonline.tinyflowers.blocks.GardenBlock;

@Mixin(TerrainParticle.class)
abstract class TerrainParticleMixin extends SingleQuadParticle {
	protected TerrainParticleMixin(ClientLevel world, double x, double y, double z, TextureAtlasSprite sprite) {
		super(world, x, y, z, sprite);
		throw new Error("Should not directly instatiiate mixin");
	}

	@Inject(method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V", at = @At("RETURN"))
	private void onConstructorReturn(ClientLevel world, double x, double y, double z,
			double velocityX, double velocityY, double velocityZ,
			BlockState state, BlockPos blockPos, CallbackInfo ci) {
		TextureAtlasSprite override = getOverrideSpriteForBlockState(state);
		if (override != null) {
			this.setSprite(override);
		}
	}

	private static TextureAtlasSprite getOverrideSpriteForBlockState(BlockState state) {
		if (!(state.getBlock() instanceof GardenBlock)) {
			return null;
		}

		// Select a random flower variant to render as the particle
		List<FlowerVariant> flowers = GardenBlock.getFlowers(state);

		if (flowers.isEmpty()) {
			return null;
		}

		FlowerVariant variant = Util.getRandom(flowers, TinyFlowersClient.RANDOM);

		Minecraft client = Minecraft.getInstance();
		ItemStack stack = new ItemStack(variant.asItem());

		TinyFlowersClient.ITEM_RENDER_STATE.clear();
		client.getItemModelResolver()
				.appendItemLayers(TinyFlowersClient.ITEM_RENDER_STATE, stack, ItemDisplayContext.GROUND, client.level, null, 0);

		return TinyFlowersClient.ITEM_RENDER_STATE.pickParticleIcon(TinyFlowersClient.RANDOM);
	}
}
