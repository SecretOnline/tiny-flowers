package co.secretonline.tinyflowers.datagen.generators.mods;

import java.util.List;

import co.secretonline.tinyflowers.data.TinyFlowerData;
import net.minecraft.resources.Identifier;

public class VanillaDataGenerator extends BaseModDataGenerator {
	@Override
	public String getModId() {
		return Identifier.DEFAULT_NAMESPACE;
	}

	@Override
	public List<TinyFlowerData> getFlowerData() {
		return List.of(
				new TinyFlowerData(Identifier.withDefaultNamespace("pink_petals"),
						Identifier.withDefaultNamespace("pink_petals"), true,
						List.of()),
				new TinyFlowerData(Identifier.withDefaultNamespace("wildflowers"),
						Identifier.withDefaultNamespace("wildflowers"), true,
						List.of()),
				new TinyFlowerData(Identifier.withDefaultNamespace("leaf_litter"),
						Identifier.withDefaultNamespace("leaf_litter"), true,
						List.of()));
	}
}
