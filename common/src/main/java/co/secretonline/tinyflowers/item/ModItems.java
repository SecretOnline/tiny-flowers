package co.secretonline.tinyflowers.item;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.platform.Services;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.component.DyedItemColor;

import java.util.function.Supplier;

public class ModItems {

	public static final Identifier FLORISTS_SHEARS_ID = TinyFlowers.id("florists_shears");
	public static final Supplier<Item> FLORISTS_SHEARS_ITEM = Services.REGISTRY.register(
		BuiltInRegistries.ITEM,
		FLORISTS_SHEARS_ID,
		() -> new FloristsShearsItem(
			new Item.Properties()
				.setId(ResourceKey.create(Registries.ITEM, FLORISTS_SHEARS_ID))
				.stacksTo(1)
				.durability(238)
				.component(DataComponents.TOOL, ShearsItem.createToolProperties())
				.component(DataComponents.DYED_COLOR,
					new DyedItemColor(DyeColor.RED.getTextureDiffuseColor()))));

	public static final Identifier TINY_FLOWER_ID = TinyFlowers.id("tiny_flower");
	public static final Supplier<Item> TINY_FLOWER_ITEM = Services.REGISTRY.register(
		BuiltInRegistries.ITEM,
		TINY_FLOWER_ID,
		() -> new TinyFlowerItem(
			new Item.Properties()
				.setId(ResourceKey.create(Registries.ITEM, TINY_FLOWER_ID))
				.useBlockDescriptionPrefix()));

	public static void initialize() {
	}
}
