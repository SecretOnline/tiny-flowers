package co.secretonline.tinyflowers.mixin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import co.secretonline.tinyflowers.client.model.TinyFlowerModelLogic;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.Identifier;

@Mixin(ModelBakery.class)
public class ModelBakeryMixin {

	/**
	 * After baking completes, bake our extra models using the baker and store
	 * them in ExtraModelRegistry.
	 */
	@ModifyReturnValue(method = "bakeModels", at = @At("RETURN"))
	private CompletableFuture<ModelBakery.BakingResult> tinyFlowers_bakeExtraModels(
			CompletableFuture<ModelBakery.BakingResult> original,
			@Local(argsOnly = true) Executor executor,
			@Local(name = "baker") ModelBakery.ModelBakerImpl baker) {
		return original.thenApplyAsync(result -> {
			TinyFlowerModelLogic.bakeExtraModels(baker);
			return result;
		}, executor);
	}

	/**
	 * Intercept item model baking to replace the tiny_flower item model with
	 * our dynamic select model.
	 */
	@WrapOperation(method = "lambda$bakeModels$1",
			at = @At(value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/item/ItemModel$Unbaked;bake(Lnet/minecraft/client/renderer/item/ItemModel$BakingContext;)Lnet/minecraft/client/renderer/item/ItemModel;"))
	private ItemModel tinyFlowers_modifyItemModel(
			ItemModel.Unbaked unbakedModel,
			ItemModel.BakingContext bakeContext,
			Operation<ItemModel> operation,
			@Local(argsOnly = true) Identifier itemId) {
		if (TinyFlowerModelLogic.shouldReplaceItemModel(itemId)) {
			ItemModel.Unbaked customModel = TinyFlowerModelLogic.createTinyFlowerItemModel();
			return customModel.bake(bakeContext);
		}
		return operation.call(unbakedModel, bakeContext);
	}
}
