package co.secretonline.tinyflowers;

import co.secretonline.tinyflowers.block.ModBlocks;
import co.secretonline.tinyflowers.block.entity.ModBlockEntities;
import co.secretonline.tinyflowers.renderer.block.TinyFlowersColorProvider;
import co.secretonline.tinyflowers.renderer.blockentity.TinyFlowerPotBlockEntityRenderer;
import co.secretonline.tinyflowers.renderer.blockentity.TinyGardenBlockEntityRenderer;
import co.secretonline.tinyflowers.resources.NeoForgeTinyFlowerResourceLoader;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.*;

@Mod(value = TinyFlowers.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = TinyFlowers.MOD_ID, value = Dist.CLIENT)
public class NeoForgeTinyFlowersClient {
	private static final NeoForgeTinyFlowerResourceLoader tinyFlowerResourceLoader = new NeoForgeTinyFlowerResourceLoader();

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(ModBlockEntities.TINY_GARDEN_BLOCK_ENTITY.get(), TinyGardenBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntities.TINY_FLOWER_POT_BLOCK_ENTITY.get(), TinyFlowerPotBlockEntityRenderer::new);
	}

	@SubscribeEvent
	public static void registerResourceLoader(RegisterClientReloadListenersEvent event) {
		event.registerReloadListener(NeoForgeTinyFlowersClient.tinyFlowerResourceLoader);
	}

	@SubscribeEvent
	public static void registerStandaloneModels(ModelEvent.RegisterAdditional event) {
		NeoForgeTinyFlowersClient.tinyFlowerResourceLoader.registerModels(event);
	}

	@SubscribeEvent // on the mod event bus only on the physical client
	public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
		event.register(TinyFlowersColorProvider::getAverageBiomeColor, ModBlocks.TINY_GARDEN_BLOCK.get());
		event.register(TinyFlowersColorProvider::getAverageBiomeColor, ModBlocks.TINY_FLOWER_POT_BLOCK.get());
	}
}
