package co.secretonline.tinyflowers.renderer.item;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.Identifier;

import java.util.function.BiConsumer;

public class ModSelectItemModelProperties {

	public static void registerClient(BiConsumer<Identifier, SelectItemModelProperty.Type<?, ?>> registerSelectItemModelProperty) {
		registerSelectItemModelProperty.accept(TinyFlowers.id("tiny_flower"), TinyFlowerProperty.TYPE);
	}
}
