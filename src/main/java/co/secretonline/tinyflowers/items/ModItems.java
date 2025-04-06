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
	private static final Map<FlowerVariant, Item> FLOWER_VARIANT_ITEMS = registerFlowerVariantItems();
	// This declaration MUST be last to keep the Block/Item mappings correct.
	public static final Item TINY_GARDEN_ITEM = registerGardenBlockItem("tiny_garden",
			(settings) -> settings.component(ModComponents.TINY_FLOWERS_COMPONENT_TYPE, TinyFlowersComponent.EMPTY));

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

	public static Item registerGardenBlockItem(String path, Function<Item.Settings, Item.Settings> settings) {
		RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, TinyFlowers.id(path));
		return Registry.register(Registries.ITEM, itemKey,
				new BlockItem(
						ModBlocks.TINY_GARDEN,
						settings.apply(new Item.Settings().registryKey(itemKey))));
	}

	public static void initialize() {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register((itemGroup) -> {
			for (Map.Entry<FlowerVariant, Item> entry : FLOWER_VARIANT_ITEMS.entrySet()) {
				itemGroup.add(entry.getValue());
			}
		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((itemGroup) -> {
			itemGroup.add(FLORISTS_SHEARS_ITEM);
		});
	}
}
