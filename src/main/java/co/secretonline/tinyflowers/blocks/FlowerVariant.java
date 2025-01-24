package co.secretonline.tinyflowers.blocks;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

public enum FlowerVariant implements StringIdentifiable {
	EMPTY(),
	PINK_PETALS(Registries.BLOCK.getId(Blocks.PINK_PETALS)),
	DANDELION(TinyFlowers.id("tiny_dandelion")),
	POPPY(TinyFlowers.id("tiny_poppy")),
	BLUE_ORCHID(TinyFlowers.id("tiny_blue_orchid")),
	ALLIUM(TinyFlowers.id("tiny_allium")),
	AZURE_BLUET(TinyFlowers.id("tiny_azure_bluet")),
	RED_TULIP(TinyFlowers.id("tiny_red_tulip")),
	ORANGE_TULIP(TinyFlowers.id("tiny_orange_tulip")),
	WHITE_TULIP(TinyFlowers.id("tiny_white_tulip")),
	PINK_TULIP(TinyFlowers.id("tiny_pink_tulip")),
	OXEYE_DAISY(TinyFlowers.id("tiny_oxeye_daisy")),
	CORNFLOWER(TinyFlowers.id("tiny_cornflower")),
	LILY_OF_THE_VALLEY(TinyFlowers.id("tiny_lily_of_the_valley")),
	TORCHFLOWER(TinyFlowers.id("tiny_torchflower")),
	OPEN_EYEBLOSSOM(TinyFlowers.id("tiny_open_eyeblossom")),
	CLOSED_EYEBLOSSOM(TinyFlowers.id("tiny_closed_eyeblossom")),
	WITHER_ROSE(TinyFlowers.id("tiny_wither_rose"));

	public final Identifier identifier;

	private FlowerVariant() {
		this.identifier = TinyFlowers.id("empty");
	}

	private FlowerVariant(Identifier identifier) {
		this.identifier = identifier;
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
}
