package co.secretonline.tinyflowers.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(BlockModelProvider::new);
		pack.addProvider(BlockTagProvider::new);
		pack.addProvider(BlockLootTableProvider::new);
		pack.addProvider(ItemTagProvider::new);
		// 1.21.1's recipe provider makes it really difficult to specify item components
		// on recipe results, so I've instead used the 1.21.4 recipes and rmeoved
		// everything eyeblossom related.
		// pack.addProvider(RecipeProvider::new);
	}

}
