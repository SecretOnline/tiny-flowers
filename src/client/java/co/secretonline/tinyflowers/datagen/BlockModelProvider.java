package co.secretonline.tinyflowers.datagen;

import co.secretonline.tinyflowers.ModBlocks;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.BlockStateVariant;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;
import net.minecraft.client.data.MultipartBlockStateSupplier;
import net.minecraft.client.data.TexturedModel;
import net.minecraft.client.data.VariantSettings;
import net.minecraft.client.data.When;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class BlockModelProvider extends FabricModelProvider {
	private static int MAX_FLOWER_AMOUNT = 4;
	private static Direction[] DIRECTIONS = new Direction[] {
			Direction.NORTH, Direction.EAST,
			Direction.SOUTH, Direction.WEST, };

	private static Block[] BLOCKS = new Block[] { ModBlocks.TEST_FLOWER };

	public BlockModelProvider(FabricDataOutput generator) {
		super(generator);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
		for (Block block : BLOCKS) {
			MultipartBlockStateSupplier supplier = MultipartBlockStateSupplier.create(block);

			Identifier identifier = TexturedModel.FLOWERBED_1.upload(block, blockStateModelGenerator.modelCollector);
			Identifier identifier2 = TexturedModel.FLOWERBED_2.upload(block, blockStateModelGenerator.modelCollector);
			Identifier identifier3 = TexturedModel.FLOWERBED_3.upload(block, blockStateModelGenerator.modelCollector);
			Identifier identifier4 = TexturedModel.FLOWERBED_4.upload(block, blockStateModelGenerator.modelCollector);

			registerVariant(supplier, identifier, 1);
			registerVariant(supplier, identifier2, 2);
			registerVariant(supplier, identifier3, 3);
			registerVariant(supplier, identifier4, 4);

			blockStateModelGenerator.blockStateCollector.accept(supplier);
		}
	}

	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
		for (Block block : BLOCKS) {
			itemModelGenerator.register(block.asItem(), Models.GENERATED);
		}
	}

	private void registerVariant(MultipartBlockStateSupplier supplier, Identifier modelIdentifier, int flowerAmount) {
		for (Direction direction : DIRECTIONS) {
			supplier.with(
					getWhen(flowerAmount, direction),
					getVariant(modelIdentifier, direction));
		}
	}

	private When getWhen(int flowerAmount, Direction direction) {
		int otherValuesLength = MAX_FLOWER_AMOUNT - flowerAmount;
		Integer[] otherValues = new Integer[otherValuesLength];
		for (int i = 0; i < otherValues.length; i++) {
			otherValues[i] = flowerAmount + i + 1;
		}

		return When.create()
				.set(Properties.FLOWER_AMOUNT, flowerAmount, otherValues)
				.set(Properties.HORIZONTAL_FACING, direction);
	}

	private BlockStateVariant getVariant(Identifier modelIdentifier, Direction direction) {
		VariantSettings.Rotation rotation;
		switch (direction) {
			case Direction.NORTH:
				rotation = VariantSettings.Rotation.R0;
				break;
			case Direction.EAST:
				rotation = VariantSettings.Rotation.R90;
				break;
			case Direction.SOUTH:
				rotation = VariantSettings.Rotation.R180;
				break;
			case Direction.WEST:
				rotation = VariantSettings.Rotation.R270;
				break;
			default:
				throw new IllegalArgumentException("Unknown direction for model");
		}

		return BlockStateVariant.create()
				.put(VariantSettings.MODEL, modelIdentifier)
				.put(VariantSettings.Y, rotation);
	}

}
