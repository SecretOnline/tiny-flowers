package co.secretonline.tinyflowers.datagen.generators.mods;

import java.util.List;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.data.special.EyeblossomOpeningSpecialFeature.When;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Blocks;

public class TinyFlowersDataGenerator extends BaseModDataGenerator {
	@Override
	public String getModId() {
		return TinyFlowers.MOD_ID;
	}

	@Override
	public List<TinyFlowersDatagenData> getFlowerData() {
		return List.of(
				TinyFlowersDatagenData.Builder
						.ofCustom(TinyFlowers.id("tiny_dandelion"), Identifier.withDefaultNamespace("dandelion"))
						.stewEffectSeconds(MobEffects.SATURATION, 0.35)
						.build(),
				TinyFlowersDatagenData.Builder
						.ofCustom(TinyFlowers.id("tiny_poppy"), Identifier.withDefaultNamespace("poppy"))
						.stewEffectSeconds(MobEffects.NIGHT_VISION, 5.0)
						.customModel(TinyFlowers.id("garden_tall"))
						.stemTexture(TinyFlowers.id("tall_tiny_flower_stem"))
						.build(),
				TinyFlowersDatagenData.Builder
						.ofCustom(TinyFlowers.id("tiny_blue_orchid"), Identifier.withDefaultNamespace("blue_orchid"))
						.layers(TinyFlowers.id("tiny_blue_orchid"),
								TinyFlowers.id("tiny_blue_orchid_upper"))
						.stewEffectSeconds(MobEffects.SATURATION, 0.35)
						.build(),
				TinyFlowersDatagenData.Builder
						.ofCustom(TinyFlowers.id("tiny_allium"), Identifier.withDefaultNamespace("allium"))
						.stewEffectSeconds(MobEffects.FIRE_RESISTANCE, 3.0)
						.build(),
				TinyFlowersDatagenData.Builder
						.ofCustom(TinyFlowers.id("tiny_azure_bluet"), Identifier.withDefaultNamespace("azure_bluet"))
						.stewEffectSeconds(MobEffects.BLINDNESS, 11.0)
						.build(),
				TinyFlowersDatagenData.Builder
						.ofCustom(TinyFlowers.id("tiny_red_tulip"), Identifier.withDefaultNamespace("red_tulip"))
						.stewEffectSeconds(MobEffects.WEAKNESS, 7.0)
						.build(),
				TinyFlowersDatagenData.Builder
						.ofCustom(TinyFlowers.id("tiny_orange_tulip"), Identifier.withDefaultNamespace("orange_tulip"))
						.stewEffectSeconds(MobEffects.WEAKNESS, 7.0)
						.build(),
				TinyFlowersDatagenData.Builder
						.ofCustom(TinyFlowers.id("tiny_white_tulip"), Identifier.withDefaultNamespace("white_tulip"))
						.stewEffectSeconds(MobEffects.WEAKNESS, 7.0)
						.build(),
				TinyFlowersDatagenData.Builder
						.ofCustom(TinyFlowers.id("tiny_pink_tulip"), Identifier.withDefaultNamespace("pink_tulip"))
						.stewEffectSeconds(MobEffects.WEAKNESS, 7.0)
						.build(),
				TinyFlowersDatagenData.Builder
						.ofCustom(TinyFlowers.id("tiny_oxeye_daisy"), Identifier.withDefaultNamespace("oxeye_daisy"))
						.stewEffectSeconds(MobEffects.REGENERATION, 7.0)
						.build(),
				TinyFlowersDatagenData.Builder
						.ofCustom(TinyFlowers.id("tiny_cornflower"), Identifier.withDefaultNamespace("cornflower"))
						.stewEffectSeconds(MobEffects.JUMP_BOOST, 5.0)
						.build(),
				TinyFlowersDatagenData.Builder
						.ofCustom(TinyFlowers.id("tiny_lily_of_the_valley"), Identifier.withDefaultNamespace("lily_of_the_valley"))
						.stewEffectSeconds(MobEffects.POISON, 11.0)
						.layers(TinyFlowers.id("tiny_lily_of_the_valley"),
								TinyFlowers.id("tiny_lily_of_the_valley_upper"))
						.build(),
				TinyFlowersDatagenData.Builder
						.ofCustom(TinyFlowers.id("tiny_torchflower"), Identifier.withDefaultNamespace("torchflower"))
						.stewEffectSeconds(MobEffects.NIGHT_VISION, 5.0)
						.layers(TinyFlowers.id("tiny_torchflower"),
								TinyFlowers.id("tiny_torchflower_middle"),
								TinyFlowers.id("tiny_torchflower_upper"))
						.untintedStem()
						.stemTexture(TinyFlowers.id("tiny_torchflower_stem"))
						.build(),
				TinyFlowersDatagenData.Builder
						.ofCustom(TinyFlowers.id("tiny_closed_eyeblossom"), Identifier.withDefaultNamespace("closed_eyeblossom"))
						.stewEffectSeconds(MobEffects.NAUSEA, 7.0)
						.layers(TinyFlowers.id("tiny_closed_eyeblossom"))
						.untintedStem()
						.stemTexture(TinyFlowers.id("tiny_eyeblossom_stem"))
						.addEyeblossomBehaviour(When.NIGHT, TinyFlowers.id("tiny_open_eyeblossom"), 16545810,
								SoundEvents.EYEBLOSSOM_OPEN_LONG, SoundEvents.EYEBLOSSOM_OPEN)
						.build(),
				TinyFlowersDatagenData.Builder
						.ofCustom(TinyFlowers.id("tiny_open_eyeblossom"), Identifier.withDefaultNamespace("open_eyeblossom"))
						.stewEffectSeconds(MobEffects.BLINDNESS, 11.0)
						.layers(TinyFlowers.id("tiny_open_eyeblossom"),
								TinyFlowers.id("tiny_open_eyeblossom_upper"))
						.customModel(TinyFlowers.id("garden_double_untinted_glow"))
						.untintedStem()
						.stemTexture(TinyFlowers.id("tiny_eyeblossom_stem"))
						.addEyeblossomBehaviour(When.DAY, TinyFlowers.id("tiny_closed_eyeblossom"), 6250335,
								SoundEvents.EYEBLOSSOM_CLOSE_LONG, SoundEvents.EYEBLOSSOM_CLOSE)
						.build(),
				TinyFlowersDatagenData.Builder
						.ofCustom(TinyFlowers.id("tiny_wither_rose"), Identifier.withDefaultNamespace("wither_rose"))
						.stewEffectSeconds(MobEffects.WITHER, 7.0)
						.addCanSurviveOn(Blocks.NETHERRACK, Blocks.SOUL_SAND, Blocks.SOUL_SOIL)
						.untintedStem()
						.stemTexture(TinyFlowers.id("tiny_wither_rose_stem"))
						.build(),
				TinyFlowersDatagenData.Builder
						.ofCustom(TinyFlowers.id("tiny_cactus_flower"), Identifier.withDefaultNamespace("cactus_flower"))
						.addCanSurviveOn(BlockTags.SAND)
						.addCanSurviveOn(Blocks.SANDSTONE, Blocks.RED_SANDSTONE)
						.customModel(TinyFlowers.id("garden_low_untinted"))
						.stemTexture(TinyFlowers.id("tiny_cactus_flower_stem"))
						.build());
	}
}
