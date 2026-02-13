package co.secretonline.tinyflowers.datagen.mods;

import java.util.List;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.datagen.ModDatagenHelper;
import co.secretonline.tinyflowers.datagen.TinyFlowersDatagenData;
import co.secretonline.tinyflowers.resources.TinyFlowerResources.TintSource;
import net.minecraft.resources.Identifier;

public class VanillaDatagenHelper extends ModDatagenHelper {
	@Override
	public String getModId() {
		return Identifier.DEFAULT_NAMESPACE;
	}

	@Override
	public List<TinyFlowersDatagenData> getFlowerData() {
		return List.of(
				TinyFlowersDatagenData.Builder
						.ofSegmented(Identifier.withDefaultNamespace("pink_petals"))
						.customModel(Identifier.withDefaultNamespace("flowerbed"))
						.stemTexture(Identifier.withDefaultNamespace("pink_petals_stem"))
						.particleTexture(Identifier.withDefaultNamespace("pink_petals"))
						.build(),
				TinyFlowersDatagenData.Builder
						.ofSegmented(Identifier.withDefaultNamespace("wildflowers"))
						.customModel(Identifier.withDefaultNamespace("flowerbed"))
						.stemTexture(Identifier.withDefaultNamespace("pink_petals_stem"))
						.particleTexture(Identifier.withDefaultNamespace("wildflowers"))
						.build(),
				TinyFlowersDatagenData.Builder
						.ofSegmented(Identifier.withDefaultNamespace("leaf_litter"))
						.customModel(TinyFlowers.id("garden_leaf_litter"))
						.tintSource(TintSource.DRY_FOLIAGE)
						.build());
	}
}
