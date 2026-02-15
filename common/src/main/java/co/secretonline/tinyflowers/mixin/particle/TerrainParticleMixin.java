package co.secretonline.tinyflowers.mixin.particle;

import co.secretonline.tinyflowers.TinyFlowersClientState;
import co.secretonline.tinyflowers.block.TinyGardenBlock;
import co.secretonline.tinyflowers.block.entity.TinyGardenBlockEntity;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(TerrainParticle.class)
abstract class TerrainParticleMixin extends SingleQuadParticle {
	protected TerrainParticleMixin(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
		super(level, x, y, z, sprite);
		throw new Error("Should not directly instantiate mixin");
	}

	@Inject(method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V", at = @At("RETURN"))
	private void onConstructorReturn(ClientLevel level, double x, double y, double z,
			double velocityX, double velocityY, double velocityZ,
			BlockState state, BlockPos blockPos, CallbackInfo ci) {
		TextureAtlasSprite override = tiny_flowers$getOverrideSpriteForBlockState(level, blockPos, state);
		if (override != null) {
			this.setSprite(override);
		}
	}

	@Unique
	private static TextureAtlasSprite tiny_flowers$getOverrideSpriteForBlockState(ClientLevel level, BlockPos blockPos,
																																								BlockState state) {
		if (!(state.getBlock() instanceof TinyGardenBlock)) {
			return null;
		}

		if (!(level.getBlockEntity(blockPos) instanceof TinyGardenBlockEntity gardenBlockEntity)) {
			// If there's no block entity, don't do anything
			return null;
		}

		// Select a random flower variant to render as the particle
		List<Identifier> flowers = gardenBlockEntity.getFlowers();
		if (flowers.isEmpty()) {
			return null;
		}

		Identifier flowerId = Util.getRandom(flowers, TinyFlowersClientState.RANDOM);
		TinyFlowerData flowerData = TinyFlowerData.findById(level.registryAccess(), flowerId);
		if (flowerData == null) {
			return null;
		}

		Minecraft client = Minecraft.getInstance();
		ItemStack stack = flowerData.getItemStack(1);

		TinyFlowersClientState.ITEM_RENDER_STATE.clear();
		client.getItemModelResolver()
				.appendItemLayers(TinyFlowersClientState.ITEM_RENDER_STATE, stack,
						ItemDisplayContext.GROUND, level, null, 0);

		return TinyFlowersClientState.ITEM_RENDER_STATE.pickParticleIcon(TinyFlowersClientState.RANDOM);
	}
}
