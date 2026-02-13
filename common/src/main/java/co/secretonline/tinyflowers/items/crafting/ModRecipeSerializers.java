package co.secretonline.tinyflowers.items.crafting;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.platform.Services;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ModRecipeSerializers {
	public static RecipeSerializer<TinyFlowerStewRecipe> TINY_FLOWER_STEW = TinyFlowerStewRecipe.SERIALIZER;

	public static RecipeSerializer<ShearTinyFlowersRecipe> SHEAR_TINY_FLOWERS = ShearTinyFlowersRecipe.SERIALIZER;

	public static void register() {
		Services.PLATFORM_REGISTRATION.registerRecipeSerializer(TinyFlowers.id("crafting_special_tiny_flower_stew"), TINY_FLOWER_STEW);
		Services.PLATFORM_REGISTRATION.registerRecipeSerializer(TinyFlowers.id("crafting_special_shear_tiny_flowers"), SHEAR_TINY_FLOWERS);
	}
}
