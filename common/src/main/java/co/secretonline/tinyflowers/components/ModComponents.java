package co.secretonline.tinyflowers.components;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.platform.Services;
import net.minecraft.core.component.DataComponentType;

import java.util.function.Supplier;

public class ModComponents {
	public static final Supplier<DataComponentType<TinyFlowerComponent>> TINY_FLOWER = Services.REGISTRY.registerDataComponent(
		TinyFlowers.id("tiny_flower"),
		() -> DataComponentType.<TinyFlowerComponent>builder()
			.persistent(TinyFlowerComponent.CODEC)
			.build());

	public static final Supplier<DataComponentType<GardenContentsComponent>> GARDEN_CONTENTS = Services.REGISTRY.registerDataComponent(
		TinyFlowers.id("garden_contents"),
		() -> DataComponentType.<GardenContentsComponent>builder()
			.persistent(GardenContentsComponent.CODEC)
			.build());

	public static void initialize() {
	}
}
