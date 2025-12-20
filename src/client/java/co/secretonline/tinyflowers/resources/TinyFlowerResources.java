package co.secretonline.tinyflowers.resources;

import java.util.HashMap;
import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.Identifier;

public record TinyFlowerResources(Identifier id, Identifier itemTexture, Identifier particleTexture,
		Identifier modelPart1, Identifier modelPart2, Identifier modelPart3, Identifier modelPart4) {
	private static Map<Identifier, TinyFlowerResources> INSTANCES = new HashMap<>();

	public static Map<Identifier, TinyFlowerResources> getInstances() {
		return INSTANCES;
	}

	public static void setInstances(Map<Identifier, TinyFlowerResources> map) {
		INSTANCES = map;
	}

	public static Codec<TinyFlowerResources> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Identifier.CODEC.fieldOf("id").forGetter(TinyFlowerResources::id),
			Identifier.CODEC.fieldOf("item_texture").forGetter(TinyFlowerResources::itemTexture),
			Identifier.CODEC.fieldOf("particle_texture").forGetter(TinyFlowerResources::particleTexture),
			Identifier.CODEC.fieldOf("modelPart1").forGetter(TinyFlowerResources::modelPart1),
			Identifier.CODEC.fieldOf("modelPart2").forGetter(TinyFlowerResources::modelPart2),
			Identifier.CODEC.fieldOf("modelPart3").forGetter(TinyFlowerResources::modelPart3),
			Identifier.CODEC.fieldOf("modelPart4").forGetter(TinyFlowerResources::modelPart4))
			.apply(instance, TinyFlowerResources::new));
}
