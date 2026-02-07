package co.secretonline.tinyflowers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import co.secretonline.tinyflowers.items.crafting.CustomRecipeWithProvider;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeInput;

@Mixin(DyeColor.class)
public class DyeColorMixin {

	private static final ThreadLocal<ServerLevel> LEVEL_LOCAL = new ThreadLocal<>();

	@Inject(method = "findColorMixInRecipes(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/item/DyeColor;)Lnet/minecraft/world/item/DyeColor;", at = @At("HEAD"))
	private static void captureLevel(ServerLevel level, DyeColor dyeColor1, DyeColor dyeColor2,
			CallbackInfoReturnable<DyeColor> cir) {
		LEVEL_LOCAL.set(level);
	}

	@Inject(method = "findColorMixInRecipes(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/item/DyeColor;)Lnet/minecraft/world/item/DyeColor;", at = @At("RETURN"))
	private static void onMethodReturn(ServerLevel level, DyeColor dyeColor1, DyeColor dyeColor2,
			CallbackInfoReturnable<DyeColor> cir) {
		LEVEL_LOCAL.remove();
	}

	@WrapOperation(method = "Lnet/minecraft/world/item/DyeColor;findColorMixInRecipes(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/item/DyeColor;)Lnet/minecraft/world/item/DyeColor;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/CraftingRecipe;assemble(Lnet/minecraft/world/item/crafting/RecipeInput;)Lnet/minecraft/world/item/ItemStack;"))
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
