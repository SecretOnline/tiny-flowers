package co.secretonline.tinyflowers.mixin.particle;

import co.secretonline.tinyflowers.helper.ParticleHelper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TerrainParticle.class)
abstract class TerrainParticleMixin extends SingleQuadParticle {
	protected TerrainParticleMixin(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
		super(level, x, y, z, sprite);
		throw new Error("Should not directly instantiate mixin");
	}

	@WrapOperation(method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/BlockModelShaper;getParticleIcon(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"))
	private static @NonNull TextureAtlasSprite replaceSprite(BlockModelShaper instance, BlockState blockState, Operation<TextureAtlasSprite> original,
																													 @Local(argsOnly = true) ClientLevel level, @Local(argsOnly = true) BlockPos blockPos) {
		TextureAtlasSprite overrideSprite = ParticleHelper.getOverrideSprite(level, blockPos);
		return  overrideSprite == null
			? original.call(instance, blockState)
			: overrideSprite;
	}
}
