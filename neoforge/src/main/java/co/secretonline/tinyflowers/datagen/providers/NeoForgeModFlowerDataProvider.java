package co.secretonline.tinyflowers.datagen.providers;

import co.secretonline.tinyflowers.data.ModRegistries;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.datagen.ModDatagenHelper;
import co.secretonline.tinyflowers.datagen.TinyFlowersDatagenData;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.JsonCodecProvider;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class NeoForgeModFlowerDataProvider extends JsonCodecProvider<TinyFlowerData> {
	private final ModDatagenHelper modData;

	public NeoForgeModFlowerDataProvider(ModDatagenHelper modData, PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
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
		for (TinyFlowersDatagenData tuple : this.modData.getFlowerData()) {
			this.unconditional(tuple.data().id(), tuple.data());
		}
	}
}
