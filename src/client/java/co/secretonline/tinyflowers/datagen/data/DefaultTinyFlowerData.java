package co.secretonline.tinyflowers.datagen.data;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import co.secretonline.tinyflowers.TinyFlowerData;
import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.component.SuspiciousStewEffects.Entry;

public class DefaultTinyFlowerData {

	public static final List<TinyFlowerData> ALL_VARIANTS = new ArrayList<>();

	private static TinyFlowerData register(Identifier id, Identifier originalId, boolean shouldCreateItem,
			@Nullable Entry stewEffect) {
		TinyFlowerData instance = new TinyFlowerData(id, originalId, shouldCreateItem, stewEffect);
		ALL_VARIANTS.add(instance);
		return instance;
	}

	public static final TinyFlowerData PINK_PETALS = register(TinyFlowers.id("pink_petals"),
			Identifier.withDefaultNamespace("pink_petals"), false, null);
	public static final TinyFlowerData WILDFLOWERS = register(TinyFlowers.id("wildflowers"),
			Identifier.withDefaultNamespace("wildflowers"), false, null);
	public static final TinyFlowerData LEAF_LITTER = register(TinyFlowers.id("leaf_litter"),
			Identifier.withDefaultNamespace("leaf_litter"), false, null);
	public static final TinyFlowerData DANDELION = register(TinyFlowers.id("tiny_dandelion"),
			Identifier.withDefaultNamespace("dandelion"),
			true, new Entry(MobEffects.SATURATION, toTicks(0.35f)));
	public static final TinyFlowerData POPPY = register(TinyFlowers.id("tiny_poppy"),
			Identifier.withDefaultNamespace("poppy"),
			true, new Entry(MobEffects.NIGHT_VISION, toTicks(5.0f)));
	public static final TinyFlowerData BLUE_ORCHID = register(TinyFlowers.id("tiny_blue_orchid"),
			Identifier.withDefaultNamespace("blue_orchid"),
			true, new Entry(MobEffects.SATURATION, toTicks(0.35f)));
	public static final TinyFlowerData ALLIUM = register(TinyFlowers.id("tiny_allium"),
			Identifier.withDefaultNamespace("allium"),
			true, new Entry(MobEffects.FIRE_RESISTANCE, toTicks(3.0f)));
	public static final TinyFlowerData AZURE_BLUET = register(TinyFlowers.id("tiny_azure_bluet"),
			Identifier.withDefaultNamespace("azure_bluet"),
			true, new Entry(MobEffects.BLINDNESS, toTicks(11.0f)));
	public static final TinyFlowerData RED_TULIP = register(TinyFlowers.id("tiny_red_tulip"),
			Identifier.withDefaultNamespace("red_tulip"),
			true, new Entry(MobEffects.WEAKNESS, toTicks(7.0f)));
	public static final TinyFlowerData ORANGE_TULIP = register(TinyFlowers.id("tiny_orange_tulip"),
			Identifier.withDefaultNamespace("orange_tulip"),
			true, new Entry(MobEffects.WEAKNESS, toTicks(7.0f)));
	public static final TinyFlowerData WHITE_TULIP = register(TinyFlowers.id("tiny_white_tulip"),
			Identifier.withDefaultNamespace("white_tulip"),
			true, new Entry(MobEffects.WEAKNESS, toTicks(7.0f)));
	public static final TinyFlowerData PINK_TULIP = register(TinyFlowers.id("tiny_pink_tulip"),
			Identifier.withDefaultNamespace("pink_tulip"),
			true, new Entry(MobEffects.WEAKNESS, toTicks(7.0f)));
	public static final TinyFlowerData OXEYE_DAISY = register(TinyFlowers.id("tiny_oxeye_daisy"),
			Identifier.withDefaultNamespace("oxeye_daisy"),
			true, new Entry(MobEffects.REGENERATION, toTicks(7.0f)));
	public static final TinyFlowerData CORNFLOWER = register(TinyFlowers.id("tiny_cornflower"),
			Identifier.withDefaultNamespace("cornflower"),
			true, new Entry(MobEffects.JUMP_BOOST, toTicks(5.0f)));
	public static final TinyFlowerData LILY_OF_THE_VALLEY = register(
			TinyFlowers.id("tiny_lily_of_the_valley"), Identifier.withDefaultNamespace("lily_of_the_valley"),
			true, new Entry(MobEffects.POISON, toTicks(11.0f)));
	public static final TinyFlowerData TORCHFLOWER = register(TinyFlowers.id("tiny_torchflower"),
			Identifier.withDefaultNamespace("torchflower"),
			true, new Entry(MobEffects.NIGHT_VISION, toTicks(5.0f)));
	public static final TinyFlowerData CLOSED_EYEBLOSSOM = register(TinyFlowers.id("tiny_closed_eyeblossom"),
			Identifier.withDefaultNamespace("closed_eyeblossom"),
			true, new Entry(MobEffects.NAUSEA, toTicks(7.0f)));
	public static final TinyFlowerData OPEN_EYEBLOSSOM = register(TinyFlowers.id("tiny_open_eyeblossom"),
			Identifier.withDefaultNamespace("open_eyeblossom"),
			true, new Entry(MobEffects.BLINDNESS, toTicks(11.0f)));
	public static final TinyFlowerData WITHER_ROSE = register(TinyFlowers.id("tiny_wither_rose"),
			Identifier.withDefaultNamespace("wither_rose"),
			true, new Entry(MobEffects.WITHER, toTicks(7.0f)));
	public static final TinyFlowerData CACTUS_FLOWER = register(TinyFlowers.id("tiny_cactus_flower"),
			Identifier.withDefaultNamespace("cactus_flower"), true, null);

	private static int toTicks(float seconds) {
		return Mth.floor(seconds * 20.0f);
	}
}
