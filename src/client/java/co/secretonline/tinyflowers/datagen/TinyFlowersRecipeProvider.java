package co.secretonline.tinyflowers.datagen;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.items.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack.RegistryDependentFactory;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

public class TinyFlowersRecipeProvider extends FabricRecipeProvider {
	private final List<TinyFlowerData> flowers;

	public TinyFlowersRecipeProvider(List<TinyFlowerData> flowers, FabricDataOutput output,
			CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);

		this.flowers = flowers;
	}

	public static RegistryDependentFactory<TinyFlowersRecipeProvider> factoryFor(List<TinyFlowerData> flowers) {
		return (FabricDataOutput output,
				CompletableFuture<HolderLookup.Provider> registriesFuture) -> new TinyFlowersRecipeProvider(flowers, output,
						registriesFuture);
	}

	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup,
			RecipeOutput exporter) {
		return new net.minecraft.data.recipes.RecipeProvider(registryLookup, exporter) {
			@Override
			public void buildRecipes() {
				// Generate recipes for each flower variant
				for (TinyFlowerData flowerData : flowers) {
					// Create tiny flower items for variants that need them.
					if (flowerData.shouldCreateItem()) {
						shapeless(RecipeCategory.DECORATIONS, flowerData.getItemStack(4))
								.requires(ModItems.FLORISTS_SHEARS_ITEM)
								.requires(BuiltInRegistries.ITEM.getValue(flowerData.originalId()))
								.group("tiny_flowers")
								.unlockedBy(getHasName(ModItems.FLORISTS_SHEARS_ITEM), has(ModItems.FLORISTS_SHEARS_ITEM))
								.save(output, "tiny_flower_from_" + flowerData.originalId().getPath());
					}
				}
			}
		};
	}

	@Override
	public String getName() {
		return "TinyFlowersRecipeProvider";
	}
}
