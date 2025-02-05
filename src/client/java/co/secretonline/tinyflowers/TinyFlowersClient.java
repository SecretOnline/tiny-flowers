package co.secretonline.tinyflowers;

import co.secretonline.tinyflowers.blocks.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.biome.GrassColors;

public class TinyFlowersClient implements ClientModInitializer {
	public static final Random RANDOM = Random.create();
	public static final ItemRenderState ITEM_RENDER_STATE = new ItemRenderState();

	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TINY_GARDEN, RenderLayer.getCutout());

		// See Pink Petals in net.minecraft.client.color.block.BlockColors
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
			if (tintIndex != 0) {
				if (world == null || pos == null) {
					return GrassColors.getDefaultColor();
				} else {
					return BiomeColors.getGrassColor(world, pos);
				}
			} else {
				return -1;
			}
		}, ModBlocks.TINY_GARDEN);
	}
}
