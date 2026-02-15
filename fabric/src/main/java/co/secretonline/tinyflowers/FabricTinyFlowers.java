package co.secretonline.tinyflowers;

import co.secretonline.tinyflowers.data.ModRegistries;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.items.FabricCreativeTabEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;

public class FabricTinyFlowers implements ModInitializer {

	@Override
	public void onInitialize() {

		DynamicRegistries.registerSynced(ModRegistries.TINY_FLOWER, TinyFlowerData.CODEC);

		FabricCreativeTabEvents.register();
	}

	static {
		TinyFlowers.initialize();
	}
}
