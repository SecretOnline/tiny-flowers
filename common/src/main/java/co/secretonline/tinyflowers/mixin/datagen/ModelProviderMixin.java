package co.secretonline.tinyflowers.mixin.datagen;

import co.secretonline.tinyflowers.datagen.PartialModelProvider;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.data.models.ModelProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ModelProvider.class)
public class ModelProviderMixin {

	@WrapWithCondition(method = "run",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/data/models/ModelProvider$BlockStateGeneratorCollector;validate()V"))
	private boolean wrapRunBlockGenerate(ModelProvider.BlockStateGeneratorCollector instance) {
		if (this instanceof PartialModelProvider provider) {
			return provider.shouldValidateAllEntries();
		}

		return true;
	}

	@WrapWithCondition(method = "run",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/data/models/ModelProvider$ItemInfoCollector;finalizeAndValidate()V"))
	private boolean wrapRunBlockGenerate(ModelProvider.ItemInfoCollector instance) {
		if (this instanceof PartialModelProvider provider) {
			return provider.shouldValidateAllEntries();
		}

		return true;
	}
}
