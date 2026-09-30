package co.secretonline.tinyflowers.datagen.providers;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.block.ModBlocks;
import co.secretonline.tinyflowers.tags.ModBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class NeoForgeBlockTagProvider extends BlockTagsProvider {
	public NeoForgeBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, TinyFlowers.MOD_ID, null);
	}

	@Override
	protected void addTags(HolderLookup.@NotNull Provider provider) {
		this.tag(BlockTags.FLOWERS).add(ModBlocks.TINY_GARDEN_BLOCK.get());
		this.tag(BlockTags.INSIDE_STEP_SOUND_BLOCKS).add(ModBlocks.TINY_GARDEN_BLOCK.get());

		this.tag(ModBlockTags.SUPPORTS_VEGETATION).addOptionalTag(BlockTags.DIRT);
		this.tag(ModBlockTags.SUPPORTS_VEGETATION).add(Blocks.GRASS_BLOCK);
	}
}
