package co.secretonline.tinyflowers.datagen;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import co.secretonline.tinyflowers.data.ModRegistries;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.datagen.data.DefaultTinyFlowerData;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput.PathProvider;
import net.minecraft.resources.Identifier;

public class TinyFlowersProvider implements DataProvider {
	private final PathProvider tinyFlowersPathProvider;

	public TinyFlowersProvider(FabricDataOutput packOutput) {
		this.tinyFlowersPathProvider = packOutput.createRegistryElementsPathProvider(ModRegistries.TINY_FLOWER);
	}

	@Override
	public String getName() {
		return "Tiny Flower Variants";
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput) {
		Map<Identifier, TinyFlowerData> flowerVariantItems = new HashMap<>();
		for (TinyFlowerData variant : DefaultTinyFlowerData.ALL_VARIANTS) {
			flowerVariantItems.put(variant.id(), variant);
		}

		return DataProvider.saveAll(cachedOutput, TinyFlowerData.CODEC, this.tinyFlowersPathProvider, flowerVariantItems);
	}
}
