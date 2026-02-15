package co.secretonline.tinyflowers.datagen.providers;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class ItemTagProvider extends ItemTagsProvider {
	public ItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, TinyFlowers.MOD_ID);
	}

	@Override
	protected void addTags(HolderLookup.@NonNull Provider provider) {
		this.tag(ItemTags.BEE_FOOD).add(ModItems.TINY_FLOWER_ITEM.get());
		this.tag(ItemTags.FLOWERS).add(ModItems.TINY_FLOWER_ITEM.get());

		this.tag(Tags.Items.TOOLS_SHEAR).add(ModItems.FLORISTS_SHEARS_ITEM.get());
	}
}
