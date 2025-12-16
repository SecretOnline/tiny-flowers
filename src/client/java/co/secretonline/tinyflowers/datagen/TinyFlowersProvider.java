package co.secretonline.tinyflowers.datagen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import co.secretonline.tinyflowers.data.ModRegistries;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput.PathProvider;
import net.minecraft.resources.Identifier;

public class TinyFlowersProvider implements DataProvider {
	private final PathProvider tinyFlowersPathProvider;
	private final List<TinyFlowerData> flowers;

	public TinyFlowersProvider(List<TinyFlowerData> flowers, FabricDataOutput packOutput) {
		this.tinyFlowersPathProvider = packOutput.createRegistryElementsPathProvider(ModRegistries.TINY_FLOWER);
		this.flowers = flowers;
	}

	public static Pack.Factory<TinyFlowersProvider> factoryFor(List<TinyFlowerData> flowers) {
		return (FabricDataOutput output) -> new TinyFlowersProvider(flowers, output);
	}

	@Override
	public String getName() {
		return "Tiny Flower Variants";
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput) {
		Map<Identifier, TinyFlowerData> flowerVariantItems = new HashMap<>();
		for (TinyFlowerData variant : this.flowers) {
			flowerVariantItems.put(variant.id(), variant);
		}

		return DataProvider.saveAll(cachedOutput, TinyFlowerData.CODEC, this.tinyFlowersPathProvider, flowerVariantItems);
	}
}
