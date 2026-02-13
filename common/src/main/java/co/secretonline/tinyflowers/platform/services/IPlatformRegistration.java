package co.secretonline.tinyflowers.platform.services;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public interface IPlatformRegistration {
	void registerBlock(ResourceKey<Block> resourceKey, Block block, MapCodec<? extends Block> codec);

	<T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> entityFactory, Block... blocks);

	<T extends BlockEntity> void registerBlockEntity(Identifier id, BlockEntityType<T> blockEntityType);

	<T extends BlockEntity, S extends BlockEntityRenderState> void registerBlockEntityRenderer(BlockEntityType<T> blockEntityType, BlockEntityRendererProvider<T, S> renderer);

	void registerItem(ResourceKey<Item> resourceKey, Item item);

	<T> void registerDataComponent(Identifier id, DataComponentType<T> component);

	<T> void registerDynamicRegistry(ResourceKey<Registry<T>> registryKey, Codec<T> codec);

	<T extends Recipe<?>> void registerRecipeSerializer(Identifier id, RecipeSerializer<T> serializer);

	void registerCreativeModeTab();
}
