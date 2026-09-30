package co.secretonline.tinyflowers.datagen.providers;

import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.datagen.mods.FlowerProvider;
import co.secretonline.tinyflowers.datagen.mods.Flower;
import co.secretonline.tinyflowers.data.TinyFlowerResources;
import co.secretonline.tinyflowers.helper.DataHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FabricModFlowersProvider implements DataProvider {
	private final FlowerProvider modData;

	private final PackOutput.PathProvider tinyFlowersData;
	private final PackOutput.PathProvider tinyFlowersResources;

	public FabricModFlowersProvider(FlowerProvider modData,
																	FabricDataOutput packOutput) {

		this.tinyFlowersData = packOutput.createPathProvider(PackOutput.Target.DATA_PACK,
			"tiny_flowers/tiny_flower");
		this.tinyFlowersResources = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK,
			"tiny_flowers/tiny_flower");

		this.modData = modData;
	}

	@Override
	public @NotNull String getName() {
		return "Mod flowers provider [" + this.modData.getModId() + "]";
	}

	@Override
	public @NotNull CompletableFuture<?> run(CachedOutput cachedOutput) {

		Map<ResourceLocation, TinyFlowerData> flowerVariantData = new HashMap<>();
		Map<ResourceLocation, TinyFlowerResources> flowerVariantResources = new HashMap<>();

		for (Flower tuple : this.modData.getFlowers()) {
			TinyFlowerData data = tuple.data();
			TinyFlowerResources resources = tuple.resources();

			flowerVariantData.put(data.id(), data);
			flowerVariantResources.put(resources.id(), resources);
		}

		return CompletableFuture.allOf(
			DataHelper.saveAll(cachedOutput, TinyFlowerData.CODEC, this.tinyFlowersData, flowerVariantData),
			DataHelper.saveAll(cachedOutput, TinyFlowerResources.CODEC, this.tinyFlowersResources,
				flowerVariantResources));
	}

}
