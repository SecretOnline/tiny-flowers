package co.secretonline.tinyflowers;

import co.secretonline.tinyflowers.blocks.ModBlockEntities;
import co.secretonline.tinyflowers.blocks.ModBlocks;
import co.secretonline.tinyflowers.items.CreativeTabEvents;
import co.secretonline.tinyflowers.renderer.block.TinyGardenBlockEntityRenderer;
import co.secretonline.tinyflowers.renderer.item.TinyFlowerProperty;
import co.secretonline.tinyflowers.resources.TinyFlowerModelDataLoader;
import co.secretonline.tinyflowers.resources.TinyFlowersModelLoadingPlugin;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.impl.client.model.loading.ModelLoadingPluginManager;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.minecraft.util.RandomSource;

public class TinyFlowersClient implements ClientModInitializer {
	public static final RandomSource RANDOM = RandomSource.create();
	public static final ItemStackRenderState ITEM_RENDER_STATE = new ItemStackRenderState();

	@Override
	public void onInitializeClient() {
		SelectItemModelProperties.ID_MAPPER.put(TinyFlowers.id("tiny_flower"), TinyFlowerProperty.TYPE);

		BlockRenderLayerMap.putBlock(ModBlocks.TINY_GARDEN_BLOCK, ChunkSectionLayer.CUTOUT);

		BlockEntityRenderers.register(ModBlockEntities.TINY_GARDEN_BLOCK_ENTITY, TinyGardenBlockEntityRenderer::new);

		ModelLoadingPluginManager.registerPlugin(new TinyFlowerModelDataLoader(), new TinyFlowersModelLoadingPlugin());

		CreativeTabEvents.initialize();
	}
}
