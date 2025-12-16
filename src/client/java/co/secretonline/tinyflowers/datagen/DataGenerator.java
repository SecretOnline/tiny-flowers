package co.secretonline.tinyflowers.datagen;

import co.secretonline.tinyflowers.datagen.data.DefaultTinyFlowerData;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		// pack.addProvider(BlockModelProvider::new);
		// pack.addProvider(BlockTagProvider::new);
		// pack.addProvider(BlockLootTableProvider::new);
		pack.addProvider(ItemTagProvider::new);
		pack.addProvider(FloristsShearsRecipeProvider::new);
		pack.addProvider(TinyFlowersRecipeProvider.factoryFor(DefaultTinyFlowerData.ALL_VARIANTS));
		pack.addProvider(TinyFlowersProvider.factoryFor(DefaultTinyFlowerData.ALL_VARIANTS));
	}

}
