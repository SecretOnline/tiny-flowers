package co.secretonline.tinyflowers;

import co.secretonline.tinyflowers.item.ModCreativeModeTabs;
import co.secretonline.tinyflowers.item.ModItems;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTabs;

public class FabricCreativeTabHandler {

	public static void addShearsItems() {
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
			.register((itemGroup) -> itemGroup.accept(ModItems.FLORISTS_SHEARS_ITEM.get()));
		CreativeModeTabEvents.modifyOutputEvent(ModCreativeModeTabs.TINY_FLOWERS_TAB_KEY)
			.register((itemGroup) -> itemGroup.accept(ModItems.FLORISTS_SHEARS_ITEM.get()));
	}

	public static void addTinyFlowerItems() {
		// Tiny flowers should be added in the order they original flowers are in their
		// own tabs. Thanks to the CreativeModeTabsMixin, we can be pretty certain that
		// the tiny flowers group will be at the end. This means we can build up a list
		// of the original flowers in order, which we can then use to make all of the
		// tiny variants in our final tab.
		// We do also need to wait for other mods to add their own
		Identifier afterDefaultPhase = TinyFlowers.id("after_default");
		CreativeModeTabEvents.MODIFY_OUTPUT_ALL.addPhaseOrdering(Event.DEFAULT_PHASE, afterDefaultPhase);

		CreativeModeTabEvents.MODIFY_OUTPUT_ALL.register(afterDefaultPhase, (tab, entries) -> {
			if (tab.equals(ModCreativeModeTabs.TINY_FLOWERS_TAB.get())) {
				ModCreativeModeTabs.addCollectedFlowers(entries.getDisplayStacks(), entries::accept);
				return;
			}

			ModCreativeModeTabs.collectFlowerData(entries.getDisplayStacks());
		});
	}
}
