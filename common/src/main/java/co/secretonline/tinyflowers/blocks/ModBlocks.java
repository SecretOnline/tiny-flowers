package co.secretonline.tinyflowers.blocks;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.platform.Services;
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
	public static final Supplier<Block> TINY_GARDEN_BLOCK = Services.REGISTRY.register(
		BuiltInRegistries.BLOCK,
		TINY_GARDEN_ID,
		() -> new TinyGardenBlock(BlockBehaviour.Properties.of()
			.mapColor(MapColor.PLANT)
			.noCollision()
			.sound(SoundType.PINK_PETALS)
			.pushReaction(PushReaction.DESTROY)
			.randomTicks()
			.setId(ResourceKey.create(Registries.BLOCK, TINY_GARDEN_ID))));
	public static final Supplier<MapCodec<TinyGardenBlock>> TINY_GARDEN_TYPE = Services.REGISTRY.register(
		BuiltInRegistries.BLOCK_TYPE,
		TINY_GARDEN_ID,
		() -> TinyGardenBlock.CODEC);

	public static void initialize() {
	}
}
