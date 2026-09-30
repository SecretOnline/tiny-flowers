package co.secretonline.tinyflowers.datagen.providers;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class NeoForgeItemTagProvider extends ItemTagsProvider {
	public NeoForgeItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
		super(output, lookupProvider, blockTags, TinyFlowers.MOD_ID, null);
	}

	@Override
	protected void addTags(HolderLookup.@NotNull Provider provider) {
		this.tag(ItemTags.BEE_FOOD).add(ModItems.TINY_FLOWER_ITEM.get());

		this.tag(Tags.Items.TOOLS_SHEAR).add(ModItems.FLORISTS_SHEARS_ITEM.get());

		this.tag(ItemTags.DYEABLE).add(ModItems.FLORISTS_SHEARS_ITEM.get());
	}
}
