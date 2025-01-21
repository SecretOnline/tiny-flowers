package co.secretonline.tinyflowers.datagen;

import co.secretonline.tinyflowers.ModBlocks;
import co.secretonline.tinyflowers.blocks.FlowerVariant;
import co.secretonline.tinyflowers.blocks.GardenBlock;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.BlockStateVariant;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.MultipartBlockStateSupplier;
import net.minecraft.client.data.VariantSettings;
import net.minecraft.client.data.When;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class BlockModelProvider extends FabricModelProvider {
	private static Direction[] DIRECTIONS = new Direction[] {
			Direction.NORTH, Direction.EAST,
			Direction.SOUTH, Direction.WEST, };

	public BlockModelProvider(FabricDataOutput generator) {
		super(generator);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
		MultipartBlockStateSupplier supplier = MultipartBlockStateSupplier.create(ModBlocks.TINY_GARDEN);

		for (FlowerVariant variant : FlowerVariant.values()) {
			if (variant == FlowerVariant.EMPTY) {
				continue;
			}

			registerVariant(supplier, variant);
		}

		blockStateModelGenerator.blockStateCollector.accept(supplier);
	}

	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
		// TODO: Add item models when there are more types
	}

	private void registerVariant(MultipartBlockStateSupplier supplier, FlowerVariant variant) {
		for (Direction direction : DIRECTIONS) {
			supplier.with(
					getWhen(direction, GardenBlock.FLOWER_VARIANT_1, variant),
					getModelVariant(direction, variant.models.model1));
			supplier.with(
					getWhen(direction, GardenBlock.FLOWER_VARIANT_2, variant),
					getModelVariant(direction, variant.models.model2));
			supplier.with(
					getWhen(direction, GardenBlock.FLOWER_VARIANT_3, variant),
					getModelVariant(direction, variant.models.model3));
			supplier.with(
					getWhen(direction, GardenBlock.FLOWER_VARIANT_4, variant),
					getModelVariant(direction, variant.models.model4));
		}
	}

	private When getWhen(Direction direction, EnumProperty<FlowerVariant> property, FlowerVariant variant) {
		return When.create()
				.set(Properties.HORIZONTAL_FACING, direction)
				.set(property, variant);
	}

	private BlockStateVariant getModelVariant(Direction direction, Identifier modelIdentifier) {
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
