package co.secretonline.tinyflowers.blocks;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {
	public static final TagKey<Item> TINY_FLOWERS = TagKey.create(Registries.ITEM, TinyFlowers.id("tiny_flowers"));
}
