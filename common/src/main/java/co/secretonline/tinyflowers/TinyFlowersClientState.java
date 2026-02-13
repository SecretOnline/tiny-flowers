package co.secretonline.tinyflowers;

import co.secretonline.tinyflowers.resources.TinyFlowerResources;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;

import java.util.HashMap;
import java.util.Map;

public class TinyFlowersClientState {
	public static final RandomSource RANDOM = RandomSource.create();
	public static final ItemStackRenderState ITEM_RENDER_STATE = new ItemStackRenderState();
	public static Map<Identifier, TinyFlowerResources> RESOURCE_INSTANCES = new HashMap<>();
}
