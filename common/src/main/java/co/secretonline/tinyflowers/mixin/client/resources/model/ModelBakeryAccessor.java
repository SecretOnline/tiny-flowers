package co.secretonline.tinyflowers.mixin.client.resources.model;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ModelBakery.class)
public interface ModelBakeryAccessor {
	@Invoker(value = "getModel")
	UnbakedModel tinyFlowers$getModel(ResourceLocation modelLocation);

	@Invoker(value = "registerModelAndLoadDependencies")
	void tinyFlowers$registerModelAndLoadDependencies(ModelResourceLocation modelResourceLocation, UnbakedModel unbakedModel);
}
