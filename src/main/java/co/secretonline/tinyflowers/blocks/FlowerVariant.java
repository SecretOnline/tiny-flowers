package co.secretonline.tinyflowers.blocks;

import org.jetbrains.annotations.Nullable;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

public enum FlowerVariant implements StringIdentifiable {
	EMPTY(TinyFlowers.id("empty")),
	PINK_PETALS(Registries.BLOCK.getId(Blocks.PINK_PETALS), Blocks.PINK_PETALS);

	public final Identifier identifier;
	@Nullable
	public final Item item;
	public final Models models;

	private FlowerVariant(Identifier identifier) {
		this.identifier = identifier;
		this.item = null;
		this.models = Models.ofEmpty();
	}

	private FlowerVariant(Identifier identifier, Block block) {
		this.identifier = identifier;
		this.item = block.asItem();
		this.models = Models.ofBlockId(Registries.BLOCK.getId(block));
	}

	@Override
	public String asString() {
		return this.identifier.toString().replaceAll("\\W", "_");
	}

	public static FlowerVariant fromIdentifier(Identifier identifier) {
		for (FlowerVariant variant : values()) {
			if (variant.identifier.equals(identifier)) {
				return variant;
			}
		}

		return EMPTY;
	}

	public static FlowerVariant fromItem(Item item) {
		for (FlowerVariant variant : values()) {
			if (variant.item == item) {
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
