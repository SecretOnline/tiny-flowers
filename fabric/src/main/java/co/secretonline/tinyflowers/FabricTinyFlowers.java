package co.secretonline.tinyflowers;

import co.secretonline.tinyflowers.blocks.ModBlockEntities;
import co.secretonline.tinyflowers.blocks.ModBlocks;
import co.secretonline.tinyflowers.components.ModComponents;
import co.secretonline.tinyflowers.data.ModRegistries;
import co.secretonline.tinyflowers.items.ModItems;
import co.secretonline.tinyflowers.items.crafting.ModRecipeSerializers;
import net.fabricmc.api.ModInitializer;

public class FabricTinyFlowers implements ModInitializer {
	@Override
	public void onInitialize() {
	}

	static {
		ModRegistries.register();
		ModComponents.register();
		ModBlocks.register();
		ModBlockEntities.register();
		ModItems.register();
		ModRecipeSerializers.register();
	}
}
