package co.secretonline.tinyflowers.items.crafting;

import org.apache.commons.lang3.NotImplementedException;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import org.jspecify.annotations.NonNull;

public abstract class CustomRecipeWithProvider extends CustomRecipe {
	@Override
	public @NonNull ItemStack assemble(CraftingInput input) {
		throw new NotImplementedException(
			"assemble(CraftingInput input) should not be called for CustomRecipeWithLevel. Is a mixin missing?");
	}

	/**
	 * A version of {@see Recipe.assemble} that takes a HolderLookup.Provider
	 */
	public abstract ItemStack assemble(CraftingInput input, HolderLookup.Provider provider);
}
