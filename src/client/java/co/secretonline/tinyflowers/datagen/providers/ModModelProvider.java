package co.secretonline.tinyflowers.datagen.providers;

import java.util.List;
import java.util.stream.Collectors;

import org.jspecify.annotations.NonNull;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.components.TinyFlowerComponent;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.items.ModItems;
import co.secretonline.tinyflowers.renderer.item.TinyFlowerProperty;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.resources.Identifier;

public class ModModelProvider extends FabricModelProvider {
	private final String modId;
	private final List<TinyFlowerData> flowers;

	public ModModelProvider(String modId, List<TinyFlowerData> flowers, FabricDataOutput generator) {
		super(generator);

		this.modId = modId;
		this.flowers = flowers;
	}

	public static Pack.Factory<ModModelProvider> factoryFor(String modName, List<TinyFlowerData> flowers) {
		return (FabricDataOutput output) -> new ModModelProvider(modName, flowers, output);
	}

	@Override
	public void generateBlockStateModels(@NonNull BlockModelGenerators blockStateModelGenerator) {
	}

	@Override
	public void generateItemModels(@NonNull ItemModelGenerators itemModelGenerator) {
		List<SelectItemModel.SwitchCase<TinyFlowerComponent>> list = this.flowers.stream()
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
