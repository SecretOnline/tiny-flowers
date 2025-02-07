package co.secretonline.tinyflowers.blocks;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

public enum FlowerVariant implements StringIdentifiable, ItemConvertible {
	EMPTY("empty", null, false),
	PINK_PETALS("pink_petals", Registries.ITEM.getId(Items.PINK_PETALS), false),
	DANDELION("dandelion", TinyFlowers.id("tiny_dandelion"), true),
	POPPY("poppy", TinyFlowers.id("tiny_poppy"), true),
	BLUE_ORCHID("blue_orchid", TinyFlowers.id("tiny_blue_orchid"), true),
	ALLIUM("allium", TinyFlowers.id("tiny_allium"), true),
	AZURE_BLUET("azure_bluet", TinyFlowers.id("tiny_azure_bluet"), true),
	RED_TULIP("red_tulip", TinyFlowers.id("tiny_red_tulip"), true),
	ORANGE_TULIP("orange_tulip", TinyFlowers.id("tiny_orange_tulip"), true),
	WHITE_TULIP("white_tulip", TinyFlowers.id("tiny_white_tulip"), true),
	PINK_TULIP("pink_tulip", TinyFlowers.id("tiny_pink_tulip"), true),
	OXEYE_DAISY("oxeye_daisy", TinyFlowers.id("tiny_oxeye_daisy"), true),
	CORNFLOWER("cornflower", TinyFlowers.id("tiny_cornflower"), true),
	LILY_OF_THE_VALLEY("lily_of_the_valley", TinyFlowers.id("tiny_lily_of_the_valley"), true),
	TORCHFLOWER("torchflower", TinyFlowers.id("tiny_torchflower"), true),
	WITHER_ROSE("wither_rose", TinyFlowers.id("tiny_wither_rose"), true);

	private final String name;
	// Unfortunately we can't directly refer to the Item this variant corresponds to
	// as the items don't get initialised until after this enum.
	private final Identifier itemId;
	private final boolean shouldCreateItemModel;

	private FlowerVariant(String name, Identifier itemId, boolean shouldCreateItemModel) {
		this.name = name;
		this.itemId = itemId;
		this.shouldCreateItemModel = shouldCreateItemModel;
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

	public boolean shouldCreateItemModel() {
		return this.shouldCreateItemModel;
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
