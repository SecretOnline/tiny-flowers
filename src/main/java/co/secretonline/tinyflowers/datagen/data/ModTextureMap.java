package co.secretonline.tinyflowers.datagen.data;

import java.util.function.Function;

import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.util.Identifier;

public class ModTextureMap {
	public static Function<Identifier, TextureMap> noStem() {
		return (itemId) -> new TextureMap()
				.put(TextureKey.PARTICLE, itemId.withPath(path -> "block/" + path))
				.put(TextureKey.FLOWERBED, itemId.withPath(path -> "block/" + path));
	}

	public static Function<Identifier, TextureMap> flowerbed() {
		return flowerbed(Identifier.ofVanilla("block/pink_petals_stem"));
	}

	public static Function<Identifier, TextureMap> flowerbed(Identifier stemIdentifier) {
		return (itemId) -> new TextureMap()
				.put(TextureKey.PARTICLE, itemId.withPath(path -> "block/" + path))
				.put(TextureKey.FLOWERBED, itemId.withPath(path -> "block/" + path))
				.put(TextureKey.STEM, stemIdentifier);
	}

	public static Function<Identifier, TextureMap> flowerbedDouble() {
		return flowerbedDouble(Identifier.ofVanilla("block/pink_petals_stem"));
	}

	public static Function<Identifier, TextureMap> flowerbedDouble(Identifier stemIdentifier) {
		return (itemId) -> new TextureMap()
				.put(TextureKey.PARTICLE, itemId.withPath(path -> "block/" + path))
				.put(TextureKey.FLOWERBED, itemId.withPath(path -> "block/" + path))
				.put(TextureKey.STEM, stemIdentifier)
				.put(ModTextureKey.FLOWERBED_UPPER, itemId.withPath(path -> "block/" + path + "_upper"));
	}

	public static Function<Identifier, TextureMap> flowerbedTriple() {
		return flowerbedTriple(Identifier.ofVanilla("block/pink_petals_stem"));
	}

	public static Function<Identifier, TextureMap> flowerbedTriple(Identifier stemIdentifier) {
		return (itemId) -> new TextureMap()
				.put(TextureKey.PARTICLE, itemId.withPath(path -> "block/" + path))
				.put(TextureKey.FLOWERBED, itemId.withPath(path -> "block/" + path))
				.put(TextureKey.STEM, stemIdentifier)
				.put(ModTextureKey.FLOWERBED_MIDDLE, itemId.withPath(path -> "block/" + path + "_middle"))
				.put(ModTextureKey.FLOWERBED_UPPER, itemId.withPath(path -> "block/" + path + "_upper"));
	}
}
