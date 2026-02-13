package co.secretonline.tinyflowers.blocks;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.platform.Services;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
	public static final BlockEntityType<TinyGardenBlockEntity> TINY_GARDEN_BLOCK_ENTITY = Services.PLATFORM_REGISTRATION
		.createBlockEntityType(TinyGardenBlockEntity::new, ModBlocks.TINY_GARDEN_BLOCK);


	public static void register() {
		Services.PLATFORM_REGISTRATION.registerBlockEntity(TinyFlowers.id("tiny_garden"), TINY_GARDEN_BLOCK_ENTITY);
	}
}
