package co.secretonline.tinyflowers.datagen.providers;

import java.util.List;
import java.util.stream.Collectors;

import org.jspecify.annotations.NonNull;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.components.TinyFlowerComponent;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.items.ModItems;
import co.secretonline.tinyflowers.renderer.item.TinyFlowerProperty;
import co.secretonline.tinyflowers.resources.TinyFlowerResources;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Tuple;

public class ModModelProvider extends FabricModelProvider {
	private final String modId;
	private final List<Tuple<TinyFlowerData, TinyFlowerResources>> flowers;

	public ModModelProvider(String modId, List<Tuple<TinyFlowerData, TinyFlowerResources>> flowers,
			FabricDataOutput generator) {
		super(generator);

		this.modId = modId;
		this.flowers = flowers;
	}

	@Override
	public void generateBlockStateModels(@NonNull BlockModelGenerators blockStateModelGenerator) {
		for (Tuple<TinyFlowerData, TinyFlowerResources> tuple : this.flowers) {
			var flowerResources = tuple.getB();

			blockStateModelGenerator.modelOutput.accept(
					flowerResources.id().withPrefix(TinyFlowers.MOD_ID + "/").withSuffix("_1"),
					flowerResources.part1()::toJsonElement);
			blockStateModelGenerator.modelOutput.accept(
					flowerResources.id().withPrefix(TinyFlowers.MOD_ID + "/").withSuffix("_2"),
					flowerResources.part2()::toJsonElement);
			blockStateModelGenerator.modelOutput.accept(
					flowerResources.id().withPrefix(TinyFlowers.MOD_ID + "/").withSuffix("_3"),
					flowerResources.part3()::toJsonElement);
			blockStateModelGenerator.modelOutput.accept(
					flowerResources.id().withPrefix(TinyFlowers.MOD_ID + "/").withSuffix("_4"),
					flowerResources.part4()::toJsonElement);
		}
	}

	@Override
	public void generateItemModels(@NonNull ItemModelGenerators itemModelGenerator) {
		List<SelectItemModel.SwitchCase<TinyFlowerComponent>> list = this.flowers.stream()
				.map(tuple -> tuple.getA())
				.filter(flowerData -> !flowerData.isSegmentable())
				.map(flowerData -> ItemModelUtils.when(
						new TinyFlowerComponent(flowerData.id()),
						modelForIdentifier(flowerData.id(), itemModelGenerator)))
				.collect(Collectors.toList());

		if (list.size() == 0) {
			return;
		}

		itemModelGenerator.itemModelOutput.accept(ModItems.TINY_FLOWER_ITEM,
				ItemModelUtils.select(
						new TinyFlowerProperty(),
						modelForIdentifier(TinyFlowers.id("tiny_garden"), itemModelGenerator),
						list));
	}

	private ItemModel.Unbaked modelForIdentifier(Identifier id, ItemModelGenerators itemModelGenerator) {
		Identifier prefixed = id.withPrefix("item/");

		return ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(
				prefixed,
				TextureMapping.layer0(prefixed),
				itemModelGenerator.modelOutput));
	}

	@Override
	public String getName() {
		return "Flowers models (" + this.modId + ")";
	}
}
