package co.secretonline.tinyflowers.datagen.generators.mods;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import co.secretonline.tinyflowers.datagen.generators.PackContributor;
import co.secretonline.tinyflowers.datagen.providers.ModFlowersProvider;
import co.secretonline.tinyflowers.datagen.providers.ModModelProvider;
import co.secretonline.tinyflowers.datagen.providers.ModRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;

public abstract class BaseModDataGenerator implements PackContributor {
	public abstract String getModId();

	public abstract List<TinyFlowersDatagenData> getFlowerData();

	@Override
	public void addProviders(Pack pack) {
		String modId = getModId();
		List<TinyFlowersDatagenData> data = this.getFlowerData();

		pack.addProvider((FabricDataOutput output) -> new ModModelProvider(modId, data, output));
		pack.addProvider((FabricDataOutput output,
				CompletableFuture<HolderLookup.Provider> registriesFuture) -> new ModRecipeProvider(modId, data,
						output, registriesFuture));
		pack.addProvider((FabricDataOutput output) -> new ModFlowersProvider(modId, data, output));
	}
}
