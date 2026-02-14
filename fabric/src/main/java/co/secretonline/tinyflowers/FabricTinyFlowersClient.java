package co.secretonline.tinyflowers;

import co.secretonline.tinyflowers.blocks.ModBlocks;
import co.secretonline.tinyflowers.init.FabricTinyFlowersClientInitializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ChunkSectionLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

public class FabricTinyFlowersClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		new FabricTinyFlowersClientInitializer()
			.initializeClient();

		ChunkSectionLayerMap.putBlock(ModBlocks.TINY_GARDEN_BLOCK.get(), ChunkSectionLayer.CUTOUT);
	}
}
