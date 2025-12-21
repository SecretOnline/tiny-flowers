package co.secretonline.tinyflowers.resources;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;

public record TinyFlowerResources(Identifier id, Identifier itemTexture, Identifier particleTexture,
		Part model1, Part model2, Part model3, Part model4) {

	public static Codec<TinyFlowerResources> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Identifier.CODEC.fieldOf("id").forGetter(TinyFlowerResources::id),
			Identifier.CODEC.fieldOf("item_texture").forGetter(TinyFlowerResources::itemTexture),
			Identifier.CODEC.fieldOf("particle_texture").forGetter(TinyFlowerResources::particleTexture),
			Part.CODEC.fieldOf("model1").forGetter(TinyFlowerResources::model1),
			Part.CODEC.fieldOf("model2").forGetter(TinyFlowerResources::model2),
			Part.CODEC.fieldOf("model3").forGetter(TinyFlowerResources::model3),
			Part.CODEC.fieldOf("model4").forGetter(TinyFlowerResources::model4))
			.apply(instance, TinyFlowerResources::new));

	public static record Part(Identifier id, TintSource tintSource) {

		public static Codec<Part> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Identifier.CODEC.fieldOf("id").forGetter(Part::id),
				TintSource.CODEC.optionalFieldOf("tintSource", TintSource.GRASS).forGetter(Part::tintSource))
				.apply(instance, Part::new));
	}

	public static enum TintSource implements StringRepresentable {
		GRASS("grass"),
		DRY_FOLIAGE("dry_foliage");

		private final String name;

		private TintSource(String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return this.name;
		}

		public static final Codec<TintSource> CODEC = StringRepresentable.fromEnum(TintSource::values);
	}
}
