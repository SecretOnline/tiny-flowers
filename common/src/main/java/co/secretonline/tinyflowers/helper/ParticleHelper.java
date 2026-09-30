package co.secretonline.tinyflowers.helper;

import co.secretonline.tinyflowers.TinyFlowersClientState;
import co.secretonline.tinyflowers.block.ModBlocks;
import co.secretonline.tinyflowers.block.entity.TinyGardenBlockEntity;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.Util;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class ParticleHelper {
	public static TextureAtlasSprite getOverrideSprite(ClientLevel level, BlockPos blockPos) {
		BlockState blockState = level.getBlockState(blockPos);
		if (!(blockState.is(ModBlocks.TINY_GARDEN_BLOCK.get()))) {
			return null;
		}

		if (!(level.getBlockEntity(blockPos) instanceof TinyGardenBlockEntity gardenBlockEntity)) {
			// If there's no block entity, don't do anything
			return null;
		}

		// Select a random flower variant to render as the particle
		List<ResourceLocation> flowers = gardenBlockEntity.getFlowers();
		if (flowers.isEmpty()) {
			return null;
		}

		ResourceLocation flowerId = Util.getRandom(flowers, TinyFlowersClientState.RANDOM);
		TinyFlowerData flowerData = TinyFlowerData.findById(level.registryAccess(), flowerId);
		if (flowerData == null) {
			return null;
		}

		Minecraft client = Minecraft.getInstance();
		ItemStack stack = flowerData.getItemStack(1);

		return client.getItemRenderer().getModel(stack, client.level, null, 0).getParticleIcon();
	}
}
