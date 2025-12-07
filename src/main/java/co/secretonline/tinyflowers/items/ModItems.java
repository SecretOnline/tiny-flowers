package co.secretonline.tinyflowers.items;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.blocks.FlowerVariant;
import co.secretonline.tinyflowers.blocks.ModBlocks;
import co.secretonline.tinyflowers.components.ModComponents;
import co.secretonline.tinyflowers.components.TinyFlowersComponent;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.component.DyedItemColor;

public class ModItems {

	// This mod defines a bunch of duplicate block items of the tiny_garden block so
	// that placing one of them will always place one. This does get a little weird
	// in places, as it definitely does not feel intended, but it works so I'm not
	// complaining.
	private static final Map<FlowerVariant, Item> FLOWER_VARIANT_ITEMS = registerFlowerVariantItems();
	// This declaration MUST be last to keep the Block/Item mappings correct.
	public static final Item TINY_GARDEN_ITEM = registerGardenBlockItem("tiny_garden",
			// Add a default component so tooltips work for older items
			(settings) -> settings.component(ModComponents.TINY_FLOWERS_COMPONENT_TYPE, TinyFlowersComponent.EMPTY));

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

	private static Map<FlowerVariant, Item> registerFlowerVariantItems() {
		Map<FlowerVariant, Item> flowerVariantItems = new HashMap<>();

		for (FlowerVariant variant : FlowerVariant.values()) {
			if (!variant.shouldCreateItem()) {
				continue;
			}

			Item item = registerGardenBlockItem(variant.getItemIdentifier().getPath());
			flowerVariantItems.put(variant, item);
		}

		return flowerVariantItems;
	}

	public static Item registerGardenBlockItem(String path) {
		return registerGardenBlockItem(path, Function.identity());
	}

	public static Item registerGardenBlockItem(String path, Function<Item.Properties, Item.Properties> settings) {
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, TinyFlowers.id(path));
		return Registry.register(BuiltInRegistries.ITEM, itemKey,
				new BlockItem(
						ModBlocks.TINY_GARDEN,
						settings.apply(new Item.Properties().setId(itemKey))));
	}

	public static void initialize() {
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register((itemGroup) -> {
			for (Map.Entry<FlowerVariant, Item> entry : FLOWER_VARIANT_ITEMS.entrySet()) {
				itemGroup.accept(entry.getValue());
			}
		});
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register((itemGroup) -> {
			itemGroup.accept(FLORISTS_SHEARS_ITEM);
		});
	}
}
