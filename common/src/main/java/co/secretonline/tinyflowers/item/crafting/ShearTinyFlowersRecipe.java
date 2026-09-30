package co.secretonline.tinyflowers.item.crafting;

import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import org.jspecify.annotations.NonNull;

import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.item.ModItems;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class ShearTinyFlowersRecipe extends CustomRecipe {
	public static final RecipeSerializer<ShearTinyFlowersRecipe> SERIALIZER = new CustomRecipe.Serializer<>(ShearTinyFlowersRecipe::new);

	public ShearTinyFlowersRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(CraftingInput recipeInput, @NonNull Level level) {
		// Quick size check. The recipe only supports florists' shears and a single
		// flower type.
		if (recipeInput.ingredientCount() != 2) {
			return false;
		}

		boolean hasFloristsShears = false;
		boolean hasFlower = false;

		for (ItemStack itemStack : recipeInput.items()) {
			// Ensure exactly one of each of Florists' Shears and a flower are present
			if (itemStack.isEmpty()) {
				continue;
			}

			if (itemStack.is(ModItems.FLORISTS_SHEARS_ITEM.get())) {
				if (hasFloristsShears) {
					return false;
				}

				hasFloristsShears = true;
				continue;
			}

			if (itemStack.getItem() instanceof BlockItem blockItem) {
				Block block = blockItem.getBlock();
				TinyFlowerData flowerData = TinyFlowerData.findByOriginalBlock(level.registryAccess(), block);
				if (flowerData == null) {
					return false;
				}
				if (hasFlower) {
					return false;
				}
				if (flowerData.isSegmentable()) {
					// Prevent duplication of segmentable types
					return false;
				}

				hasFlower = true;
				continue;
			}

			return false;
		}

		return hasFloristsShears && hasFlower;
	}

	@Override
	public @NonNull ItemStack assemble(CraftingInput recipeInput, @NonNull Provider provider) {

		for (ItemStack itemStack : recipeInput.items()) {
			if (itemStack.getItem() instanceof BlockItem blockItem) {
				Block block = blockItem.getBlock();
				TinyFlowerData flowerData = TinyFlowerData.findByOriginalBlock(provider, block);
				if (flowerData == null) {
					continue;
				}

				return flowerData.getItemStack(4);
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public @NonNull NonNullList<ItemStack> getRemainingItems(CraftingInput craftingInput) {
		NonNullList<ItemStack> nonNullList = NonNullList.withSize(craftingInput.size(), ItemStack.EMPTY);

		for (int i = 0; i < nonNullList.size(); i++) {
			ItemStack itemStack = craftingInput.getItem(i);

			// Florists' shears should be damaged. This can't be a regular recipe
			// remainder otherwise it the shears duplicate when combining or dyeing.
			if (itemStack.is(ModItems.FLORISTS_SHEARS_ITEM.get())) {
				if (itemStack.getDamageValue() < itemStack.getMaxDamage() - 1) {
					ItemStack moreDamaged = itemStack.copy();
					moreDamaged.setDamageValue(itemStack.getDamageValue() + 1);

					nonNullList.set(i, moreDamaged);
				} else {
					nonNullList.set(i, ItemStack.EMPTY);
				}
			} else {
				ItemStack remainder = itemStack.getItem().getCraftingRemainder();
				if (!remainder.isEmpty()) {
					nonNullList.set(i, remainder);
				}
			}
		}

		return nonNullList;
	}

	@Override
	public @NonNull RecipeSerializer<ShearTinyFlowersRecipe> getSerializer() {
		return ModRecipeSerializers.SHEAR_TINY_FLOWERS.get();
	}
}
