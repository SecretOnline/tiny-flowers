package co.secretonline.tinyflowers.mixin.crafting;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import co.secretonline.tinyflowers.item.crafting.CustomRecipeWithProvider;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.CrafterMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeInput;

@Mixin(CrafterMenu.class)
public class CrafterMenuMixin {

	@Unique
	private static final ThreadLocal<ServerLevel> LEVEL_LOCAL = new ThreadLocal<>();

	@WrapOperation(method = "refreshRecipeResult()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/CrafterBlock;getPotentialResults(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/crafting/CraftingInput;)Ljava/util/Optional;"))
	private Optional<?> captureAndGetPotentialResults(ServerLevel level, CraftingInput input,
			Operation<Optional<?>> original) {
		LEVEL_LOCAL.set(level);
		return original.call(level, input);
	}

	@Inject(method = "refreshRecipeResult()V", at = @At("RETURN"))
	private void onMethodReturn(CallbackInfo ci) {
		LEVEL_LOCAL.remove();
	}

	@WrapOperation(method = "lambda$refreshRecipeResult$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/CraftingRecipe;assemble(Lnet/minecraft/world/item/crafting/RecipeInput;)Lnet/minecraft/world/item/ItemStack;"))
	private static ItemStack redirectAssemble(CraftingRecipe recipe, RecipeInput input, Operation<ItemStack> original) {
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
