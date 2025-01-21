package co.secretonline.tinyflowers.items;

import co.secretonline.tinyflowers.TinyFlowers;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class ModItems {
	public static final RegistryKey<Item> TINY_RED_TULIP_KEY = RegistryKey.of(
			RegistryKeys.ITEM,
			TinyFlowers.id("tiny_red_tulip"));
	public static final Item TINY_RED_TULIP = register(
			new Item(new Item.Settings().registryKey(TINY_RED_TULIP_KEY)),
			TINY_RED_TULIP_KEY);

	public static Item register(Item item, RegistryKey<Item> registryKey) {
		return Registry.register(Registries.ITEM, registryKey.getValue(), item);
	}

	public static void initialize() {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register((itemGroup) -> {
			itemGroup.add(TINY_RED_TULIP);
		});
	}
}
