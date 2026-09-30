package co.secretonline.tinyflowers.datagen.providers;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.AdvancementRequirements.Strategy;
import net.minecraft.advancements.AdvancementRewards.Builder;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShapelessItemStackRecipeBuilder implements RecipeBuilder {
	private final RecipeCategory category;
	private final ItemStack result;
	private final NonNullList<Ingredient> ingredients = NonNullList.create();
	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
	@Nullable
	private String group;

	public ShapelessItemStackRecipeBuilder(RecipeCategory category, ItemStack result) {
		this.category = category;
		this.result = result;
	}

	public static ShapelessItemStackRecipeBuilder shapeless(RecipeCategory category, ItemStack result) {
		return new ShapelessItemStackRecipeBuilder(category, result);
	}

	public ShapelessItemStackRecipeBuilder requires(TagKey<Item> tag) {
		return this.requires(Ingredient.of(tag));
	}

	public ShapelessItemStackRecipeBuilder requires(ItemLike item) {
		return this.requires((ItemLike)item, 1);
	}

	public ShapelessItemStackRecipeBuilder requires(ItemLike item, int quantity) {
		for(int i = 0; i < quantity; ++i) {
			this.requires(Ingredient.of(new ItemLike[]{item}));
		}

		return this;
	}

	public ShapelessItemStackRecipeBuilder requires(Ingredient ingredient) {
		return this.requires((Ingredient)ingredient, 1);
	}

	public ShapelessItemStackRecipeBuilder requires(Ingredient ingredient, int quantity) {
		for(int i = 0; i < quantity; ++i) {
			this.ingredients.add(ingredient);
		}

		return this;
	}

	public @NotNull ShapelessItemStackRecipeBuilder unlockedBy(@NotNull String name, @NotNull Criterion<?> criterion) {
		this.criteria.put(name, criterion);
		return this;
	}

	public @NotNull ShapelessItemStackRecipeBuilder group(@Nullable String groupName) {
		this.group = groupName;
		return this;
	}

	public @NotNull Item getResult() {
		return this.result.getItem();
	}

	public void save(RecipeOutput recipeOutput, @NotNull ResourceLocation id) {
		this.ensureValid(id);
		Advancement.Builder advancement$builder = recipeOutput.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(Builder.recipe(id)).requirements(Strategy.OR);
		Objects.requireNonNull(advancement$builder);
		this.criteria.forEach(advancement$builder::addCriterion);
		ShapelessRecipe shapelessrecipe = new ShapelessRecipe((String)Objects.requireNonNullElse(this.group, ""), RecipeBuilder.determineBookCategory(this.category), this.result, this.ingredients);
		recipeOutput.accept(id, shapelessrecipe, advancement$builder.build(id.withPrefix("recipes/" + this.category.getFolderName() + "/")));
	}

	private void ensureValid(ResourceLocation id) {
		if (this.criteria.isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + String.valueOf(id));
		}
	}
}

