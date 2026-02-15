package co.secretonline.tinyflowers.platform;

public class FabricDeferredRegistryObject<T> implements DeferredRegistryObject<T> {
	private final T obj;

	public FabricDeferredRegistryObject(T obj) {
		this.obj = obj;
	}

	public T get() {
		return this.obj;
	}
}
