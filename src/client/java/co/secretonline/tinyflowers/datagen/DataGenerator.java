package co.secretonline.tinyflowers.datagen;

import java.util.List;

import co.secretonline.tinyflowers.datagen.generators.DefaultDataGenerator;
import co.secretonline.tinyflowers.datagen.generators.PackContributor;
import co.secretonline.tinyflowers.datagen.generators.mods.TinyFlowersDataGenerator;
import co.secretonline.tinyflowers.datagen.generators.mods.VanillaDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		List<PackContributor> contributors = List.of(
				new DefaultDataGenerator(),
				new TinyFlowersDataGenerator(),
				new VanillaDataGenerator());

		for (PackContributor contrib : contributors) {
			contrib.addProviders(pack);
		}
	}
}
