package co.secretonline.tinyflowers;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FlowerbedBlock;
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

	public static final RegistryKey<Block> TEST_FLOWER_KEY = RegistryKey.of(
			RegistryKeys.BLOCK,
			TinyFlowers.id("test_flower"));

	public static final Block TEST_FLOWER = register(
			new FlowerbedBlock(
					AbstractBlock.Settings.create()
							.registryKey(TEST_FLOWER_KEY)
							.mapColor(MapColor.DARK_GREEN)
							.noCollision()
							.sounds(BlockSoundGroup.PINK_PETALS)
							.pistonBehavior(PistonBehavior.DESTROY)),
			TEST_FLOWER_KEY);

	public static Block register(Block block, RegistryKey<Block> blockKey) {
		RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, blockKey.getValue());

		BlockItem blockItem = new BlockItem(block, new Item.Settings().registryKey(itemKey));
		Registry.register(Registries.ITEM, itemKey, blockItem);

		return Registry.register(Registries.BLOCK, blockKey, block);
	}

	static void initialize() {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register((itemGroup) -> {
			itemGroup.add(ModBlocks.TEST_FLOWER.asItem());
		});
	}
}
