package co.secretonline.tinyflowers.items.crafting;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ModRecipeSerializers {
	public static RecipeSerializer<TinyFlowerStewRecipe> TINY_FLOWER_STEW = Registry.register(
			BuiltInRegistries.RECIPE_SERIALIZER,
			TinyFlowers.id("crafting_special_tiny_flower_stew"),
			TinyFlowerStewRecipe.SERIALIZER);

	public static RecipeSerializer<ShearTinyFlowersRecipe> SHEAR_TINY_FLOWERS = Registry.register(
			BuiltInRegistries.RECIPE_SERIALIZER,
			TinyFlowers.id("crafting_special_shear_tiny_flowers"),
			ShearTinyFlowersRecipe.SERIALIZER);

	public static void initialize() {
	}
}
