package co.secretonline.tinyflowers.blocks;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;

public class ModBlocks {

	public static final RegistryKey<Block> TINY_GARDEN_KEY = RegistryKey.of(
			RegistryKeys.BLOCK,
			TinyFlowers.id("tiny_garden"));

	public static final Block TINY_GARDEN = registerBlockOnly(
			new GardenBlock(
					AbstractBlock.Settings.create()
							.mapColor(MapColor.DARK_GREEN)
							.noCollision()
							.sounds(BlockSoundGroup.PINK_PETALS)
							.pistonBehavior(PistonBehavior.DESTROY)),
			TINY_GARDEN_KEY);

	public static Block registerBlockOnly(Block block, RegistryKey<Block> blockKey) {
		return Registry.register(Registries.BLOCK, blockKey, block);
	}

	public static void initialize() {
		Registry.register(Registries.BLOCK_TYPE, TinyFlowers.id("tiny_garden"), GardenBlock.CODEC);
	}
}
