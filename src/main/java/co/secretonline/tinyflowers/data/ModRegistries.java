package co.secretonline.tinyflowers.data;

import com.mojang.serialization.Codec;

import co.secretonline.tinyflowers.TinyFlowers;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class ModRegistries {
	public static ResourceKey<Registry<TinyFlowerData>> TINY_FLOWER = register(
			TinyFlowers.id("tiny_flower"),
			TinyFlowerData.CODEC,
			true);

	private static <T> ResourceKey<Registry<T>> register(Identifier id, Codec<T> codec, boolean sync) {
		ResourceKey<Registry<T>> key = ResourceKey.createRegistryKey(TinyFlowers.id("tiny_flower"));

		if (sync) {
			DynamicRegistries.registerSynced(key, codec);
		}

		return key;
	}

	public static void initialize() {
	}
}
