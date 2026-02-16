package co.secretonline.tinyflowers.mixin.particle;

import co.secretonline.tinyflowers.helper.ParticleHelper;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TerrainParticle.class)
abstract class NeoForgeTerrainParticleMixin extends SingleQuadParticle {
	protected NeoForgeTerrainParticleMixin(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
		super(level, x, y, z, sprite);
		throw new Error("Should not directly instantiate mixin");
	}

	@WrapMethod(method = "updateSprite")
	private TerrainParticle wrapUpdateSprite(BlockState state, BlockPos pos, Operation<TerrainParticle> original) {
		if (pos != null) {
			TextureAtlasSprite overrideSprite = ParticleHelper.getOverrideSprite(this.level, pos);

			if (overrideSprite != null) {
				this.setSprite(overrideSprite);
				return (TerrainParticle) (SingleQuadParticle) this;
			}
		}

		return original.call(state, pos);
	}

}
