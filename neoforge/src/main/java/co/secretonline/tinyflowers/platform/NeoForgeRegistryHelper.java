package co.secretonline.tinyflowers.platform;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.platform.services.RegistryHelper;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NeoForgeRegistryHelper implements RegistryHelper {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, TinyFlowers.MOD_ID);
	public static final DeferredRegister<MapCodec<? extends Block>> BLOCK_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_TYPE, TinyFlowers.MOD_ID);
	public static final DeferredRegister<BlockEntityType<? extends BlockEntity>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TinyFlowers.MOD_ID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, TinyFlowers.MOD_ID);
	public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, TinyFlowers.MOD_ID);
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, TinyFlowers.MOD_ID);

	public void registerToBus(IEventBus modBus) {
		BLOCKS.register(modBus);
		BLOCK_TYPES.register(modBus);
		BLOCK_ENTITY_TYPES.register(modBus);
		ITEMS.register(modBus);
		DATA_COMPONENT_TYPES.register(modBus);
		RECIPE_SERIALIZERS.register(modBus);
	}

	@Override
	public Supplier<Block> registerBlock(Identifier id, Supplier<Block> blockSupplier, MapCodec<? extends Block> codec) {
		BLOCK_TYPES.register(id.getPath(), () -> codec);
		return BLOCKS.register(id.getPath(), blockSupplier);
	}

	@Override
	public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(Identifier id, Supplier<BlockEntityType<T>> blockEntitySupplier) {
		return BLOCK_ENTITY_TYPES.register(id.getPath(), blockEntitySupplier);
	}

	@Override
	public Supplier<Item> registerItem(Identifier id, Supplier<Item> itemSupplier) {
		return ITEMS.register(id.getPath(), itemSupplier);
	}

	@Override
	public <T> Supplier<DataComponentType<T>> registerDataComponent(Identifier id, Supplier<DataComponentType<T>> componentSupplier) {
		return DATA_COMPONENT_TYPES.register(id.getPath(), componentSupplier);
	}

	@Override
	public <T extends Recipe<?>> Supplier<RecipeSerializer<T>> registerRecipeSerializer(Identifier id, Supplier<RecipeSerializer<T>> serializerSupplier) {
		return RECIPE_SERIALIZERS.register(id.getPath(), serializerSupplier);
	}
}
