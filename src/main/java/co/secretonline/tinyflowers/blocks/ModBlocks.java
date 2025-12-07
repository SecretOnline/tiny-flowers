package co.secretonline.tinyflowers.blocks;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class ModBlocks {

	public static final ResourceKey<Block> TINY_GARDEN_KEY = ResourceKey.create(
			Registries.BLOCK,
			TinyFlowers.id("tiny_garden"));

	public static final Block TINY_GARDEN = registerBlockOnly(
			new GardenBlock(
					BlockBehaviour.Properties.of()
							.setId(TINY_GARDEN_KEY)
							.mapColor(MapColor.PLANT)
							.noCollision()
							.sound(SoundType.PINK_PETALS)
							.pushReaction(PushReaction.DESTROY)),
			TINY_GARDEN_KEY);

	public static Block registerBlockOnly(Block block, ResourceKey<Block> blockKey) {
		return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
	}

	public static void initialize() {
		Registry.register(BuiltInRegistries.BLOCK_TYPE, TinyFlowers.id("tiny_garden"), GardenBlock.CODEC);
	}
}
