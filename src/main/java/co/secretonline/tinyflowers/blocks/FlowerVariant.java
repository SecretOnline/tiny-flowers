package co.secretonline.tinyflowers.blocks;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

public enum FlowerVariant implements StringIdentifiable {
	EMPTY(TinyFlowers.id("empty")),
	PINK_PETALS(Registries.BLOCK.getId(Blocks.PINK_PETALS), false),
	TINY_RED_TULIP(TinyFlowers.id("tiny_red_tulip"), true);

	public final Identifier identifier;
	public final Models models;
	public final boolean shouldGenerateItemModel;

	private FlowerVariant(Identifier identifier) {
		this.identifier = identifier;
		this.models = Models.ofEmpty();
		this.shouldGenerateItemModel = false;
	}

	private FlowerVariant(Identifier identifier, boolean shouldGenerateItemModel) {
		this.identifier = identifier;
		this.models = Models.ofBlockId(identifier);
		this.shouldGenerateItemModel = shouldGenerateItemModel;
	}

	@Override
	public String asString() {
		return this.identifier.toString().replaceAll("\\W", "_");
	}

	public static FlowerVariant fromItem(ItemConvertible item) {
		Identifier itemId = Registries.ITEM.getId(item.asItem());

		for (FlowerVariant variant : values()) {
			if (variant.identifier.equals(itemId)) {
				return variant;
			}
		}

		return EMPTY;
	}

	public static class Models {
		public final Identifier model1;
		public final Identifier model2;
		public final Identifier model3;
		public final Identifier model4;

		private Models(
				Identifier model1, Identifier model2,
				Identifier model3, Identifier model4) {
			this.model1 = model1;
			this.model2 = model2;
			this.model3 = model3;
			this.model4 = model4;
		}

		public static Models ofBlockId(Identifier blockId) {
			return new Models(
					blockId.withPath(path -> "block/" + path + "_1"),
					blockId.withPath(path -> "block/" + path + "_2"),
					blockId.withPath(path -> "block/" + path + "_3"),
					blockId.withPath(path -> "block/" + path + "_4"));
		}

		public static Models ofEmpty() {
			Identifier airId = Registries.BLOCK.getId(Blocks.AIR);
			Identifier modelId = airId.withPath(path -> "block/" + path);

			return new Models(modelId, modelId, modelId, modelId);
		}
	}
}
