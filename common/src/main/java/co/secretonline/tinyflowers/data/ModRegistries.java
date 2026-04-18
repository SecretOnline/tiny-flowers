package co.secretonline.tinyflowers.data;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ModRegistries {
	public static final ResourceKey<Registry<TinyFlowerData>> TINY_FLOWER = ResourceKey
		.createRegistryKey(TinyFlowers.id("tiny_flower"));
}
