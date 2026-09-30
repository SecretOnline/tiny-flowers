package co.secretonline.tinyflowers.datagen.providers;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

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

	private final FloristsShearsRecipeProvider provider;

	public FabricFloristsShearsRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);

		provider = new FloristsShearsRecipeProvider(output, registriesFuture, COLOR_TAGS);
	}

	@Override
	public void buildRecipes(RecipeOutput exporter) {
		provider.buildRecipes(exporter);
	}

	@Override
	public @NotNull String getName() {
		return "FloristsShearsRecipeProvider";
	}
}
