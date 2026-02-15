package co.secretonline.tinyflowers.datagen.providers;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.datagen.ModDatagenHelper;
import co.secretonline.tinyflowers.datagen.PartialModelProvider;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.PackOutput;
import org.jspecify.annotations.NonNull;

public class NeoForgeModModelProvider extends ModelProvider implements PartialModelProvider {
	private final ModDatagenHelper modData;

	public NeoForgeModModelProvider(ModDatagenHelper modData, PackOutput output) {
		super(output, modData.getModId());

		this.modData = modData;
	}

	@Override
	protected void registerModels(@NonNull BlockModelGenerators blockModels, @NonNull ItemModelGenerators itemModels) {
		this.modData.generateBlockStateModels(blockModels.modelOutput);
		this.modData.generateItemModels(itemModels.itemModelOutput, itemModels.modelOutput);
	}

	@Override
	public @NonNull String getName() {
		return "Mod models provider [" + this.modData.getModId() + "]";
	}

	@Override
	public boolean shouldValidateAllEntries() {
		return false;
	}
}
