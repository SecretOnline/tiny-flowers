package co.secretonline.tinyflowers.datagen.mods;

import java.util.List;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.data.behavior.TransformDayNightBehavior.When;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Blocks;

public class TinyFlowersFlowerProvider extends FlowerProvider {
	@Override
	public String getModId() {
		return TinyFlowers.MOD_ID;
	}

	@Override
	public List<Flower> getFlowers() {
		return List.of(
			Flower.Builder
				.ofCustom(TinyFlowers.id("tiny_dandelion"), ResourceLocation.withDefaultNamespace("dandelion"))
				.stewEffectSeconds(MobEffects.SATURATION, 0.35)
				.build(),
			Flower.Builder
				.ofCustom(TinyFlowers.id("tiny_poppy"), ResourceLocation.withDefaultNamespace("poppy"))
				.stewEffectSeconds(MobEffects.NIGHT_VISION, 5.0)
				.customModel(TinyFlowers.id("garden_tall"))
				.stemTexture(TinyFlowers.id("tall_tiny_flower_stem"))
				.build(),
			Flower.Builder
				.ofCustom(TinyFlowers.id("tiny_blue_orchid"), ResourceLocation.withDefaultNamespace("blue_orchid"))
				.layers(TinyFlowers.id("tiny_blue_orchid"),
					TinyFlowers.id("tiny_blue_orchid_upper"))
				.stewEffectSeconds(MobEffects.SATURATION, 0.35)
				.build(),
			Flower.Builder
				.ofCustom(TinyFlowers.id("tiny_allium"), ResourceLocation.withDefaultNamespace("allium"))
				.stewEffectSeconds(MobEffects.FIRE_RESISTANCE, 3.0)
				.build(),
			Flower.Builder
				.ofCustom(TinyFlowers.id("tiny_azure_bluet"), ResourceLocation.withDefaultNamespace("azure_bluet"))
				.stewEffectSeconds(MobEffects.BLINDNESS, 11.0)
				.build(),
			Flower.Builder
				.ofCustom(TinyFlowers.id("tiny_red_tulip"), ResourceLocation.withDefaultNamespace("red_tulip"))
				.stewEffectSeconds(MobEffects.WEAKNESS, 7.0)
				.build(),
			Flower.Builder
				.ofCustom(TinyFlowers.id("tiny_orange_tulip"), ResourceLocation.withDefaultNamespace("orange_tulip"))
				.stewEffectSeconds(MobEffects.WEAKNESS, 7.0)
				.build(),
			Flower.Builder
				.ofCustom(TinyFlowers.id("tiny_white_tulip"), ResourceLocation.withDefaultNamespace("white_tulip"))
				.stewEffectSeconds(MobEffects.WEAKNESS, 7.0)
				.build(),
			Flower.Builder
				.ofCustom(TinyFlowers.id("tiny_pink_tulip"), ResourceLocation.withDefaultNamespace("pink_tulip"))
				.stewEffectSeconds(MobEffects.WEAKNESS, 7.0)
				.build(),
			Flower.Builder
				.ofCustom(TinyFlowers.id("tiny_oxeye_daisy"), ResourceLocation.withDefaultNamespace("oxeye_daisy"))
				.stewEffectSeconds(MobEffects.REGENERATION, 7.0)
				.build(),
			Flower.Builder
				.ofCustom(TinyFlowers.id("tiny_cornflower"), ResourceLocation.withDefaultNamespace("cornflower"))
				.stewEffectSeconds(MobEffects.JUMP, 5.0)
				.build(),
			Flower.Builder
				.ofCustom(TinyFlowers.id("tiny_lily_of_the_valley"), ResourceLocation.withDefaultNamespace("lily_of_the_valley"))
				.stewEffectSeconds(MobEffects.POISON, 11.0)
				.layers(TinyFlowers.id("tiny_lily_of_the_valley"),
					TinyFlowers.id("tiny_lily_of_the_valley_upper"))
				.build(),
			Flower.Builder
				.ofCustom(TinyFlowers.id("tiny_torchflower"), ResourceLocation.withDefaultNamespace("torchflower"))
				.stewEffectSeconds(MobEffects.NIGHT_VISION, 5.0)
				.layers(TinyFlowers.id("tiny_torchflower"),
					TinyFlowers.id("tiny_torchflower_middle"),
					TinyFlowers.id("tiny_torchflower_upper"))
				.untintedStem()
				.stemTexture(TinyFlowers.id("tiny_torchflower_stem"))
				.build(),
			Flower.Builder
				.ofCustom(TinyFlowers.id("tiny_wither_rose"), ResourceLocation.withDefaultNamespace("wither_rose"))
				.stewEffectSeconds(MobEffects.WITHER, 7.0)
				.addCanSurviveOn(Blocks.NETHERRACK, Blocks.SOUL_SAND, Blocks.SOUL_SOIL)
				.untintedStem()
				.stemTexture(TinyFlowers.id("tiny_wither_rose_stem"))
				.build());
	}
}
