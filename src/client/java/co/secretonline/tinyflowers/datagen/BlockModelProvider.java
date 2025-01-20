package co.secretonline.tinyflowers.datagen;

import co.secretonline.tinyflowers.ModBlocks;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;

public class BlockModelProvider extends FabricModelProvider {
	public BlockModelProvider(FabricDataOutput generator) {
		super(generator);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
		blockStateModelGenerator.registerFlowerbed(ModBlocks.TEST_FLOWER);
	}

	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
		// Item models are registered in blockStateModelGenerator.registerFlowerbed()
	}

}
