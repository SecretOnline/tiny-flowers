package co.secretonline.tinyflowers.mixin.item;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.helper.ItemModelHelper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ModelBakery.class)
public class ModelBakeryMixin {
	@Unique
	private final static Identifier TINY_FLOWER_ID = TinyFlowers.id("tiny_flower");

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
		if (itemId.equals(TINY_FLOWER_ID)) {
			ItemModel.Unbaked customModel = ItemModelHelper.createTinyFlowerItemModel();
			return customModel.bake(bakeContext);
		}
		return operation.call(unbakedModel, bakeContext);
	}
}
