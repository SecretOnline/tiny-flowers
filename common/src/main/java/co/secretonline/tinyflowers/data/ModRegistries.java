package co.secretonline.tinyflowers.data;

import co.secretonline.tinyflowers.TinyFlowers;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Consumer;

public class ModRegistries {
	public static ResourceKey<Registry<TinyFlowerData>> TINY_FLOWER = ResourceKey
		.createRegistryKey(TinyFlowers.id("tiny_flower"));

	public static void register(Consumer<RegistryInfo<?>> register) {
		register.accept(new RegistryInfo<>(TINY_FLOWER, TinyFlowerData.CODEC));
	}

	public record RegistryInfo<T>(ResourceKey<Registry<T>> registryKey, Codec<T> codec) {
	}
}
