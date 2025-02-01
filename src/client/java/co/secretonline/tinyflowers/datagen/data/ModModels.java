package co.secretonline.tinyflowers.datagen.data;

import java.util.Optional;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.client.data.Model;
import net.minecraft.client.data.TextureKey;

public class ModModels {
	public static final Model GARDEN_1 = block(
			"garden_1", "_1",
			TextureKey.FLOWERBED, TextureKey.STEM);
	public static final Model GARDEN_2 = block(
			"garden_2", "_2",
			TextureKey.FLOWERBED, TextureKey.STEM);
	public static final Model GARDEN_3 = block(
			"garden_3", "_3",
			TextureKey.FLOWERBED, TextureKey.STEM);
	public static final Model GARDEN_4 = block(
			"garden_4", "_4",
			TextureKey.FLOWERBED, TextureKey.STEM);

	public static final Model GARDEN_UNTINTED_1 = block(
			"garden_untinted_1", "_1",
			TextureKey.FLOWERBED, TextureKey.STEM);
	public static final Model GARDEN_UNTINTED_2 = block(
			"garden_untinted_2", "_2",
			TextureKey.FLOWERBED, TextureKey.STEM);
	public static final Model GARDEN_UNTINTED_3 = block(
			"garden_untinted_3", "_3",
			TextureKey.FLOWERBED, TextureKey.STEM);
	public static final Model GARDEN_UNTINTED_4 = block(
			"garden_untinted_4", "_4",
			TextureKey.FLOWERBED, TextureKey.STEM);

	public static final Model GARDEN_TALL_1 = block(
			"garden_tall_1", "_1",
			TextureKey.FLOWERBED, TextureKey.STEM);
	public static final Model GARDEN_TALL_2 = block(
			"garden_tall_2", "_2",
			TextureKey.FLOWERBED, TextureKey.STEM);
	public static final Model GARDEN_TALL_3 = block(
			"garden_tall_3", "_3",
			TextureKey.FLOWERBED, TextureKey.STEM);
	public static final Model GARDEN_TALL_4 = block(
			"garden_tall_4", "_4",
			TextureKey.FLOWERBED, TextureKey.STEM);

	public static final Model GARDEN_DOUBLE_1 = block(
			"garden_double_1", "_1",
			TextureKey.FLOWERBED, TextureKey.STEM, ModTextureKey.FLOWERBED_UPPER);
	public static final Model GARDEN_DOUBLE_2 = block(
			"garden_double_2", "_2",
			TextureKey.FLOWERBED, TextureKey.STEM, ModTextureKey.FLOWERBED_UPPER);
	public static final Model GARDEN_DOUBLE_3 = block(
			"garden_double_3", "_3",
			TextureKey.FLOWERBED, TextureKey.STEM, ModTextureKey.FLOWERBED_UPPER);
	public static final Model GARDEN_DOUBLE_4 = block(
			"garden_double_4", "_4",
			TextureKey.FLOWERBED, TextureKey.STEM, ModTextureKey.FLOWERBED_UPPER);

	public static final Model GARDEN_DOUBLE_UNTINTED_1 = block(
			"garden_double_untinted_1", "_1",
			TextureKey.FLOWERBED, TextureKey.STEM, ModTextureKey.FLOWERBED_UPPER);
	public static final Model GARDEN_DOUBLE_UNTINTED_2 = block(
			"garden_double_untinted_2", "_2",
			TextureKey.FLOWERBED, TextureKey.STEM, ModTextureKey.FLOWERBED_UPPER);
	public static final Model GARDEN_DOUBLE_UNTINTED_3 = block(
			"garden_double_untinted_3", "_3",
			TextureKey.FLOWERBED, TextureKey.STEM, ModTextureKey.FLOWERBED_UPPER);
	public static final Model GARDEN_DOUBLE_UNTINTED_4 = block(
			"garden_double_untinted_4", "_4",
			TextureKey.FLOWERBED, TextureKey.STEM, ModTextureKey.FLOWERBED_UPPER);

