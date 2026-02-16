package co.secretonline.tinyflowers.datagen.providers;

import co.secretonline.tinyflowers.data.ModRegistries;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.datagen.mods.FlowerProvider;
import co.secretonline.tinyflowers.datagen.mods.Flower;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.JsonCodecProvider;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class NeoForgeModFlowerDataProvider extends JsonCodecProvider<TinyFlowerData> {
	private final FlowerProvider modData;

	public NeoForgeModFlowerDataProvider(FlowerProvider modData, PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(
			packOutput,
			PackOutput.Target.DATA_PACK,
			Registries.elementsDirPath(ModRegistries.TINY_FLOWER),
			TinyFlowerData.CODEC,
			lookupProvider,
			modData.getModId());

		this.modData = modData;
	}

	@Override
	public @NonNull String getName() {
		return "Mod flowers data provider [" + this.modData.getModId() + "]";
	}


	@Override
	protected void gather() {
		for (Flower tuple : this.modData.getFlowers()) {
			this.unconditional(tuple.data().id(), tuple.data());
		}
	}
}
