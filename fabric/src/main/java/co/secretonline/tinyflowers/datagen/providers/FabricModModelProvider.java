package co.secretonline.tinyflowers.datagen.providers;

import co.secretonline.tinyflowers.datagen.mods.FlowerProvider;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import org.jspecify.annotations.NonNull;

public class FabricModModelProvider extends FabricModelProvider implements PartialModelProvider {
	private final FlowerProvider modData;

	public FabricModModelProvider(FlowerProvider modData, FabricPackOutput output) {
		super(output);

		this.modData = modData;
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
		this.modData.generateBlockStateModels(blockModelGenerators.modelOutput);
	}

	@Override
	public void generateItemModels(ItemModelGenerators itemModelGenerators) {
		this.modData.generateItemModels(itemModelGenerators.itemModelOutput, itemModelGenerators.modelOutput);
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
