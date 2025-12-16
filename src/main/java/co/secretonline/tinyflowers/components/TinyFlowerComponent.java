package co.secretonline.tinyflowers.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

public record TinyFlowerComponent(Identifier id) {
	public String getTranslationKey() {
		return Util.makeDescriptionId("item", this.id());
	}

	public static final Codec<TinyFlowerComponent> CODEC = RecordCodecBuilder.create(builder -> {
		return builder.group(
				Identifier.CODEC.fieldOf("tiny_flower").forGetter(TinyFlowerComponent::id))
				.apply(builder, TinyFlowerComponent::new);
	});
}
