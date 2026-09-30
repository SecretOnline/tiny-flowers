package co.secretonline.tinyflowers.datagen.mods;

import java.util.List;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.resources.ResourceLocation;

public class VanillaFlowerProvider extends FlowerProvider {
	@Override
	public String getModId() {
		return ResourceLocation.DEFAULT_NAMESPACE;
	}

	@Override
	public List<Flower> getFlowers() {
		return List.of(
			Flower.Builder
				.ofSegmented(ResourceLocation.withDefaultNamespace("pink_petals"))
				.customModel(ResourceLocation.withDefaultNamespace("flowerbed"))
				.customPottedModel(TinyFlowers.id("garden_potted"))
				.stemTexture(ResourceLocation.withDefaultNamespace("pink_petals_stem"))
				.particleTexture(ResourceLocation.withDefaultNamespace("pink_petals"))
				.build());
	}
}
