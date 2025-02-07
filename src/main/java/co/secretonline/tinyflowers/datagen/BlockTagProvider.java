package co.secretonline.tinyflowers.datagen;

import java.util.concurrent.CompletableFuture;

import co.secretonline.tinyflowers.blocks.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

public class BlockTagProvider extends FabricTagProvider<Block> {
	public BlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, RegistryKeys.BLOCK, registriesFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		getOrCreateTagBuilder(BlockTags.INSIDE_STEP_SOUND_BLOCKS).add(ModBlocks.TINY_GARDEN);
		getOrCreateTagBuilder(BlockTags.SWORD_EFFICIENT).add(ModBlocks.TINY_GARDEN);
		getOrCreateTagBuilder(BlockTags.FLOWERS).add(ModBlocks.TINY_GARDEN);
	}
}
