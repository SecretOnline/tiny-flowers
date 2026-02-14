package co.secretonline.tinyflowers.data;

import co.secretonline.tinyflowers.TinyFlowers;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Consumer;

public class ModRegistries {
	public static ResourceKey<Registry<TinyFlowerData>> TINY_FLOWER = ResourceKey
		.createRegistryKey(TinyFlowers.id("tiny_flower"));
}
