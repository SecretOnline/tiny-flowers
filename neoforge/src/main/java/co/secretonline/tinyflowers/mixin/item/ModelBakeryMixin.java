package co.secretonline.tinyflowers.mixin.item;

import co.secretonline.tinyflowers.helper.ItemModelHelper;
import co.secretonline.tinyflowers.mixin.client.resources.model.ModelBakeryAccessor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {
	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/ModelBakery;loadSpecialItemModelAndDependencies(Lnet/minecraft/client/resources/model/ModelResourceLocation;)V"))
	private void tinyFlowers$injectTinyFlowerItemModels(BlockColors blockColors, ProfilerFiller profilerFiller, Map<ResourceLocation, BlockModel> modelResources, Map<ResourceLocation, List<BlockStateModelLoader.LoadedJson>> blockStateResources, CallbackInfo ci) {
		List<ModelResourceLocation> tinyFlowerItemModels = ItemModelHelper.allResourceLocations();

		for (ModelResourceLocation modelResourceLocation : tinyFlowerItemModels) {
			((ModelBakeryAccessor) this).tinyFlowers$registerModelAndLoadDependencies(modelResourceLocation, ((ModelBakeryAccessor) this).tinyFlowers$getModel(modelResourceLocation.id()));
		}
	}
}
