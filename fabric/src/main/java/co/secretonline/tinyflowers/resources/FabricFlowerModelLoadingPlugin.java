package co.secretonline.tinyflowers.resources;

import co.secretonline.tinyflowers.platform.Services;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin.Context;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.util.Map;

public class FabricFlowerModelLoadingPlugin
	implements PreparableModelLoadingPlugin<Map<Identifier, TinyFlowerResources>> {

	@Override
	public void initialize(Map<Identifier, TinyFlowerResources> data, @NonNull Context pluginContext) {
		for (var entry : data.entrySet()) {
			TinyFlowerResources resources = entry.getValue();

			Services.FLOWER_MODELS.registerModel(resources.model1(), pluginContext);
			Services.FLOWER_MODELS.registerModel(resources.model2(), pluginContext);
			Services.FLOWER_MODELS.registerModel(resources.model3(), pluginContext);
			Services.FLOWER_MODELS.registerModel(resources.model4(), pluginContext);
		}
	}
}
