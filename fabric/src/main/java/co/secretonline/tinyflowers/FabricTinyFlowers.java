package co.secretonline.tinyflowers;

import co.secretonline.tinyflowers.blocks.ModBlockEntities;
import co.secretonline.tinyflowers.blocks.ModBlocks;
import co.secretonline.tinyflowers.components.ModComponents;
import co.secretonline.tinyflowers.data.ModRegistries;
import co.secretonline.tinyflowers.items.FabricCreativeTabEvents;
import co.secretonline.tinyflowers.items.ModItems;
import co.secretonline.tinyflowers.items.crafting.ModRecipeSerializers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class FabricTinyFlowers implements ModInitializer {
	@Override
	public void onInitialize() {
		new FabricTinyFlowersRegistration()
			.initialize();

		FabricCreativeTabEvents.register();
	}
}
