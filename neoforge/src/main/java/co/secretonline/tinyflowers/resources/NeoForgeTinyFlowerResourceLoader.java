package co.secretonline.tinyflowers.resources;

import co.secretonline.tinyflowers.TinyFlowersClientState;
import co.secretonline.tinyflowers.data.TinyFlowerResources;
import co.secretonline.tinyflowers.helper.FlowerModelHelper;
import co.secretonline.tinyflowers.platform.ClientServiceLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.neoforge.client.event.ModelEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NeoForgeTinyFlowerResourceLoader extends SimplePreparableReloadListener<Map<ResourceLocation, co.secretonline.tinyflowers.data.TinyFlowerResources>> {
	private final Set<ResourceLocation> knownIds = new HashSet<>();

	@Override
	protected @NotNull Map<ResourceLocation, co.secretonline.tinyflowers.data.TinyFlowerResources> prepare(@NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
		var resources = FlowerModelHelper.readResourceFiles(resourceManager);

		TinyFlowersClientState.RESOURCE_INSTANCES = resources;

		knownIds.clear();
		for (co.secretonline.tinyflowers.data.TinyFlowerResources flowerResources : resources.values()) {
			knownIds.add(flowerResources.model1());
			knownIds.add(flowerResources.model2());
			knownIds.add(flowerResources.model3());
			knownIds.add(flowerResources.model4());

			if (flowerResources.modelPotted().isPresent()) {
				knownIds.add(flowerResources.modelPotted().get());
			}
		}

		return resources;
	}

	@Override
	protected void apply(@NotNull Map<ResourceLocation, TinyFlowerResources> identifierTinyFlowerResourcesMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
	}

	public void registerModels(ModelEvent.RegisterAdditional event) {
		for (ResourceLocation id : knownIds) {
			ClientServiceLoader.FLOWER_MODELS.registerModel(id, event);
		}
	}
}
