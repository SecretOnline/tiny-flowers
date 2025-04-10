package co.secretonline.tinyflowers.blocks;

import com.mojang.serialization.Codec;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.block.SuspiciousStewIngredient;
import net.minecraft.component.type.SuspiciousStewEffectsComponent;
import net.minecraft.component.type.SuspiciousStewEffectsComponent.StewEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.MathHelper;

public enum FlowerVariant implements StringIdentifiable, ItemConvertible, SuspiciousStewIngredient {
	EMPTY("empty", null, false),
	PINK_PETALS("pink_petals", Registries.ITEM.getId(Items.PINK_PETALS), false),
	WILDFLOWERS("wildflowers", Registries.ITEM.getId(Items.WILDFLOWERS), false),
	LEAF_LITTER("leaf_litter", Registries.ITEM.getId(Items.LEAF_LITTER), false),
	DANDELION("dandelion", TinyFlowers.id("tiny_dandelion"), true,
			new StewEffect(StatusEffects.SATURATION, toTicks(0.15f))),
	POPPY("poppy", TinyFlowers.id("tiny_poppy"), true,
			new StewEffect(StatusEffects.NIGHT_VISION, toTicks(2.5f))),
	BLUE_ORCHID("blue_orchid", TinyFlowers.id("tiny_blue_orchid"), true,
			new StewEffect(StatusEffects.SATURATION, toTicks(0.15f))),
	ALLIUM("allium", TinyFlowers.id("tiny_allium"), true,
			new StewEffect(StatusEffects.FIRE_RESISTANCE, toTicks(1.5f))),
	AZURE_BLUET("azure_bluet", TinyFlowers.id("tiny_azure_bluet"), true,
			new StewEffect(StatusEffects.BLINDNESS, toTicks(5.5f))),
	RED_TULIP("red_tulip", TinyFlowers.id("tiny_red_tulip"), true,
			new StewEffect(StatusEffects.WEAKNESS, toTicks(3.5f))),
	ORANGE_TULIP("orange_tulip", TinyFlowers.id("tiny_orange_tulip"), true,
			new StewEffect(StatusEffects.WEAKNESS, toTicks(3.5f))),
	WHITE_TULIP("white_tulip", TinyFlowers.id("tiny_white_tulip"), true,
			new StewEffect(StatusEffects.WEAKNESS, toTicks(3.5f))),
	PINK_TULIP("pink_tulip", TinyFlowers.id("tiny_pink_tulip"), true,
			new StewEffect(StatusEffects.WEAKNESS, toTicks(3.5f))),
	OXEYE_DAISY("oxeye_daisy", TinyFlowers.id("tiny_oxeye_daisy"), true,
			new StewEffect(StatusEffects.REGENERATION, toTicks(3.5f))),
	CORNFLOWER("cornflower", TinyFlowers.id("tiny_cornflower"), true,
			new StewEffect(StatusEffects.JUMP_BOOST, toTicks(2.5f))),
	LILY_OF_THE_VALLEY("lily_of_the_valley", TinyFlowers.id("tiny_lily_of_the_valley"), true,
			new StewEffect(StatusEffects.POISON, toTicks(5.5f))),
	TORCHFLOWER("torchflower", TinyFlowers.id("tiny_torchflower"), true,
			new StewEffect(StatusEffects.NIGHT_VISION, toTicks(2.5f))),
	CLOSED_EYEBLOSSOM("closed_eyeblossom", TinyFlowers.id("tiny_closed_eyeblossom"), true,
			new StewEffect(StatusEffects.NAUSEA, toTicks(3.5f))),
	OPEN_EYEBLOSSOM("open_eyeblossom", TinyFlowers.id("tiny_open_eyeblossom"), true,
			new StewEffect(StatusEffects.BLINDNESS, toTicks(5.5f))),
	WITHER_ROSE("wither_rose", TinyFlowers.id("tiny_wither_rose"), true,
			new StewEffect(StatusEffects.WITHER, toTicks(3.5f))),
	CACTUS_FLOWER("cactus_flower", TinyFlowers.id("tiny_cactus_flower"), true);

	private final String name;
	// Unfortunately we can't directly refer to the Item this variant corresponds to
	// as the items don't get initialised until after this enum.
	private final Identifier itemId;
	private final boolean shouldCreateItem;
	private final SuspiciousStewEffectsComponent stewEffectsComponent;

	private FlowerVariant(String name, Identifier itemId, boolean shouldCreateItem) {
		this(name, itemId, shouldCreateItem, null);
	}

	private FlowerVariant(String name, Identifier itemId, boolean shouldCreateItem, StewEffect stewEffect) {
		this.name = name;
		this.itemId = itemId;
		this.shouldCreateItem = shouldCreateItem;

		if (stewEffect == null) {
			this.stewEffectsComponent = null;
		} else {
			this.stewEffectsComponent = SuspiciousStewEffectsComponent.DEFAULT.with(stewEffect);
		}
	}

	@Override
	public String asString() {
		return this.name;
	}

	@Override
	public Item asItem() {
		if (this.itemId == null) {
			throw new IllegalStateException("Entry does not have an associated item");
		}

		return Registries.ITEM.get(this.itemId);
	}

	@Override
	public SuspiciousStewEffectsComponent getStewEffects() {
		return stewEffectsComponent;
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

	public boolean shouldCreateItem() {
		return this.shouldCreateItem;
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

	public static final Codec<FlowerVariant> CODEC = StringIdentifiable.createCodec(FlowerVariant::values);

	private static int toTicks(float seconds) {
		return MathHelper.floor(seconds * 20.0f);
	}
}
