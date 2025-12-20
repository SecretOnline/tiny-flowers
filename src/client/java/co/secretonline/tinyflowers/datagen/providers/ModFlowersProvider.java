package co.secretonline.tinyflowers.datagen.providers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import co.secretonline.tinyflowers.data.ModRegistries;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.renderer.TinyFlowerResources;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput.PathProvider;
import net.minecraft.data.PackOutput.Target;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Tuple;

public class ModFlowersProvider implements DataProvider {
	private final PathProvider tinyFlowersData;
	private final PathProvider tinyFlowersResources;
	private final String modId;
	private final List<Tuple<TinyFlowerData, TinyFlowerResources>> flowers;

	public ModFlowersProvider(String modId, List<Tuple<TinyFlowerData, TinyFlowerResources>> flowers,
			FabricDataOutput packOutput) {
		this.tinyFlowersData = packOutput.createRegistryElementsPathProvider(ModRegistries.TINY_FLOWER);
		this.tinyFlowersResources = packOutput.createPathProvider(Target.RESOURCE_PACK,
				Registries.elementsDirPath(ModRegistries.TINY_FLOWER));
		this.modId = modId;
		this.flowers = flowers;
	}

	@Override
	public String getName() {
		return "Flowers registry data (" + this.modId + ")";
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput) {
		Map<Identifier, TinyFlowerData> flowerVariantData = new HashMap<>();
		Map<Identifier, TinyFlowerResources> flowerVariantResources = new HashMap<>();

		for (Tuple<TinyFlowerData, TinyFlowerResources> tuple : this.flowers) {
			TinyFlowerData data = tuple.getA();
			TinyFlowerResources resources = tuple.getB();

			flowerVariantData.put(data.id(), data);
			flowerVariantResources.put(resources.id(), resources);
		}

		return CompletableFuture.allOf(
				DataProvider.saveAll(cachedOutput, TinyFlowerData.CODEC, this.tinyFlowersData, flowerVariantData),
				DataProvider.saveAll(cachedOutput, TinyFlowerResources.CODEC, this.tinyFlowersResources,
						flowerVariantResources));
	}
}
