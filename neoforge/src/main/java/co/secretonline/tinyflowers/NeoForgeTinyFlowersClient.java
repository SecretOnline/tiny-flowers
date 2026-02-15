package co.secretonline.tinyflowers;

import co.secretonline.tinyflowers.block.entity.ModBlockEntities;
import co.secretonline.tinyflowers.renderer.blockentity.TinyGardenBlockEntityRenderer;
import co.secretonline.tinyflowers.renderer.item.ModSelectItemModelProperties;
import co.secretonline.tinyflowers.resources.NeoForgeTinyFlowerResourceLoader;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.resources.VanillaClientListeners;

@Mod(value = TinyFlowers.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = TinyFlowers.MOD_ID)
public class NeoForgeTinyFlowersClient {
	private static final NeoForgeTinyFlowerResourceLoader tinyFlowerResourceLoader = new NeoForgeTinyFlowerResourceLoader();

	@SubscribeEvent
	public static void registerSelectProperties(RegisterSelectItemModelPropertyEvent event) {
		event.register(ModSelectItemModelProperties.TINY_FLOWER_PROPERTY_ID, ModSelectItemModelProperties.TINY_FLOWER_PROPERTY);
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(ModBlockEntities.TINY_GARDEN_BLOCK_ENTITY.get(), TinyGardenBlockEntityRenderer::new);
	}

	@SubscribeEvent
	public static void registerResourceLoader(AddClientReloadListenersEvent event) {
		Identifier flowerModelReload = TinyFlowers.id("flower_models");
		event.addListener(flowerModelReload, NeoForgeTinyFlowersClient.tinyFlowerResourceLoader);
		event.addDependency(flowerModelReload, VanillaClientListeners.MODELS);
	}

	@SubscribeEvent
	public static void registerStandaloneModels(ModelEvent.RegisterStandalone event) {
		NeoForgeTinyFlowersClient.tinyFlowerResourceLoader.registerModels(event);
	}
}
