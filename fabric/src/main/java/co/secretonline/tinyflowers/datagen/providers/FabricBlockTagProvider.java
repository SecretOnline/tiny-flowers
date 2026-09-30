package co.secretonline.tinyflowers.datagen.providers;

import java.util.concurrent.CompletableFuture;

import co.secretonline.tinyflowers.block.ModBlocks;
import co.secretonline.tinyflowers.tags.ModBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;

public class FabricBlockTagProvider extends FabricTagProvider.BlockTagProvider {
	public FabricBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
		tag(BlockTags.FLOWERS).add(ModBlocks.TINY_GARDEN_KEY);
		tag(BlockTags.INSIDE_STEP_SOUND_BLOCKS).add(ModBlocks.TINY_GARDEN_KEY);

		tag(ModBlockTags.SUPPORTS_VEGETATION).forceAddTag(BlockTags.DIRT);
		tag(ModBlockTags.SUPPORTS_VEGETATION).add(reverseLookup(Blocks.GRASS_BLOCK));
	}

}
