package co.secretonline.tinyflowers.datagen.generators.mods;

import java.util.List;

import co.secretonline.tinyflowers.TinyFlowers;
import net.minecraft.resources.Identifier;

public class VanillaDataGenerator extends BaseModDataGenerator {
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
						.build(),
				TinyFlowersDatagenData.Builder
						.ofSegmented(Identifier.withDefaultNamespace("wildflowers"))
						.customModel(Identifier.withDefaultNamespace("flowerbed"))
						.build(),
				TinyFlowersDatagenData.Builder
						.ofSegmented(Identifier.withDefaultNamespace("leaf_litter"))
						.customModel(TinyFlowers.id("garden_leaf_litter"))
						.noStem()
						.build());
	}
}
