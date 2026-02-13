package co.secretonline.tinyflowers.client.model;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.Identifier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Central registry for extra baked models that aren't part of standard
 * blockstate/item JSON files. Platform-specific code populates this after
 * model baking; common code queries it at render time.
 */
public final class TinyFlowerModelHolder {
	private static final Map<Identifier, BlockStateModel> BAKED_MODELS = new ConcurrentHashMap<>();

	private TinyFlowerModelHolder() {
	}

	public static void setModel(Identifier modelId, BlockStateModel model) {
		BAKED_MODELS.put(modelId, model);
	}

	public static BlockStateModel getModel(Identifier modelId) {
		return BAKED_MODELS.get(modelId);
	}

	public static void clear() {
		BAKED_MODELS.clear();
	}
}
