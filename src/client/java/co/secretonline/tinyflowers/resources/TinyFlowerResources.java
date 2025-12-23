package co.secretonline.tinyflowers.resources;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;

public record TinyFlowerResources(Identifier id, Identifier itemTexture,
		TintSource tintSource,
		Identifier model1, Identifier model2, Identifier model3, Identifier model4) {

	public static Codec<TinyFlowerResources> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Identifier.CODEC.fieldOf("id").forGetter(TinyFlowerResources::id),
			Identifier.CODEC.fieldOf("item_texture").forGetter(TinyFlowerResources::itemTexture),
			TintSource.CODEC.optionalFieldOf("tintSource", TintSource.GRASS).forGetter(TinyFlowerResources::tintSource),
			Identifier.CODEC.fieldOf("model1").forGetter(TinyFlowerResources::model1),
			Identifier.CODEC.fieldOf("model2").forGetter(TinyFlowerResources::model2),
			Identifier.CODEC.fieldOf("model3").forGetter(TinyFlowerResources::model3),
			Identifier.CODEC.fieldOf("model4").forGetter(TinyFlowerResources::model4))
			.apply(instance, TinyFlowerResources::new));

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
