package co.secretonline.tinyflowers.datagen.providers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.items.ModItems;
import co.secretonline.tinyflowers.resources.TinyFlowerResources;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.util.Tuple;

public class ModRecipeProvider extends FabricRecipeProvider {
	private final String modId;
	private final List<Tuple<TinyFlowerData, TinyFlowerResources>> flowers;

	public ModRecipeProvider(String modId, List<Tuple<TinyFlowerData, TinyFlowerResources>> flowers,
			FabricDataOutput output,
			CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);

		this.modId = modId;
		this.flowers = flowers;
	}

	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup,
			RecipeOutput exporter) {
		return new net.minecraft.data.recipes.RecipeProvider(registryLookup, exporter) {
			@Override
			public void buildRecipes() {
				// Generate recipes for each flower variant
				for (Tuple<TinyFlowerData, TinyFlowerResources> tuple : flowers) {
					var flowerData = tuple.getA();
					// Create tiny flower items for variants that need them.
					if (!flowerData.isSegmentable()) {
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
		return "Flowers recipes (" + this.modId + ")";
	}
}
