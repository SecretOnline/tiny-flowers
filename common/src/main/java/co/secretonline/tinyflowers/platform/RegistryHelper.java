package co.secretonline.tinyflowers.platform;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

/**
 * Both (Neo)Forge and Fabric have their own ways of registering things to the registry, with not much overlap.
 */
public interface RegistryHelper {
	Supplier<Block> registerBlock(Identifier id, Supplier<Block> blockSupplier, MapCodec<? extends Block> codec);

	<T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(Identifier id, Supplier<BlockEntityType<T>> blockEntitySupplier);

	Supplier<Item> registerItem(Identifier id, Supplier<Item> itemSupplier);

	<T> Supplier<DataComponentType<T>> registerDataComponent(Identifier id, Supplier<DataComponentType<T>> componentSupplier);

	<T extends Recipe<?>> Supplier<RecipeSerializer<T>> registerRecipeSerializer(Identifier id, Supplier<RecipeSerializer<T>> serializerSupplier);
}
