package co.secretonline.tinyflowers;

import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.item.ModItems;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@EventBusSubscriber(modid = TinyFlowers.MOD_ID)
public class NeoForgeCreativeTabHandler {
	public static final ResourceKey<CreativeModeTab> TINY_FLOWERS_GROUP_KEY = ResourceKey
		.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), TinyFlowers.id("tiny_flowers"));
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, TinyFlowers.MOD_ID);

	public static final Supplier<CreativeModeTab> TINY_FLOWERS_GROUP = CREATIVE_MODE_TAB.register(TinyFlowers.MOD_ID, () -> CreativeModeTab.builder()
		.title(Component.translatable("itemGroup." + TinyFlowers.MOD_ID))
		.icon(() -> new ItemStack(ModItems.TINY_FLOWER_ITEM.get()))
		.displayItems((params, output) -> {
			output.accept(ModItems.FLORISTS_SHEARS_ITEM.get());
		})
		.build()
	);

	private static final List<TinyFlowerData> orderedFlowerData = new ArrayList<>();

	public static void registerToBus(IEventBus modBus) {
		CREATIVE_MODE_TAB.register(modBus);
	}

	@SubscribeEvent
	public static void addShearsToToolsTab(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
			event.accept(ModItems.FLORISTS_SHEARS_ITEM.get());
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void addTinyFlowers(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == TINY_FLOWERS_GROUP_KEY) {
			// We're pretty certain at this stage that all of the other tab groups have
			// gone, so we can add all the flowers now.

			ObjectSortedSet<ItemStack> currentList = event.getParentEntries();
			for (TinyFlowerData tinyFlowerData : orderedFlowerData) {
				ItemStack newStack = tinyFlowerData.getItemStack(1);
				if (!currentList.contains(newStack)) {
					event.accept(newStack);
				}
			}
			orderedFlowerData.clear();
			return;
		}

		// I don't like that we are grabbing the registry from the Minecraft client, but
		// I couldn't really figure anything else out. Minecraft's Creative inventory
		// code is already split across server and client, so I hope this isn't too bad.
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.level == null) {
			return;
		}
		RegistryAccess registryAccess = minecraft.level.registryAccess();

		for (ItemStack itemStack : event.getParentEntries()) {
			if (itemStack.getItem() instanceof BlockItem blockItem) {
				Block block = blockItem.getBlock();
				TinyFlowerData flowerData = TinyFlowerData.findByOriginalBlock(registryAccess, block);
				if (flowerData != null) {
					orderedFlowerData.add(flowerData);
				}
			}
		}
	}
}
