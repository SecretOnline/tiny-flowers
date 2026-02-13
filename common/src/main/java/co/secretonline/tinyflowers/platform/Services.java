package co.secretonline.tinyflowers.platform;

import co.secretonline.tinyflowers.platform.services.IPlatformRegistration;

import java.util.ServiceLoader;

public class Services {

	public static final IPlatformRegistration PLATFORM_REGISTRATION = load(IPlatformRegistration.class);

	public static <T> T load(Class<T> clazz) {
		return ServiceLoader.load(clazz)
				.findFirst()
				.orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
	}
}
