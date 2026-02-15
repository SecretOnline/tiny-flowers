package co.secretonline.tinyflowers;

import co.secretonline.tinyflowers.block.entity.ModBlockEntities;
import co.secretonline.tinyflowers.block.ModBlocks;
import co.secretonline.tinyflowers.item.component.ModComponents;
import co.secretonline.tinyflowers.data.ModRegistries;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.item.ModItems;
import co.secretonline.tinyflowers.item.crafting.ModRecipeSerializers;
import co.secretonline.tinyflowers.platform.NeoForgeRegistryHelper;
import co.secretonline.tinyflowers.platform.Services;
import co.secretonline.tinyflowers.platform.RegistryHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@Mod(value = TinyFlowers.MOD_ID)
@EventBusSubscriber(modid = TinyFlowers.MOD_ID)
public class NeoForgeTinyFlowers {
	public NeoForgeTinyFlowers(ModContainer container, IEventBus modBus) {
		ModBlocks.initialize();
		ModBlockEntities.initialize();
		ModComponents.initialize();
		ModItems.initialize();
		ModRecipeSerializers.initialize();

		RegistryHelper registryHelper = Services.REGISTRY;
		if (registryHelper instanceof NeoForgeRegistryHelper neoForgeRegistryHelper) {
			neoForgeRegistryHelper.registerToBus(modBus);
		} else {
			throw new NullPointerException("Registry helper was not for NeoForge");
		}
	}

	@SubscribeEvent
	public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
		event.dataPackRegistry(ModRegistries.TINY_FLOWER, TinyFlowerData.CODEC, TinyFlowerData.CODEC);
	}
}
