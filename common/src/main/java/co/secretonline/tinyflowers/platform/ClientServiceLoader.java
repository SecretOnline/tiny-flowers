package co.secretonline.tinyflowers.platform;

import java.util.ServiceLoader;

public class ClientServiceLoader {

	public static final FlowerModelHelper FLOWER_MODELS = load(FlowerModelHelper.class);

	public static <T> T load(Class<T> clazz) {
		return ServiceLoader.load(clazz, clazz.getClassLoader())
				.findFirst()
				.orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
	}
}
