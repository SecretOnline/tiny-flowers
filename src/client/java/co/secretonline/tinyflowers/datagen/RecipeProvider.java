package co.secretonline.tinyflowers.datagen;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import co.secretonline.tinyflowers.blocks.FlowerVariant;
import co.secretonline.tinyflowers.items.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public class RecipeProvider extends FabricRecipeProvider {

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
				for (DyeColor color : DyeColor.values()) {
					ItemStack stack = new ItemStack(
							Registries.ITEM.getEntry(ModItems.FLORISTS_SHEARS_ITEM),
							1,
							ComponentChanges.builder()
									.add(DataComponentTypes.DYED_COLOR, new DyedColorComponent(color.getEntityColor(), false))
									.build());

					RegistryKey<Recipe<?>> recipeKey = RegistryKey.of(
							RegistryKeys.RECIPE,
							shearsId.withPath((path) -> path + "_" + color.asString()));

					// No method for creating ItemStack of shaped?
					createShapeless(RecipeCategory.TOOLS, stack)
							.input(Items.SHEARS)
							.input(DyeItem.byColor(color))
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
