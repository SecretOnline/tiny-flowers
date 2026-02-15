package co.secretonline.tinyflowers.datagen;

import co.secretonline.tinyflowers.datagen.mods.TinyFlowersDatagenHelper;
import co.secretonline.tinyflowers.datagen.mods.VanillaDatagenHelper;
import co.secretonline.tinyflowers.datagen.providers.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;

@EventBusSubscriber
public class NeoForgeTinyFlowersDataGenerator {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		event.createProvider(BlockTagProvider::new);
		event.createProvider(ItemTagProvider::new);
		event.createProvider(FloristsShearsRecipeProvider::new);
		event.createProvider(DefaultModelProvider::new);

		List<ModDatagenHelper> mods = List.of(
			new VanillaDatagenHelper(),
			new TinyFlowersDatagenHelper());
		for (ModDatagenHelper mod : mods) {
			event.createProvider((output, registryLookup) -> new NeoForgeModFlowerDataProvider(mod, output, registryLookup));
			event.createProvider((output, registryLookup) -> new NeoForgeModFlowerResourcesProvider(mod, output, registryLookup));
			event.createProvider((output) -> new NeoForgeModModelProvider(mod, output));
		}
	}
}
