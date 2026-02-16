package co.secretonline.tinyflowers.datagen.mods;

import java.util.List;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.data.TinyFlowerResources.TintSource;
import net.minecraft.resources.Identifier;

public class VanillaFlowerProvider extends FlowerProvider {
	@Override
	public String getModId() {
		return Identifier.DEFAULT_NAMESPACE;
	}

	@Override
	public List<Flower> getFlowers() {
		return List.of(
			Flower.Builder
				.ofSegmented(Identifier.withDefaultNamespace("pink_petals"))
				.customModel(Identifier.withDefaultNamespace("flowerbed"))
				.stemTexture(Identifier.withDefaultNamespace("pink_petals_stem"))
				.particleTexture(Identifier.withDefaultNamespace("pink_petals"))
				.build(),
			Flower.Builder
				.ofSegmented(Identifier.withDefaultNamespace("wildflowers"))
				.customModel(Identifier.withDefaultNamespace("flowerbed"))
				.stemTexture(Identifier.withDefaultNamespace("pink_petals_stem"))
				.particleTexture(Identifier.withDefaultNamespace("wildflowers"))
				.build(),
			Flower.Builder
				.ofSegmented(Identifier.withDefaultNamespace("leaf_litter"))
				.addSturdyPlacementBehavior()
				.customModel(TinyFlowers.id("garden_leaf_litter"))
				.tintSource(TintSource.DRY_FOLIAGE)
				.build());
	}
}
