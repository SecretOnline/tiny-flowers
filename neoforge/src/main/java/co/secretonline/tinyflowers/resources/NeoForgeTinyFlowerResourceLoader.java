package co.secretonline.tinyflowers.resources;

import co.secretonline.tinyflowers.TinyFlowersClientState;
import co.secretonline.tinyflowers.data.TinyFlowerResources;
import co.secretonline.tinyflowers.helper.FlowerModelHelper;
import co.secretonline.tinyflowers.platform.Services;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.neoforge.client.event.ModelEvent;
import org.jspecify.annotations.NonNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NeoForgeTinyFlowerResourceLoader extends SimplePreparableReloadListener<Map<Identifier, co.secretonline.tinyflowers.data.TinyFlowerResources>> {
	private final Set<Identifier> knownIds = new HashSet<>();

	@Override
	protected Map<Identifier, co.secretonline.tinyflowers.data.TinyFlowerResources> prepare(@NonNull ResourceManager resourceManager, @NonNull ProfilerFiller profilerFiller) {
		var resources = FlowerModelHelper.readResourceFiles(resourceManager);

		TinyFlowersClientState.RESOURCE_INSTANCES = resources;

		knownIds.clear();
		for (co.secretonline.tinyflowers.data.TinyFlowerResources flowerResources : resources.values()) {
			knownIds.add(flowerResources.model1());
			knownIds.add(flowerResources.model2());
			knownIds.add(flowerResources.model3());
			knownIds.add(flowerResources.model4());
		}

		return resources;
	}

	@Override
	protected void apply(Map<Identifier, TinyFlowerResources> identifierTinyFlowerResourcesMap, @NonNull ResourceManager resourceManager, @NonNull ProfilerFiller profilerFiller) {
	}

	public void registerModels(ModelEvent.RegisterStandalone event) {
		for (Identifier id : knownIds) {
			Services.FLOWER_MODELS.registerModel(id, event);
		}
	}
}
