package co.secretonline.tinyflowers.datagen.providers;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FabricFloristsShearsRecipeProvider extends FabricRecipeProvider {
	private static final Map<DyeColor, TagKey<Item>> COLOR_TAGS = Map.ofEntries(
		Map.entry(DyeColor.WHITE, ConventionalItemTags.WHITE_DYES),
		Map.entry(DyeColor.ORANGE, ConventionalItemTags.ORANGE_DYES),
		Map.entry(DyeColor.MAGENTA, ConventionalItemTags.MAGENTA_DYES),
		Map.entry(DyeColor.LIGHT_BLUE, ConventionalItemTags.LIGHT_BLUE_DYES),
		Map.entry(DyeColor.YELLOW, ConventionalItemTags.YELLOW_DYES),
		Map.entry(DyeColor.LIME, ConventionalItemTags.LIME_DYES),
		Map.entry(DyeColor.PINK, ConventionalItemTags.PINK_DYES),
		Map.entry(DyeColor.GRAY, ConventionalItemTags.GRAY_DYES),
		Map.entry(DyeColor.LIGHT_GRAY, ConventionalItemTags.LIGHT_GRAY_DYES),
		Map.entry(DyeColor.CYAN, ConventionalItemTags.CYAN_DYES),
		Map.entry(DyeColor.PURPLE, ConventionalItemTags.PURPLE_DYES),
		Map.entry(DyeColor.BLUE, ConventionalItemTags.BLUE_DYES),
		Map.entry(DyeColor.BROWN, ConventionalItemTags.BROWN_DYES),
		Map.entry(DyeColor.GREEN, ConventionalItemTags.GREEN_DYES),
		Map.entry(DyeColor.RED, ConventionalItemTags.RED_DYES),
		Map.entry(DyeColor.BLACK, ConventionalItemTags.BLACK_DYES));

	public FabricFloristsShearsRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected @NonNull RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider registryLookup, @NonNull RecipeOutput exporter) {
		return new FloristsShearsRecipeProvider(registryLookup, exporter, COLOR_TAGS);
	}

	@Override
	public @NonNull String getName() {
		return "FloristsShearsRecipeProvider";
	}
}
