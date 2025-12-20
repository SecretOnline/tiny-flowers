package co.secretonline.tinyflowers.resources;

import java.util.HashMap;
import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.Identifier;

public record TinyFlowerResources(Identifier id, Part part1, Part part2, Part part3, Part part4) {
	private static Map<Identifier, TinyFlowerResources> INSTANCES = new HashMap<>();

	public static Map<Identifier, TinyFlowerResources> getInstances() {
		return INSTANCES;
	}

	public static void setInstances(Map<Identifier, TinyFlowerResources> map) {
		TinyFlowers.LOGGER.info("did set map");
		INSTANCES = map;
	}

	public static Codec<TinyFlowerResources> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Identifier.CODEC.fieldOf("id").forGetter(TinyFlowerResources::id),
			Part.CODEC.fieldOf("part1").forGetter(TinyFlowerResources::part1),
			Part.CODEC.fieldOf("part2").forGetter(TinyFlowerResources::part2),
			Part.CODEC.fieldOf("part3").forGetter(TinyFlowerResources::part3),
			Part.CODEC.fieldOf("part4").forGetter(TinyFlowerResources::part4))
			.apply(instance, TinyFlowerResources::new));

	static public record Part(Identifier model, Map<String, Identifier> textures) {

		public static Codec<Part> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Identifier.CODEC.fieldOf("model").forGetter(Part::model),
				Codec.unboundedMap(Codec.STRING, Identifier.CODEC).fieldOf("textures").forGetter(Part::textures))
				.apply(instance, Part::new));
	}

	static public class Builder {
		private static final TextureSlot FLOWERBED_MIDDLE = TextureSlot.create("flowerbed_middle");
		private static final TextureSlot FLOWERBED_UPPER = TextureSlot.create("flowerbed_upper");

		private final Identifier id;
		private int layers = 0;
		private boolean untintedStem = false;
		private boolean includeStemTexture = true;
		private Identifier stemTexture = Identifier.withDefaultNamespace("block/pink_petals_stem");
		private Map<String, Identifier> textureMap = new HashMap<>();
		private Identifier special = null;

		public Builder(Identifier id) {
			this.id = id;
		}

		public Builder layers(Identifier flowerbedTexture) {
			layers = 1;
			textureMap.put(TextureSlot.PARTICLE.getId(), flowerbedTexture.withPrefix("block/"));
			textureMap.put(TextureSlot.FLOWERBED.getId(), flowerbedTexture.withPrefix("block/"));

			return this;
		}

		public Builder layers(Identifier lowerTexture, Identifier upperTexture) {
			layers = 2;
			textureMap.put(TextureSlot.PARTICLE.getId(), lowerTexture.withPrefix("block/"));
			textureMap.put(TextureSlot.FLOWERBED.getId(), lowerTexture.withPrefix("block/"));
			textureMap.put(FLOWERBED_UPPER.getId(), upperTexture.withPrefix("block/"));

			return this;
		}

		public Builder layers(Identifier lowerTexture, Identifier middleTexture, Identifier upperTexture) {
			layers = 2;
			textureMap.put(TextureSlot.PARTICLE.getId(), lowerTexture.withPrefix("block/"));
			textureMap.put(TextureSlot.FLOWERBED.getId(), lowerTexture.withPrefix("block/"));
			textureMap.put(FLOWERBED_MIDDLE.getId(), middleTexture.withPrefix("block/"));
			textureMap.put(FLOWERBED_UPPER.getId(), upperTexture.withPrefix("block/"));

			return this;
		}

		public Builder untintedStem() {
			this.untintedStem = true;
			return this;
		}

		public Builder noStem() {
			this.includeStemTexture = false;
			return this;
		}

		public Builder stemTexture(Identifier stemTexture) {
			this.stemTexture = stemTexture.withPrefix("block/");
			return this;
		}

		public Builder special(Identifier special) {
			this.special = special.withPrefix("block/");
			return this;
		}

		public TinyFlowerResources build() {
			if (layers == 0 && special == null) {
				throw new Error("TinyFlowerResources.Builder: layers() or special() must be called once.");
			}

			Identifier parentId = null;

			if (special != null) {
				parentId = special;
			} else if (layers == 1) {
				if (untintedStem) {
					parentId = TinyFlowers.id("block/garden_untinted");
				} else {
					parentId = TinyFlowers.id("block/garden");
				}
			} else if (layers == 2) {
				if (untintedStem) {
					parentId = TinyFlowers.id("block/garden_double_untinted");
				} else {
					parentId = TinyFlowers.id("block/garden_double");
				}
			} else if (layers == 3) {
				if (untintedStem) {
					parentId = TinyFlowers.id("block/garden_triple_untinted");
				} else {
					parentId = TinyFlowers.id("block/garden_triple");
				}
			}

			if (includeStemTexture) {
				textureMap.put("stem", this.stemTexture);
			}

			return new TinyFlowerResources(this.id, new Part(parentId.withSuffix("_1"), textureMap),
					new Part(parentId.withSuffix("_2"), textureMap),
					new Part(parentId.withSuffix("_3"), textureMap),
					new Part(parentId.withSuffix("_4"), textureMap));
		}
	}
}
