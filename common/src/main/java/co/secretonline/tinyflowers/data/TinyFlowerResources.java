package co.secretonline.tinyflowers.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

public record TinyFlowerResources(Identifier id, Identifier itemModel,
		Identifier model1, Identifier model2, Identifier model3, Identifier model4) {

	public static final Codec<TinyFlowerResources> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Identifier.CODEC.fieldOf("id").forGetter(TinyFlowerResources::id),
			Identifier.CODEC.fieldOf("item_model").forGetter(TinyFlowerResources::itemModel),
			Identifier.CODEC.fieldOf("model1").forGetter(TinyFlowerResources::model1),
			Identifier.CODEC.fieldOf("model2").forGetter(TinyFlowerResources::model2),
			Identifier.CODEC.fieldOf("model3").forGetter(TinyFlowerResources::model3),
			Identifier.CODEC.fieldOf("model4").forGetter(TinyFlowerResources::model4))
			.apply(instance, TinyFlowerResources::new));
}
