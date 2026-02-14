package co.secretonline.tinyflowers.items;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.platform.Services;
import com.google.common.base.Suppliers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.component.DyedItemColor;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ModItems {

	public static final Identifier FLORISTS_SHEARS_ID = TinyFlowers.id("florists_shears");
	public static final ResourceKey<Item> FLORISTS_SHEARS_ITEM_KEY = ResourceKey.create(Registries.ITEM,
		TinyFlowers.id("florists_shears"));
	public static final Supplier<Item> FLORISTS_SHEARS_ITEM = Suppliers.memoize(() -> new FloristsShearsItem(
		new Item.Properties()
			.setId(ResourceKey.create(Registries.ITEM, FLORISTS_SHEARS_ID))
			.stacksTo(1)
			.durability(238)
			.component(DataComponents.TOOL, ShearsItem.createToolProperties())
			.component(DataComponents.DYED_COLOR,
				new DyedItemColor(DyeColor.RED.getTextureDiffuseColor()))));

	public static final Identifier TINY_FLOWER_ID = TinyFlowers.id("tiny_flower");
	public static final Supplier<Item> TINY_FLOWER_ITEM = Suppliers.memoize(() -> new TinyFlowerItem(
		new Item.Properties()
			.setId(ResourceKey.create(Registries.ITEM, TINY_FLOWER_ID))
			.useBlockDescriptionPrefix()));

	public static void register(BiConsumer<Identifier, Supplier<Item>> register) {
		register.accept(FLORISTS_SHEARS_ID, FLORISTS_SHEARS_ITEM);
		register.accept(TINY_FLOWER_ID, TINY_FLOWER_ITEM);
	}
}
