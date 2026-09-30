package co.secretonline.tinyflowers.datagen.providers;

import java.util.concurrent.CompletableFuture;

import co.secretonline.tinyflowers.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import org.jspecify.annotations.NonNull;

public class FabricItemTagProvider extends FabricTagProvider.FabricValueLookupTagProvider.ItemTagProvider {
	public FabricItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.@NonNull Provider wrapperLookup) {
		valueLookupBuilder(ItemTags.BEE_FOOD).add(ModItems.TINY_FLOWER_ITEM.get());

		valueLookupBuilder(ConventionalItemTags.FLOWERS).add(ModItems.TINY_FLOWER_ITEM.get());
		valueLookupBuilder(ConventionalItemTags.SHEAR_TOOLS).add(ModItems.FLORISTS_SHEARS_ITEM.get());

		valueLookupBuilder(ItemTags.DYEABLE).add(ModItems.FLORISTS_SHEARS_ITEM.get());
	}
}
