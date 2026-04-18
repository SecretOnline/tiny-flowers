package co.secretonline.tinyflowers.datagen;

import co.secretonline.tinyflowers.datagen.mods.FlowerProvider;
import co.secretonline.tinyflowers.datagen.mods.TinyFlowersFlowerProvider;
import co.secretonline.tinyflowers.datagen.mods.VanillaFlowerProvider;
import co.secretonline.tinyflowers.datagen.providers.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import java.util.List;

public class FabricTinyFlowersDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(FabricItemTagProvider::new);
		pack.addProvider(FabricBlockTagProvider::new);
		pack.addProvider(FabricFloristsShearsRecipeProvider::new);
		pack.addProvider(FabricDefaultModelProvider::new);

		List<FlowerProvider> mods = List.of(
			new TinyFlowersFlowerProvider(),
			new VanillaFlowerProvider());
		for (FlowerProvider mod : mods) {
			pack.addProvider((output,_) -> new FabricModFlowersProvider(mod, output));
			pack.addProvider((output,_) -> new FabricModModelProvider(mod, output));
		}
	}
}
