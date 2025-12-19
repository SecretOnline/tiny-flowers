package co.secretonline.tinyflowers.blocks;

import java.util.function.Function;

import com.mojang.serialization.MapCodec;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class ModBlocks {
	public static final Block TINY_GARDEN_BLOCK = register(
			"tiny_garden",
			TinyGardenBlock::new,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.PLANT)
					.noCollision()
					.sound(SoundType.PINK_PETALS)
					.pushReaction(PushReaction.DESTROY),
			TinyGardenBlock.CODEC,
			true);

	private static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory,
			BlockBehaviour.Properties settings, MapCodec<? extends Block> codec, boolean shouldRegisterItem) {
		Identifier id = TinyFlowers.id(name);

		// Create a registry key for the block
		ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, id);
		// Create the block instance
		Block block = blockFactory.apply(settings.setId(blockKey));

		Registry.register(BuiltInRegistries.BLOCK_TYPE, id, codec);
		return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
	}

	public static void initialize() {
	}
}
