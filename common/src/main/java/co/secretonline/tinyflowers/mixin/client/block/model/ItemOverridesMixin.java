package co.secretonline.tinyflowers.mixin.client.block.model;

import co.secretonline.tinyflowers.TinyFlowersClientState;
import co.secretonline.tinyflowers.data.TinyFlowerResources;
import co.secretonline.tinyflowers.helper.ItemModelHelper;
import co.secretonline.tinyflowers.item.ModItems;
import co.secretonline.tinyflowers.item.component.GardenContentsComponent;
import co.secretonline.tinyflowers.item.component.ModComponents;
import co.secretonline.tinyflowers.item.component.TinyFlowerComponent;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemOverrides.class)
public class ItemOverridesMixin {

	@WrapMethod(method = "resolve")
	public BakedModel wrapResolve(BakedModel model, ItemStack stack, ClientLevel level, LivingEntity entity, int seed, Operation<BakedModel> original) {
		if (stack.is(ModItems.TINY_FLOWER_ITEM.get())) {
			TinyFlowerComponent flowerComponent = stack.get(ModComponents.TINY_FLOWER.get());
			GardenContentsComponent gardenComponent = stack.get(ModComponents.GARDEN_CONTENTS.get());
			// Only override the model if this item has the flower component and no garden component.
			// A ctrl+picked item from a garden can end up with both, in which case the garden should take priority.
			if (flowerComponent != null && gardenComponent == null) {
				TinyFlowerResources resources = TinyFlowersClientState.RESOURCE_INSTANCES.get(flowerComponent.id());
				if (resources != null) {
					ModelResourceLocation modelId = ItemModelHelper.getModelResourceLocation(resources);

					Minecraft client = Minecraft.getInstance();
					return client.getModelManager().getModel(modelId);
				}
			}
		}

		return original.call(model, stack, level, entity, seed);
	}
}
