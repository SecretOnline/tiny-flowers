package co.secretonline.tinyflowers.datagen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.data.special.SpecialFeature;
import co.secretonline.tinyflowers.data.special.TransformDayNightSpecialFeature;
import co.secretonline.tinyflowers.data.TinyFlowerResources;
import co.secretonline.tinyflowers.data.TinyFlowerResources.TintSource;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs.TagOrElementLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.component.SuspiciousStewEffects.Entry;
import net.minecraft.world.level.block.Block;

public class TinyFlowersDatagenData {
	private static final String MOD_ID_PREFIX = TinyFlowers.MOD_ID + "/";
	private static final String BLOCK_MOD_PREFIX = "block/" + MOD_ID_PREFIX;

	private Identifier id;
	private Identifier itemTexture;
	private Identifier originalBlockId;
	private boolean isSegmentable;
	@NonNull
	private List<Entry> suspiciousStewEffects;
	@NonNull
	private List<TagOrElementLocation> canSurviveOn;
	private TintSource tintSource = TintSource.GRASS;
	@NonNull
	private List<SpecialFeature> specialFeatures;

	private ModelPart modelPart1;
	private ModelPart modelPart2;
	private ModelPart modelPart3;
	private ModelPart modelPart4;

	public TinyFlowerData data() {
		return new TinyFlowerData(id, originalBlockId, isSegmentable, canSurviveOn, suspiciousStewEffects, specialFeatures);
	}

	public TinyFlowerResources resources() {
		return new TinyFlowerResources(id, itemTexture, tintSource,
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
		private static final String FLOWERBED_MIDDLE = ("flowerbed_middle");
		private static final String FLOWERBED_UPPER = ("flowerbed_upper");

		private Identifier id;
		private Identifier itemTexture;
		private Identifier originalBlockId;
		private boolean isSegmentable = false;
		private final List<Entry> suspiciousStewEffects = new ArrayList<>();
		private List<TagOrElementLocation> canSurviveOn = new ArrayList<>(
				List.of(new TagOrElementLocation(BlockTags.SUPPORTS_VEGETATION.location(), true)));
		@NonNull
		private final List<SpecialFeature> specialFeatures = new ArrayList<>();

		private int layers = 0;
		private boolean untintedStem = false;
		private Identifier stemTexture = null;
		private Identifier particleTexture = null;
		private final Map<String, Identifier> textureMap = new HashMap<>();
		private Identifier customModel = null;
		private TintSource tintSource = TintSource.GRASS;

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

		public Builder replaceCanSurviveOn(TagOrElementLocation... canSurviveOn) {
			this.canSurviveOn = new ArrayList<>(Arrays.asList(canSurviveOn));
			return this;
		}

		public Builder addCanSurviveOn(TagOrElementLocation... canSurviveOn) {
			this.canSurviveOn.addAll(Arrays.asList(canSurviveOn));
			return this;
		}

		public Builder addCanSurviveOn(Block... blocks) {
			for (Block block : blocks) {
				var id = BuiltInRegistries.BLOCK.getKey(block);
				this.canSurviveOn.add(new TagOrElementLocation(id, false));
			}
			return this;
		}

		@SafeVarargs
		public final Builder addCanSurviveOn(TagKey<Block>... tags) {
			for (TagKey<Block> tag : tags) {
				this.canSurviveOn.add(new TagOrElementLocation(tag.location(), true));
			}
			return this;
		}

		public Builder itemTexture(Identifier itemTexture) {
			this.itemTexture = itemTexture.withPrefix("item/");
			return this;
		}

		public Builder layers(Identifier flowerbedTexture) {
			layers = 1;
			textureMap.put(TextureSlot.FLOWERBED.getId(), flowerbedTexture.withPrefix("block/"));

			return this;
		}

		public Builder layers(Identifier lowerTexture, Identifier upperTexture) {
			layers = 2;
			textureMap.put(TextureSlot.FLOWERBED.getId(), lowerTexture.withPrefix("block/"));
			textureMap.put(FLOWERBED_UPPER, upperTexture.withPrefix("block/"));

			return this;
		}

		public Builder layers(Identifier lowerTexture, Identifier middleTexture, Identifier upperTexture) {
			layers = 3;
			textureMap.put(TextureSlot.FLOWERBED.getId(), lowerTexture.withPrefix("block/"));
			textureMap.put(FLOWERBED_MIDDLE, middleTexture.withPrefix("block/"));
			textureMap.put(FLOWERBED_UPPER, upperTexture.withPrefix("block/"));

			return this;
		}

		public Builder untintedStem() {
			this.untintedStem = true;
			return this;
		}

		public Builder stemTexture(Identifier stemTexture) {
			this.stemTexture = stemTexture.withPrefix("block/");
			return this;
		}

		public Builder particleTexture(Identifier particleTexture) {
			this.particleTexture = particleTexture.withPrefix("block/");
			return this;
		}

		public Builder customModel(Identifier model) {
			this.customModel = model.withPrefix("block/");
			return this;
		}

		public Builder tintSource(TintSource tintSource) {
			this.tintSource = tintSource;
			return this;
		}

		public Builder addTransformDayNightBehaviour(TransformDayNightSpecialFeature.When when, Identifier turnsInto) {
			return this.addTransformDayNightBehaviour(when, turnsInto, 0, null, null);
		}

		public Builder addTransformDayNightBehaviour(TransformDayNightSpecialFeature.When when, Identifier turnsInto,
				int particleColor, @Nullable SoundEvent soundEventLong, @Nullable SoundEvent soundEventShort) {
			Optional<Identifier> longOptional = (soundEventLong == null ? Optional.empty()
					: Optional.of(soundEventLong.location()));
			Optional<Identifier> shortOptional = (soundEventShort == null ? Optional.empty()
					: Optional.of(soundEventShort.location()));

			this.specialFeatures.add(new TransformDayNightSpecialFeature(when, turnsInto, particleColor,
					longOptional, shortOptional));

			return this;
		}

		public TinyFlowersDatagenData build() {
			TinyFlowersDatagenData data = new TinyFlowersDatagenData();

			data.id = id;
			data.itemTexture = itemTexture;
			data.originalBlockId = originalBlockId;
			data.isSegmentable = isSegmentable;
			data.suspiciousStewEffects = suspiciousStewEffects;
			data.canSurviveOn = canSurviveOn;
			data.tintSource = tintSource;
			data.specialFeatures = specialFeatures;

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

			if (parentId == null) {
				parentId = TinyFlowers.id("block/garden");
			}

			if (this.stemTexture != null) {
				textureMap.put("stem", this.stemTexture);
			}
			if (this.particleTexture != null) {
				textureMap.put("particle", this.particleTexture);
			}

			data.modelPart1 = new ModelPart(id.withSuffix("_1"), parentId.withSuffix("_1"), textureMap);
			data.modelPart2 = new ModelPart(id.withSuffix("_2"), parentId.withSuffix("_2"), textureMap);
			data.modelPart3 = new ModelPart(id.withSuffix("_3"), parentId.withSuffix("_3"), textureMap);
			data.modelPart4 = new ModelPart(id.withSuffix("_4"), parentId.withSuffix("_4"), textureMap);

			return data;
		}
	}
}
