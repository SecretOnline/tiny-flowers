package co.secretonline.tinyflowers.datagen;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

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

	private static Map<FlowerVariant, Item> RECIPE_ITEMS = Map.ofEntries(
			Map.entry(FlowerVariant.DANDELION, Items.DANDELION),
			Map.entry(FlowerVariant.POPPY, Items.POPPY),
			Map.entry(FlowerVariant.BLUE_ORCHID, Items.BLUE_ORCHID),
			Map.entry(FlowerVariant.ALLIUM, Items.ALLIUM),
			Map.entry(FlowerVariant.AZURE_BLUET, Items.AZURE_BLUET),
			Map.entry(FlowerVariant.RED_TULIP, Items.RED_TULIP),
			Map.entry(FlowerVariant.ORANGE_TULIP, Items.ORANGE_TULIP),
			Map.entry(FlowerVariant.WHITE_TULIP, Items.WHITE_TULIP),
			Map.entry(FlowerVariant.PINK_TULIP, Items.PINK_TULIP),
			Map.entry(FlowerVariant.OXEYE_DAISY, Items.OXEYE_DAISY),
			Map.entry(FlowerVariant.CORNFLOWER, Items.CORNFLOWER),
			Map.entry(FlowerVariant.LILY_OF_THE_VALLEY, Items.LILY_OF_THE_VALLEY),
			Map.entry(FlowerVariant.TORCHFLOWER, Items.TORCHFLOWER),
			Map.entry(FlowerVariant.CLOSED_EYEBLOSSOM, Items.CLOSED_EYEBLOSSOM),
			Map.entry(FlowerVariant.OPEN_EYEBLOSSOM, Items.OPEN_EYEBLOSSOM),
			Map.entry(FlowerVariant.WITHER_ROSE, Items.WITHER_ROSE));

	public RecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
		return new RecipeGenerator(registryLookup, exporter) {
			@Override
			public void generate() {
				// Generate recipes for each flower variant
				for (Map.Entry<FlowerVariant, Item> entry : RECIPE_ITEMS.entrySet()) {
					createShapeless(RecipeCategory.DECORATIONS, entry.getKey(), 4)
							.input(ModItems.FLORISTS_SHEARS_ITEM).input(entry.getValue())
							.group("tiny_flowers")
							.criterion(hasItem(ModItems.FLORISTS_SHEARS_ITEM), conditionsFromItem(ModItems.FLORISTS_SHEARS_ITEM))
							.offerTo(exporter);
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
