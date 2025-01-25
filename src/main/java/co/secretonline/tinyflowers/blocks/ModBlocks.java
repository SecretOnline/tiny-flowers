package co.secretonline.tinyflowers.blocks;

import co.secretonline.tinyflowers.TinyFlowers;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;

public class ModBlocks {

	public static final RegistryKey<Block> TINY_GARDEN_KEY = RegistryKey.of(
			RegistryKeys.BLOCK,
			TinyFlowers.id("tiny_garden"));

	public static final Block TINY_GARDEN = registerBlockOnly(
			new GardenBlock(
					AbstractBlock.Settings.create()
							.registryKey(TINY_GARDEN_KEY)
							.mapColor(MapColor.DARK_GREEN)
							.noCollision()
							.sounds(BlockSoundGroup.PINK_PETALS)
							.pistonBehavior(PistonBehavior.DESTROY)),
			TINY_GARDEN_KEY);

	public static final Item TINY_DANDELION = registerBlockItem("tiny_dandelion");
	public static final Item TINY_POPPY = registerBlockItem("tiny_poppy");
	public static final Item TINY_BLUE_ORCHID = registerBlockItem("tiny_blue_orchid");
	public static final Item TINY_ALLIUM = registerBlockItem("tiny_allium");
	public static final Item TINY_AZURE_BLUET = registerBlockItem("tiny_azure_bluet");
	public static final Item TINY_RED_TULIP = registerBlockItem("tiny_red_tulip");
	public static final Item TINY_ORANGE_TULIP = registerBlockItem("tiny_orange_tulip");
	public static final Item TINY_WHITE_TULIP = registerBlockItem("tiny_white_tulip");
	public static final Item TINY_PINK_TULIP = registerBlockItem("tiny_pink_tulip");
	public static final Item TINY_OXEYE_DAISY = registerBlockItem("tiny_oxeye_daisy");
	public static final Item TINY_CORNFLOWER = registerBlockItem("tiny_cornflower");
	public static final Item TINY_LILY_OF_THE_VALLEY = registerBlockItem("tiny_lily_of_the_valley");
	public static final Item TINY_TORCHFLOWER = registerBlockItem("tiny_torchflower");
	public static final Item TINY_OPEN_EYEBLOSSOM = registerBlockItem("tiny_open_eyeblossom");
	public static final Item TINY_CLOSED_EYEBLOSSOM = registerBlockItem("tiny_closed_eyeblossom");
	public static final Item TINY_WITHER_ROSE = registerBlockItem("tiny_wither_rose");

	public static Block registerBlockOnly(Block block, RegistryKey<Block> blockKey) {
		return Registry.register(Registries.BLOCK, blockKey, block);
	}

	public static Item registerBlockItem(String path) {
		RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, TinyFlowers.id(path));
		return Registry.register(Registries.ITEM, itemKey,
				new BlockItem(TINY_GARDEN, new Item.Settings().registryKey(itemKey)));
	}

	public static Item registerItem(Item item, RegistryKey<Item> registryKey) {
		return Registry.register(Registries.ITEM, registryKey.getValue(), item);
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
	}
}
