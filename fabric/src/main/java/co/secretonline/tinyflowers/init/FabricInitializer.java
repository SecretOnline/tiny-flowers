package co.secretonline.tinyflowers.init;

import co.secretonline.tinyflowers.data.ModRegistries;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class FabricInitializer extends TinyFlowersInitializer {
	@Override
	public void registerBlock(Identifier id, Supplier<Block> blockSupplier, MapCodec<? extends Block> codec) {
		Registry.register(BuiltInRegistries.BLOCK_TYPE, id, codec);
		Registry.register(BuiltInRegistries.BLOCK, id, blockSupplier.get());
	}

	@Override
	public void registerBlockEntity(Identifier id, Supplier<? extends BlockEntityType<? extends BlockEntity>> blockEntitySupplier) {
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, blockEntitySupplier.get());
	}

	@Override
	public void registerItem(Identifier id, Supplier<Item> itemSupplier) {
		Registry.register(BuiltInRegistries.ITEM, id, itemSupplier.get());
	}

	@Override
	public void registerDataComponent(Identifier id, Supplier<? extends DataComponentType<?>> componentSupplier) {
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id, componentSupplier.get());
	}

	@Override
	public void registerRecipeSerializer(Identifier id, Supplier<RecipeSerializer<?>> serializerSupplier) {
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id, serializerSupplier.get());
	}
}
