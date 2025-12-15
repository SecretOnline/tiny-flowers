package co.secretonline.tinyflowers;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.component.SuspiciousStewEffects.Entry;

public record TinyFlowerData(Identifier id, Identifier originalId, boolean shouldCreateItem,
		@Nullable Entry stewEffect) {

	public static final Codec<TinyFlowerData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Identifier.CODEC.fieldOf("id").forGetter(TinyFlowerData::id),
			Identifier.CODEC.fieldOf("original_id").forGetter(TinyFlowerData::originalId),
			Codec.BOOL.fieldOf("should_create_item").forGetter(TinyFlowerData::shouldCreateItem),
			Entry.CODEC.optionalFieldOf("stew_effect", null).forGetter(TinyFlowerData::stewEffect))
			.apply(instance, TinyFlowerData::new));
}
