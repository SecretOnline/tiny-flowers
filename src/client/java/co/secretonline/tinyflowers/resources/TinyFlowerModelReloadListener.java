package co.secretonline.tinyflowers.resources;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.data.ModRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;

public class TinyFlowerModelReloadListener implements PreparableReloadListener {
	@Override
	public CompletableFuture<Void> reload(SharedState sharedState, Executor prepareExecutor,
			PreparationBarrier preparationBarrier, Executor applyExecutor) {
		return CompletableFuture
				.runAsync(() -> this.getResourcesMap(sharedState.resourceManager()), prepareExecutor)
				.thenCompose(preparationBarrier::wait)
				.thenRunAsync(() -> {
				}, applyExecutor);
	}

	private void getResourcesMap(ResourceManager resourceManager) {
		Map<Identifier, TinyFlowerResources> map = new HashMap<>();

		var allVariantJsonFiles = resourceManager.listResources(
				ModRegistries.TINY_FLOWER.identifier()
						.withPrefix(TinyFlowers.MOD_ID + "/")
						.getPath(),
				identifier -> identifier.getPath().endsWith(".json"));

		allVariantJsonFiles.forEach((identifier, rawResource) -> {
			try (var reader = rawResource.openAsReader()) {
				JsonObject data = GsonHelper.parse(reader).getAsJsonObject();
				DataResult<TinyFlowerResources> readResult = TinyFlowerResources.CODEC.parse(JsonOps.INSTANCE, data);
				if (readResult.isError()) {
					TinyFlowers.LOGGER
							.warn("Failed to read data for tiny flower resource info " + identifier.toString() + ". Skipping");
					return;
				}

				map.put(identifier, readResult.getOrThrow());
			} catch (IOException ex) {
				TinyFlowers.LOGGER
						.warn("Failed to read data for tiny flower resource info " + identifier.toString() + ". Skipping");
			} catch (Exception ex) {
				TinyFlowers.LOGGER
						.warn("Error while reading tiny flower resource info " + identifier.toString() + ". Skipping");
			}
		});

		TinyFlowerResources.setInstances(map);
	}
}
