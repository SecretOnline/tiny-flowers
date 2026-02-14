package co.secretonline.tinyflowers;

import co.secretonline.tinyflowers.blocks.ModBlockEntities;
import co.secretonline.tinyflowers.renderer.block.TinyGardenBlockEntityRenderer;
import co.secretonline.tinyflowers.renderer.item.ModSelectItemModelProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterSelectItemModelPropertyEvent;

@Mod(value = TinyFlowers.MOD_ID, dist = Dist.CLIENT)
public class NeoForgeTinyFlowersClient {

	@SubscribeEvent
	public static void registerSelectProperties(RegisterSelectItemModelPropertyEvent event) {
		event.register(ModSelectItemModelProperties.TINY_FLOWER_PROPERTY_ID, ModSelectItemModelProperties.TINY_FLOWER_PROPERTY);
	}

	@SubscribeEvent // on the mod event bus only on the physical client
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(ModBlockEntities.TINY_GARDEN_BLOCK_ENTITY.get(), TinyGardenBlockEntityRenderer::new);
	}
}
