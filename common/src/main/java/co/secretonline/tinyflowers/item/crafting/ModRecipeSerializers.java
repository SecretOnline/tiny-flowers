package co.secretonline.tinyflowers.item.crafting;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.platform.ServerServiceLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Supplier;

public class ModRecipeSerializers {
	public static final Supplier<RecipeSerializer<TinyFlowerStewRecipe>> TINY_FLOWER_STEW = ServerServiceLoader.REGISTRY.register(
		BuiltInRegistries.RECIPE_SERIALIZER,
		TinyFlowers.id("crafting_special_tiny_flower_stew"),
		() -> TinyFlowerStewRecipe.SERIALIZER);

	public static final Supplier<RecipeSerializer<ShearTinyFlowersRecipe>> SHEAR_TINY_FLOWERS = ServerServiceLoader.REGISTRY.register(
		BuiltInRegistries.RECIPE_SERIALIZER,
		TinyFlowers.id("crafting_special_shear_tiny_flowers"),
		() -> ShearTinyFlowersRecipe.SERIALIZER);

	public static void initialize() {
	}
}
