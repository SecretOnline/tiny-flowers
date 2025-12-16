package co.secretonline.tinyflowers.data;

import java.util.List;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import co.secretonline.tinyflowers.components.ModComponents;
import co.secretonline.tinyflowers.components.TinyFlowerComponent;
import co.secretonline.tinyflowers.items.ModItems;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.item.component.SuspiciousStewEffects.Entry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SuspiciousEffectHolder;

/**
 * Data for a tiny flower variant.
 *
 * @param id               A unique identifier for this vatiant. Usually matches
 *                         the pack namespace and file name. Used for getting
 *                         textures, models, and other things.
 * @param originalId       The original plant block that is used to create the
 *                         tiny flowers.
 * @param shouldCreateItem Whether an entry should be added into the Creative
 *                         menu for this variant. Defaults to true. Set to false
 *                         if this is already a segmentable block like Pink
 *                         Petals or Wildflowers.
 * @param stewEffect       A potion effect for Suspicious Stew.
 */
public record TinyFlowerData(Identifier id, Identifier originalId, boolean shouldCreateItem,
		@Nullable List<Entry> suspiciousStewEffects) implements SuspiciousEffectHolder {

	@Override
	public SuspiciousStewEffects getSuspiciousEffects() {
		if (this.suspiciousStewEffects() == null) {
			return new SuspiciousStewEffects(List.of());
		}

		return new SuspiciousStewEffects(this.suspiciousStewEffects());
	}

	public ItemStack getItemStack(int count) {
		return new ItemStack(
				BuiltInRegistries.ITEM.wrapAsHolder(ModItems.TINY_FLOWER_ITEM),
				count,
				DataComponentPatch.builder()
						.set(ModComponents.TINY_FLOWER, new TinyFlowerComponent(this.id()))
						.build());
	}

	@Nullable
	private static TinyFlowerData ofPredicate(RegistryAccess registryAccess, Predicate<TinyFlowerData> predicate) {
		return registryAccess.get(ModRegistries.TINY_FLOWER)
				.get()
				.value()
				.stream()
				.filter(predicate)
				.findFirst()
				.orElse(null);
	}

	@Nullable
	public static TinyFlowerData findByOriginalBlock(RegistryAccess registryAccess, Block block) {
		return ofPredicate(registryAccess,
				flowerData -> flowerData.originalId().equals(BuiltInRegistries.BLOCK.getKey(block)));
	}

	@Nullable
	public static TinyFlowerData findById(RegistryAccess registryAccess, Identifier id) {
		return ofPredicate(registryAccess, flowerData -> flowerData.id().equals(id));
	}

	public static final Codec<TinyFlowerData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Identifier.CODEC.fieldOf("id").forGetter(TinyFlowerData::id),
			Identifier.CODEC.fieldOf("original_id").forGetter(TinyFlowerData::originalId),
			Codec.BOOL.optionalFieldOf("should_create_item", true).forGetter(TinyFlowerData::shouldCreateItem),
			Entry.CODEC.listOf().optionalFieldOf("suspicious_stew_effects", List.of())
					.forGetter(TinyFlowerData::suspiciousStewEffects))
			.apply(instance, TinyFlowerData::new));
}
