package co.secretonline.tinyflowers.datagen.providers;

import java.util.concurrent.CompletableFuture;

import co.secretonline.tinyflowers.block.ModBlocks;
import co.secretonline.tinyflowers.tags.ModBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NonNull;

public class FabricBlockTagProvider extends FabricTagProvider.FabricValueLookupTagProvider.BlockTagProvider {
	public FabricBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.@NonNull Provider wrapperLookup) {
		valueLookupBuilder(BlockTags.FLOWERS).add(ModBlocks.TINY_GARDEN_BLOCK.get());
		valueLookupBuilder(BlockTags.BEE_ATTRACTIVE).add(ModBlocks.TINY_GARDEN_BLOCK.get());
		valueLookupBuilder(BlockTags.INSIDE_STEP_SOUND_BLOCKS).add(ModBlocks.TINY_GARDEN_BLOCK.get());
		valueLookupBuilder(ConventionalBlockTags.FLOWERS).add(ModBlocks.TINY_GARDEN_BLOCK.get());

		valueLookupBuilder(ModBlockTags.SUPPORTS_VEGETATION).forceAddTag(BlockTags.DIRT);
		valueLookupBuilder(ModBlockTags.SUPPORTS_VEGETATION).add(Blocks.GRASS_BLOCK);
	}

}
