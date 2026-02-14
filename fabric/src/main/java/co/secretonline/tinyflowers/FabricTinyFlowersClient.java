package co.secretonline.tinyflowers;

import co.secretonline.tinyflowers.blocks.ModBlocks;
import co.secretonline.tinyflowers.init.FabricClientInitializer;
import co.secretonline.tinyflowers.init.TinyFlowersClientInitializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ChunkSectionLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

public class FabricTinyFlowersClient implements ClientModInitializer {
	private final TinyFlowersClientInitializer clientInit = new FabricClientInitializer();

	@Override
	public void onInitializeClient() {
		this.clientInit.initializeClient();

		ChunkSectionLayerMap.putBlock(ModBlocks.TINY_GARDEN_BLOCK.get(), ChunkSectionLayer.CUTOUT);
	}
}
