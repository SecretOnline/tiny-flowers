package co.secretonline.tinyflowers.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public record TinyFlowerResources(ResourceLocation id, ResourceLocation itemModel,
		ResourceLocation model1, ResourceLocation model2, ResourceLocation model3, ResourceLocation model4, Optional<ResourceLocation> modelPotted) {

	public static final Codec<TinyFlowerResources> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ResourceLocation.CODEC.fieldOf("id").forGetter(TinyFlowerResources::id),
			ResourceLocation.CODEC.fieldOf("item_model").forGetter(TinyFlowerResources::itemModel),
			ResourceLocation.CODEC.fieldOf("model1").forGetter(TinyFlowerResources::model1),
			ResourceLocation.CODEC.fieldOf("model2").forGetter(TinyFlowerResources::model2),
			ResourceLocation.CODEC.fieldOf("model3").forGetter(TinyFlowerResources::model3),
			ResourceLocation.CODEC.fieldOf("model4").forGetter(TinyFlowerResources::model4),
			ResourceLocation.CODEC.optionalFieldOf("model_potted").forGetter(TinyFlowerResources::modelPotted))
			.apply(instance, TinyFlowerResources::new));
}
