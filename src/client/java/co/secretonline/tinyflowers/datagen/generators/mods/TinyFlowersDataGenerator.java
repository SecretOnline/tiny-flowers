package co.secretonline.tinyflowers.datagen.generators.mods;

import java.util.List;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.component.SuspiciousStewEffects.Entry;

public class TinyFlowersDataGenerator extends BaseModDataGenerator {
	@Override
	public String getModId() {
		return TinyFlowers.MOD_ID;
	}

	@Override
	public List<TinyFlowerData> getFlowerData() {
		return List.of(
				new TinyFlowerData(TinyFlowers.id("tiny_dandelion"), Identifier.withDefaultNamespace("dandelion"), false,
						List.of(new Entry(MobEffects.SATURATION, toTicks(0.35f)))),
				new TinyFlowerData(TinyFlowers.id("tiny_poppy"), Identifier.withDefaultNamespace("poppy"), false,
						List.of(new Entry(MobEffects.NIGHT_VISION, toTicks(5.0f)))),
				new TinyFlowerData(TinyFlowers.id("tiny_blue_orchid"), Identifier.withDefaultNamespace("blue_orchid"), false,
						List.of(new Entry(MobEffects.SATURATION, toTicks(0.35f)))),
				new TinyFlowerData(TinyFlowers.id("tiny_allium"), Identifier.withDefaultNamespace("allium"), false,
						List.of(new Entry(MobEffects.FIRE_RESISTANCE, toTicks(3.0f)))),
				new TinyFlowerData(TinyFlowers.id("tiny_azure_bluet"), Identifier.withDefaultNamespace("azure_bluet"), false,
						List.of(new Entry(MobEffects.BLINDNESS, toTicks(11.0f)))),
				new TinyFlowerData(TinyFlowers.id("tiny_red_tulip"), Identifier.withDefaultNamespace("red_tulip"), false,
						List.of(new Entry(MobEffects.WEAKNESS, toTicks(7.0f)))),
				new TinyFlowerData(TinyFlowers.id("tiny_orange_tulip"), Identifier.withDefaultNamespace("orange_tulip"), false,
						List.of(new Entry(MobEffects.WEAKNESS, toTicks(7.0f)))),
				new TinyFlowerData(TinyFlowers.id("tiny_white_tulip"), Identifier.withDefaultNamespace("white_tulip"), false,
						List.of(new Entry(MobEffects.WEAKNESS, toTicks(7.0f)))),
				new TinyFlowerData(TinyFlowers.id("tiny_pink_tulip"), Identifier.withDefaultNamespace("pink_tulip"), false,
						List.of(new Entry(MobEffects.WEAKNESS, toTicks(7.0f)))),
				new TinyFlowerData(TinyFlowers.id("tiny_oxeye_daisy"), Identifier.withDefaultNamespace("oxeye_daisy"), false,
						List.of(new Entry(MobEffects.REGENERATION, toTicks(7.0f)))),
				new TinyFlowerData(TinyFlowers.id("tiny_cornflower"), Identifier.withDefaultNamespace("cornflower"), false,
						List.of(new Entry(MobEffects.JUMP_BOOST, toTicks(5.0f)))),
				new TinyFlowerData(TinyFlowers.id("tiny_lily_of_the_valley"),
						Identifier.withDefaultNamespace("lily_of_the_valley"), false,
						List.of(new Entry(MobEffects.POISON, toTicks(11.0f)))),
				new TinyFlowerData(TinyFlowers.id("tiny_torchflower"), Identifier.withDefaultNamespace("torchflower"), false,
						List.of(new Entry(MobEffects.NIGHT_VISION, toTicks(5.0f)))),
				new TinyFlowerData(TinyFlowers.id("tiny_closed_eyeblossom"),
						Identifier.withDefaultNamespace("closed_eyeblossom"), false,
						List.of(new Entry(MobEffects.NAUSEA, toTicks(7.0f)))),
				new TinyFlowerData(TinyFlowers.id("tiny_open_eyeblossom"), Identifier.withDefaultNamespace("open_eyeblossom"),
						false, List.of(new Entry(MobEffects.BLINDNESS, toTicks(11.0f)))),
				new TinyFlowerData(TinyFlowers.id("tiny_wither_rose"), Identifier.withDefaultNamespace("wither_rose"), false,
						List.of(new Entry(MobEffects.WITHER, toTicks(7.0f)))),
				new TinyFlowerData(TinyFlowers.id("tiny_cactus_flower"), Identifier.withDefaultNamespace("cactus_flower"),
						false,
						List.of()));
	}
}
