package co.secretonline.tinyflowers;

import co.secretonline.tinyflowers.block.ModBlocks;
import co.secretonline.tinyflowers.block.entity.ModBlockEntities;
import co.secretonline.tinyflowers.renderer.block.TinyFlowersColorProvider;
import co.secretonline.tinyflowers.renderer.blockentity.TinyFlowerPotBlockEntityRenderer;
import co.secretonline.tinyflowers.renderer.blockentity.TinyGardenBlockEntityRenderer;
import co.secretonline.tinyflowers.resources.FabricFlowerModelDataLoader;
import co.secretonline.tinyflowers.resources.FabricFlowerModelLoadingPlugin;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.impl.client.model.loading.ModelLoadingPluginManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class FabricTinyFlowersClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TINY_GARDEN_BLOCK.get(), RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TINY_FLOWER_POT_BLOCK.get(), RenderType.cutout());

		BlockEntityRenderers.register(ModBlockEntities.TINY_GARDEN_BLOCK_ENTITY.get(), TinyGardenBlockEntityRenderer::new);
		BlockEntityRenderers.register(ModBlockEntities.TINY_FLOWER_POT_BLOCK_ENTITY.get(), TinyFlowerPotBlockEntityRenderer::new);

		ModelLoadingPluginManager.registerPlugin(new FabricFlowerModelDataLoader(), new FabricFlowerModelLoadingPlugin());

		ColorProviderRegistry.BLOCK.register(TinyFlowersColorProvider::getAverageBiomeColor, ModBlocks.TINY_GARDEN_BLOCK.get());
		ColorProviderRegistry.BLOCK.register(TinyFlowersColorProvider::getAverageBiomeColor, ModBlocks.TINY_FLOWER_POT_BLOCK.get());
	}
}
