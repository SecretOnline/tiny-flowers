package co.secretonline.tinyflowers;

import co.secretonline.tinyflowers.item.ModCreativeModeTabs;
import co.secretonline.tinyflowers.item.ModItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@EventBusSubscriber(modid = TinyFlowers.MOD_ID)
public class NeoForgeCreativeTabHandler {

	@SubscribeEvent
	public static void addShearsItems(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES ||
			event.getTabKey() == ModCreativeModeTabs.TINY_FLOWERS_TAB_KEY) {
			event.accept(ModItems.FLORISTS_SHEARS_ITEM.get());
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void addTinyFlowerItems(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == ModCreativeModeTabs.TINY_FLOWERS_TAB_KEY) {
			ModCreativeModeTabs.addCollectedFlowers(event.getParentEntries(), event::accept);
			return;
		}

		ModCreativeModeTabs.collectFlowerData(event.getParentEntries());
	}
}
