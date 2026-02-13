package co.secretonline.tinyflowers.blocks;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class ModBlocks {
	public static final ResourceKey<Block> TINY_GARDEN_BLOCK_KEY = ResourceKey.create(Registries.BLOCK, TinyFlowers.id("tiny_garden"));
	public static final Block TINY_GARDEN_BLOCK = new TinyGardenBlock(BlockBehaviour.Properties.of()
		.mapColor(MapColor.PLANT)
		.noCollision()
		.sound(SoundType.PINK_PETALS)
		.pushReaction(PushReaction.DESTROY)
		.randomTicks()
		.setId(TINY_GARDEN_BLOCK_KEY));


	public static void register() {
		Services.PLATFORM_REGISTRATION.registerBlock(TINY_GARDEN_BLOCK_KEY, TINY_GARDEN_BLOCK, TinyGardenBlock.CODEC);
	}
}
