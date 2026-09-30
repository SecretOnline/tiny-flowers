package co.secretonline.tinyflowers.datagen.providers;

import co.secretonline.tinyflowers.datagen.mods.FlowerProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;

public class FabricModModelProvider extends FabricModelProvider implements PartialModelProvider {
	private final FlowerProvider modData;

	public FabricModModelProvider(FlowerProvider modData, FabricDataOutput output) {
		super(output);

		this.modData = modData;
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
		this.modData.generateBlockStateModels(blockModelGenerators.modelOutput);
	}

	@Override
	public void generateItemModels(ItemModelGenerators itemModelGenerators) {
		this.modData.generateItemModels(itemModelGenerators.output);
	}

	@Override
	public String getName() {
		return "Mod models provider [" + this.modData.getModId() + "]";
	}

	@Override
	public boolean shouldValidateAllEntries() {
		return false;
	}
}
