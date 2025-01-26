package co.secretonline.tinyflowers.datagen.data;

import net.minecraft.client.data.TextureKey;
import net.minecraft.client.data.TextureMap;
import net.minecraft.util.Identifier;

public class ModTextureMap {
	public static TextureMap flowerbed(Identifier itemId) {
		return new TextureMap()
				.put(TextureKey.FLOWERBED, itemId.withPath(path -> "block/" + path))
				.put(TextureKey.STEM, Identifier.ofVanilla("block/pink_petals_stem"));
	}

	public static TextureMap flowerbedStem(Identifier itemId) {
		return new TextureMap()
				.put(TextureKey.FLOWERBED, itemId.withPath(path -> "block/" + path))
				.put(TextureKey.STEM, itemId.withPath(path -> "block/" + path + "_stem"));
	}

	public static TextureMap flowerbedDouble(Identifier itemId) {
		return new TextureMap()
				.put(TextureKey.FLOWERBED, itemId.withPath(path -> "block/" + path))
				.put(TextureKey.STEM, itemId.withPath(path -> "block/" + path + "_stem"))
				.put(ModTextureKey.FLOWERBED_UPPER, itemId.withPath(path -> "block/" + path + "_upper"));
	}
}
