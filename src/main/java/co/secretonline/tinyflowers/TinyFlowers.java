package co.secretonline.tinyflowers;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.secretonline.tinyflowers.blocks.ModBlocks;

public class TinyFlowers implements ModInitializer {
	public static final String MOD_ID = "tiny-flowers";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		ModBlocks.initialize();
	}
}
