package co.secretonline.tinyflowers.platform;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class NeoForgeFlowerModelHelper implements FlowerModelHelper {
	private final Map<Identifier, StandaloneModelKey<BlockStateModel>> knownModels = new HashMap<>();

	@Override
	public <T> void registerModel(@NonNull Identifier id, @NonNull T context) {
		if (!(context instanceof ModelEvent.RegisterStandalone event)) {
			throw new IllegalArgumentException("Tried to register flower models with incorrect context");
		}

		StandaloneModelKey<BlockStateModel> standaloneModelKey = new StandaloneModelKey<>(new TinyFlowersModelDebugName(id));
		event.register(standaloneModelKey, SimpleUnbakedStandaloneModel.blockStateModel(id));

		knownModels.put(id, standaloneModelKey);
	}

	@Override
	public void clear() {
		knownModels.clear();
	}

	@Override
	public @Nullable BlockStateModel getModel(@NonNull Minecraft client, @NonNull Identifier id) {
		var standaloneModelKey = knownModels.get(id);
		if (standaloneModelKey == null) {
			return null;
		}

		ModelManager modelManager = client.getModelManager();
		return modelManager.getStandaloneModel(standaloneModelKey);
	}

	private record TinyFlowersModelDebugName(Identifier id) implements ModelDebugName {
		@Override
		public @NonNull String debugName() {
			return TinyFlowers.MOD_ID + ":ModelKey[" + id.toString() + "]";
		}
	}
}
