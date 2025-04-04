package co.secretonline.tinyflowers.datagen;

import java.util.concurrent.CompletableFuture;

import co.secretonline.tinyflowers.blocks.FlowerVariant;
import co.secretonline.tinyflowers.blocks.ModItemTags;
import co.secretonline.tinyflowers.items.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

public class ItemTagProvider extends FabricTagProvider<Item> {
	public ItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, RegistryKeys.ITEM, registriesFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		FabricTagBuilder builder = getOrCreateTagBuilder(ModItemTags.TINY_FLOWERS);

		// Add all items/blocks that correspond to tiny flower variants to tag
		for (FlowerVariant variant : FlowerVariant.values()) {
			if (variant.isEmpty()) {
				continue;
			}

			builder.add(variant.asItem());
		}

		getOrCreateTagBuilder(ItemTags.BEE_FOOD).addTag(ModItemTags.TINY_FLOWERS);

		getOrCreateTagBuilder(ConventionalItemTags.SHEAR_TOOLS).add(ModItems.FLORISTS_SHEARS_ITEM);
	}
}
