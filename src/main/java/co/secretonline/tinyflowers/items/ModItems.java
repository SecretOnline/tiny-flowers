package co.secretonline.tinyflowers.items;

import co.secretonline.tinyflowers.TinyFlowers;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.component.DyedItemColor;

public class ModItems {
	public static final ResourceKey<Item> FLORISTS_SHEARS_ITEM_KEY = ResourceKey.create(Registries.ITEM,
			TinyFlowers.id("florists_shears"));
	public static final Item FLORISTS_SHEARS_ITEM = Registry.register(BuiltInRegistries.ITEM, FLORISTS_SHEARS_ITEM_KEY,
			new FloristsShearsItem(
					new Item.Properties()
							.setId(FLORISTS_SHEARS_ITEM_KEY)
							.stacksTo(1)
							.durability(238)
							.component(DataComponents.TOOL, ShearsItem.createToolProperties())
							.component(DataComponents.DYED_COLOR,
									new DyedItemColor(DyeColor.RED.getTextureDiffuseColor()))));

	public static final ResourceKey<Item> TINY_FLOWER_ITEM_KEY = ResourceKey.create(Registries.ITEM,
			TinyFlowers.id("tiny_flower"));
	public static final Item TINY_FLOWER_ITEM = Registry.register(BuiltInRegistries.ITEM, TINY_FLOWER_ITEM_KEY,
			new TinyFlowerItem(
					new Item.Properties()
							.setId(TINY_FLOWER_ITEM_KEY)
							.useBlockDescriptionPrefix()));

	public static void initialize() {
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register((itemGroup) -> {
			itemGroup.accept(FLORISTS_SHEARS_ITEM);
		});
	}
}
