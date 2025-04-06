package co.secretonline.tinyflowers.components;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModComponents {
	public static final ComponentType<TinyFlowersComponent> TINY_FLOWERS_COMPONENT_TYPE = Registry.register(
			Registries.DATA_COMPONENT_TYPE,
			TinyFlowers.id("tiny_flowers"),
			ComponentType.<TinyFlowersComponent>builder().codec(TinyFlowersComponent.CODEC).build());

	public static void initialize() {
	}
}
