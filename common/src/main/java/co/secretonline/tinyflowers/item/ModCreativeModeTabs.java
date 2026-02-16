package co.secretonline.tinyflowers.item;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModCreativeModeTabs {
	private static final Identifier TINY_FLOWERS_TAB_ID = TinyFlowers.id("tiny_flowers");
	public static final ResourceKey<CreativeModeTab> TINY_FLOWERS_TAB_KEY = ResourceKey
		.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), TINY_FLOWERS_TAB_ID);

	public static final Supplier<CreativeModeTab> TINY_FLOWERS_TAB = Services.REGISTRY.register(
		BuiltInRegistries.CREATIVE_MODE_TAB,
		TINY_FLOWERS_TAB_ID,
		() -> CreativeModeTab
			.builder(CreativeModeTab.Row.TOP, 0)
			.title(Component.translatable("itemGroup." + TinyFlowers.MOD_ID))
			.icon(() -> new ItemStack(ModItems.TINY_FLOWER_ITEM.get()))
			.build());

	private static final List<TinyFlowerData> orderedFlowerData = new ArrayList<>();

	public static void collectFlowerData(Iterable<ItemStack> displayStacks) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.level == null) return;
		RegistryAccess registryAccess = minecraft.level.registryAccess();

		for (ItemStack itemStack : displayStacks) {
			if (itemStack.getItem() instanceof BlockItem blockItem) {
				Block block = blockItem.getBlock();
				TinyFlowerData flowerData = TinyFlowerData.findByOriginalBlock(registryAccess, block);
				if (flowerData != null) {
					orderedFlowerData.add(flowerData);
				}
			}
		}
	}

	public static void addCollectedFlowers(
		Collection<ItemStack> currentEntries,
		Consumer<ItemStack> consumer
	) {
		for (TinyFlowerData tinyFlowerData : orderedFlowerData) {
			ItemStack newStack = tinyFlowerData.getItemStack(1);
			if (!currentEntries.contains(newStack)) {
				consumer.accept(newStack);
			}
		}
		orderedFlowerData.clear();
	}

	public static void initialize(){}
}
