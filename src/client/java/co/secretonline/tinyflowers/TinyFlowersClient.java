package co.secretonline.tinyflowers;

import co.secretonline.tinyflowers.blocks.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.biome.DryFoliageColors;
import net.minecraft.world.biome.GrassColors;

public class TinyFlowersClient implements ClientModInitializer {
	public static final Random RANDOM = Random.create();
	public static final ItemRenderState ITEM_RENDER_STATE = new ItemRenderState();

	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.putBlock(ModBlocks.TINY_GARDEN, BlockRenderLayer.CUTOUT);

		// See Pink Petals in net.minecraft.client.color.block.BlockColors
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
			boolean hasWorld = world == null || pos == null;

			switch (tintIndex) {
				case 1 -> {
					if (hasWorld) {
						return GrassColors.getDefaultColor();
					} else {
						return BiomeColors.getGrassColor(world, pos);
					}
				}
				case 2 -> {
					if (hasWorld) {
						return DryFoliageColors.getColor(0.5, 1.0);
					} else {
						return BiomeColors.getDryFoliageColor(world, pos);
					}
				}
				default -> {
					return -1;
				}
			}
		}, ModBlocks.TINY_GARDEN);
	}
}
