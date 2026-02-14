package co.secretonline.tinyflowers.components;

import co.secretonline.tinyflowers.TinyFlowers;
import com.google.common.base.Suppliers;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.Identifier;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ModComponents {
	public static final Supplier<DataComponentType<TinyFlowerComponent>> TINY_FLOWER = Suppliers.memoize(() -> DataComponentType.<TinyFlowerComponent>builder()
		.persistent(TinyFlowerComponent.CODEC)
		.build());

	public static final Supplier<DataComponentType<GardenContentsComponent>> GARDEN_CONTENTS = Suppliers.memoize(() -> DataComponentType.<GardenContentsComponent>builder()
		.persistent(GardenContentsComponent.CODEC)
		.build());

	public static void register(BiConsumer<Identifier, Supplier<? extends DataComponentType<?>>> register) {
		register.accept(TinyFlowers.id("tiny_flower"), TINY_FLOWER);
		register.accept(TinyFlowers.id("garden_contents"), GARDEN_CONTENTS);
	}
}
