package co.secretonline.tinyflowers;

import co.secretonline.tinyflowers.blocks.ModBlocks;
import co.secretonline.tinyflowers.renderer.item.TinyFlowerProperty;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.DryFoliageColor;
import net.minecraft.world.level.GrassColor;

public class TinyFlowersClient implements ClientModInitializer {
	public static final RandomSource RANDOM = RandomSource.create();
	public static final ItemStackRenderState ITEM_RENDER_STATE = new ItemStackRenderState();

	@Override
	public void onInitializeClient() {
		SelectItemModelProperties.ID_MAPPER.put(TinyFlowers.id("tiny_flower"), TinyFlowerProperty.TYPE);

		BlockRenderLayerMap.putBlock(ModBlocks.TINY_GARDEN_BLOCK, ChunkSectionLayer.CUTOUT);

		// See Pink Petals in net.minecraft.client.color.block.BlockColors
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
			boolean hasWorld = world == null || pos == null;

			switch (tintIndex) {
				case 1 -> {
					if (hasWorld) {
						return GrassColor.getDefaultColor();
					} else {
						return BiomeColors.getAverageGrassColor(world, pos);
					}
				}
				case 2 -> {
					if (hasWorld) {
						return DryFoliageColor.get(0.5, 1.0);
					} else {
						return BiomeColors.getAverageDryFoliageColor(world, pos);
					}
				}
				default -> {
					return -1;
				}
			}
		}, ModBlocks.TINY_GARDEN_BLOCK);

	}
}
