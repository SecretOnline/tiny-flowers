package co.secretonline.tinyflowers.datagen.generators;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

@FunctionalInterface
public interface PackContributor {
	void addProviders(FabricDataGenerator.Pack pack);
}
