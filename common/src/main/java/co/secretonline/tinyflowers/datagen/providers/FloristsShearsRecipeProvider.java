package co.secretonline.tinyflowers.datagen.providers;

import co.secretonline.tinyflowers.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FloristsShearsRecipeProvider extends RecipeProvider {
	private final Map<DyeColor, TagKey<Item>> colorMap;

	public FloristsShearsRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, Map<DyeColor, TagKey<Item>> colorMap) {
		super(output, registries);
		this.colorMap = colorMap;
	}

	@Override
	public void buildRecipes(@NotNull RecipeOutput output) {
		// Generate recipes for each colour of shears.
		ResourceLocation shearsId = BuiltInRegistries.ITEM.getKey(ModItems.FLORISTS_SHEARS_ITEM.get());
		for (var entry : this.colorMap.entrySet()) {
			DyeColor color = entry.getKey();
			TagKey<Item> tagKey = entry.getValue();
			ItemStack stack = new ItemStack(
				BuiltInRegistries.ITEM.wrapAsHolder(ModItems.FLORISTS_SHEARS_ITEM.get()),
				1,
				DataComponentPatch.builder()
					.set(DataComponents.DYED_COLOR, new DyedItemColor(color.getTextureDiffuseColor(), true))
					.build());

			ResourceKey<Recipe<?>> recipeKey = ResourceKey.create(
				Registries.RECIPE,
				shearsId.withPath((path) -> path + "_" + color.getSerializedName()));

			ShapelessItemStackRecipeBuilder.shapeless(RecipeCategory.TOOLS, stack)
				.requires(Items.SHEARS)
				.requires(tagKey)
				.group("florists_shears")
				.unlockedBy(getHasName(Items.SHEARS), has(Items.SHEARS))
				.save(output, recipeKey.location());
		}
	}
}
