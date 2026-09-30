package co.secretonline.tinyflowers.helper;

import co.secretonline.tinyflowers.TinyFlowersClientState;
import co.secretonline.tinyflowers.data.TinyFlowerResources;
import net.minecraft.client.resources.model.ModelResourceLocation;

import java.util.ArrayList;
import java.util.List;

public final class ItemModelHelper {

	public static ModelResourceLocation getModelResourceLocation(TinyFlowerResources resources) {
		return ModelResourceLocation.inventory(resources.itemModel());
	}

	public static List<ModelResourceLocation> allResourceLocations() {
		List<ModelResourceLocation> resourceLocations = new ArrayList<>();

		for (var entry : TinyFlowersClientState.RESOURCE_INSTANCES.entrySet()) {
			resourceLocations.add(getModelResourceLocation(entry.getValue()));
		}

		return resourceLocations;
	}
}
