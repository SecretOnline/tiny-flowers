package co.secretonline.tinyflowers.resources;

import co.secretonline.tinyflowers.data.TinyFlowerResources;
import co.secretonline.tinyflowers.helper.FlowerModelHelper;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener.SharedState;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class FabricFlowerModelDataLoader
	implements PreparableModelLoadingPlugin.DataLoader<Map<Identifier, co.secretonline.tinyflowers.data.TinyFlowerResources>> {
	@Override
	public @NonNull CompletableFuture<Map<Identifier, TinyFlowerResources>> load(@NonNull SharedState sharedState, @NonNull Executor executor) {
		return CompletableFuture.supplyAsync(
			() -> FlowerModelHelper.readResourceFiles(sharedState.resourceManager()),
			executor);
	}
}
