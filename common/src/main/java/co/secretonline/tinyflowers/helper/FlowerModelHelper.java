package co.secretonline.tinyflowers.helper;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.data.ModRegistries;
import co.secretonline.tinyflowers.data.TinyFlowerResources;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class FlowerModelHelper {
	public static Map<Identifier, TinyFlowerResources> readResourceFiles(ResourceManager resourceManager) {
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
						.warn("Failed to parse data for tiny flower resource info {}. Skipping", identifier);
					return;
				}

				TinyFlowerResources resources = readResult.getOrThrow();
				map.put(resources.id(), resources);
			} catch (IOException ex) {
				TinyFlowers.LOGGER
					.warn("Failed to read data for tiny flower resource info {}. Skipping", identifier);
			} catch (Exception ex) {
				TinyFlowers.LOGGER
					.warn("Error while reading tiny flower resource info {}. Skipping", identifier);
			}
		});

		return map;
	}
}
