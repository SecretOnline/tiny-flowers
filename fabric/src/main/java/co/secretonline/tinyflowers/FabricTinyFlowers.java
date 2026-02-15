package co.secretonline.tinyflowers;

import co.secretonline.tinyflowers.blocks.ModBlockEntities;
import co.secretonline.tinyflowers.blocks.ModBlocks;
import co.secretonline.tinyflowers.components.ModComponents;
import co.secretonline.tinyflowers.data.ModRegistries;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.items.FabricCreativeTabEvents;
import co.secretonline.tinyflowers.items.ModItems;
import co.secretonline.tinyflowers.items.crafting.ModRecipeSerializers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;

public class FabricTinyFlowers implements ModInitializer {

	@Override
	public void onInitialize() {
		ModBlocks.initialize();
		ModBlockEntities.initialize();
		ModComponents.initialize();
		ModItems.initialize();
		ModRecipeSerializers.initialize();

		DynamicRegistries.registerSynced(ModRegistries.TINY_FLOWER, TinyFlowerData.CODEC);

		FabricCreativeTabEvents.register();
	}
}
