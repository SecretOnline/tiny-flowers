package co.secretonline.tinyflowers.resources;

import co.secretonline.tinyflowers.data.TinyFlowerResources;
import co.secretonline.tinyflowers.helper.FlowerModelHelper;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class FabricFlowerModelDataLoader
	implements PreparableModelLoadingPlugin.DataLoader<Map<ResourceLocation, co.secretonline.tinyflowers.data.TinyFlowerResources>> {
	@Override
	public CompletableFuture<Map<ResourceLocation, TinyFlowerResources>> load(ResourceManager resourceManager, Executor executor) {
		return CompletableFuture.supplyAsync(
			() -> FlowerModelHelper.readResourceFiles(resourceManager),
			executor);
	}
}
