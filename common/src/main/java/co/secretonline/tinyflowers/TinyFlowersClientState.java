package co.secretonline.tinyflowers;

import co.secretonline.tinyflowers.data.TinyFlowerResources;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.util.HashMap;
import java.util.Map;

public class TinyFlowersClientState {
	public static final RandomSource RANDOM = RandomSource.create();

	public static Map<ResourceLocation, TinyFlowerResources> RESOURCE_INSTANCES = new HashMap<>();
}
