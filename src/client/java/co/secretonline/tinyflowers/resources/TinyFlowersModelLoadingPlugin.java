package co.secretonline.tinyflowers.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.components.TinyFlowerComponent;
import co.secretonline.tinyflowers.items.ModItems;
import co.secretonline.tinyflowers.renderer.item.TinyFlowerProperty;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin.Context;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.resources.Identifier;

public class TinyFlowersModelLoadingPlugin
		implements PreparableModelLoadingPlugin<Map<Identifier, TinyFlowerResolvedResources>> {

	@Override
	public void initialize(Map<Identifier, TinyFlowerResolvedResources> data, Context pluginContext) {
		TinyFlowerResolvedResources.setInstances(data);

		List<SelectItemModel.SwitchCase<TinyFlowerComponent>> itemModels = new ArrayList<>();

		for (var entry : data.entrySet()) {
			TinyFlowerResolvedResources resources = entry.getValue();

			pluginContext.addModel(
					resources.model1().extraModelKey(),
					SimpleUnbakedExtraModel.blockStateModel(resources.model1().modelId()));
			pluginContext.addModel(
					resources.model2().extraModelKey(),
					SimpleUnbakedExtraModel.blockStateModel(resources.model2().modelId()));
			pluginContext.addModel(
					resources.model3().extraModelKey(),
					SimpleUnbakedExtraModel.blockStateModel(resources.model3().modelId()));
			pluginContext.addModel(
					resources.model4().extraModelKey(),
					SimpleUnbakedExtraModel.blockStateModel(resources.model4().modelId()));

			itemModels.add(ItemModelUtils.when(
					new TinyFlowerComponent(resources.id()),
					modelForIdentifier(resources.itemModel())));
		}

		pluginContext.modifyItemModelBeforeBake().register((model, itemContext) -> {
			if (itemContext.itemId().equals(ModItems.TINY_FLOWER_ITEM_KEY.identifier())) {
				return ItemModelUtils.select(
						new TinyFlowerProperty(),
						modelForIdentifier(TinyFlowers.id("item/tiny_garden")),
						itemModels);
			}

			return model;
		});
	}

	private ItemModel.Unbaked modelForIdentifier(Identifier id) {
		return ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(
				id,
				TextureMapping.layer0(id),
				this::consumeModel));
	}

	private void consumeModel(Identifier id, ModelInstance model) {
		// TODO: if this ever
	}
}
