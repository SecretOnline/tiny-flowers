package co.secretonline.tinyflowers.datagen;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.blocks.FlowerVariant;
import co.secretonline.tinyflowers.items.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public class RecipeProvider extends FabricRecipeProvider {

	private static Map<DyeColor, TagKey<Item>> COLOR_TAGS = Map.ofEntries(
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

	public RecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
		return new RecipeGenerator(registryLookup, exporter) {
			@Override
			public void generate() {
				Identifier stewId = Registries.ITEM.getId(Items.SUSPICIOUS_STEW);

				// Generate recipes for each flower variant
				for (FlowerVariant flowerVariant : FlowerVariant.values()) {
					// Create tiny flower items for variants that need them.
					if (flowerVariant.shouldCreateItem()) {
						createShapeless(RecipeCategory.DECORATIONS, flowerVariant, 4)
								.input(ModItems.FLORISTS_SHEARS_ITEM).input(flowerVariant.getOriginalBlock())
								.group("tiny_flowers")
								.criterion(hasItem(ModItems.FLORISTS_SHEARS_ITEM), conditionsFromItem(ModItems.FLORISTS_SHEARS_ITEM))
								.offerTo(exporter);
					}

					// Create recipes for the flower variants that can be used in suspicious stew.
					if (flowerVariant.getStewEffects() != null) {
						ItemStack stack = new ItemStack(
								Registries.ITEM.getEntry(Items.SUSPICIOUS_STEW),
								1,
								ComponentChanges.builder()
										.add(DataComponentTypes.SUSPICIOUS_STEW_EFFECTS, flowerVariant.getStewEffects())
										.build());

						RegistryKey<Recipe<?>> recipeKey = RegistryKey.of(
								RegistryKeys.RECIPE,
								TinyFlowers.id(stewId.getPath() + "_from_" + flowerVariant.getItemIdentifier().getPath()));

						createShapeless(RecipeCategory.FOOD, stack)
								.input(Items.BOWL)
								.input(Items.BROWN_MUSHROOM)
								.input(Items.RED_MUSHROOM)
								.input(flowerVariant)
								.input(flowerVariant)
								.group("suspicious_stew")
								.criterion(hasItem(flowerVariant), this.conditionsFromItem(flowerVariant))
								.offerTo(exporter, recipeKey);
					}
				}

				// Generate recipes for each colour of shears.
				Identifier shearsId = Registries.ITEM.getId(ModItems.FLORISTS_SHEARS_ITEM);
				for (var entry : COLOR_TAGS.entrySet()) {
					DyeColor color = entry.getKey();
					TagKey<Item> tagKey = entry.getValue();
					ItemStack stack = new ItemStack(
							Registries.ITEM.getEntry(ModItems.FLORISTS_SHEARS_ITEM),
							1,
							ComponentChanges.builder()
									.add(DataComponentTypes.DYED_COLOR, new DyedColorComponent(color.getEntityColor()))
									.build());

					RegistryKey<Recipe<?>> recipeKey = RegistryKey.of(
							RegistryKeys.RECIPE,
							shearsId.withPath((path) -> path + "_" + color.asString()));

					// No method for creating ItemStack of shaped?
					createShapeless(RecipeCategory.TOOLS, stack)
							.input(Items.SHEARS)
							.input(tagKey)
							.group("florists_shears")
							.criterion(hasItem(Items.SHEARS), conditionsFromItem(Items.SHEARS))
							.offerTo(exporter, recipeKey);
				}
			}
		};
	}

	@Override
	public String getName() {
		return "TinyFlowersRecipeProvider";
	}
}
