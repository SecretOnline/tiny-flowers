package co.secretonline.tinyflowers.data;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ModRegistries {
	public static ResourceKey<Registry<TinyFlowerData>> TINY_FLOWER = ResourceKey
			.createRegistryKey(TinyFlowers.id("tiny_flower"));

	public static void register() {
		Services.PLATFORM_REGISTRATION.registerDynamicRegistry(TINY_FLOWER, TinyFlowerData.CODEC);
	}
}
