package co.secretonline.tinyflowers.blocks;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModItemTags {
	public static final TagKey<Item> TINY_FLOWERS = TagKey.of(RegistryKeys.ITEM, TinyFlowers.id("tiny_flowers"));
}
