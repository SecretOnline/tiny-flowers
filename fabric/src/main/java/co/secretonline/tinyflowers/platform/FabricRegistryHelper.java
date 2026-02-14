package co.secretonline.tinyflowers.platform;

import co.secretonline.tinyflowers.platform.services.RegistryHelper;
import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class FabricRegistryHelper implements RegistryHelper {
	@Override
	public Supplier<Block> registerBlock(Identifier id, Supplier<Block> blockSupplier, MapCodec<? extends Block> codec) {
		Registry.register(BuiltInRegistries.BLOCK_TYPE, id, codec);
		Registry.register(BuiltInRegistries.BLOCK, id, blockSupplier.get());

		return Suppliers.memoize(blockSupplier::get);
	}

	@Override
	public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(Identifier id, Supplier<BlockEntityType<T>> blockEntitySupplier) {
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, blockEntitySupplier.get());

		return Suppliers.memoize(blockEntitySupplier::get);
	}

	@Override
	public Supplier<Item> registerItem(Identifier id, Supplier<Item> itemSupplier) {
		Registry.register(BuiltInRegistries.ITEM, id, itemSupplier.get());

		return Suppliers.memoize(itemSupplier::get);
	}

	@Override
	public <T> Supplier<DataComponentType<T>> registerDataComponent(Identifier id, Supplier<DataComponentType<T>> componentSupplier) {
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id, componentSupplier.get());

		return Suppliers.memoize(componentSupplier::get);
	}

	@Override
	public <T extends Recipe<?>> Supplier<RecipeSerializer<T>> registerRecipeSerializer(Identifier id, Supplier<RecipeSerializer<T>> serializerSupplier) {
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id, serializerSupplier.get());

		return Suppliers.memoize(serializerSupplier::get);
	}
}
