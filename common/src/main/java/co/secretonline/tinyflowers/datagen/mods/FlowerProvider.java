package co.secretonline.tinyflowers.datagen.mods;

import com.google.gson.JsonElement;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public abstract class FlowerProvider {
	public abstract String getModId();

	public abstract List<Flower> getFlowers();

	public void generateBlockStateModels(BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput) {
		for (Flower tuple : this.getFlowers()) {
			Flower.ModelParts models = tuple.modelParts();

			models.part1().outputModel(modelOutput);
			models.part2().outputModel(modelOutput);
			models.part3().outputModel(modelOutput);
			models.part4().outputModel(modelOutput);
			if (models.partPotted() != null) {
				models.partPotted().outputModel(modelOutput);
			}
		}
	}

	public void generateItemModels(BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput) {
		this.getFlowers()
			.stream()
			.map(Flower::data)
			.filter(flowerData -> !flowerData.isSegmentable())
			.forEach(flowerData -> {
				ResourceLocation prefixed = flowerData.id().withPrefix("item/");

				ModelTemplates.FLAT_ITEM.create(
					prefixed,
					TextureMapping.layer0(prefixed),
					modelOutput);
			});
	}
}
