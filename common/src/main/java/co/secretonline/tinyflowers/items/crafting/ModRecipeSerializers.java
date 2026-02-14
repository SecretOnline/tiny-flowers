package co.secretonline.tinyflowers.items.crafting;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.platform.Services;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ModRecipeSerializers {
	public static Supplier<RecipeSerializer<TinyFlowerStewRecipe>> TINY_FLOWER_STEW = Services.REGISTRY.registerRecipeSerializer(
		TinyFlowers.id("crafting_special_tiny_flower_stew"),
		() -> TinyFlowerStewRecipe.SERIALIZER);

	public static Supplier<RecipeSerializer<ShearTinyFlowersRecipe>> SHEAR_TINY_FLOWERS = Services.REGISTRY.registerRecipeSerializer(
		TinyFlowers.id("crafting_special_shear_tiny_flowers"),
		() -> ShearTinyFlowersRecipe.SERIALIZER);

	public static void initialize() {
	}
}
