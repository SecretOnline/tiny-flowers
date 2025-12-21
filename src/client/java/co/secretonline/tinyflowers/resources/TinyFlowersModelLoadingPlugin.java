package co.secretonline.tinyflowers.resources;

import java.util.Map;

import co.secretonline.tinyflowers.items.ModItems;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin.Context;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;
import net.minecraft.resources.Identifier;

public class TinyFlowersModelLoadingPlugin
		implements PreparableModelLoadingPlugin<Map<Identifier, TinyFlowerResources>> {

	@Override
	public void initialize(Map<Identifier, TinyFlowerResources> data, Context pluginContext) {
		TinyFlowerResources.setInstances(data);

		for (var entry : data.entrySet()) {
			TinyFlowerResources resources = entry.getValue();

			pluginContext.addModel(
					ExtraModelKey.create(resources.modelPart1()::toString),
					SimpleUnbakedExtraModel.blockStateModel(resources.modelPart1()));
			pluginContext.addModel(
					ExtraModelKey.create(resources.modelPart2()::toString),
					SimpleUnbakedExtraModel.blockStateModel(resources.modelPart2()));
			pluginContext.addModel(
					ExtraModelKey.create(resources.modelPart3()::toString),
					SimpleUnbakedExtraModel.blockStateModel(resources.modelPart3()));
			pluginContext.addModel(
					ExtraModelKey.create(resources.modelPart4()::toString),
					SimpleUnbakedExtraModel.blockStateModel(resources.modelPart4()));
		}

		pluginContext.modifyItemModelBeforeBake().register((model, itemContext) -> {
			if (itemContext.itemId().equals(ModItems.TINY_FLOWER_ITEM_KEY.identifier())) {
			}

			return model;
		});
	}

}
