package co.secretonline.tinyflowers.data;

import co.secretonline.tinyflowers.TinyFlowers;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ModRegistries {
	public static ResourceKey<Registry<TinyFlowerData>> TINY_FLOWER = ResourceKey
			.createRegistryKey(TinyFlowers.id("tiny_flower"));

	public static void initialize() {
		DynamicRegistries.registerSynced(TINY_FLOWER, TinyFlowerData.CODEC);
	}
}
