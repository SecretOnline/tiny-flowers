package co.secretonline.tinyflowers.datagen.generators.mods;

import java.util.List;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.resources.TinyFlowerResources;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Tuple;

public class VanillaDataGenerator extends BaseModDataGenerator {
	@Override
	public String getModId() {
		return Identifier.DEFAULT_NAMESPACE;
	}

	@Override
	public List<Tuple<TinyFlowerData, TinyFlowerResources>> getFlowerData() {
		return List.of(
				new Tuple<TinyFlowerData, TinyFlowerResources>(
						new TinyFlowerData(Identifier.withDefaultNamespace("pink_petals"),
								Identifier.withDefaultNamespace("pink_petals"), true,
								List.of()),
						new TinyFlowerResources.Builder(Identifier.withDefaultNamespace("pink_petals"))
								.layers(Identifier.withDefaultNamespace("pink_petals"))
								.build()),
				new Tuple<TinyFlowerData, TinyFlowerResources>(
						new TinyFlowerData(Identifier.withDefaultNamespace("wildflowers"),
								Identifier.withDefaultNamespace("wildflowers"), true,
								List.of()),
						new TinyFlowerResources.Builder(Identifier.withDefaultNamespace("wildflowers"))
								.layers(Identifier.withDefaultNamespace("wildflowers"))
								.build()),
				new Tuple<TinyFlowerData, TinyFlowerResources>(
						new TinyFlowerData(Identifier.withDefaultNamespace("leaf_litter"),
								Identifier.withDefaultNamespace("leaf_litter"), true,
								List.of()),
						new TinyFlowerResources.Builder(Identifier.withDefaultNamespace("leaf_litter"))
								.layers(Identifier.withDefaultNamespace("leaf_litter"))
								.noStem()
								.special(TinyFlowers.id("garden_leaf_litter"))
								.build()));
	}
}
