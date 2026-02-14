package co.secretonline.tinyflowers;

import co.secretonline.tinyflowers.init.FabricTinyFlowersInitialization;
import co.secretonline.tinyflowers.items.FabricCreativeTabEvents;
import net.fabricmc.api.ModInitializer;

public class FabricTinyFlowers implements ModInitializer {
	@Override
	public void onInitialize() {
		new FabricTinyFlowersInitialization()
			.initialize();

		FabricCreativeTabEvents.register();
	}
}
