package co.secretonline.tinyflowers.datagen;

import co.secretonline.tinyflowers.datagen.mods.TinyFlowersDatagenHelper;
import co.secretonline.tinyflowers.datagen.mods.VanillaDatagenHelper;
import co.secretonline.tinyflowers.datagen.providers.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import java.util.List;

public class FabricTinyFlowersDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		List<ModDatagenHelper> mods = List.of(
			new TinyFlowersDatagenHelper(),
			new VanillaDatagenHelper());

		pack.addProvider(ItemTagProvider::new);
		pack.addProvider(BlockTagProvider::new);
		pack.addProvider(FloristsShearsRecipeProvider::new);
		pack.addProvider(DefaultModelProvider::new);

		for (ModDatagenHelper mod : mods) {
			pack.addProvider((FabricDataGenerator.Pack.Factory<FabricModFlowersProvider>) (output) -> new FabricModFlowersProvider(mod, output));
			pack.addProvider((FabricDataGenerator.Pack.Factory<FabricModModelProvider>) (output) -> new FabricModModelProvider(mod, output));
		}
	}
}
