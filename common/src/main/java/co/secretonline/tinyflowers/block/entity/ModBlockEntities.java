package co.secretonline.tinyflowers.block.entity;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.block.ModBlocks;
import co.secretonline.tinyflowers.platform.ServerServiceLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class ModBlockEntities {
	public static final Supplier<BlockEntityType<TinyGardenBlockEntity>> TINY_GARDEN_BLOCK_ENTITY = ServerServiceLoader.REGISTRY.register(
		BuiltInRegistries.BLOCK_ENTITY_TYPE,
		TinyFlowers.id("tiny_garden"),
		() -> ServerServiceLoader.PLATFORM_REGISTRATION
			.createBlockEntityType(TinyGardenBlockEntity::new, ModBlocks.TINY_GARDEN_BLOCK.get()));

	public static void initialize() {
	}
}
