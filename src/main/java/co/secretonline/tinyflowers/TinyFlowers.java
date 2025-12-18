package co.secretonline.tinyflowers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.secretonline.tinyflowers.blocks.ModBlockEntities;
import co.secretonline.tinyflowers.blocks.ModBlocks;
import co.secretonline.tinyflowers.components.ModComponents;
import co.secretonline.tinyflowers.data.ModRegistries;
import co.secretonline.tinyflowers.items.ModItems;
import co.secretonline.tinyflowers.items.crafting.ModRecipeSerializers;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

public class TinyFlowers implements ModInitializer {
	public static final String MOD_ID = "tiny_flowers";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		ModRegistries.initialize();
		ModComponents.initialize();
		ModItems.initialize();
		ModBlocks.initialize();
		ModBlockEntities.initialize();
		ModRecipeSerializers.initialize();
	}
}
