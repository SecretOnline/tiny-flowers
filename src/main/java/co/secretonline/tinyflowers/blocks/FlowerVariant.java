package co.secretonline.tinyflowers.blocks;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.item.component.SuspiciousStewEffects.Entry;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import co.secretonline.tinyflowers.TinyFlowers;

public enum FlowerVariant implements StringRepresentable, ItemLike, SuspiciousEffectHolder {
	EMPTY("empty", false, null, null),
	PINK_PETALS("pink_petals", false,
			ResourceLocation.withDefaultNamespace("pink_petals"), ResourceLocation.withDefaultNamespace("pink_petals")),
	WILDFLOWERS("wildflowers", false,
			ResourceLocation.withDefaultNamespace("wildflowers"), ResourceLocation.withDefaultNamespace("wildflowers")),
	LEAF_LITTER("leaf_litter", false,
			ResourceLocation.withDefaultNamespace("leaf_litter"), ResourceLocation.withDefaultNamespace("leaf_litter")),
	DANDELION("dandelion", true,
			TinyFlowers.id("tiny_dandelion"), ResourceLocation.withDefaultNamespace("dandelion"),
			new Entry(MobEffects.SATURATION, toTicks(0.35f))),
	POPPY("poppy", true,
			TinyFlowers.id("tiny_poppy"), ResourceLocation.withDefaultNamespace("poppy"),
			new Entry(MobEffects.NIGHT_VISION, toTicks(5.0f))),
	BLUE_ORCHID("blue_orchid", true,
			TinyFlowers.id("tiny_blue_orchid"), ResourceLocation.withDefaultNamespace("blue_orchid"),
			new Entry(MobEffects.SATURATION, toTicks(0.35f))),
	ALLIUM("allium", true,
			TinyFlowers.id("tiny_allium"), ResourceLocation.withDefaultNamespace("allium"),
			new Entry(MobEffects.FIRE_RESISTANCE, toTicks(3.0f))),
	AZURE_BLUET("azure_bluet", true,
			TinyFlowers.id("tiny_azure_bluet"), ResourceLocation.withDefaultNamespace("azure_bluet"),
			new Entry(MobEffects.BLINDNESS, toTicks(11.0f))),
	RED_TULIP("red_tulip", true,
			TinyFlowers.id("tiny_red_tulip"), ResourceLocation.withDefaultNamespace("red_tulip"),
			new Entry(MobEffects.WEAKNESS, toTicks(7.0f))),
	ORANGE_TULIP("orange_tulip", true,
			TinyFlowers.id("tiny_orange_tulip"), ResourceLocation.withDefaultNamespace("orange_tulip"),
			new Entry(MobEffects.WEAKNESS, toTicks(7.0f))),
	WHITE_TULIP("white_tulip", true,
			TinyFlowers.id("tiny_white_tulip"), ResourceLocation.withDefaultNamespace("white_tulip"),
			new Entry(MobEffects.WEAKNESS, toTicks(7.0f))),
	PINK_TULIP("pink_tulip", true,
			TinyFlowers.id("tiny_pink_tulip"), ResourceLocation.withDefaultNamespace("pink_tulip"),
			new Entry(MobEffects.WEAKNESS, toTicks(7.0f))),
	OXEYE_DAISY("oxeye_daisy", true,
			TinyFlowers.id("tiny_oxeye_daisy"), ResourceLocation.withDefaultNamespace("oxeye_daisy"),
			new Entry(MobEffects.REGENERATION, toTicks(7.0f))),
	CORNFLOWER("cornflower", true,
			TinyFlowers.id("tiny_cornflower"), ResourceLocation.withDefaultNamespace("cornflower"),
			new Entry(MobEffects.JUMP_BOOST, toTicks(5.0f))),
	LILY_OF_THE_VALLEY("lily_of_the_valley", true,
			TinyFlowers.id("tiny_lily_of_the_valley"), ResourceLocation.withDefaultNamespace("lily_of_the_valley"),
			new Entry(MobEffects.POISON, toTicks(11.0f))),
	TORCHFLOWER("torchflower", true,
			TinyFlowers.id("tiny_torchflower"), ResourceLocation.withDefaultNamespace("torchflower"),
			new Entry(MobEffects.NIGHT_VISION, toTicks(5.0f))),
	CLOSED_EYEBLOSSOM("closed_eyeblossom", true,
			TinyFlowers.id("tiny_closed_eyeblossom"), ResourceLocation.withDefaultNamespace("closed_eyeblossom"),
			new Entry(MobEffects.NAUSEA, toTicks(7.0f))),
	OPEN_EYEBLOSSOM("open_eyeblossom", true,
			TinyFlowers.id("tiny_open_eyeblossom"), ResourceLocation.withDefaultNamespace("open_eyeblossom"),
			new Entry(MobEffects.BLINDNESS, toTicks(11.0f))),
	WITHER_ROSE("wither_rose", true,
			TinyFlowers.id("tiny_wither_rose"), ResourceLocation.withDefaultNamespace("wither_rose"),
			new Entry(MobEffects.WITHER, toTicks(7.0f))),
	CACTUS_FLOWER("cactus_flower", true,
			TinyFlowers.id("tiny_cactus_flower"), ResourceLocation.withDefaultNamespace("cactus_flower"));

