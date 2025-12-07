package co.secretonline.tinyflowers.datagen.data;

import java.util.Optional;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureSlot;
import co.secretonline.tinyflowers.TinyFlowers;

public class ModModels {
	public static final ModelTemplate GARDEN_1 = block(
			"garden_1", "_1",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM);
	public static final ModelTemplate GARDEN_2 = block(
			"garden_2", "_2",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM);
	public static final ModelTemplate GARDEN_3 = block(
			"garden_3", "_3",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM);
	public static final ModelTemplate GARDEN_4 = block(
			"garden_4", "_4",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM);

	public static final ModelTemplate GARDEN_UNTINTED_1 = block(
			"garden_untinted_1", "_1",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM);
	public static final ModelTemplate GARDEN_UNTINTED_2 = block(
			"garden_untinted_2", "_2",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM);
	public static final ModelTemplate GARDEN_UNTINTED_3 = block(
			"garden_untinted_3", "_3",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM);
	public static final ModelTemplate GARDEN_UNTINTED_4 = block(
			"garden_untinted_4", "_4",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM);

	public static final ModelTemplate GARDEN_TALL_1 = block(
			"garden_tall_1", "_1",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM);
	public static final ModelTemplate GARDEN_TALL_2 = block(
			"garden_tall_2", "_2",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM);
	public static final ModelTemplate GARDEN_TALL_3 = block(
			"garden_tall_3", "_3",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM);
	public static final ModelTemplate GARDEN_TALL_4 = block(
			"garden_tall_4", "_4",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM);

	public static final ModelTemplate GARDEN_DOUBLE_1 = block(
			"garden_double_1", "_1",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM, ModTextureKey.FLOWERBED_UPPER);
	public static final ModelTemplate GARDEN_DOUBLE_2 = block(
			"garden_double_2", "_2",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM, ModTextureKey.FLOWERBED_UPPER);
	public static final ModelTemplate GARDEN_DOUBLE_3 = block(
			"garden_double_3", "_3",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM, ModTextureKey.FLOWERBED_UPPER);
	public static final ModelTemplate GARDEN_DOUBLE_4 = block(
			"garden_double_4", "_4",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM, ModTextureKey.FLOWERBED_UPPER);

	public static final ModelTemplate GARDEN_DOUBLE_UNTINTED_1 = block(
			"garden_double_untinted_1", "_1",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM, ModTextureKey.FLOWERBED_UPPER);
	public static final ModelTemplate GARDEN_DOUBLE_UNTINTED_2 = block(
			"garden_double_untinted_2", "_2",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM, ModTextureKey.FLOWERBED_UPPER);
	public static final ModelTemplate GARDEN_DOUBLE_UNTINTED_3 = block(
			"garden_double_untinted_3", "_3",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM, ModTextureKey.FLOWERBED_UPPER);
	public static final ModelTemplate GARDEN_DOUBLE_UNTINTED_4 = block(
			"garden_double_untinted_4", "_4",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM, ModTextureKey.FLOWERBED_UPPER);

	public static final ModelTemplate GARDEN_DOUBLE_UNTINTED_GLOW_1 = block(
			"garden_double_untinted_glow_1", "_1",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM, ModTextureKey.FLOWERBED_UPPER);
	public static final ModelTemplate GARDEN_DOUBLE_UNTINTED_GLOW_2 = block(
			"garden_double_untinted_glow_2", "_2",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM, ModTextureKey.FLOWERBED_UPPER);
	public static final ModelTemplate GARDEN_DOUBLE_UNTINTED_GLOW_3 = block(
			"garden_double_untinted_glow_3", "_3",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM, ModTextureKey.FLOWERBED_UPPER);
	public static final ModelTemplate GARDEN_DOUBLE_UNTINTED_GLOW_4 = block(
			"garden_double_untinted_glow_4", "_4",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM, ModTextureKey.FLOWERBED_UPPER);

	public static final ModelTemplate GARDEN_TRIPLE_UNTINTED_1 = block(
			"garden_triple_untinted_1", "_1",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM,
			ModTextureKey.FLOWERBED_MIDDLE, ModTextureKey.FLOWERBED_UPPER);
	public static final ModelTemplate GARDEN_TRIPLE_UNTINTED_2 = block(
			"garden_triple_untinted_2", "_2",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM,
			ModTextureKey.FLOWERBED_MIDDLE, ModTextureKey.FLOWERBED_UPPER);
	public static final ModelTemplate GARDEN_TRIPLE_UNTINTED_3 = block(
			"garden_triple_untinted_3", "_3",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM,
			ModTextureKey.FLOWERBED_MIDDLE, ModTextureKey.FLOWERBED_UPPER);
	public static final ModelTemplate GARDEN_TRIPLE_UNTINTED_4 = block(
			"garden_triple_untinted_4", "_4",
			TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM,
			ModTextureKey.FLOWERBED_MIDDLE, ModTextureKey.FLOWERBED_UPPER);

	public static final ModelTemplate GARDEN_LEAF_LITTER_1 = block(
			"garden_leaf_litter_1", "_1", TextureSlot.PARTICLE, TextureSlot.FLOWERBED);
	public static final ModelTemplate GARDEN_LEAF_LITTER_2 = block(
			"garden_leaf_litter_2", "_2", TextureSlot.PARTICLE, TextureSlot.FLOWERBED);
	public static final ModelTemplate GARDEN_LEAF_LITTER_3 = block(
			"garden_leaf_litter_3", "_3", TextureSlot.PARTICLE, TextureSlot.FLOWERBED);
	public static final ModelTemplate GARDEN_LEAF_LITTER_4 = block(
			"garden_leaf_litter_4", "_4", TextureSlot.PARTICLE, TextureSlot.FLOWERBED);

	public static final ModelTemplate GARDEN_LOW_UNTINTED_1 = block(
			"garden_low_untinted_1", "_1", TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM);
	public static final ModelTemplate GARDEN_LOW_UNTINTED_2 = block(
			"garden_low_untinted_2", "_2", TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM);
	public static final ModelTemplate GARDEN_LOW_UNTINTED_3 = block(
			"garden_low_untinted_3", "_3", TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM);
	public static final ModelTemplate GARDEN_LOW_UNTINTED_4 = block(
			"garden_low_untinted_4", "_4", TextureSlot.PARTICLE, TextureSlot.FLOWERBED, TextureSlot.STEM);

	private static ModelTemplate block(String parent, String variant, TextureSlot... requiredTextureKeys) {
		return new ModelTemplate(Optional.of(TinyFlowers.id("block/" + parent)), Optional.of(variant), requiredTextureKeys);
	}

	public record Quartet(ModelTemplate model1, ModelTemplate model2, ModelTemplate model3, ModelTemplate model4) {
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

		public static Quartet GARDEN_LEAF_LITTER = new Quartet(
				GARDEN_LEAF_LITTER_1, GARDEN_LEAF_LITTER_2,
				GARDEN_LEAF_LITTER_3, GARDEN_LEAF_LITTER_4);

		public static Quartet GARDEN_LOW_UNTINTED = new Quartet(
				GARDEN_LOW_UNTINTED_1, GARDEN_LOW_UNTINTED_2,
				GARDEN_LOW_UNTINTED_3, GARDEN_LOW_UNTINTED_4);
	}
}
