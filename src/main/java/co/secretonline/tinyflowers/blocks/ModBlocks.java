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

	public static final RegistryKey<Item> TINY_RED_TULIP_KEY = RegistryKey.of(
			RegistryKeys.ITEM,
			TinyFlowers.id("tiny_red_tulip"));
	public static final Item TINY_RED_TULIP = registerItem(
			new BlockItem(TINY_GARDEN, new Item.Settings().registryKey(TINY_RED_TULIP_KEY)),
			TINY_RED_TULIP_KEY);

	public static Block registerBlockOnly(Block block, RegistryKey<Block> blockKey) {
		return Registry.register(Registries.BLOCK, blockKey, block);
	}

	public static Item registerItem(Item item, RegistryKey<Item> registryKey) {
		return Registry.register(Registries.ITEM, registryKey.getValue(), item);
	}

	public static void initialize() {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register((itemGroup) -> {
			itemGroup.add(TINY_RED_TULIP);
		});
	}
}
