package co.secretonline.tinyflowers.resources;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.data.ModRegistries;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener.SharedState;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;

public class TinyFlowerModelDataLoader
		implements PreparableModelLoadingPlugin.DataLoader<Map<Identifier, TinyFlowerResolvedResources>> {
	@Override
	public CompletableFuture<Map<Identifier, TinyFlowerResolvedResources>> load(SharedState sharedState,
			Executor executor) {
		return CompletableFuture
				.supplyAsync(() -> this.readResourceFiles(sharedState.resourceManager()), executor)
				.thenApplyAsync((map) -> this.resolveAll(map), executor);
	}

	private Map<Identifier, TinyFlowerResources> readResourceFiles(ResourceManager resourceManager) {
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

				TinyFlowerResources resources = readResult.getOrThrow();
				map.put(resources.id(), resources);
			} catch (IOException ex) {
				TinyFlowers.LOGGER
						.warn("Failed to read data for tiny flower resource info " + identifier.toString() + ". Skipping");
			} catch (Exception ex) {
				TinyFlowers.LOGGER
						.warn("Error while reading tiny flower resource info " + identifier.toString() + ". Skipping");
			}
		});

		return map;
	}

	private Map<Identifier, TinyFlowerResolvedResources> resolveAll(Map<Identifier, TinyFlowerResources> map) {
		return map.entrySet()
				.stream()
				.map(entry -> {
					var resources = entry.getValue();

					return new AbstractMap.SimpleEntry<>(entry.getKey(),
							new TinyFlowerResolvedResources(resources.id(), resources.itemTexture(), resources.particleTexture(),
									new TinyFlowerResolvedResources.Part(resources.model1().id(),
											ExtraModelKey.create(resources.model1().id()::toString),
											resources.model1().tintSource()),
									new TinyFlowerResolvedResources.Part(resources.model2().id(),
											ExtraModelKey.create(resources.model2().id()::toString),
											resources.model2().tintSource()),
									new TinyFlowerResolvedResources.Part(resources.model3().id(),
											ExtraModelKey.create(resources.model3().id()::toString),
											resources.model3().tintSource()),
									new TinyFlowerResolvedResources.Part(resources.model4().id(),
											ExtraModelKey.create(resources.model4().id()::toString),
											resources.model4().tintSource())));
				})
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						Map.Entry::getValue));
	}
}
