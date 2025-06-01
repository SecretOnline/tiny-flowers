package co.secretonline.tinyflowers.blocks;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.block.Block;
import net.minecraft.block.SuspiciousStewIngredient;
import net.minecraft.component.type.SuspiciousStewEffectsComponent;
import net.minecraft.component.type.SuspiciousStewEffectsComponent.StewEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.MathHelper;

public enum FlowerVariant implements StringIdentifiable, ItemConvertible, SuspiciousStewIngredient {
	EMPTY("empty", false, null, null),
	PINK_PETALS("pink_petals", false,
			Identifier.ofVanilla("pink_petals"), Identifier.ofVanilla("pink_petals")),
	WILDFLOWERS("wildflowers", false,
			Identifier.ofVanilla("wildflowers"), Identifier.ofVanilla("wildflowers")),
	LEAF_LITTER("leaf_litter", false,
			Identifier.ofVanilla("leaf_litter"), Identifier.ofVanilla("leaf_litter")),
	DANDELION("dandelion", true,
			TinyFlowers.id("tiny_dandelion"), Identifier.ofVanilla("dandelion"),
			new StewEffect(StatusEffects.SATURATION, toTicks(0.35f))),
	POPPY("poppy", true,
			TinyFlowers.id("tiny_poppy"), Identifier.ofVanilla("poppy"),
			new StewEffect(StatusEffects.NIGHT_VISION, toTicks(5.0f))),
	BLUE_ORCHID("blue_orchid", true,
			TinyFlowers.id("tiny_blue_orchid"), Identifier.ofVanilla("blue_orchid"),
			new StewEffect(StatusEffects.SATURATION, toTicks(0.35f))),
	ALLIUM("allium", true,
			TinyFlowers.id("tiny_allium"), Identifier.ofVanilla("allium"),
			new StewEffect(StatusEffects.FIRE_RESISTANCE, toTicks(3.0f))),
	AZURE_BLUET("azure_bluet", true,
			TinyFlowers.id("tiny_azure_bluet"), Identifier.ofVanilla("azure_bluet"),
			new StewEffect(StatusEffects.BLINDNESS, toTicks(11.0f))),
	RED_TULIP("red_tulip", true,
			TinyFlowers.id("tiny_red_tulip"), Identifier.ofVanilla("red_tulip"),
			new StewEffect(StatusEffects.WEAKNESS, toTicks(7.0f))),
	ORANGE_TULIP("orange_tulip", true,
			TinyFlowers.id("tiny_orange_tulip"), Identifier.ofVanilla("orange_tulip"),
			new StewEffect(StatusEffects.WEAKNESS, toTicks(7.0f))),
	WHITE_TULIP("white_tulip", true,
			TinyFlowers.id("tiny_white_tulip"), Identifier.ofVanilla("white_tulip"),
			new StewEffect(StatusEffects.WEAKNESS, toTicks(7.0f))),
	PINK_TULIP("pink_tulip", true,
			TinyFlowers.id("tiny_pink_tulip"), Identifier.ofVanilla("pink_tulip"),
			new StewEffect(StatusEffects.WEAKNESS, toTicks(7.0f))),
	OXEYE_DAISY("oxeye_daisy", true,
			TinyFlowers.id("tiny_oxeye_daisy"), Identifier.ofVanilla("oxeye_daisy"),
			new StewEffect(StatusEffects.REGENERATION, toTicks(7.0f))),
	CORNFLOWER("cornflower", true,
			TinyFlowers.id("tiny_cornflower"), Identifier.ofVanilla("cornflower"),
			new StewEffect(StatusEffects.JUMP_BOOST, toTicks(5.0f))),
	LILY_OF_THE_VALLEY("lily_of_the_valley", true,
			TinyFlowers.id("tiny_lily_of_the_valley"), Identifier.ofVanilla("lily_of_the_valley"),
			new StewEffect(StatusEffects.POISON, toTicks(11.0f))),
	TORCHFLOWER("torchflower", true,
			TinyFlowers.id("tiny_torchflower"), Identifier.ofVanilla("torchflower"),
			new StewEffect(StatusEffects.NIGHT_VISION, toTicks(5.0f))),
	CLOSED_EYEBLOSSOM("closed_eyeblossom", true,
			TinyFlowers.id("tiny_closed_eyeblossom"), Identifier.ofVanilla("closed_eyeblossom"),
			new StewEffect(StatusEffects.NAUSEA, toTicks(7.0f))),
	OPEN_EYEBLOSSOM("open_eyeblossom", true,
			TinyFlowers.id("tiny_open_eyeblossom"), Identifier.ofVanilla("open_eyeblossom"),
			new StewEffect(StatusEffects.BLINDNESS, toTicks(11.0f))),
	WITHER_ROSE("wither_rose", true,
			TinyFlowers.id("tiny_wither_rose"), Identifier.ofVanilla("wither_rose"),
			new StewEffect(StatusEffects.WITHER, toTicks(7.0f))),
	CACTUS_FLOWER("cactus_flower", true,
			TinyFlowers.id("tiny_cactus_flower"), Identifier.ofVanilla("cactus_flower"));

	private final String name;
	/**
	 * The identifier of the BlockItem this variant corresponds to.
	 * For Segmented blocks, this is the ID of the vanilla block.
	 * For items that are added by this mod, this is a mod-specific ID.
	 * <p>
	 * It would be nice to refer to the RegistryEntry directly, but that causes a
	 * circular initialisation loop.
	 */
	private final Identifier itemId;
	/**
	 * For variants that correspond to non-Segmented blocks, this is the original
	 * flower type. This gets used when using shears to convert flowers to a tiny
	 * garden.
	 */
	@Nullable
	private final Identifier originalBlockId;
	private final boolean shouldCreateItem;
	private final SuspiciousStewEffectsComponent stewEffectsComponent;

	private FlowerVariant(String name, boolean shouldCreateItem, Identifier itemId, Identifier originalBlockId) {
		this(name, shouldCreateItem, itemId, originalBlockId, null);
	}

	private FlowerVariant(String name, boolean shouldCreateItem, Identifier itemId, Identifier originalBlockId,
			StewEffect stewEffect) {
		this.name = name;
		this.itemId = itemId;
		this.originalBlockId = originalBlockId;
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

	@Nullable
	public Block getOriginalBlock() {
		return Registries.BLOCK.get(originalBlockId);
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

	public static FlowerVariant fromOriginalBlock(Block block) {
		for (FlowerVariant variant : values()) {
			if (variant.isEmpty()) {
				continue;
			}

			Identifier blockId = Registries.BLOCK.getId(block);

			if (blockId.equals(variant.originalBlockId)) {
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
