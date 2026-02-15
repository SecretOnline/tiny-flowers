package co.secretonline.tinyflowers.mixin.crafting;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import co.secretonline.tinyflowers.item.crafting.CustomRecipeWithProvider;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;

@Mixin(CraftingMenu.class)
public class CraftingMenuMixin {

	@Unique
	private static final ThreadLocal<ServerLevel> LEVEL_LOCAL = new ThreadLocal<>();

	@Inject(method = "slotChangedCraftingGrid(Lnet/minecraft/world/inventory/AbstractContainerMenu;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/inventory/CraftingContainer;Lnet/minecraft/world/inventory/ResultContainer;Lnet/minecraft/world/item/crafting/RecipeHolder;)V", at = @At("HEAD"))
	private static void captureLevel(AbstractContainerMenu menu, ServerLevel level, Player player,
			CraftingContainer container, ResultContainer resultSlots, RecipeHolder<CraftingRecipe> recipeHint,
			CallbackInfo ci) {
		LEVEL_LOCAL.set(level);
	}

	@Inject(method = "slotChangedCraftingGrid(Lnet/minecraft/world/inventory/AbstractContainerMenu;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/inventory/CraftingContainer;Lnet/minecraft/world/inventory/ResultContainer;Lnet/minecraft/world/item/crafting/RecipeHolder;)V", at = @At("RETURN"))
	private static void onMethodReturn(CallbackInfo ci) {
		LEVEL_LOCAL.remove();
	}

	@WrapOperation(method = "slotChangedCraftingGrid(Lnet/minecraft/world/inventory/AbstractContainerMenu;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/inventory/CraftingContainer;Lnet/minecraft/world/inventory/ResultContainer;Lnet/minecraft/world/item/crafting/RecipeHolder;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/CraftingRecipe;assemble(Lnet/minecraft/world/item/crafting/RecipeInput;)Lnet/minecraft/world/item/ItemStack;"))
	private static ItemStack redirectAssemble(CraftingRecipe recipe, RecipeInput input,
			Operation<ItemStack> original) {
		ServerLevel level = LEVEL_LOCAL.get();
		if (level == null) {
			return original.call(recipe, input);
		}

		if (recipe instanceof CustomRecipeWithProvider customRecipeWithLevel
				&& input instanceof CraftingInput craftingInput) {
			return customRecipeWithLevel.assemble(craftingInput, level.registryAccess());
		}

		return original.call(recipe, input);
	}
}
