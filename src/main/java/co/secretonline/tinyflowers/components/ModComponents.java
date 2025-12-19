package co.secretonline.tinyflowers.components;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

public class ModComponents {
	public static final DataComponentType<TinyFlowerComponent> TINY_FLOWER = Registry.register(
			BuiltInRegistries.DATA_COMPONENT_TYPE,
			TinyFlowers.id("tiny_flower"),
			DataComponentType.<TinyFlowerComponent>builder().persistent(TinyFlowerComponent.CODEC).build());

	public static final DataComponentType<GardenContentsComponent> GARDEN_CONTENTS = Registry.register(
			BuiltInRegistries.DATA_COMPONENT_TYPE,
			TinyFlowers.id("garden_contents"),
			DataComponentType.<GardenContentsComponent>builder().persistent(GardenContentsComponent.CODEC).build());

	public static void initialize() {
	}
}
