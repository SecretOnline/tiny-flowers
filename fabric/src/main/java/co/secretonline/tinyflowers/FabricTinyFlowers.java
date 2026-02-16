package co.secretonline.tinyflowers;

import co.secretonline.tinyflowers.block.entity.ModBlockEntities;
import co.secretonline.tinyflowers.block.ModBlocks;
import co.secretonline.tinyflowers.item.ModCreativeModeTabs;
import co.secretonline.tinyflowers.item.component.ModComponents;
import co.secretonline.tinyflowers.data.ModRegistries;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.item.ModItems;
import co.secretonline.tinyflowers.item.crafting.ModRecipeSerializers;
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
		ModCreativeModeTabs.initialize();

		DynamicRegistries.registerSynced(ModRegistries.TINY_FLOWER, TinyFlowerData.CODEC);

		FabricCreativeTabHandler.addShearsItems();
		FabricCreativeTabHandler.addTinyFlowerItems();
	}
}
