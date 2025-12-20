package co.secretonline.tinyflowers.datagen.generators.mods;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.datagen.generators.PackContributor;
import co.secretonline.tinyflowers.datagen.providers.ModModelProvider;
import co.secretonline.tinyflowers.datagen.providers.ModFlowersProvider;
import co.secretonline.tinyflowers.datagen.providers.ModRecipeProvider;
import co.secretonline.tinyflowers.renderer.TinyFlowerResources;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;

public abstract class BaseModDataGenerator implements PackContributor {
	public abstract String getModId();

	public abstract List<Tuple<TinyFlowerData, TinyFlowerResources>> getFlowerData();

	@Override
	public void addProviders(Pack pack) {
		String modId = getModId();
		List<Tuple<TinyFlowerData, TinyFlowerResources>> data = this.getFlowerData();

		pack.addProvider((FabricDataOutput output) -> new ModModelProvider(modId, data, output));
		pack.addProvider((FabricDataOutput output,
				CompletableFuture<HolderLookup.Provider> registriesFuture) -> new ModRecipeProvider(modId, data,
						output, registriesFuture));
		pack.addProvider((FabricDataOutput output) -> new ModFlowersProvider(modId, data, output));
	}

	protected static int toTicks(float seconds) {
		return Mth.floor(seconds * 20.0f);
	}
}
