package co.secretonline.tinyflowers.datagen.providers;

import java.util.concurrent.CompletableFuture;

import co.secretonline.tinyflowers.blocks.ModBlockTags;
import co.secretonline.tinyflowers.blocks.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;

public class BlockTagProvider extends FabricTagProvider.FabricValueLookupTagProvider.BlockTagProvider {
	public BlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
		valueLookupBuilder(BlockTags.FLOWERS).add(ModBlocks.TINY_GARDEN_BLOCK);

		valueLookupBuilder(ModBlockTags.TINY_FLOWER_CAN_SURVIVE_ON).forceAddTag(BlockTags.DIRT);
		valueLookupBuilder(ModBlockTags.TINY_FLOWER_CAN_SURVIVE_ON).add(Blocks.FARMLAND);
	}

}
