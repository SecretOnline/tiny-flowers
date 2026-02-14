package co.secretonline.tinyflowers.init;

import co.secretonline.tinyflowers.blocks.ModBlockEntities;
import co.secretonline.tinyflowers.blocks.ModBlocks;
import co.secretonline.tinyflowers.components.ModComponents;
import co.secretonline.tinyflowers.data.ModRegistries;
import co.secretonline.tinyflowers.items.ModItems;
import co.secretonline.tinyflowers.items.crafting.ModRecipeSerializers;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public abstract class TinyFlowersInitializer {
	public void initialize() {
		ModBlocks.register(this::registerBlock);
		ModBlockEntities.register(this::registerBlockEntity);
		ModComponents.register(this::registerDataComponent);
		ModItems.register(this::registerItem);
		ModRecipeSerializers.register(this::registerRecipeSerializer);
	}

	abstract void registerBlock(Identifier id, Supplier<Block> blockSupplier, MapCodec<? extends Block> codec);

	abstract void registerBlockEntity(Identifier id, Supplier<? extends BlockEntityType<? extends BlockEntity>> blockEntitySupplier);

	abstract void registerItem(Identifier id, Supplier<Item> itemSupplier);

	abstract void registerDataComponent(Identifier id, Supplier<? extends DataComponentType<?>> componentSupplier);

	abstract void registerRecipeSerializer(Identifier id, Supplier<RecipeSerializer<?>> serializerSupplier);
}
