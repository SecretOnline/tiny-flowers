package co.secretonline.tinyflowers.datagen.providers;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class NeoForgeBlockTagProvider extends BlockTagsProvider {
	public NeoForgeBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, TinyFlowers.MOD_ID);
	}

	@Override
	protected void addTags(HolderLookup.@NonNull Provider provider) {
		this.tag(BlockTags.FLOWERS).add(ModBlocks.TINY_GARDEN_BLOCK.get());
		this.tag(BlockTags.BEE_ATTRACTIVE).add(ModBlocks.TINY_GARDEN_BLOCK.get());
		this.tag(BlockTags.INSIDE_STEP_SOUND_BLOCKS).add(ModBlocks.TINY_GARDEN_BLOCK.get());
		this.tag(Tags.Blocks.FLOWERS).add(ModBlocks.TINY_GARDEN_BLOCK.get());
	}
}
