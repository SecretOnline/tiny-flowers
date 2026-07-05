package co.secretonline.tinyflowers.resources;

import co.secretonline.tinyflowers.TinyFlowersClientState;
import co.secretonline.tinyflowers.data.TinyFlowerResources;
import co.secretonline.tinyflowers.platform.ClientServiceLoader;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin.Context;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.util.Map;

public class FabricFlowerModelLoadingPlugin
	implements PreparableModelLoadingPlugin<Map<Identifier, co.secretonline.tinyflowers.data.TinyFlowerResources>> {

	@Override
	public void initialize(Map<Identifier, co.secretonline.tinyflowers.data.TinyFlowerResources> data, @NonNull Context pluginContext) {
		TinyFlowersClientState.RESOURCE_INSTANCES = data;

		for (var entry : data.entrySet()) {
			TinyFlowerResources resources = entry.getValue();

			ClientServiceLoader.FLOWER_MODELS.registerModel(resources.model1(), pluginContext);
			ClientServiceLoader.FLOWER_MODELS.registerModel(resources.model2(), pluginContext);
			ClientServiceLoader.FLOWER_MODELS.registerModel(resources.model3(), pluginContext);
			ClientServiceLoader.FLOWER_MODELS.registerModel(resources.model4(), pluginContext);
		}
	}
}
