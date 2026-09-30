package co.secretonline.tinyflowers.platform;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;

public interface FlowerModelHelper {
	<T> void registerModel(ResourceLocation id, T context);

	void clear();

	BakedModel getModel(Minecraft client, ResourceLocation id);
}
