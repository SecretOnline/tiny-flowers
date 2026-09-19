package co.secretonline.tinyflowers.datagen;

import co.secretonline.tinyflowers.datagen.mods.FlowerProvider;
import co.secretonline.tinyflowers.datagen.mods.TinyFlowersFlowerProvider;
import co.secretonline.tinyflowers.datagen.mods.VanillaFlowerProvider;
import co.secretonline.tinyflowers.datagen.providers.*;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.recipes.RecipeProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;

@EventBusSubscriber(value = Dist.CLIENT)
public class NeoForgeTinyFlowersDataGenerator {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		event.createProvider(NeoForgeBlockTagProvider::new);
		event.createProvider(NeoForgeItemTagProvider::new);
		event.createProvider(NeoForgeDefaultModelProvider::new);

		event.createReloadableRegistryObjects(new RegistrySetBuilder().add(RecipeProvider.asBootstrap(NeoForgeFloristsShearsRecipeProvider::new)));

		List<FlowerProvider> mods = List.of(
			new VanillaFlowerProvider(),
			new TinyFlowersFlowerProvider());
		for (FlowerProvider mod : mods) {
			event.createProvider((output, registryLookup) -> new NeoForgeModFlowerDataProvider(mod, output, registryLookup));
			event.createProvider((output, registryLookup) -> new NeoForgeModFlowerResourcesProvider(mod, output, registryLookup));
			event.createProvider((output) -> new NeoForgeModModelProvider(mod, output));
		}
	}
}
