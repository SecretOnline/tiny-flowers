package co.secretonline.tinyflowers.datagen.generators.mods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.jspecify.annotations.NonNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.resources.TinyFlowerResources;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.component.SuspiciousStewEffects.Entry;

public class TinyFlowersDatagenData {
	private static String MOD_ID_PREFIX = TinyFlowers.MOD_ID + "/";
	private static String BLOCK_MOD_PREFIX = "block/" + MOD_ID_PREFIX;

	private Identifier id;
	private Identifier itemTexture;
	private Identifier particleTexture;
	private Identifier originalBlockId;
	private boolean isSegmentable;
	@NonNull
	private List<Entry> suspiciousStewEffects;

	private ModelPart modelPart1;
	private ModelPart modelPart2;
	private ModelPart modelPart3;
	private ModelPart modelPart4;

	public TinyFlowerData data() {
		return new TinyFlowerData(id, originalBlockId, isSegmentable, suspiciousStewEffects);
	}

	public TinyFlowerResources resources() {
		return new TinyFlowerResources(id, itemTexture, particleTexture,
				modelPart1.id().withPrefix(BLOCK_MOD_PREFIX),
				modelPart2.id().withPrefix(BLOCK_MOD_PREFIX),
				modelPart3.id().withPrefix(BLOCK_MOD_PREFIX),
				modelPart4.id().withPrefix(BLOCK_MOD_PREFIX));
	}

	public ModelParts modelParts() {
		return new ModelParts(
				modelPart1,
				modelPart2,
				modelPart3,
				modelPart4);
	}

	public static record ModelPart(Identifier id, Identifier parent, Map<String, Identifier> textures) {

		public JsonElement toJsonElement() {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("parent", parent.toString());
			if (!textures.isEmpty()) {
				JsonObject texturesObject = new JsonObject();
				textures.forEach((textureSlot, identifier) -> {
					texturesObject.addProperty(textureSlot, identifier.toString());
				});
				jsonObject.add("textures", texturesObject);
			}

			return jsonObject;
		}

		public void outputModel(BiConsumer<Identifier, ModelInstance> consumer) {
			consumer.accept(id.withPrefix(BLOCK_MOD_PREFIX), this::toJsonElement);
		}
	}

	public static record ModelParts(ModelPart part1, ModelPart part2, ModelPart part3, ModelPart part4) {
	}

	public static class Builder {
		private static final TextureSlot FLOWERBED_MIDDLE = TextureSlot.create("flowerbed_middle");
		private static final TextureSlot FLOWERBED_UPPER = TextureSlot.create("flowerbed_upper");

		private Identifier id;
		private Identifier itemTexture;
		private Identifier particleTexture;
		private Identifier originalBlockId;
		private boolean isSegmentable = false;
		private List<Entry> suspiciousStewEffects = new ArrayList<>();

		private int layers = 0;
		private boolean untintedStem = false;
		private boolean includeStemTexture = true;
		private Identifier stemTexture = Identifier.withDefaultNamespace("block/pink_petals_stem");
		private Map<String, Identifier> textureMap = new HashMap<>();
		private Identifier customModel = null;

		public static Builder ofCustom(Identifier id, Identifier originalBlockId) {
			return new Builder()
					.id(id)
					.itemTexture(id)
					.originalBlockId(originalBlockId)
					.layers(id);
		}

		public static Builder ofSegmented(Identifier originalBlockId) {
			return new Builder()
					.id(originalBlockId)
					.itemTexture(originalBlockId)
					.originalBlockId(originalBlockId)
					.segmentable()
					.layers(originalBlockId);
		}

		public static Builder ofStandard(Identifier originalBlockId) {
			Identifier id = originalBlockId.withPrefix("tiny_");

			return new Builder()
					.id(id)
					.itemTexture(id)
					.originalBlockId(originalBlockId)
					.layers(id);
		}

		public Builder id(Identifier id) {
			this.id = id;
			return this;
		}

		public Builder originalBlockId(Identifier originalBlockId) {
			this.originalBlockId = originalBlockId;
			return this;
		}

		public Builder segmentable() {
			this.isSegmentable = true;
			return this;
		}

		public Builder stewEffect(Holder<MobEffect> effect, int ticks) {
			this.suspiciousStewEffects.add(new Entry(effect, ticks));
			return this;
		}

		public Builder stewEffectSeconds(Holder<MobEffect> effect, double seconds) {
			return this.stewEffect(effect, Mth.floor(seconds * 20.0f));
		}

		public Builder itemTexture(Identifier itemTexture) {
			this.itemTexture = itemTexture.withPrefix("item/");
			return this;
		}

		public Builder particleTexture(Identifier particleTexture) {
			this.particleTexture = particleTexture.withPrefix("block/");
			return this;
		}

		public Builder layers(Identifier flowerbedTexture) {
			layers = 1;
			particleTexture = flowerbedTexture.withPrefix("block/");
			textureMap.put(TextureSlot.PARTICLE.getId(), flowerbedTexture.withPrefix("block/"));
			textureMap.put(TextureSlot.FLOWERBED.getId(), flowerbedTexture.withPrefix("block/"));

			return this;
		}

		public Builder layers(Identifier lowerTexture, Identifier upperTexture) {
			layers = 2;
			particleTexture = lowerTexture.withPrefix("block/");
			textureMap.put(TextureSlot.PARTICLE.getId(), lowerTexture.withPrefix("block/"));
			textureMap.put(TextureSlot.FLOWERBED.getId(), lowerTexture.withPrefix("block/"));
			textureMap.put(FLOWERBED_UPPER.getId(), upperTexture.withPrefix("block/"));

			return this;
		}

		public Builder layers(Identifier lowerTexture, Identifier middleTexture, Identifier upperTexture) {
			layers = 2;
			particleTexture = lowerTexture.withPrefix("block/");
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

		public Builder customModel(Identifier model) {
			this.customModel = model.withPrefix("block/");
			return this;
		}

		public TinyFlowersDatagenData build() {
			TinyFlowersDatagenData data = new TinyFlowersDatagenData();

			data.id = id;
			data.itemTexture = itemTexture;
			data.particleTexture = particleTexture;
			data.originalBlockId = originalBlockId;
			data.isSegmentable = isSegmentable;
			data.suspiciousStewEffects = suspiciousStewEffects;

			if (layers == 0 && customModel == null) {
				throw new Error("TinyFlowerResources.Builder: layers() or special() must be called once.");
			}

			Identifier parentId = null;
			if (customModel != null) {
				parentId = customModel;
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

			data.modelPart1 = new ModelPart(id.withSuffix("_1"), parentId.withSuffix("_1"), textureMap);
			data.modelPart2 = new ModelPart(id.withSuffix("_2"), parentId.withSuffix("_2"), textureMap);
			data.modelPart3 = new ModelPart(id.withSuffix("_3"), parentId.withSuffix("_3"), textureMap);
			data.modelPart4 = new ModelPart(id.withSuffix("_4"), parentId.withSuffix("_4"), textureMap);

			return data;
		}
	}
}
