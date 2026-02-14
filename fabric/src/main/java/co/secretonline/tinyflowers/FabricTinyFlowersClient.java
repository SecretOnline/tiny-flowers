package co.secretonline.tinyflowers;

import co.secretonline.tinyflowers.blocks.ModBlocks;
import co.secretonline.tinyflowers.renderer.block.ModBlockEntityRenderers;
import co.secretonline.tinyflowers.renderer.item.TinyFlowerProperty;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ChunkSectionLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;

public class FabricTinyFlowersClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		SelectItemModelProperties.ID_MAPPER.put(TinyFlowers.id("tiny_flower"), TinyFlowerProperty.TYPE);

		ChunkSectionLayerMap.putBlock(ModBlocks.TINY_GARDEN_BLOCK, ChunkSectionLayer.CUTOUT);

		ModBlockEntityRenderers.register();
	}
}
