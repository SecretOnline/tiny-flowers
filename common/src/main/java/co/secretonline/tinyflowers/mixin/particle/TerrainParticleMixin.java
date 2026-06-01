package co.secretonline.tinyflowers.mixin.particle;

import co.secretonline.tinyflowers.helper.ParticleHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TerrainParticle.class)
abstract class TerrainParticleMixin extends SingleQuadParticle {

	protected TerrainParticleMixin(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
		super(level, x, y, z, sprite);
		throw new Error("Should not directly instantiate mixin");
	}

	@Inject(method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V", at = @At("TAIL"))
	private void replaceSprite(ClientLevel level, double x, double y, double z, double xa, double ya, double za, BlockState blockState, BlockPos pos, CallbackInfo ci) {
		TextureAtlasSprite overrideSprite = ParticleHelper.getOverrideSprite(level, pos);
		if (overrideSprite != null) {
			this.sprite = overrideSprite;
		}
	}
}
