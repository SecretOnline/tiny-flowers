package co.secretonline.tinyflowers.datagen.mods;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.data.TinyFlowerResources;
import co.secretonline.tinyflowers.data.behavior.Behavior;
import co.secretonline.tinyflowers.data.behavior.SturdyPlacementBehavior;
import co.secretonline.tinyflowers.data.behavior.TransformDayNightBehavior;
import co.secretonline.tinyflowers.data.behavior.TransformWeatherBehavior;
import co.secretonline.tinyflowers.tags.ModBlockTags;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs.TagOrElementLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.component.SuspiciousStewEffects.Entry;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class Flower {
	private static final String MOD_ID_PREFIX = TinyFlowers.MOD_ID + "/";
	private static final String BLOCK_MOD_PREFIX = "block/" + MOD_ID_PREFIX;

	@NotNull
	private final ResourceLocation id;
	@NotNull
	private final ResourceLocation itemTexture;
	@NotNull
	private final ResourceLocation originalBlockId;
	private final boolean isSegmentable;
	private final boolean canBePotted;

	@NotNull
	private final List<Entry> suspiciousStewEffects;
	@NotNull
	private final List<TagOrElementLocation> canSurviveOn;
	@NotNull
	private final List<Behavior> behaviors;

	@NotNull
	private final ModelPart modelPart1;
	@NotNull
	private final ModelPart modelPart2;
	@NotNull
	private final ModelPart modelPart3;
	@NotNull
	private final ModelPart modelPart4;
	private final ModelPart modelPartPotted;

	private Flower(@NotNull ResourceLocation id, @NotNull ResourceLocation itemTexture, @NotNull ResourceLocation originalBlockId, boolean isSegmentable, boolean canBePotted,
								 @NotNull List<Entry> suspiciousStewEffects, @NotNull List<TagOrElementLocation> canSurviveOn, @NotNull List<Behavior> behaviors,
								 @NotNull ModelPart modelPart1, @NotNull ModelPart modelPart2, @NotNull ModelPart modelPart3, @NotNull ModelPart modelPart4, ModelPart modelPartPotted) {
		this.id = id;
		this.itemTexture = itemTexture;
		this.originalBlockId = originalBlockId;
		this.isSegmentable = isSegmentable;
		this.canBePotted = canBePotted;

		this.suspiciousStewEffects = suspiciousStewEffects;
		this.canSurviveOn = canSurviveOn;

		this.behaviors = behaviors;

		this.modelPart1 = modelPart1;
		this.modelPart2 = modelPart2;
		this.modelPart3 = modelPart3;
		this.modelPart4 = modelPart4;
		this.modelPartPotted = modelPartPotted;
	}

	public TinyFlowerData data() {
		return new TinyFlowerData(id, originalBlockId, isSegmentable, canBePotted, canSurviveOn, suspiciousStewEffects, behaviors);
	}

	public TinyFlowerResources resources() {


		return new TinyFlowerResources(id, itemTexture,
			modelPart1.id().withPrefix(BLOCK_MOD_PREFIX),
			modelPart2.id().withPrefix(BLOCK_MOD_PREFIX),
			modelPart3.id().withPrefix(BLOCK_MOD_PREFIX),
			modelPart4.id().withPrefix(BLOCK_MOD_PREFIX),
			Optional.ofNullable(modelPartPotted == null
				? null
				: modelPartPotted.id().withPrefix(BLOCK_MOD_PREFIX))
		);
	}

	public ModelParts modelParts() {
		return new ModelParts(
			modelPart1,
			modelPart2,
			modelPart3,
			modelPart4,
			modelPartPotted);
	}

	public record ModelPart(ResourceLocation id, ResourceLocation parent, Map<String, ResourceLocation> textures) {

		public JsonElement toJsonElement() {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("parent", parent.toString());
			if (!textures.isEmpty()) {
				JsonObject texturesObject = new JsonObject();
				textures.forEach((textureSlot, identifier) -> texturesObject.addProperty(textureSlot, identifier.toString()));
				jsonObject.add("textures", texturesObject);
			}

			return jsonObject;
		}

		public void outputModel(BiConsumer<ResourceLocation, Supplier<JsonElement>> consumer) {
			consumer.accept(id.withPrefix(BLOCK_MOD_PREFIX), this::toJsonElement);
		}
	}

	public record ModelParts(ModelPart part1, ModelPart part2,
													 ModelPart part3, ModelPart part4,
													 ModelPart partPotted) {
	}

	public static class Builder {
		private static final String FLOWERBED_MIDDLE = ("flowerbed_middle");
		private static final String FLOWERBED_UPPER = ("flowerbed_upper");

		@Nullable
		private ResourceLocation id;
		@Nullable
		private ResourceLocation itemTexture;
		@Nullable
		private ResourceLocation originalBlockId;
		private boolean isSegmentable = false;
		private boolean canBePotted = true;
		@NotNull
		private final List<Entry> suspiciousStewEffects = new ArrayList<>();
		@NotNull
		private List<TagOrElementLocation> canSurviveOn = new ArrayList<>(
			List.of(new TagOrElementLocation(ModBlockTags.SUPPORTS_VEGETATION.location(), true)));
		@NotNull
		private final List<Behavior> behaviors = new ArrayList<>();

		private int layers = 0;
		private boolean untintedStem = false;
		@Nullable
		private ResourceLocation stemTexture = null;
		@Nullable
		private ResourceLocation particleTexture = null;
		@NotNull
		private final Map<String, ResourceLocation> textureMap = new HashMap<>();
		@Nullable
		private ResourceLocation customModel = null;
		@Nullable
		private ResourceLocation customModelPotted = null;

		public static Builder ofCustom(ResourceLocation id, ResourceLocation originalBlockId) {
			return new Builder()
				.id(id)
				.itemTexture(id)
				.originalBlockId(originalBlockId)
				.layers(id);
		}

		public static Builder ofSegmented(ResourceLocation originalBlockId) {
			return new Builder()
				.id(originalBlockId)
				.itemTexture(originalBlockId)
				.originalBlockId(originalBlockId)
				.segmentable()
				.layers(originalBlockId);
		}

		public static Builder ofStandard(ResourceLocation originalBlockId) {
			ResourceLocation id = originalBlockId.withPrefix("tiny_");

			return new Builder()
				.id(id)
				.itemTexture(id)
				.originalBlockId(originalBlockId)
				.layers(id);
		}

		public Builder id(ResourceLocation id) {
			this.id = id;
			return this;
		}

		public Builder originalBlockId(ResourceLocation originalBlockId) {
			this.originalBlockId = originalBlockId;
			return this;
		}

		public Builder segmentable() {
			this.isSegmentable = true;
			return this;
		}

		public Builder noFlowerPot() {
			this.canBePotted = false;
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

		public Builder itemTexture(ResourceLocation itemTexture) {
			this.itemTexture = itemTexture.withPrefix("item/");
			return this;
		}

		public Builder layers(ResourceLocation flowerbedTexture) {
			layers = 1;
			textureMap.put(TextureSlot.FLOWERBED.getId(), flowerbedTexture.withPrefix("block/"));

			return this;
		}

		public Builder layers(ResourceLocation lowerTexture, ResourceLocation upperTexture) {
			layers = 2;
			textureMap.put(TextureSlot.FLOWERBED.getId(), lowerTexture.withPrefix("block/"));
			textureMap.put(FLOWERBED_UPPER, upperTexture.withPrefix("block/"));

			return this;
		}

		public Builder layers(ResourceLocation lowerTexture, ResourceLocation middleTexture, ResourceLocation upperTexture) {
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

		public Builder stemTexture(ResourceLocation stemTexture) {
			this.stemTexture = stemTexture.withPrefix("block/");
			return this;
		}

		public Builder particleTexture(ResourceLocation particleTexture) {
			this.particleTexture = particleTexture.withPrefix("block/");
			return this;
		}

		public Builder customModel(ResourceLocation model) {
			this.customModel = model.withPrefix("block/");
			return this;
		}

		public Builder customPottedModel(ResourceLocation modelPotted) {
			this.customModelPotted = modelPotted.withPrefix("block/");
			return this;
		}

		public Builder addTransformDayNightBehavior(TransformDayNightBehavior.When when, ResourceLocation turnsInto) {
			return this.addTransformDayNightBehavior(when, turnsInto, 0, null, null);
		}

		public Builder addTransformDayNightBehavior(TransformDayNightBehavior.When when, ResourceLocation turnsInto,
																								int particleColor, SoundEvent soundEventLong, SoundEvent soundEventShort) {
			Optional<ResourceLocation> longOptional = (soundEventLong == null ? Optional.empty()
				: Optional.of(soundEventLong.getLocation()));
			Optional<ResourceLocation> shortOptional = (soundEventShort == null ? Optional.empty()
				: Optional.of(soundEventShort.getLocation()));

			this.behaviors.add(new TransformDayNightBehavior(when, turnsInto, particleColor,
				longOptional, shortOptional));

			return this;
		}

		public Builder addTransformWeatherBehavior(TransformWeatherBehavior.When when, ResourceLocation turnsInto) {
			return this.addTransformWeatherBehavior(when, turnsInto, 0, null, null);
		}

		public Builder addTransformWeatherBehavior(TransformWeatherBehavior.When when, ResourceLocation turnsInto,
																							 int particleColor, SoundEvent soundEventLong, SoundEvent soundEventShort) {
			Optional<ResourceLocation> longOptional = (soundEventLong == null ? Optional.empty()
				: Optional.of(soundEventLong.getLocation()));
			Optional<ResourceLocation> shortOptional = (soundEventShort == null ? Optional.empty()
				: Optional.of(soundEventShort.getLocation()));

			this.behaviors.add(new TransformWeatherBehavior(when, turnsInto, particleColor,
				longOptional, shortOptional));

			return this;
		}

		public Builder addSturdyPlacementBehavior() {
			this.behaviors.add(new SturdyPlacementBehavior(true));

			return this;
		}

		public Flower build() {
			String errorPrefix = "TinyFlowerResources.Builder (" + id + "): ";
			if (layers == 0 && customModel == null) {
				throw new Error(errorPrefix + "layers() or customModel() must be called once.");
			}
			if (id == null) {
				throw new Error(errorPrefix + "TinyFlowerResources.Builder: id is null");
			}
			if (itemTexture == null) {
				throw new Error(errorPrefix + "TinyFlowerResources.Builder: itemTexture is null");
			}
			if (originalBlockId == null) {
				throw new Error(errorPrefix + "TinyFlowerResources.Builder: originalBlockId is null");
			}

			ResourceLocation parentId = null;
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

			ModelPart modelPart1 = new ModelPart(id.withSuffix("_1"), parentId.withSuffix("_1"), textureMap);
			ModelPart modelPart2 = new ModelPart(id.withSuffix("_2"), parentId.withSuffix("_2"), textureMap);
			ModelPart modelPart3 = new ModelPart(id.withSuffix("_3"), parentId.withSuffix("_3"), textureMap);
			ModelPart modelPart4 = new ModelPart(id.withSuffix("_4"), parentId.withSuffix("_4"), textureMap);

			ModelPart modelPartPotted = null;
			if (canBePotted) {
				if (customModelPotted != null) {
					modelPartPotted = new ModelPart(id.withSuffix("_potted"), customModelPotted, textureMap);
				} else {
					modelPartPotted = new ModelPart(id.withSuffix("_potted"), parentId.withSuffix("_potted"), textureMap);
				}
			}

			return new Flower(id, itemTexture, originalBlockId, isSegmentable, canBePotted,
				suspiciousStewEffects, canSurviveOn, behaviors,
				modelPart1, modelPart2, modelPart3, modelPart4,
				modelPartPotted
			);
		}
	}
}
