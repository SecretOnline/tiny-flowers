package co.secretonline.tinyflowers.datagen;

import java.util.List;
import java.util.stream.Collectors;

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
import net.minecraft.client.renderer.item.SelectItemModel;

public class TinyFlowersItemModelProvider extends FabricModelProvider {
	private final List<TinyFlowerData> flowers;

	public TinyFlowersItemModelProvider(List<TinyFlowerData> flowers, FabricDataOutput generator) {
		super(generator);

		this.flowers = flowers;
	}

	public static Pack.Factory<TinyFlowersItemModelProvider> factoryFor(List<TinyFlowerData> flowers) {
		return (FabricDataOutput output) -> new TinyFlowersItemModelProvider(flowers, output);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
	}

	@Override
	public void generateItemModels(ItemModelGenerators itemModelGenerator) {
		List<SelectItemModel.SwitchCase<TinyFlowerComponent>> list = this.flowers.stream()
				.filter(flowerData -> flowerData.shouldCreateItem())
				.map(flowerData -> ItemModelUtils.when(
						new TinyFlowerComponent(flowerData.id()),
						ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(
								flowerData.id().withPrefix("item/"),
								TextureMapping.layer0(flowerData.id().withPrefix("item/")),
								itemModelGenerator.modelOutput))))
				.collect(Collectors.toList());

		itemModelGenerator.itemModelOutput.accept(ModItems.TINY_FLOWER_ITEM,
				ItemModelUtils.select(new TinyFlowerProperty(), list));
	}

	@Override
	public String getName() {
		return "TinyFlowersItemModelProvider";
	}
}
