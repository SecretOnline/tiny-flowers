package co.secretonline.tinyflowers.blocks;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.platform.Services;
import com.google.common.base.Suppliers;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ModBlockEntities {
	public static final Supplier<BlockEntityType<TinyGardenBlockEntity>> TINY_GARDEN_BLOCK_ENTITY = Suppliers.memoize(() -> Services.PLATFORM_REGISTRATION
		.createBlockEntityType(TinyGardenBlockEntity::new, ModBlocks.TINY_GARDEN_BLOCK.get()));

	public static void register(BiConsumer<Identifier, Supplier<? extends BlockEntityType<? extends BlockEntity>>> registerBlockEntity) {
		registerBlockEntity.accept(TinyFlowers.id("tiny_garden"), TINY_GARDEN_BLOCK_ENTITY);
	}
}
