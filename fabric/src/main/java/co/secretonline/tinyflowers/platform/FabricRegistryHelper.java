package co.secretonline.tinyflowers.platform;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public class FabricRegistryHelper implements RegistryHelper {
	@Override
	public <T, U extends T> DeferredRegistryObject<U> register(Registry<T> objRegistry, Identifier id, Supplier<U> objSupplier) {
		return new FabricDeferredRegistryObject<>(Registry.register(objRegistry, id, objSupplier.get()));
	}

	public static class FabricDeferredRegistryObject<T> implements DeferredRegistryObject<T> {
		private final T obj;

		public FabricDeferredRegistryObject(T obj) {
			this.obj = obj;
		}

		public T get() {
			return this.obj;
		}
	}
}
