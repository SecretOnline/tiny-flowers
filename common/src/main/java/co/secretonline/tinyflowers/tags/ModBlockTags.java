package co.secretonline.tinyflowers.tags;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {
	public static final TagKey<Block> SUPPORTS_VEGETATION = create(TinyFlowers.id("supports_vegetation"));

	private static TagKey<Block> create(final Identifier id) {
		return TagKey.create(Registries.BLOCK, id);
	}
}
