package co.secretonline.tinyflowers.components;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.platform.Services;
import net.minecraft.core.component.DataComponentType;

public class ModComponents {
	public static final DataComponentType<TinyFlowerComponent> TINY_FLOWER = DataComponentType.<TinyFlowerComponent>builder()
		.persistent(TinyFlowerComponent.CODEC)
		.build();

	public static final DataComponentType<GardenContentsComponent> GARDEN_CONTENTS = DataComponentType.<GardenContentsComponent>builder()
		.persistent(GardenContentsComponent.CODEC)
		.build();

	public static void register() {
		Services.PLATFORM_REGISTRATION.registerDataComponent(TinyFlowers.id("tiny_flower"), TINY_FLOWER);
		Services.PLATFORM_REGISTRATION.registerDataComponent(TinyFlowers.id("garden_contents"), GARDEN_CONTENTS);
	}
}
