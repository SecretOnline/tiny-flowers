package co.secretonline.tinyflowers.platform;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

/**
 * Both (Neo)Forge and Fabric have their own ways of registering things to the registry, with not much overlap.
 */
public interface RegistryHelper {
	<T, U extends T> DeferredRegistryObject<U> register(Registry<T> objRegistry, ResourceLocation id, Supplier<U> objSupplier);
}
