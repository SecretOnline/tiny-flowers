package co.secretonline.tinyflowers.datagen.providers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import co.secretonline.tinyflowers.data.ModRegistries;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput.PathProvider;
import net.minecraft.resources.Identifier;

public class ModFlowersProvider implements DataProvider {
	private final PathProvider tinyFlowersPathProvider;
	private final String modId;
	private final List<TinyFlowerData> flowers;

	public ModFlowersProvider(String modId, List<TinyFlowerData> flowers, FabricDataOutput packOutput) {
		this.tinyFlowersPathProvider = packOutput.createRegistryElementsPathProvider(ModRegistries.TINY_FLOWER);
		this.modId = modId;
		this.flowers = flowers;
	}

	@Override
	public String getName() {
		return "Flowers registry data (" + this.modId + ")";
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
