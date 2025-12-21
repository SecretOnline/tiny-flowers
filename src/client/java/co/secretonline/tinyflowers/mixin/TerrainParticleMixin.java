package co.secretonline.tinyflowers.mixin;

import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import co.secretonline.tinyflowers.TinyFlowersClient;
import co.secretonline.tinyflowers.blocks.TinyGardenBlock;
import co.secretonline.tinyflowers.blocks.TinyGardenBlockEntity;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.resources.TinyFlowerResolvedResources;
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
		TextureAtlasSprite override = getOverrideSpriteForBlockState(level, blockPos, state);
		if (override != null) {
			this.setSprite(override);
		}
	}

	private static TextureAtlasSprite getOverrideSpriteForBlockState(ClientLevel level, BlockPos blockPos,
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

		Identifier flowerId = Util.getRandom(flowers, TinyFlowersClient.RANDOM);
		Map<Identifier, TinyFlowerResolvedResources> knownVariants = TinyFlowerResolvedResources.getInstances();
		TinyFlowerResolvedResources flowerResources = knownVariants.get(flowerId);
		if (flowerResources == null) {
			return null;
		}

		TinyFlowerData flowerData = TinyFlowerData.findById(level.registryAccess(), flowerId);
		if (flowerData == null) {
			return null;
		}

		Minecraft client = Minecraft.getInstance();
		ItemStack stack = flowerData.getItemStack(1);

		TinyFlowersClient.ITEM_RENDER_STATE.clear();
		client.getItemModelResolver()
				.appendItemLayers(TinyFlowersClient.ITEM_RENDER_STATE, stack,
						ItemDisplayContext.GROUND, level, null, 0);

		return TinyFlowersClient.ITEM_RENDER_STATE.pickParticleIcon(TinyFlowersClient.RANDOM);
	}
}
