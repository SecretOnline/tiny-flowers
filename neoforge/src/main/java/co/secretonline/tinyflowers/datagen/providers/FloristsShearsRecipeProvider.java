package co.secretonline.tinyflowers.datagen.providers;

import co.secretonline.tinyflowers.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FloristsShearsRecipeProvider extends RecipeProvider.Runner {
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

	public FloristsShearsRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected @NonNull RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider registryLookup, @NonNull RecipeOutput exporter) {
		return new net.minecraft.data.recipes.RecipeProvider(registryLookup, exporter) {
			@Override
			public void buildRecipes() {
				// Generate recipes for each colour of shears.
				Identifier shearsId = BuiltInRegistries.ITEM.getKey(ModItems.FLORISTS_SHEARS_ITEM.get());
				for (var entry : COLOR_TAGS.entrySet()) {
					DyeColor color = entry.getKey();
					TagKey<Item> tagKey = entry.getValue();
					ItemStackTemplate stack = new ItemStackTemplate(
						BuiltInRegistries.ITEM.wrapAsHolder(ModItems.FLORISTS_SHEARS_ITEM.get()),
						1,
						DataComponentPatch.builder()
							.set(DataComponents.DYED_COLOR, new DyedItemColor(color.getTextureDiffuseColor()))
							.build());

					ResourceKey<Recipe<?>> recipeKey = ResourceKey.create(
						Registries.RECIPE,
						shearsId.withPath((path) -> path + "_" + color.getSerializedName()));

					// No method for creating ItemStack of shaped?
					shapeless(RecipeCategory.TOOLS, stack)
						.requires(Items.SHEARS)
						.requires(tagKey)
						.group("florists_shears")
						.unlockedBy(getHasName(Items.SHEARS), has(Items.SHEARS))
						.save(output, recipeKey);
				}

				dyedItem(ModItems.FLORISTS_SHEARS_ITEM.get(), "florists_shears_dyed");
			}
		};
	}

	@Override
	public @NonNull String getName() {
		return "FloristsShearsRecipeProvider";
	}
}
