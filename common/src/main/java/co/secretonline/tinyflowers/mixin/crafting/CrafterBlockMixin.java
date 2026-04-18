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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.CrafterBlock;

@Mixin(CrafterBlock.class)
public class CrafterBlockMixin {

	@Unique
	private static final ThreadLocal<ServerLevel> LEVEL_LOCAL = new ThreadLocal<>();

	@Inject(method = "dispenseFrom(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)V", at = @At("HEAD"))
	private void captureLevel(BlockState state, ServerLevel level, BlockPos pos, CallbackInfo ci) {
		LEVEL_LOCAL.set(level);
	}

	@Inject(method = "dispenseFrom(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)V", at = @At("RETURN"))
	private void onMethodReturn(CallbackInfo ci) {
		LEVEL_LOCAL.remove();
	}

	@WrapOperation(method = "dispenseFrom(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/CraftingRecipe;assemble(Lnet/minecraft/world/item/crafting/RecipeInput;)Lnet/minecraft/world/item/ItemStack;"))
	private ItemStack redirectAssemble(CraftingRecipe recipe, RecipeInput input,
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
