package co.secretonline.tinyflowers.components;

import com.mojang.serialization.Codec;

import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

public record TinyFlowerComponent(Identifier id) {
	public String getTranslationKey() {
		return Util.makeDescriptionId("block", this.id());
	}

	public static final Codec<TinyFlowerComponent> CODEC = Identifier.CODEC.xmap(TinyFlowerComponent::new,
			TinyFlowerComponent::id);
}
