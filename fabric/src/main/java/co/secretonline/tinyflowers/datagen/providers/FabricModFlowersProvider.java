package co.secretonline.tinyflowers.datagen.providers;

import co.secretonline.tinyflowers.data.ModRegistries;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.datagen.ModDatagenHelper;
import co.secretonline.tinyflowers.datagen.TinyFlowersDatagenData;
import co.secretonline.tinyflowers.data.TinyFlowerResources;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FabricModFlowersProvider implements DataProvider {
	private final ModDatagenHelper modData;

	private final PackOutput.PathProvider tinyFlowersData;
	private final PackOutput.PathProvider tinyFlowersResources;

	public FabricModFlowersProvider(ModDatagenHelper modData,
																	FabricPackOutput packOutput) {

		this.tinyFlowersData = packOutput.createRegistryElementsPathProvider(ModRegistries.TINY_FLOWER);
		this.tinyFlowersResources = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK,
			Registries.elementsDirPath(ModRegistries.TINY_FLOWER));

		this.modData = modData;
	}

	@Override
	public String getName() {
		return "Mod flowers provider [" + this.modData.getModId() + "]";
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput) {

		Map<Identifier, TinyFlowerData> flowerVariantData = new HashMap<>();
		Map<Identifier, TinyFlowerResources> flowerVariantResources = new HashMap<>();

		for (TinyFlowersDatagenData tuple : this.modData.getFlowerData()) {
			TinyFlowerData data = tuple.data();
			TinyFlowerResources resources = tuple.resources();

			flowerVariantData.put(data.id(), data);
			flowerVariantResources.put(resources.id(), resources);
		}

		return CompletableFuture.allOf(
			DataProvider.saveAll(cachedOutput, TinyFlowerData.CODEC, this.tinyFlowersData, flowerVariantData),
			DataProvider.saveAll(cachedOutput, TinyFlowerResources.CODEC, this.tinyFlowersResources,
				flowerVariantResources));
	}
}
