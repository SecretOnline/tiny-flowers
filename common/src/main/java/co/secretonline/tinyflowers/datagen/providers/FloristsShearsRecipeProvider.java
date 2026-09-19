package co.secretonline.tinyflowers.datagen.providers;

import co.secretonline.tinyflowers.item.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.MultiRegistryBootstrap;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.Recipe;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.Set;

public class FloristsShearsRecipeProvider extends RecipeProvider {
	private final Map<DyeColor, TagKey<Item>> colorMap;

	public FloristsShearsRecipeProvider(@NonNull BootstrapContext<Recipe<?>> recipes, @NonNull BootstrapContext<Advancement> advancements, Map<DyeColor, TagKey<Item>> colorMap) {
		super(recipes, advancements);
		this.colorMap = colorMap;
	}

	@Override
	public void buildRecipes() {
		// Generate recipes for each colour of shears.
		Identifier shearsId = BuiltInRegistries.ITEM.getKey(ModItems.FLORISTS_SHEARS_ITEM.get());
		for (var entry : this.colorMap.entrySet()) {
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

			shapeless(RecipeCategory.TOOLS, stack)
				.requires(Items.SHEARS)
				.requires(tagKey)
				.group("florists_shears")
				.unlockedBy(getHasName(Items.SHEARS), has(Items.SHEARS))
				.save(output, recipeKey);
		}

		dyedItem(ModItems.FLORISTS_SHEARS_ITEM.get(), "florists_shears_dyed");
	}
}
