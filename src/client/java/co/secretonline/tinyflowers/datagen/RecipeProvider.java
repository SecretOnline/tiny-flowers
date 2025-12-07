package co.secretonline.tinyflowers.datagen;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.blocks.FlowerVariant;
import co.secretonline.tinyflowers.items.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.Recipe;

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

	public RecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected net.minecraft.data.recipes.RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup,
			RecipeOutput exporter) {
		return new net.minecraft.data.recipes.RecipeProvider(registryLookup, exporter) {
			@Override
			public void buildRecipes() {
				ResourceLocation stewId = BuiltInRegistries.ITEM.getKey(Items.SUSPICIOUS_STEW);

				// Generate recipes for each flower variant
				for (FlowerVariant flowerVariant : FlowerVariant.values()) {
					// Create tiny flower items for variants that need them.
					if (flowerVariant.shouldCreateItem()) {
						shapeless(RecipeCategory.DECORATIONS, flowerVariant, 4)
								.requires(ModItems.FLORISTS_SHEARS_ITEM).requires(flowerVariant.getOriginalBlock())
								.group("tiny_flowers")
								.unlockedBy(getHasName(ModItems.FLORISTS_SHEARS_ITEM), has(ModItems.FLORISTS_SHEARS_ITEM))
								.save(output);
					}

					// Create recipes for the flower variants that can be used in suspicious stew.
					if (flowerVariant.getSuspiciousEffects() != null) {
						ItemStack stack = new ItemStack(
								BuiltInRegistries.ITEM.wrapAsHolder(Items.SUSPICIOUS_STEW),
								1,
								DataComponentPatch.builder()
										.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, flowerVariant.getSuspiciousEffects())
										.build());

						ResourceKey<Recipe<?>> recipeKey = ResourceKey.create(
								Registries.RECIPE,
								TinyFlowers.id(stewId.getPath() + "_from_" + flowerVariant.getItemIdentifier().getPath()));

						shapeless(RecipeCategory.FOOD, stack)
								.requires(Items.BOWL)
								.requires(Items.BROWN_MUSHROOM)
								.requires(Items.RED_MUSHROOM)
								.requires(flowerVariant)
								.requires(flowerVariant)
								.group("suspicious_stew")
								.unlockedBy(getHasName(flowerVariant), this.has(flowerVariant))
								.save(output, recipeKey);
					}
				}

				// Generate recipes for each colour of shears.
				ResourceLocation shearsId = BuiltInRegistries.ITEM.getKey(ModItems.FLORISTS_SHEARS_ITEM);
				for (var entry : COLOR_TAGS.entrySet()) {
					DyeColor color = entry.getKey();
					TagKey<Item> tagKey = entry.getValue();
					ItemStack stack = new ItemStack(
							BuiltInRegistries.ITEM.wrapAsHolder(ModItems.FLORISTS_SHEARS_ITEM),
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
			}
		};
	}

	@Override
	public String getName() {
		return "TinyFlowersRecipeProvider";
	}
}
