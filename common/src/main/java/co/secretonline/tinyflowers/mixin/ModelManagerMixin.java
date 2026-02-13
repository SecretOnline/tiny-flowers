package co.secretonline.tinyflowers.mixin;

import co.secretonline.tinyflowers.client.model.TinyFlowerModelLogic;
import co.secretonline.tinyflowers.resources.TinyFlowerModelDataLoader;
import co.secretonline.tinyflowers.resources.TinyFlowerResources;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.resources.model.ModelDiscovery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ModelManager.class)
public class ModelManagerMixin {
	@Unique
	private static volatile CompletableFuture<Map<Identifier, TinyFlowerResources>> tinyFlowers_resourcesFuture;

	/**
	 * Hook into the start of model reload to kick off async two-phase resource
	 * loading on the prepare executor.
	 */
	@Inject(method = "reload", at = @At("HEAD"))
	private void tinyFlowers_onReloadStart(
			PreparableReloadListener.SharedState sharedState,
			Executor prepareExecutor,
			PreparableReloadListener.PreparationBarrier synchronizer,
			Executor applyExecutor,
			CallbackInfoReturnable<CompletableFuture<Void>> cir) {
		tinyFlowers_resourcesFuture = CompletableFuture
				.supplyAsync(() -> TinyFlowerModelDataLoader.readResourceFiles(sharedState.resourceManager()),
						prepareExecutor);
	}

	/**
	 * Before model dependencies are resolved, join our resource loading future
	 * and add our extra models as roots in the dependency graph.
	 */
	@Inject(method = "discoverModelDependencies",
			at = @At(value = "INVOKE",
					target = "Lnet/minecraft/client/resources/model/ModelDiscovery;resolve()Ljava/util/Map;"))
	private static void tinyFlowers_addExtraModelDependencies(
			CallbackInfoReturnable<?> cir,
			@Local(name = "result") ModelDiscovery result) {
		if (tinyFlowers_resourcesFuture != null) {
			Map<Identifier, TinyFlowerResources> resources = tinyFlowers_resourcesFuture.join();
			TinyFlowerModelLogic.onResourcesLoaded(resources);
			TinyFlowerModelLogic.addModelDependencies(result);
		}
	}
}
