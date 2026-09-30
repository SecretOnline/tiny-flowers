package co.secretonline.tinyflowers.resources;

import co.secretonline.tinyflowers.TinyFlowersClientState;
import co.secretonline.tinyflowers.data.TinyFlowerResources;
import co.secretonline.tinyflowers.platform.ClientServiceLoader;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin.Context;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class FabricFlowerModelLoadingPlugin
	implements PreparableModelLoadingPlugin<Map<ResourceLocation, co.secretonline.tinyflowers.data.TinyFlowerResources>> {

	@Override
	public void onInitializeModelLoader(Map<ResourceLocation, co.secretonline.tinyflowers.data.TinyFlowerResources> data, Context pluginContext) {
		TinyFlowersClientState.RESOURCE_INSTANCES = data;

		for (var entry : data.entrySet()) {
			TinyFlowerResources resources = entry.getValue();

			ClientServiceLoader.FLOWER_MODELS.registerModel(resources.model1(), pluginContext);
			ClientServiceLoader.FLOWER_MODELS.registerModel(resources.model2(), pluginContext);
			ClientServiceLoader.FLOWER_MODELS.registerModel(resources.model3(), pluginContext);
			ClientServiceLoader.FLOWER_MODELS.registerModel(resources.model4(), pluginContext);

			if (resources.modelPotted().isPresent()) {
				ClientServiceLoader.FLOWER_MODELS.registerModel(resources.modelPotted().get(), pluginContext);
			}
		}
	}
}
