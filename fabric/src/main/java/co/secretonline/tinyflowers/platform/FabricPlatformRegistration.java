package co.secretonline.tinyflowers.platform;

import co.secretonline.tinyflowers.items.FabricCreativeTabEvents;
import co.secretonline.tinyflowers.platform.services.IPlatformRegistration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
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

public class FabricPlatformRegistration implements IPlatformRegistration {
	@Override
	public void registerBlock(ResourceKey<Block> resourceKey, Block block, MapCodec<? extends Block> codec) {
		Registry.register(BuiltInRegistries.BLOCK_TYPE, resourceKey.identifier(), codec);
		Registry.register(BuiltInRegistries.BLOCK, resourceKey, block);
	}

	@Override
	public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> entityFactory, Block... blocks) {
		return FabricBlockEntityTypeBuilder.create(entityFactory::apply, blocks).build();
	}
	@Override
	public <T extends BlockEntity> void registerBlockEntity(Identifier id, BlockEntityType<T> blockEntityType) {
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, blockEntityType);
	}

	@Override
	public <T extends BlockEntity, S extends BlockEntityRenderState> void registerBlockEntityRenderer(BlockEntityType<T> blockEntityType, BlockEntityRendererProvider<T, S> renderer) {
		BlockEntityRenderers.register(blockEntityType, renderer);
	}

	@Override
	public void registerItem(ResourceKey<Item> resourceKey, Item item) {
		Registry.register(BuiltInRegistries.ITEM, resourceKey, item);
	}

	@Override
	public <T> void registerDataComponent(Identifier id, DataComponentType<T> component) {
		Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id, component);
	}

	@Override
	public <T> void registerDynamicRegistry(ResourceKey<Registry<T>> registryKey, Codec<T> codec) {
		DynamicRegistries.registerSynced(registryKey, codec);
	}

	@Override
	public <T extends Recipe<?>> void registerRecipeSerializer(Identifier id, RecipeSerializer<T> serializer) {
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id, serializer);
	}

	@Override
	public void registerCreativeModeTab() {
		FabricCreativeTabEvents.register();
	}
}
