package co.secretonline.tinyflowers.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class NeoForgeFloristsShearsRecipeProvider extends RecipeProvider.Runner {
	private static final Map<DyeColor, TagKey<Item>> COLOR_TAGS = Map.ofEntries(
		Map.entry(DyeColor.WHITE, Tags.Items.DYES_WHITE),
		Map.entry(DyeColor.ORANGE, Tags.Items.DYES_ORANGE),
		Map.entry(DyeColor.MAGENTA, Tags.Items.DYES_MAGENTA),
		Map.entry(DyeColor.LIGHT_BLUE, Tags.Items.DYES_LIGHT_BLUE),
		Map.entry(DyeColor.YELLOW, Tags.Items.DYES_YELLOW),
		Map.entry(DyeColor.LIME, Tags.Items.DYES_LIME),
		Map.entry(DyeColor.PINK, Tags.Items.DYES_PINK),
		Map.entry(DyeColor.GRAY, Tags.Items.DYES_GRAY),
		Map.entry(DyeColor.LIGHT_GRAY, Tags.Items.DYES_LIGHT_GRAY),
		Map.entry(DyeColor.CYAN, Tags.Items.DYES_CYAN),
		Map.entry(DyeColor.PURPLE, Tags.Items.DYES_PURPLE),
		Map.entry(DyeColor.BLUE, Tags.Items.DYES_BLUE),
		Map.entry(DyeColor.BROWN, Tags.Items.DYES_BROWN),
		Map.entry(DyeColor.GREEN, Tags.Items.DYES_GREEN),
		Map.entry(DyeColor.RED, Tags.Items.DYES_RED),
		Map.entry(DyeColor.BLACK, Tags.Items.DYES_BLACK));

	public NeoForgeFloristsShearsRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
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
