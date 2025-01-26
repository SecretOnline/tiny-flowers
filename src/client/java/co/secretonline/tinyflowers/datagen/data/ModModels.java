package co.secretonline.tinyflowers.datagen.data;

import java.util.Optional;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.client.data.Model;
import net.minecraft.client.data.Models;
import net.minecraft.client.data.TextureKey;

public class ModModels {
	public static final Model FLOWERBED_STEM_1 = block(
			"flowerbed_stem_1", "_1",
			TextureKey.FLOWERBED, TextureKey.STEM);
	public static final Model FLOWERBED_STEM_2 = block(
			"flowerbed_stem_2", "_2",
			TextureKey.FLOWERBED, TextureKey.STEM);
	public static final Model FLOWERBED_STEM_3 = block(
			"flowerbed_stem_3", "_3",
			TextureKey.FLOWERBED, TextureKey.STEM);
	public static final Model FLOWERBED_STEM_4 = block(
			"flowerbed_stem_4", "_4",
			TextureKey.FLOWERBED, TextureKey.STEM);

	public static final Model FLOWERBED_DOUBLE_GLOW_1 = block(
			"flowerbed_double_glow_1", "_1",
			TextureKey.FLOWERBED, TextureKey.STEM, ModTextureKey.FLOWERBED_UPPER);
	public static final Model FLOWERBED_DOUBLE_GLOW_2 = block(
			"flowerbed_double_glow_2", "_2",
			TextureKey.FLOWERBED, TextureKey.STEM, ModTextureKey.FLOWERBED_UPPER);
	public static final Model FLOWERBED_DOUBLE_GLOW_3 = block(
			"flowerbed_double_glow_3", "_3",
			TextureKey.FLOWERBED, TextureKey.STEM, ModTextureKey.FLOWERBED_UPPER);
	public static final Model FLOWERBED_DOUBLE_GLOW_4 = block(
			"flowerbed_double_glow_4", "_4",
			TextureKey.FLOWERBED, TextureKey.STEM, ModTextureKey.FLOWERBED_UPPER);

	private static Model block(String parent, String variant, TextureKey... requiredTextureKeys) {
		return new Model(Optional.of(TinyFlowers.id("block/" + parent)), Optional.of(variant), requiredTextureKeys);
	}

	public record Quartet(Model model1, Model model2, Model model3, Model model4) {
		public static Quartet FLOWERBED = new Quartet(
				Models.FLOWERBED_1, Models.FLOWERBED_2,
				Models.FLOWERBED_3, Models.FLOWERBED_4);

		public static Quartet FLOWERBED_STEM = new Quartet(
				FLOWERBED_STEM_1, FLOWERBED_STEM_2,
				FLOWERBED_STEM_3, FLOWERBED_STEM_4);

		public static Quartet FLOWERBED_DOUBLE_GLOW = new Quartet(
				FLOWERBED_DOUBLE_GLOW_1, FLOWERBED_DOUBLE_GLOW_2,
				FLOWERBED_DOUBLE_GLOW_3, FLOWERBED_DOUBLE_GLOW_4);
	}
}
