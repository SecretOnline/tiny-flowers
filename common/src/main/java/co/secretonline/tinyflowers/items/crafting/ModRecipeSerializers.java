package co.secretonline.tinyflowers.items.crafting;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ModRecipeSerializers {
	public static RecipeSerializer<TinyFlowerStewRecipe> TINY_FLOWER_STEW = TinyFlowerStewRecipe.SERIALIZER;

	public static RecipeSerializer<ShearTinyFlowersRecipe> SHEAR_TINY_FLOWERS = ShearTinyFlowersRecipe.SERIALIZER;

	public static void register(BiConsumer<Identifier, Supplier<RecipeSerializer<?>>> register) {
		register.accept(TinyFlowers.id("crafting_special_tiny_flower_stew"), () -> TinyFlowerStewRecipe.SERIALIZER);
		register.accept(TinyFlowers.id("crafting_special_shear_tiny_flowers"), () -> ShearTinyFlowersRecipe.SERIALIZER);
	}
}
