package co.secretonline.tinyflowers.platform;

import java.util.function.Supplier;

public interface DeferredRegistryObject<T> extends Supplier<T> {
	T get();
}
