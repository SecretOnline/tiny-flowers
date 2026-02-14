package co.secretonline.tinyflowers.datagen;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.components.TinyFlowerComponent;
import co.secretonline.tinyflowers.items.ModItems;
import co.secretonline.tinyflowers.renderer.item.TinyFlowerProperty;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public abstract class ModDatagenHelper {
	public abstract String getModId();

	public abstract List<co.secretonline.tinyflowers.datagen.TinyFlowersDatagenData> getFlowerData();

	public void generateBlockStateModels(BiConsumer<Identifier, ModelInstance> modelOutput) {
		for (TinyFlowersDatagenData tuple : this.getFlowerData()) {
			TinyFlowersDatagenData.ModelParts models = tuple.modelParts();

			models.part1().outputModel(modelOutput);
			models.part2().outputModel(modelOutput);
			models.part3().outputModel(modelOutput);
			models.part4().outputModel(modelOutput);
		}
	}

	public void generateItemModels(ItemModelOutput itemModelOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
		List<SelectItemModel.SwitchCase<TinyFlowerComponent>> list = this.getFlowerData()
			.stream()
			.map(tuple -> tuple.data())
			.filter(flowerData -> !flowerData.isSegmentable())
			.map(flowerData -> ItemModelUtils.when(
				new TinyFlowerComponent(flowerData.id()),
				flatItemForIdentifier(flowerData.id(), modelOutput)))
			.collect(Collectors.toList());

		if (list.isEmpty()) {
			return;
		}

		itemModelOutput.accept(ModItems.TINY_FLOWER_ITEM.get(),
			ItemModelUtils.select(
				new TinyFlowerProperty(),
				flatItemForIdentifier(TinyFlowers.id("tiny_garden"), modelOutput),
				list));
	}

	private ItemModel.Unbaked flatItemForIdentifier(Identifier id, BiConsumer<Identifier, ModelInstance> itemModelOutput) {
		Identifier prefixed = id.withPrefix("item/");

		return ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(
			prefixed,
			TextureMapping.layer0(prefixed),
			itemModelOutput));
	}
}
