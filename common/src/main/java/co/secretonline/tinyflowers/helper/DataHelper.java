package co.secretonline.tinyflowers.helper;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class DataHelper {
	public static <T> CompletableFuture<?> saveAll(CachedOutput cachedOutput, Codec<T> codec, PackOutput.PathProvider pathProvider, Map<ResourceLocation, T> map) {
		return saveAll(cachedOutput, codec, pathProvider::json, map);
	}

	public static <T, E> CompletableFuture<?> saveAll(CachedOutput cachedOutput, Codec<E> codec, Function<T, Path> pathFunction, Map<T, E> map) {
		return saveAll(cachedOutput, object -> codec.encodeStart(JsonOps.INSTANCE, object).getOrThrow(), pathFunction, map);
	}

	public static <T, E> CompletableFuture<?> saveAll(CachedOutput cachedOutput, Function<E, JsonElement> elementFunction, Function<T, Path> pathFunction, Map<T, E> map) {
		return CompletableFuture.allOf(map.entrySet().stream().map(entry -> {
			Path path = pathFunction.apply(entry.getKey());
			JsonElement jsonElement = elementFunction.apply(entry.getValue());
			return DataProvider.saveStable(cachedOutput, jsonElement, path);
		}).toArray(CompletableFuture[]::new));
	}
}
