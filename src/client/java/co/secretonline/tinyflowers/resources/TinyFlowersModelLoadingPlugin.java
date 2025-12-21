package co.secretonline.tinyflowers.resources;

import java.util.Map;

import co.secretonline.tinyflowers.items.ModItems;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin.Context;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.Identifier;

public class TinyFlowersModelLoadingPlugin
		implements PreparableModelLoadingPlugin<Map<Identifier, TinyFlowerResolvedResources>> {

	@Override
	public void initialize(Map<Identifier, TinyFlowerResolvedResources> data, Context pluginContext) {
		TinyFlowerResolvedResources.setInstances(data);

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
		}

		pluginContext.modifyItemModelBeforeBake().register((model, itemContext) -> {
			if (itemContext.itemId().equals(ModItems.TINY_FLOWER_ITEM_KEY.identifier())) {
			}

			return model;
		});
	}

}
