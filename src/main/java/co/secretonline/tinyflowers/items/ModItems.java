package co.secretonline.tinyflowers.items;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.blocks.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ShearsItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.DyeColor;

public class ModItems {

	// This mod defines a bunch of duplicate block items of the tiny_garden block so
	// that placing one of them will always place one. This does get a little weird
	// in places, as it definitely does not feel intended, but it works so I'm not
	// complaining.
	public static final Item TINY_DANDELION = registerGardenBlockItem("tiny_dandelion");
	public static final Item TINY_POPPY = registerGardenBlockItem("tiny_poppy");
	public static final Item TINY_BLUE_ORCHID = registerGardenBlockItem("tiny_blue_orchid");
	public static final Item TINY_ALLIUM = registerGardenBlockItem("tiny_allium");
	public static final Item TINY_AZURE_BLUET = registerGardenBlockItem("tiny_azure_bluet");
	public static final Item TINY_RED_TULIP = registerGardenBlockItem("tiny_red_tulip");
	public static final Item TINY_ORANGE_TULIP = registerGardenBlockItem("tiny_orange_tulip");
	public static final Item TINY_WHITE_TULIP = registerGardenBlockItem("tiny_white_tulip");
	public static final Item TINY_PINK_TULIP = registerGardenBlockItem("tiny_pink_tulip");
	public static final Item TINY_OXEYE_DAISY = registerGardenBlockItem("tiny_oxeye_daisy");
	public static final Item TINY_CORNFLOWER = registerGardenBlockItem("tiny_cornflower");
	public static final Item TINY_LILY_OF_THE_VALLEY = registerGardenBlockItem("tiny_lily_of_the_valley");
	public static final Item TINY_TORCHFLOWER = registerGardenBlockItem("tiny_torchflower");
	public static final Item TINY_OPEN_EYEBLOSSOM = registerGardenBlockItem("tiny_open_eyeblossom");
	public static final Item TINY_CLOSED_EYEBLOSSOM = registerGardenBlockItem("tiny_closed_eyeblossom");
	public static final Item TINY_WITHER_ROSE = registerGardenBlockItem("tiny_wither_rose");
	// This declaration MUST be last to keep the Block/Item mappings correct.
	public static final Item TINY_GARDEN_ITEM = registerGardenBlockItem("tiny_garden");

	public static final RegistryKey<Item> FLORISTS_SHEARS_ITEM_KEY = RegistryKey.of(RegistryKeys.ITEM,
			TinyFlowers.id("florists_shears"));
	public static final Item FLORISTS_SHEARS_ITEM = Registry.register(Registries.ITEM, FLORISTS_SHEARS_ITEM_KEY,
			new FloristsShearsItem(
					new Item.Settings()
							.registryKey(FLORISTS_SHEARS_ITEM_KEY)
							.maxCount(1)
							.maxDamage(238)
							.component(DataComponentTypes.TOOL, ShearsItem.createToolComponent())
							.component(DataComponentTypes.DYED_COLOR,
									new DyedColorComponent(DyeColor.RED.getEntityColor()))));

	public static Item registerGardenBlockItem(String path) {
		RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, TinyFlowers.id(path));
		return Registry.register(Registries.ITEM, itemKey,
				new BlockItem(ModBlocks.TINY_GARDEN, new Item.Settings().registryKey(itemKey)));
	}

	public static void initialize() {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register((itemGroup) -> {
			itemGroup.add(TINY_DANDELION);
			itemGroup.add(TINY_POPPY);
			itemGroup.add(TINY_BLUE_ORCHID);
			itemGroup.add(TINY_ALLIUM);
			itemGroup.add(TINY_AZURE_BLUET);
			itemGroup.add(TINY_RED_TULIP);
			itemGroup.add(TINY_ORANGE_TULIP);
			itemGroup.add(TINY_WHITE_TULIP);
			itemGroup.add(TINY_PINK_TULIP);
			itemGroup.add(TINY_OXEYE_DAISY);
			itemGroup.add(TINY_CORNFLOWER);
			itemGroup.add(TINY_LILY_OF_THE_VALLEY);
			itemGroup.add(TINY_TORCHFLOWER);
			itemGroup.add(TINY_CLOSED_EYEBLOSSOM);
			itemGroup.add(TINY_OPEN_EYEBLOSSOM);
			itemGroup.add(TINY_WITHER_ROSE);
		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((itemGroup) -> {
			itemGroup.add(FLORISTS_SHEARS_ITEM);
		});
	}
}
