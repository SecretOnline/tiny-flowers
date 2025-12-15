package co.secretonline.tinyflowers.data;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.component.SuspiciousStewEffects.Entry;

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
		@Nullable List<Entry> suspiciousStewEffects) {

	public static final Codec<TinyFlowerData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Identifier.CODEC.fieldOf("id").forGetter(TinyFlowerData::id),
			Identifier.CODEC.fieldOf("original_id").forGetter(TinyFlowerData::originalId),
			Codec.BOOL.optionalFieldOf("should_create_item", true).forGetter(TinyFlowerData::shouldCreateItem),
			Entry.CODEC.listOf().optionalFieldOf("suspicious_stew_effects", List.of())
					.forGetter(TinyFlowerData::suspiciousStewEffects))
			.apply(instance, TinyFlowerData::new));
}
