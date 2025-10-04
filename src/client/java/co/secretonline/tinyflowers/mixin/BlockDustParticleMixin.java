package co.secretonline.tinyflowers.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import co.secretonline.tinyflowers.TinyFlowersClient;
import co.secretonline.tinyflowers.blocks.FlowerVariant;
import co.secretonline.tinyflowers.blocks.GardenBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.BillboardParticle;
import net.minecraft.client.particle.BlockDustParticle;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;

@Mixin(BlockDustParticle.class)
abstract class BlockDustParticleMixin extends BillboardParticle {
	protected BlockDustParticleMixin(ClientWorld world, double x, double y, double z, Sprite sprite) {
		super(world, x, y, z, sprite);
		throw new Error("Should not directly instatiiate mixin");
	}

	@Inject(method = "<init>(Lnet/minecraft/client/world/ClientWorld;DDDDDDLnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)V", at = @At("RETURN"))
	private void onConstructorReturn(ClientWorld world, double x, double y, double z,
			double velocityX, double velocityY, double velocityZ,
			BlockState state, BlockPos blockPos, CallbackInfo ci) {
		Sprite override = getOverrideSpriteForBlockState(state);
		if (override != null) {
			this.setSprite(override);
		}
	}

	private static Sprite getOverrideSpriteForBlockState(BlockState state) {
		if (!(state.getBlock() instanceof GardenBlock)) {
			return null;
		}

		// Select a random flower variant to render as the particle
		List<FlowerVariant> flowers = GardenBlock.getFlowers(state);

		if (flowers.isEmpty()) {
			return null;
		}

		FlowerVariant variant = Util.getRandom(flowers, TinyFlowersClient.RANDOM);

		MinecraftClient client = MinecraftClient.getInstance();
		ItemStack stack = new ItemStack(variant.asItem());

		TinyFlowersClient.ITEM_RENDER_STATE.clear();
		client.getItemModelManager()
				.update(TinyFlowersClient.ITEM_RENDER_STATE, stack, ItemDisplayContext.GROUND, client.world, null, 0);

		return TinyFlowersClient.ITEM_RENDER_STATE.getParticleSprite(TinyFlowersClient.RANDOM);
	}
}
