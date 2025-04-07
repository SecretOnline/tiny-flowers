package co.secretonline.tinyflowers.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import co.secretonline.tinyflowers.TinyFlowersClient;
import co.secretonline.tinyflowers.blocks.FlowerVariant;
import co.secretonline.tinyflowers.blocks.GardenBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.BlockDustParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;

@Mixin(BlockDustParticle.class)
abstract class BlockDustParticleMixin {
	@ModifyArgs(method = "<init>(Lnet/minecraft/client/world/ClientWorld;DDDDDDLnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)V", at = @At(value = "INVOKE", target = "net/minecraft/client/particle.BlockDustParticle.setSprite(Lnet/minecraft/client/texture/Sprite;)V"))
	private void modifySetSpriteArgument(
			Args args, ClientWorld world,
			double x, double y, double z,
			double velocityX, double velocityY, double velocityZ,
			BlockState state, BlockPos blockPos) {
		if (state.getBlock() instanceof GardenBlock) {
			// Select a random flower variant to render as the particle
			List<FlowerVariant> flowers = GardenBlock.getFlowers(state);
			FlowerVariant variant = Util.getRandom(flowers, TinyFlowersClient.RANDOM);

			MinecraftClient client = MinecraftClient.getInstance();
			ItemStack stack = new ItemStack(variant.asItem());

			TinyFlowersClient.ITEM_RENDER_STATE.clear();
			client.getItemModelManager()
					.update(TinyFlowersClient.ITEM_RENDER_STATE, stack, ItemDisplayContext.GROUND, client.world, null, 0);

			args.set(0, TinyFlowersClient.ITEM_RENDER_STATE.getParticleSprite(TinyFlowersClient.RANDOM));
		}
	}
}
