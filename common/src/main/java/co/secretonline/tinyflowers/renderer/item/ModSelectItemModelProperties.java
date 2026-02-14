package co.secretonline.tinyflowers.renderer.item;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.Identifier;

import java.util.function.BiConsumer;

public class ModSelectItemModelProperties {
	public static Identifier TINY_FLOWER_PROPERTY_ID = TinyFlowers.id("tiny_flower");
	public static SelectItemModelProperty.Type<?, ?> TINY_FLOWER_PROPERTY = TinyFlowerProperty.TYPE;
}
