package co.secretonline.tinyflowers.items;

import java.util.ArrayList;
import java.util.List;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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

	public static final ResourceKey<CreativeModeTab> TINY_FLOWERS_GROUP_KEY = ResourceKey
			.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), TinyFlowers.id("tiny_flowers"));
	public static final CreativeModeTab TINY_FLOWERS_GROUP = FabricItemGroup.builder()
			.icon(() -> new ItemStack(TINY_FLOWER_ITEM))
			.title(Component.translatable("itemGroup." + TinyFlowers.MOD_ID))
			.build();

	public static void initialize() {
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TINY_FLOWERS_GROUP_KEY, TINY_FLOWERS_GROUP);

		ItemGroupEvents.modifyEntriesEvent(TINY_FLOWERS_GROUP_KEY).register((itemGroup) -> {
			itemGroup.accept(FLORISTS_SHEARS_ITEM);
		});

		// Tiny flowers should be added in the order they original flowers are in their
		// own tabs.
		// Thanks to the CreativeModeTabsMixin, we can be pretty certain that the tiny
		// flowers group
		// will be at the end. This means we can build up a list of he original flowers
		// in order, which we can then use to make all of the tiny variants in our final
		// tab.
		Identifier afterDefaultPhase = TinyFlowers.id("after_default");
		ItemGroupEvents.MODIFY_ENTRIES_ALL.addPhaseOrdering(Event.DEFAULT_PHASE, afterDefaultPhase);

		List<TinyFlowerData> orderedFlowerData = new ArrayList<>();
		ItemGroupEvents.MODIFY_ENTRIES_ALL.register(afterDefaultPhase, (tab, entries) -> {

			// NOTE: This is the point where adding tiny flowers to the Creative menu breaks down. I've been able to solve a bunch of little problems, but this one evades me. If we could get the registry contents here, then we could do it easily. In fact most of the ode for it is below. Unfortunately, it doesn't seem like there's a reliable way to do so, so it sort of just stops here.
			RegistryAccess registryAccess = ????????;

			if (tab.equals(TINY_FLOWERS_GROUP)) {
				// We're pretty certain at this stage that all of the other tab groups have
				// gone, so we can add all the flowers now.

				for (TinyFlowerData tinyFlowerData : orderedFlowerData) {
					entries.accept(tinyFlowerData.getItemStack(1));
				}
				return;
			}

			for (ItemStack itemStack : entries.getDisplayStacks()) {
				TinyFlowerData flowerData = TinyFlowerData.findByItemStack(registryAccess, itemStack);
				if (flowerData != null) {
					orderedFlowerData.add(flowerData);
				}
			}
		});

	}
}
