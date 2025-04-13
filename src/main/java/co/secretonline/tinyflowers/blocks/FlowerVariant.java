package co.secretonline.tinyflowers.blocks;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
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
	EMPTY("empty", false, null, null),
	PINK_PETALS("pink_petals", false,
			Registries.ITEM.getId(Items.PINK_PETALS), Blocks.PINK_PETALS),
	WILDFLOWERS("wildflowers", false,
			Registries.ITEM.getId(Items.WILDFLOWERS), Blocks.WILDFLOWERS),
	LEAF_LITTER("leaf_litter", false,
			Registries.ITEM.getId(Items.LEAF_LITTER), Blocks.LEAF_LITTER),
	DANDELION("dandelion", true,
			TinyFlowers.id("tiny_dandelion"), Blocks.DANDELION,
			new StewEffect(StatusEffects.SATURATION, toTicks(0.15f))),
	POPPY("poppy", true,
			TinyFlowers.id("tiny_poppy"), Blocks.POPPY,
			new StewEffect(StatusEffects.NIGHT_VISION, toTicks(2.5f))),
	BLUE_ORCHID("blue_orchid", true,
			TinyFlowers.id("tiny_blue_orchid"), Blocks.BLUE_ORCHID,
			new StewEffect(StatusEffects.SATURATION, toTicks(0.15f))),
	ALLIUM("allium", true,
			TinyFlowers.id("tiny_allium"), Blocks.ALLIUM,
			new StewEffect(StatusEffects.FIRE_RESISTANCE, toTicks(1.5f))),
	AZURE_BLUET("azure_bluet", true,
			TinyFlowers.id("tiny_azure_bluet"), Blocks.AZURE_BLUET,
			new StewEffect(StatusEffects.BLINDNESS, toTicks(5.5f))),
	RED_TULIP("red_tulip", true,
			TinyFlowers.id("tiny_red_tulip"), Blocks.RED_TULIP,
			new StewEffect(StatusEffects.WEAKNESS, toTicks(3.5f))),
	ORANGE_TULIP("orange_tulip", true,
			TinyFlowers.id("tiny_orange_tulip"), Blocks.ORANGE_TULIP,
			new StewEffect(StatusEffects.WEAKNESS, toTicks(3.5f))),
	WHITE_TULIP("white_tulip", true,
			TinyFlowers.id("tiny_white_tulip"), Blocks.WHITE_TULIP,
			new StewEffect(StatusEffects.WEAKNESS, toTicks(3.5f))),
	PINK_TULIP("pink_tulip", true,
			TinyFlowers.id("tiny_pink_tulip"), Blocks.PINK_TULIP,
			new StewEffect(StatusEffects.WEAKNESS, toTicks(3.5f))),
	OXEYE_DAISY("oxeye_daisy", true,
			TinyFlowers.id("tiny_oxeye_daisy"), Blocks.OXEYE_DAISY,
			new StewEffect(StatusEffects.REGENERATION, toTicks(3.5f))),
	CORNFLOWER("cornflower", true,
			TinyFlowers.id("tiny_cornflower"), Blocks.CORNFLOWER,
			new StewEffect(StatusEffects.JUMP_BOOST, toTicks(2.5f))),
	LILY_OF_THE_VALLEY("lily_of_the_valley", true,
			TinyFlowers.id("tiny_lily_of_the_valley"), Blocks.LILY_OF_THE_VALLEY,
			new StewEffect(StatusEffects.POISON, toTicks(5.5f))),
	TORCHFLOWER("torchflower", true,
			TinyFlowers.id("tiny_torchflower"), Blocks.TORCHFLOWER,
			new StewEffect(StatusEffects.NIGHT_VISION, toTicks(2.5f))),
	CLOSED_EYEBLOSSOM("closed_eyeblossom", true,
			TinyFlowers.id("tiny_closed_eyeblossom"), Blocks.CLOSED_EYEBLOSSOM,
			new StewEffect(StatusEffects.NAUSEA, toTicks(3.5f))),
	OPEN_EYEBLOSSOM("open_eyeblossom", true,
			TinyFlowers.id("tiny_open_eyeblossom"), Blocks.OPEN_EYEBLOSSOM,
			new StewEffect(StatusEffects.BLINDNESS, toTicks(5.5f))),
	WITHER_ROSE("wither_rose", true,
			TinyFlowers.id("tiny_wither_rose"), Blocks.WITHER_ROSE,
			new StewEffect(StatusEffects.WITHER, toTicks(3.5f))),
	CACTUS_FLOWER("cactus_flower", true,
			TinyFlowers.id("tiny_cactus_flower"), Blocks.CACTUS_FLOWER);

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
	private final Block originalBlock;
	private final boolean shouldCreateItem;
	private final SuspiciousStewEffectsComponent stewEffectsComponent;

	private FlowerVariant(String name, boolean shouldCreateItem, Identifier itemId, Block originalBlock) {
		this(name, shouldCreateItem, itemId, originalBlock, null);
	}

	private FlowerVariant(String name, boolean shouldCreateItem, Identifier itemId, Block originalBlock,
			StewEffect stewEffect) {
		this.name = name;
		this.itemId = itemId;
		this.originalBlock = originalBlock;
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
		return this.originalBlock;
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

			if (variant.originalBlock.equals(block)) {
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
