package co.secretonline.tinyflowers.data;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import co.secretonline.tinyflowers.blocks.ModBlockTags;
import co.secretonline.tinyflowers.components.ModComponents;
import co.secretonline.tinyflowers.components.TinyFlowerComponent;
import co.secretonline.tinyflowers.data.special.SpecialFeature;
import co.secretonline.tinyflowers.items.ModItems;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.ExtraCodecs.TagOrElementLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.item.component.SuspiciousStewEffects.Entry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SuspiciousEffectHolder;

/**
 * Data for a tiny flower variant.
 *
 * @param id              A unique identifier for this vatiant. Usually matches
 *                        the pack namespace and file name. Used for getting
 *                        textures, models, and other things.
 * @param originalId      The original plant block that is used to create the
 *                        tiny flowers.
 * @param isSegmentable   Whether an entry is for a block that implements
 *                        {@link net.minecraft.world.level.block.SegmentableBlock
 *                        SegmentableBlock}. This flag affects the behaviour of
 *                        the
 *                        mod in the following ways:
 *                        <ul>
 *                        <li>Using Florists' Shears on a Tiny Garden with this
 *                        variant will pop the original item, rather than a Tiny
 *                        Flower item.
 *                        <li>The data generation will not create a crafting
 *                        recipe
 *                        for creating Tiny Flowers of this type, as the item
 *                        already exists.
 *                        </ul>
 * @param canSurviveOn    Block IDs or #-prefixed tags for what blocks this
 *                        flower
 *                        type can be placed on. This defaults to the
 *                        `#tiny_flowers:tiny_flower_can_survive_on` tag, which
 *                        contains `#minecraft:dirt` and `minecraft:farmland`.
 * @param stewEffect      A potion effect for Suspicious Stew.
 * @param specialFeatures Any special features this flower type might have.
 */
public record TinyFlowerData(Identifier id, Identifier originalId, boolean isSegmentable,
		@NonNull List<TagOrElementLocation> canSurviveOn, @NonNull List<Entry> suspiciousStewEffects,
		@NonNull List<SpecialFeature> specialFeatures)
		implements SuspiciousEffectHolder {

	public boolean canSurviveOn(Block supportingBlock) {
		var blockHolder = BuiltInRegistries.BLOCK.wrapAsHolder(supportingBlock);

		for (TagOrElementLocation tagOrElementLocation : canSurviveOn) {
			Identifier id = tagOrElementLocation.id();
			if (tagOrElementLocation.tag()) {
				if (blockHolder.is(TagKey.create(Registries.BLOCK, id))) {
					return true;
				}
			} else {
				if (blockHolder.is(id)) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public SuspiciousStewEffects getSuspiciousEffects() {
		if (this.suspiciousStewEffects() == null) {
			return new SuspiciousStewEffects(List.of());
		}

		return new SuspiciousStewEffects(this.suspiciousStewEffects());
	}

	public ItemStack getItemStack(int count) {
		// For existing segmented-like flower types, just pop one of those items
		// instead.
		if (isSegmentable()) {
			Optional<Reference<Item>> item = BuiltInRegistries.ITEM.get(this.originalId);
			if (item.isEmpty()) {
				// Since this mod is data driven, it's possible that a garden block or tiny
				// flower item refers to a flower type that no longer exists (i.e. from a mod
				// that has been removed). In this case, pop nothing.
				return ItemStack.EMPTY;
			}

			return new ItemStack(item.get(), count);
		}

		return new ItemStack(
				BuiltInRegistries.ITEM.wrapAsHolder(ModItems.TINY_FLOWER_ITEM),
				count,
				DataComponentPatch.builder()
						.set(ModComponents.TINY_FLOWER, new TinyFlowerComponent(this.id()))
						.build());
	}

	@Nullable
	private static TinyFlowerData ofPredicate(HolderLookup.Provider provider, Predicate<TinyFlowerData> predicate) {
		return provider.lookupOrThrow(ModRegistries.TINY_FLOWER)
				.listElements()
				.map(ref -> ref.value())
				.filter(predicate)
				.findFirst()
				.orElse(null);
	}

	@Nullable
	public static TinyFlowerData findByOriginalBlock(HolderLookup.Provider provider, Block block) {
		return ofPredicate(provider,
				flowerData -> flowerData.originalId().equals(BuiltInRegistries.BLOCK.getKey(block)));
	}

	@Nullable
	public static TinyFlowerData findById(HolderLookup.Provider provider, Identifier id) {
		return ofPredicate(provider, flowerData -> flowerData.id().equals(id));
	}

	@Nullable
	public static TinyFlowerData findByItemStack(HolderLookup.Provider provider, ItemStack itemStack) {
		var key = BuiltInRegistries.ITEM.getKey(itemStack.getItem());

		return ofPredicate(provider, flowerData -> {
			if (flowerData.isSegmentable()) {
				// If the block is already segmented, then just check the original block ID.
				return key.equals(flowerData.originalId());
			}

			// Ensure the item stack has the right component.
			// This does mean that items other than this mod's Tiny Flower item will trigger
			// this, but I think that's fine. If someone has gone out of their way to add
			// the component to their item, then they probably wanted it to happen.
			TinyFlowerComponent component = itemStack.get(ModComponents.TINY_FLOWER);
			if (component == null) {
				return false;
			}

			return component.id().equals(flowerData.id());
		});
	}

	public static final Codec<TinyFlowerData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Identifier.CODEC.fieldOf("id").forGetter(TinyFlowerData::id),
			Identifier.CODEC.fieldOf("original_id").forGetter(TinyFlowerData::originalId),
			Codec.BOOL.optionalFieldOf("is_segmented", false).forGetter(TinyFlowerData::isSegmentable),
			ExtraCodecs.TAG_OR_ELEMENT_ID.listOf()
					.optionalFieldOf("can_survive_on", List.of(ModBlockTags.TINY_FLOWER_CAN_SURVIVE_ON_LOCATION))
					.forGetter(TinyFlowerData::canSurviveOn),
			Entry.CODEC.listOf().optionalFieldOf("suspicious_stew_effects", List.of())
					.forGetter(TinyFlowerData::suspiciousStewEffects),
			SpecialFeature.CODEC.listOf().optionalFieldOf("special_features", List.of())
					.forGetter(TinyFlowerData::specialFeatures))
			.apply(instance, TinyFlowerData::new));
}