	public static final Model GARDEN_DOUBLE_UNTINTED_GLOW_1 = block(
			"garden_double_untinted_glow_1", "_1",
			TextureKey.FLOWERBED, TextureKey.STEM, ModTextureKey.FLOWERBED_UPPER);
	public static final Model GARDEN_DOUBLE_UNTINTED_GLOW_2 = block(
			"garden_double_untinted_glow_2", "_2",
			TextureKey.FLOWERBED, TextureKey.STEM, ModTextureKey.FLOWERBED_UPPER);
	public static final Model GARDEN_DOUBLE_UNTINTED_GLOW_3 = block(
			"garden_double_untinted_glow_3", "_3",
			TextureKey.FLOWERBED, TextureKey.STEM, ModTextureKey.FLOWERBED_UPPER);
	public static final Model GARDEN_DOUBLE_UNTINTED_GLOW_4 = block(
			"garden_double_untinted_glow_4", "_4",
			TextureKey.FLOWERBED, TextureKey.STEM, ModTextureKey.FLOWERBED_UPPER);

	public static final Model GARDEN_TRIPLE_UNTINTED_1 = block(
			"garden_triple_untinted_1", "_1",
			TextureKey.FLOWERBED, TextureKey.STEM, ModTextureKey.FLOWERBED_MIDDLE, ModTextureKey.FLOWERBED_UPPER);
	public static final Model GARDEN_TRIPLE_UNTINTED_2 = block(
			"garden_triple_untinted_2", "_2",
			TextureKey.FLOWERBED, TextureKey.STEM, ModTextureKey.FLOWERBED_MIDDLE, ModTextureKey.FLOWERBED_UPPER);
	public static final Model GARDEN_TRIPLE_UNTINTED_3 = block(
			"garden_triple_untinted_3", "_3",
			TextureKey.FLOWERBED, TextureKey.STEM, ModTextureKey.FLOWERBED_MIDDLE, ModTextureKey.FLOWERBED_UPPER);
	public static final Model GARDEN_TRIPLE_UNTINTED_4 = block(
			"garden_triple_untinted_4", "_4",
			TextureKey.FLOWERBED, TextureKey.STEM, ModTextureKey.FLOWERBED_MIDDLE, ModTextureKey.FLOWERBED_UPPER);

	private static Model block(String parent, String variant, TextureKey... requiredTextureKeys) {
		return new Model(Optional.of(TinyFlowers.id("block/" + parent)), Optional.of(variant), requiredTextureKeys);
	}

	public record Quartet(Model model1, Model model2, Model model3, Model model4) {
		public static Quartet GARDEN = new Quartet(
				GARDEN_1, GARDEN_2,
				GARDEN_3, GARDEN_4);

		public static Quartet GARDEN_UNTINTED = new Quartet(
				GARDEN_UNTINTED_1, GARDEN_UNTINTED_2,
				GARDEN_UNTINTED_3, GARDEN_UNTINTED_4);

		public static Quartet GARDEN_TALL = new Quartet(
				GARDEN_TALL_1, GARDEN_TALL_2,
				GARDEN_TALL_3, GARDEN_TALL_4);

		public static Quartet GARDEN_DOUBLE = new Quartet(
				GARDEN_DOUBLE_1, GARDEN_DOUBLE_2,
				GARDEN_DOUBLE_3, GARDEN_DOUBLE_4);

		public static Quartet GARDEN_DOUBLE_UNTINTED = new Quartet(
				GARDEN_DOUBLE_UNTINTED_1, GARDEN_DOUBLE_UNTINTED_2,
				GARDEN_DOUBLE_UNTINTED_3, GARDEN_DOUBLE_UNTINTED_4);

		public static Quartet GARDEN_DOUBLE_UNTINTED_GLOW = new Quartet(
				GARDEN_DOUBLE_UNTINTED_GLOW_1, GARDEN_DOUBLE_UNTINTED_GLOW_2,
				GARDEN_DOUBLE_UNTINTED_GLOW_3, GARDEN_DOUBLE_UNTINTED_GLOW_4);

		public static Quartet GARDEN_TRIPLE_UNTINTED = new Quartet(
				GARDEN_TRIPLE_UNTINTED_1, GARDEN_TRIPLE_UNTINTED_2,
				GARDEN_TRIPLE_UNTINTED_3, GARDEN_TRIPLE_UNTINTED_4);
	}
}
