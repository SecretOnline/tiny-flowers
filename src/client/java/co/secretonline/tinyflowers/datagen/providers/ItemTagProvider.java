package co.secretonline.tinyflowers.datagen.providers;

import java.util.concurrent.CompletableFuture;

import co.secretonline.tinyflowers.items.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;

public class ItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
	public ItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
		valueLookupBuilder(ItemTags.BEE_FOOD).add(ModItems.TINY_FLOWER_ITEM);
		valueLookupBuilder(ItemTags.FLOWERS).add(ModItems.TINY_FLOWER_ITEM);

		valueLookupBuilder(ConventionalItemTags.SHEAR_TOOLS).add(ModItems.FLORISTS_SHEARS_ITEM);
		valueLookupBuilder(ItemTags.DYEABLE).add(ModItems.FLORISTS_SHEARS_ITEM);
	}
}
