package co.secretonline.tinyflowers.datagen.providers;

import co.secretonline.tinyflowers.data.ModRegistries;
import co.secretonline.tinyflowers.data.TinyFlowerResources;
import co.secretonline.tinyflowers.datagen.ModDatagenHelper;
import co.secretonline.tinyflowers.datagen.TinyFlowersDatagenData;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.JsonCodecProvider;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class NeoForgeModFlowerResourcesProvider extends JsonCodecProvider<TinyFlowerResources> {
	private final ModDatagenHelper modData;

	public NeoForgeModFlowerResourcesProvider(ModDatagenHelper modData, PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(
			packOutput,
			PackOutput.Target.RESOURCE_PACK,
			Registries.elementsDirPath(ModRegistries.TINY_FLOWER),
			TinyFlowerResources.CODEC,
			lookupProvider,
			modData.getModId());

		this.modData = modData;
	}

	@Override
	public @NonNull String getName() {
		return "Mod flowers resources provider [" + this.modData.getModId() + "]";
	}


	@Override
	protected void gather() {
		for (TinyFlowersDatagenData tuple : this.modData.getFlowerData()) {
			this.unconditional(tuple.resources().id(), tuple.resources());
		}
	}
}
