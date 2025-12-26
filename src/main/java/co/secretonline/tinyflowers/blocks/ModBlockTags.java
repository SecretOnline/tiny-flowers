package co.secretonline.tinyflowers.blocks;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs.TagOrElementLocation;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {
	public static final TagKey<Block> TINY_FLOWER_CAN_SURVIVE_ON = register("tiny_flower_can_survive_on");

	public static final TagOrElementLocation TINY_FLOWER_CAN_SURVIVE_ON_LOCATION = new TagOrElementLocation(
			TINY_FLOWER_CAN_SURVIVE_ON.location(), true);

	private static TagKey<Block> register(String string) {
		return TagKey.create(Registries.BLOCK, TinyFlowers.id(string));
	}

	public static void initialize() {
	}
}
