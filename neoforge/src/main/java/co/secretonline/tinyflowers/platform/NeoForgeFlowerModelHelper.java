package co.secretonline.tinyflowers.platform;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.ModelEvent;

import java.util.HashMap;
import java.util.Map;

public class NeoForgeFlowerModelHelper implements FlowerModelHelper {
	private final Map<ResourceLocation, ModelResourceLocation> knownModels = new HashMap<>();

	@Override
	public <T> void registerModel(ResourceLocation id, T context) {
		if (!(context instanceof ModelEvent.RegisterAdditional event)) {
			throw new IllegalArgumentException("Tried to register flower models with incorrect context");
		}

		ModelResourceLocation standaloneModelKey = ModelResourceLocation.standalone(id);
		event.register(standaloneModelKey);

		knownModels.put(id, standaloneModelKey);
	}

	@Override
	public void clear() {
		knownModels.clear();
	}

	@Override
	public BakedModel getModel(Minecraft client, ResourceLocation id) {
		var standaloneModelKey = knownModels.get(id);
		if (standaloneModelKey == null) {
			return null;
		}

		ModelManager modelManager = client.getModelManager();
		return modelManager.getModel(standaloneModelKey);
	}
}
