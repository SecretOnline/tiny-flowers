package co.secretonline.tinyflowers;

import co.secretonline.tinyflowers.data.ModRegistries;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.platform.NeoForgeRegistryHelper;
import co.secretonline.tinyflowers.platform.Services;
import co.secretonline.tinyflowers.platform.services.RegistryHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@Mod(value = TinyFlowers.MOD_ID)
public class NeoForgeTinyFlowers {
	public NeoForgeTinyFlowers(ModContainer container, IEventBus modBus) {
		RegistryHelper registryHelper = Services.REGISTRY;
		if (registryHelper instanceof NeoForgeRegistryHelper neoForgeRegistryHelper) {
			neoForgeRegistryHelper.registerToBus(modBus);
		} else {
			throw new NullPointerException("Registry helper was not for NeoForge");
		}
	}

	@SubscribeEvent
	public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
		event.dataPackRegistry(ModRegistries.TINY_FLOWER, TinyFlowerData.CODEC);
	}

	static {
		TinyFlowers.initialize();
	}
}
