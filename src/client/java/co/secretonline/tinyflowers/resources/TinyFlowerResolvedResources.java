package co.secretonline.tinyflowers.resources;

import java.util.HashMap;
import java.util.Map;

import co.secretonline.tinyflowers.resources.TinyFlowerResources.TintSource;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.Identifier;

public record TinyFlowerResolvedResources(Identifier id, Identifier itemTexture, Identifier particleTexture,
		Part model1, Part model2, Part model3, Part model4) {
	private static Map<Identifier, TinyFlowerResolvedResources> INSTANCES = new HashMap<>();

	public static Map<Identifier, TinyFlowerResolvedResources> getInstances() {
		return INSTANCES;
	}

	public static void setInstances(Map<Identifier, TinyFlowerResolvedResources> map) {
		INSTANCES = map;
	}

	public static record Part(Identifier modelId, ExtraModelKey<BlockStateModel> extraModelKey, TintSource tintSource) {
	}
}
