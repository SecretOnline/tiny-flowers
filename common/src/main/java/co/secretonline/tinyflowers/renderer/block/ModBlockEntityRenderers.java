package co.secretonline.tinyflowers.renderer.block;

import co.secretonline.tinyflowers.blocks.ModBlockEntities;
import co.secretonline.tinyflowers.platform.Services;

public class ModBlockEntityRenderers {
	public static void register() {
		Services.PLATFORM_REGISTRATION.registerBlockEntityRenderer(ModBlockEntities.TINY_GARDEN_BLOCK_ENTITY, TinyGardenBlockEntityRenderer::new);
	}
}
