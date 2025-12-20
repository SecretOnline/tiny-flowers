package co.secretonline.tinyflowers.datagen.generators;

import co.secretonline.tinyflowers.datagen.providers.DefaultModelProvider;
import co.secretonline.tinyflowers.datagen.providers.FloristsShearsRecipeProvider;
import co.secretonline.tinyflowers.datagen.providers.ItemTagProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack;

public class DefaultDataGenerator implements PackContributor {
	@Override
	public void addProviders(Pack pack) {
		pack.addProvider(ItemTagProvider::new);
		pack.addProvider(FloristsShearsRecipeProvider::new);
		pack.addProvider(DefaultModelProvider::new);
	}
}
