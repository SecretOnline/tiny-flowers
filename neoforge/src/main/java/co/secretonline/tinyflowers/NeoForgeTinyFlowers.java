package co.secretonline.tinyflowers;

import co.secretonline.tinyflowers.data.ModRegistries;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.init.NeoForgeInitializer;
import co.secretonline.tinyflowers.init.TinyFlowersInitializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@Mod(value = TinyFlowers.MOD_ID)
public class NeoForgeTinyFlowers {
	private final TinyFlowersInitializer init;

	public NeoForgeTinyFlowers(ModContainer container, IEventBus modBus) {
		init = new NeoForgeInitializer(modBus);
	}

	@SubscribeEvent
	public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
		event.dataPackRegistry(ModRegistries.TINY_FLOWER, TinyFlowerData.CODEC);
	}
}
