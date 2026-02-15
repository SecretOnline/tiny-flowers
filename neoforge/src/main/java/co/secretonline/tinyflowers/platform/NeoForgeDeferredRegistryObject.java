package co.secretonline.tinyflowers.platform;

import java.util.function.Supplier;

public class NeoForgeDeferredRegistryObject<T> implements DeferredRegistryObject<T> {

	private final Supplier<T> supplier;

	public NeoForgeDeferredRegistryObject(Supplier<T> supplier) {
		this.supplier = supplier;
	}

	public T get() {
		return this.supplier.get();
	}
}
