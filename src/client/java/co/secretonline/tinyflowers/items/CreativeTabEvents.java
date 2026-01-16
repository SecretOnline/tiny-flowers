package co.secretonline.tinyflowers.items;

import java.util.ArrayList;
import java.util.List;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class CreativeTabEvents {
	public static void initialize() {
		// Tiny flowers should be added in the order they original flowers are in their
		// own tabs. Thanks to the CreativeModeTabsMixin, we can be pretty certain that
		// the tiny flowers group will be at the end. This means we can build up a list
		// of the original flowers in order, which we can then use to make all of the
		// tiny variants in our final tab.
		// We do also need to wait for other mods to add their own
		Identifier afterDefaultPhase = TinyFlowers.id("after_default");
		CreativeModeTabEvents.MODIFY_OUTPUT_ALL.addPhaseOrdering(Event.DEFAULT_PHASE, afterDefaultPhase);

		List<TinyFlowerData> orderedFlowerData = new ArrayList<>();
		CreativeModeTabEvents.MODIFY_OUTPUT_ALL.register(afterDefaultPhase, (tab, entries) -> {
			// I don't like that we are grabbing the registry from the Minecraft client, but
			// I coulnd't really figure anythin g else out. Minecraft's Creative inventory
			// code is already split across server and client, so I hope this isn't too bad.
			Minecraft minecraft = Minecraft.getInstance();
			if (minecraft.level == null) {
				return;
			}
			RegistryAccess registryAccess = minecraft.level.registryAccess();

			if (tab.equals(ModItems.TINY_FLOWERS_GROUP)) {
				// We're pretty certain at this stage that all of the other tab groups have
				// gone, so we can add all the flowers now.

				for (TinyFlowerData tinyFlowerData : orderedFlowerData) {
					entries.accept(tinyFlowerData.getItemStack(1));
				}
				return;
			}

			for (ItemStack itemStack : entries.getDisplayStacks()) {
				if (itemStack.getItem() instanceof BlockItem blockItem) {
					Block block = blockItem.getBlock();
					TinyFlowerData flowerData = TinyFlowerData.findByOriginalBlock(registryAccess, block);
					if (flowerData != null) {
						orderedFlowerData.add(flowerData);
					}
				}
			}
		});
	}
}
