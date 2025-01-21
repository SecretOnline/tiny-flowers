package co.secretonline.tinyflowers.blocks;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

public enum FlowerVariant implements StringIdentifiable {
	EMPTY(TinyFlowers.id("empty"), Models.ofBlock(Blocks.AIR)),
	PINK_PETALS(Registries.BLOCK.getId(Blocks.PINK_PETALS), Models.ofBlock(Blocks.PINK_PETALS));

	private final String name;
	public final Models models;

	private FlowerVariant(Identifier identifier, Models models) {
		this.name = identifier.toString().replaceAll("\\W", "_");
		this.models = models;
	}

	@Override
	public String asString() {
		return this.name.toString();
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

		public static Models ofBlock(Block block) {
			Identifier blockId = Registries.BLOCK.getId(block);

			return new Models(
					blockId.withPath(path -> "block/" + path + "_1"),
					blockId.withPath(path -> "block/" + path + "_2"),
					blockId.withPath(path -> "block/" + path + "_3"),
					blockId.withPath(path -> "block/" + path + "_4"));
		}

		public Models ofEmpty() {
			Identifier airId = Registries.BLOCK.getId(Blocks.AIR);
			Identifier modelId = airId.withPath(path -> "block/" + path);

			return new Models(modelId, modelId, modelId, modelId);
		}
	}
}