	private final String name;
	/**
	 * The identifier of the BlockItem this variant corresponds to.
	 * For Segmented blocks, this is the ID of the vanilla block.
	 * For items that are added by this mod, this is a mod-specific ID.
	 * <p>
	 * It would be nice to refer to the RegistryEntry directly, but that causes a
	 * circular initialisation loop.
	 */
	private final ResourceLocation itemId;
	/**
	 * For variants that correspond to non-Segmented blocks, this is the original
	 * flower type. This gets used when using shears to convert flowers to a tiny
	 * garden.
	 */
	@Nullable
	private final ResourceLocation originalBlockId;
	private final boolean shouldCreateItem;
	private final SuspiciousStewEffects stewEffectsComponent;

	private FlowerVariant(String name, boolean shouldCreateItem, ResourceLocation itemId, ResourceLocation originalBlockId) {
		this(name, shouldCreateItem, itemId, originalBlockId, null);
	}

	private FlowerVariant(String name, boolean shouldCreateItem, ResourceLocation itemId, ResourceLocation originalBlockId,
			Entry stewEffect) {
		this.name = name;
		this.itemId = itemId;
		this.originalBlockId = originalBlockId;
		this.shouldCreateItem = shouldCreateItem;

		if (stewEffect == null) {
			this.stewEffectsComponent = null;
		} else {
			this.stewEffectsComponent = SuspiciousStewEffects.EMPTY.withEffectAdded(stewEffect);
		}
	}

	@Override
	public String getSerializedName() {
		return this.name;
	}

	@Override
	public Item asItem() {
		if (this.itemId == null) {
			throw new IllegalStateException("Entry does not have an associated item");
		}

		return BuiltInRegistries.ITEM.getValue(this.itemId);
	}

	@Override
	public SuspiciousStewEffects getSuspiciousEffects() {
		return stewEffectsComponent;
	}

	public boolean isEmpty() {
		return this == EMPTY;
	}

	public ResourceLocation getItemIdentifier() {
		return this.itemId;
	}

	public String getTranslationKey() {
		if (this.isEmpty()) {
			return "item.tiny-flowers.tiny_garden.empty";
		}

		return this.asItem().getDescriptionId();
	}

	@Nullable
	public Block getOriginalBlock() {
		return BuiltInRegistries.BLOCK.getValue(originalBlockId);
	}

	public boolean shouldCreateItem() {
		return this.shouldCreateItem;
	}

	public static FlowerVariant fromItem(ItemLike itemConvertible) {
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

			ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);

			if (blockId.equals(variant.originalBlockId)) {
				return variant;
			}
		}

		return EMPTY;
	}

	public static final Codec<FlowerVariant> CODEC = StringRepresentable.fromEnum(FlowerVariant::values);

	private static int toTicks(float seconds) {
		return Mth.floor(seconds * 20.0f);
	}
}
