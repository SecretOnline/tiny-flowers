package co.secretonline.tinyflowers.datagen;

import co.secretonline.tinyflowers.datagen.mods.FlowerProvider;
import co.secretonline.tinyflowers.datagen.mods.TinyFlowersFlowerProvider;
import co.secretonline.tinyflowers.datagen.mods.VanillaFlowerProvider;
import co.secretonline.tinyflowers.datagen.providers.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(value = Dist.CLIENT)
public class NeoForgeTinyFlowersDataGenerator {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		event.createBlockAndItemTags(NeoForgeBlockTagProvider::new, NeoForgeItemTagProvider::new);
		event.createProvider(NeoForgeFloristsShearsRecipeProvider::new);
		generator.addProvider(
			event.includeClient(),
			new NeoForgeDefaultBlockModelProvider(output, existingFileHelper)
		);
		generator.addProvider(
			event.includeClient(),
			new NeoForgeDefaultItemModelProvider(output, existingFileHelper)
		);

		List<FlowerProvider> mods = List.of(
			new VanillaFlowerProvider(),
			new TinyFlowersFlowerProvider());
		for (FlowerProvider mod : mods) {
			event.createProvider((o, r) -> new NeoForgeModFlowerDataProvider(mod, o, r, existingFileHelper));
			event.createProvider((o, r) -> new NeoForgeModFlowerResourcesProvider(mod, o, r, existingFileHelper));
			event.createProvider((o, r) -> new NeoForgeModModelProvider(mod, o));
		}
	}
}
