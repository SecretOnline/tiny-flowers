package co.secretonline.tinyflowers.block;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.platform.ServerServiceLoader;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Supplier;

public class ModBlocks {
	private static final ResourceLocation TINY_GARDEN_ID = TinyFlowers.id("tiny_garden");
	public static final ResourceKey<Block> TINY_GARDEN_KEY = ResourceKey.create(Registries.BLOCK, TINY_GARDEN_ID);
	public static final Supplier<Block> TINY_GARDEN_BLOCK = ServerServiceLoader.REGISTRY.register(
		BuiltInRegistries.BLOCK,
		TINY_GARDEN_ID,
		() -> new TinyGardenBlock(BlockBehaviour.Properties.of()
			.mapColor(MapColor.PLANT)
			.noCollission()
			.sound(SoundType.PINK_PETALS)
			.randomTicks()
			.pushReaction(PushReaction.DESTROY)));
	public static final Supplier<MapCodec<TinyGardenBlock>> TINY_GARDEN_TYPE = ServerServiceLoader.REGISTRY.register(
		BuiltInRegistries.BLOCK_TYPE,
		TINY_GARDEN_ID,
		() -> TinyGardenBlock.CODEC);



	private static final ResourceLocation TINY_FLOWER_POT_ID = TinyFlowers.id("tiny_flower_pot");
	public static final ResourceKey<Block> TINY_FLOWER_POT_KEY = ResourceKey.create(Registries.BLOCK, TINY_FLOWER_POT_ID);
	public static final Supplier<Block> TINY_FLOWER_POT_BLOCK = ServerServiceLoader.REGISTRY.register(
		BuiltInRegistries.BLOCK,
		TINY_FLOWER_POT_ID,
		() -> new TinyFlowerPotBlock(BlockBehaviour.Properties.of()
			.instabreak()
			.noOcclusion()
			.pushReaction(PushReaction.DESTROY)));
	public static final Supplier<MapCodec<TinyFlowerPotBlock>> TINY_FLOWER_POT_TYPE = ServerServiceLoader.REGISTRY.register(
		BuiltInRegistries.BLOCK_TYPE,
		TINY_FLOWER_POT_ID,
		() -> TinyFlowerPotBlock.CODEC);

	public static void initialize() {
	}
}
