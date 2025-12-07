package co.secretonline.tinyflowers.datagen.data;

import java.util.function.Function;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;

public class ModTextureMap {
	public static Function<ResourceLocation, TextureMapping> noStem() {
		return (itemId) -> new TextureMapping()
				.put(TextureSlot.PARTICLE, itemId.withPath(path -> "block/" + path))
				.put(TextureSlot.FLOWERBED, itemId.withPath(path -> "block/" + path));
	}

	public static Function<ResourceLocation, TextureMapping> flowerbed() {
		return flowerbed(ResourceLocation.withDefaultNamespace("block/pink_petals_stem"));
	}

	public static Function<ResourceLocation, TextureMapping> flowerbed(ResourceLocation stemIdentifier) {
		return (itemId) -> new TextureMapping()
				.put(TextureSlot.PARTICLE, itemId.withPath(path -> "block/" + path))
				.put(TextureSlot.FLOWERBED, itemId.withPath(path -> "block/" + path))
				.put(TextureSlot.STEM, stemIdentifier);
	}

	public static Function<ResourceLocation, TextureMapping> flowerbedDouble() {
		return flowerbedDouble(ResourceLocation.withDefaultNamespace("block/pink_petals_stem"));
	}

	public static Function<ResourceLocation, TextureMapping> flowerbedDouble(ResourceLocation stemIdentifier) {
		return (itemId) -> new TextureMapping()
				.put(TextureSlot.PARTICLE, itemId.withPath(path -> "block/" + path))
				.put(TextureSlot.FLOWERBED, itemId.withPath(path -> "block/" + path))
				.put(TextureSlot.STEM, stemIdentifier)
				.put(ModTextureKey.FLOWERBED_UPPER, itemId.withPath(path -> "block/" + path + "_upper"));
	}

	public static Function<ResourceLocation, TextureMapping> flowerbedTriple() {
		return flowerbedTriple(ResourceLocation.withDefaultNamespace("block/pink_petals_stem"));
	}

	public static Function<ResourceLocation, TextureMapping> flowerbedTriple(ResourceLocation stemIdentifier) {
		return (itemId) -> new TextureMapping()
				.put(TextureSlot.PARTICLE, itemId.withPath(path -> "block/" + path))
				.put(TextureSlot.FLOWERBED, itemId.withPath(path -> "block/" + path))
				.put(TextureSlot.STEM, stemIdentifier)
				.put(ModTextureKey.FLOWERBED_MIDDLE, itemId.withPath(path -> "block/" + path + "_middle"))
				.put(ModTextureKey.FLOWERBED_UPPER, itemId.withPath(path -> "block/" + path + "_upper"));
	}
}
