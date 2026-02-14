package co.secretonline.tinyflowers.init;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.data.ModRegistries;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NeoForgeInitializer extends TinyFlowersInitializer {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, TinyFlowers.MOD_ID);
	public static final DeferredRegister<MapCodec<? extends Block>> BLOCK_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_TYPE, TinyFlowers.MOD_ID);
	public static final DeferredRegister<BlockEntityType<? extends BlockEntity>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TinyFlowers.MOD_ID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, TinyFlowers.MOD_ID);
	public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, TinyFlowers.MOD_ID);
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, TinyFlowers.MOD_ID);

	private final IEventBus modBus;

	public NeoForgeInitializer(IEventBus modBus) {
		this.modBus = modBus;

		BLOCKS.register(modBus);
		BLOCK_TYPES.register(modBus);
		BLOCK_ENTITY_TYPES.register(modBus);
		ITEMS.register(modBus);
		DATA_COMPONENT_TYPES.register(modBus);
		RECIPE_SERIALIZERS.register(modBus);
	}

	@Override
	void registerBlock(Identifier id, Supplier<Block> blockSupplier, MapCodec<? extends Block> codec) {
		BLOCK_TYPES.register(id.getPath(), () -> codec);
		BLOCKS.register(id.getPath(), blockSupplier);
	}

	@Override
	void registerBlockEntity(Identifier id, Supplier<? extends BlockEntityType<? extends BlockEntity>> blockEntitySupplier) {
		BLOCK_ENTITY_TYPES.register(id.getPath(), blockEntitySupplier);
	}

	@Override
	void registerItem(Identifier id, Supplier<Item> itemSupplier) {
		ITEMS.register(id.getPath(), itemSupplier);
	}

	@Override
	void registerDataComponent(Identifier id, Supplier<? extends DataComponentType<?>> componentSupplier) {
		DATA_COMPONENT_TYPES.register(id.getPath(), componentSupplier);
	}

	@Override
	void registerRecipeSerializer(Identifier id, Supplier<RecipeSerializer<?>> serializerSupplier) {
		RECIPE_SERIALIZERS.register(id.getPath(), serializerSupplier);
	}
}
