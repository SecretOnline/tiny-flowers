package co.secretonline.tinyflowers.blocks;

import co.secretonline.tinyflowers.TinyFlowers;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
	public static final BlockEntityType<TinyGardenBlockEntity> TINY_GARDEN_BLOCK_ENTITY = register("tiny_garden",
			TinyGardenBlockEntity::new, ModBlocks.TINY_GARDEN_BLOCK);

	private static <T extends BlockEntity> BlockEntityType<T> register(
			String name,
			FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
			Block... blocks) {
		Identifier id = TinyFlowers.id(name);
		return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id,
				FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
	}

	public static void initialize() {
	}
}
