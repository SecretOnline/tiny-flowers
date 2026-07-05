package co.secretonline.tinyflowers.platform;

import java.util.ServiceLoader;

public class ServerServiceLoader {

	public static final AccessHelper PLATFORM_REGISTRATION = load(AccessHelper.class);

	public static final RegistryHelper REGISTRY = load(RegistryHelper.class);

	public static <T> T load(Class<T> clazz) {
		return ServiceLoader.load(clazz, clazz.getClassLoader())
				.findFirst()
				.orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
	}
}
