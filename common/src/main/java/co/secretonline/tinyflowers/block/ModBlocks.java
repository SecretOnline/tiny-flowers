package co.secretonline.tinyflowers.block;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.platform.ServerServiceLoader;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Supplier;

public class ModBlocks {
	private static final Identifier TINY_GARDEN_ID = TinyFlowers.id("tiny_garden");
	public static final ResourceKey<Block> TINY_GARDEN_KEY = ResourceKey.create(Registries.BLOCK, TINY_GARDEN_ID);
	public static final Supplier<Block> TINY_GARDEN_BLOCK = ServerServiceLoader.REGISTRY.register(
		BuiltInRegistries.BLOCK,
		TINY_GARDEN_ID,
		() -> new TinyGardenBlock(BlockBehaviour.Properties.of()
			.mapColor(MapColor.PLANT)
			.noCollision()
			.sound(SoundType.PINK_PETALS)
			.pushReaction(PushReaction.POPPED)
			.randomTicks()
			.setId(TINY_GARDEN_KEY)));


	private static final Identifier TINY_FLOWER_POT_ID = TinyFlowers.id("tiny_flower_pot");
	public static final ResourceKey<Block> TINY_FLOWER_POT_KEY = ResourceKey.create(Registries.BLOCK, TINY_FLOWER_POT_ID);
	public static final Supplier<Block> TINY_FLOWER_POT_BLOCK = ServerServiceLoader.REGISTRY.register(
		BuiltInRegistries.BLOCK,
		TINY_FLOWER_POT_ID,
		() -> new TinyGardenBlock(BlockBehaviour.Properties.of()
			.instabreak()
			.noOcclusion()
			.pushReaction(PushReaction.POPPED)
			.setId(TINY_FLOWER_POT_KEY)));

	public static void initialize() {
	}
}
