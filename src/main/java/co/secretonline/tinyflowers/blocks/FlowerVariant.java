package co.secretonline.tinyflowers.blocks;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

public enum FlowerVariant implements StringIdentifiable, ItemConvertible {
	EMPTY("empty", null),
	PINK_PETALS("pink_petals", Registries.ITEM.getId(Items.PINK_PETALS)),
	DANDELION("dandelion", TinyFlowers.id("tiny_dandelion")),
	POPPY("poppy", TinyFlowers.id("tiny_poppy")),
	BLUE_ORCHID("blue_orchid", TinyFlowers.id("tiny_blue_orchid")),
	ALLIUM("allium", TinyFlowers.id("tiny_allium")),
	AZURE_BLUET("azure_bluet", TinyFlowers.id("tiny_azure_bluet")),
	RED_TULIP("red_tulip", TinyFlowers.id("tiny_red_tulip")),
	ORANGE_TULIP("orange_tulip", TinyFlowers.id("tiny_orange_tulip")),
	WHITE_TULIP("white_tulip", TinyFlowers.id("tiny_white_tulip")),
	PINK_TULIP("pink_tulip", TinyFlowers.id("tiny_pink_tulip")),
	OXEYE_DAISY("oxeye_daisy", TinyFlowers.id("tiny_oxeye_daisy")),
	CORNFLOWER("cornflower", TinyFlowers.id("tiny_cornflower")),
	LILY_OF_THE_VALLEY("lily_of_the_valley", TinyFlowers.id("tiny_lily_of_the_valley")),
	TORCHFLOWER("torchflower", TinyFlowers.id("tiny_torchflower")),
	CLOSED_EYEBLOSSOM("closed_eyeblossom", TinyFlowers.id("tiny_closed_eyeblossom")),
	OPEN_EYEBLOSSOM("open_eyeblossom", TinyFlowers.id("tiny_open_eyeblossom")),
	WITHER_ROSE("wither_rose", TinyFlowers.id("tiny_wither_rose"));

	private final String name;
	// Unfortunately we can't directly refer to the Item this variant corresponds to
	// as the items don't get initialised until after this enum.
	private final Identifier itemId;

	private FlowerVariant(String name, Identifier itemId) {
		this.name = name;
		this.itemId = itemId;
	}

	@Override
	public String asString() {
		return this.name;
	}

	public Item asItem() {
		if (this.itemId == null) {
			throw new IllegalStateException("Entry does not have an associated item");
		}

		return Registries.ITEM.get(this.itemId);
	}

	public boolean isEmpty() {
		return this == EMPTY;
	}

	public Identifier getItemIdentifier() {
		return this.itemId;
	}

	public String getTranslationKey() {
		if (this.isEmpty()) {
			return "item.tiny-flowers.tiny_garden.empty";
		}

		return this.asItem().getTranslationKey();
	}

	public static FlowerVariant fromItem(ItemConvertible itemConvertible) {
		Item item = itemConvertible.asItem();

		for (FlowerVariant variant : values()) {
			if (variant.isEmpty()) {
				continue;
			}

			if (variant.asItem().equals(item)) {
				return variant;
			}
		}

		return EMPTY;
	}
}
