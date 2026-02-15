package co.secretonline.tinyflowers.platform;

import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.FabricModelManager;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class FabricFlowerModelHelper implements FlowerModelHelper {
	private final Map<Identifier, ExtraModelKey<BlockStateModel>> knownModels = new HashMap<>();

	@Override
	public <T> void registerModel(@NonNull Identifier id, @NonNull T context) {
		if (!(context instanceof ModelLoadingPlugin.Context pluginContext)) {
			throw new IllegalArgumentException("Tried to register flower models with incorrect context");
		}

		ExtraModelKey<BlockStateModel> extraModelKey = ExtraModelKey.create(id::toString);
		pluginContext.addModel(extraModelKey, SimpleUnbakedExtraModel.blockStateModel(id));

		knownModels.put(id, extraModelKey);
	}

	@Override
	public void clear() {
		knownModels.clear();
	}

	@Override
	public @Nullable BlockStateModel getModel(@NonNull Minecraft client, @NonNull Identifier id) {
		var extraModelKey = knownModels.get(id);
		if (extraModelKey == null) {
			return null;
		}

		FabricModelManager modelManager = client.getModelManager();
		return modelManager.getModel(extraModelKey);
	}
}
