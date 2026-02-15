package co.secretonline.tinyflowers;

import co.secretonline.tinyflowers.block.entity.ModBlockEntities;
import co.secretonline.tinyflowers.block.ModBlocks;
import co.secretonline.tinyflowers.renderer.blockentity.TinyGardenBlockEntityRenderer;
import co.secretonline.tinyflowers.renderer.item.ModSelectItemModelProperties;
import co.secretonline.tinyflowers.resources.FabricFlowerModelDataLoader;
import co.secretonline.tinyflowers.resources.FabricFlowerModelLoadingPlugin;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ChunkSectionLayerMap;
import net.fabricmc.fabric.impl.client.model.loading.ModelLoadingPluginManager;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;

public class FabricTinyFlowersClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		SelectItemModelProperties.ID_MAPPER.put(ModSelectItemModelProperties.TINY_FLOWER_PROPERTY_ID, ModSelectItemModelProperties.TINY_FLOWER_PROPERTY);
		BlockEntityRenderers.register(ModBlockEntities.TINY_GARDEN_BLOCK_ENTITY.get(), TinyGardenBlockEntityRenderer::new);
		ChunkSectionLayerMap.putBlock(ModBlocks.TINY_GARDEN_BLOCK.get(), ChunkSectionLayer.CUTOUT);

		ModelLoadingPluginManager.registerPlugin(new FabricFlowerModelDataLoader(), new FabricFlowerModelLoadingPlugin());
	}
}
