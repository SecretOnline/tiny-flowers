package co.secretonline.tinyflowers.platform;

import net.fabricmc.fabric.api.client.model.loading.v1.FabricBakedModelManager;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class FabricFlowerModelHelper implements FlowerModelHelper {
	private final Set<ResourceLocation> knownModels = new HashSet<>();

	@Override
	public <T> void registerModel(ResourceLocation id, T context) {
		if (!(context instanceof ModelLoadingPlugin.Context pluginContext)) {
			throw new IllegalArgumentException("Tried to register flower models with incorrect context");
		}

		pluginContext.addModels(id);

		knownModels.add(id);
	}

	@Override
	public void clear() {
		knownModels.clear();
	}

	@Override
	public BakedModel getModel(Minecraft client, ResourceLocation id) {
		if (!knownModels.contains(id)) {
			return null;
		}

		FabricBakedModelManager modelManager = client.getModelManager();
		return modelManager.getModel(id);
	}
}
