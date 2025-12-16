package co.secretonline.tinyflowers.items.crafting;

import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ModRecipeSerializers {
	public static RecipeSerializer<TinyFlowerStewRecipe> TINY_FLOWER_STEW = RecipeSerializer
			.register("crafting_special_tiny_flower_stew",
					new CustomRecipe.Serializer<TinyFlowerStewRecipe>(TinyFlowerStewRecipe::new));

	public static void initialize() {
	}
}
