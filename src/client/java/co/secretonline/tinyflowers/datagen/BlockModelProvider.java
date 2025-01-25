package co.secretonline.tinyflowers.datagen;

import co.secretonline.tinyflowers.blocks.FlowerVariant;
import co.secretonline.tinyflowers.blocks.GardenBlock;
import co.secretonline.tinyflowers.blocks.ModBlocks;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.BlockStateVariant;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;
import net.minecraft.client.data.MultipartBlockStateSupplier;
import net.minecraft.client.data.TextureKey;
import net.minecraft.client.data.TextureMap;
import net.minecraft.client.data.TexturedModel;
import net.minecraft.client.data.VariantSettings;
import net.minecraft.client.data.When;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class BlockModelProvider extends FabricModelProvider {
	private final static Direction[] DIRECTIONS = new Direction[] {
			Direction.NORTH, Direction.EAST,
			Direction.SOUTH, Direction.WEST, };

	private final static FlowerVariant[] VARIANTS_TO_GENERATE_MODELS_FOR = new FlowerVariant[] {
			FlowerVariant.DANDELION,
			FlowerVariant.POPPY,
			FlowerVariant.BLUE_ORCHID,
			FlowerVariant.ALLIUM,
			FlowerVariant.AZURE_BLUET,
			FlowerVariant.RED_TULIP,
			FlowerVariant.ORANGE_TULIP,
			FlowerVariant.WHITE_TULIP,
			FlowerVariant.PINK_TULIP,
			FlowerVariant.OXEYE_DAISY,
			FlowerVariant.CORNFLOWER,
			FlowerVariant.LILY_OF_THE_VALLEY,
			FlowerVariant.TORCHFLOWER,
			FlowerVariant.CLOSED_EYEBLOSSOM,
			FlowerVariant.OPEN_EYEBLOSSOM,
			FlowerVariant.WITHER_ROSE,
	};

	public BlockModelProvider(FabricDataOutput generator) {
		super(generator);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
		MultipartBlockStateSupplier supplier = MultipartBlockStateSupplier.create(ModBlocks.TINY_GARDEN);

		// Generate blockstate variants for all flower variants.
		for (FlowerVariant variant : FlowerVariant.values()) {
			if (variant.isEmpty()) {
				continue;
			}

			Identifier itemId = variant.getItemIdentifier();

			Identifier model1 = itemId.withPath(path -> "block/" + path + "_1");
			Identifier model2 = itemId.withPath(path -> "block/" + path + "_2");
			Identifier model3 = itemId.withPath(path -> "block/" + path + "_3");
			Identifier model4 = itemId.withPath(path -> "block/" + path + "_4");

			registerPartInAllDirections(supplier, variant, GardenBlock.FLOWER_VARIANT_1, model1);
			registerPartInAllDirections(supplier, variant, GardenBlock.FLOWER_VARIANT_2, model2);
			registerPartInAllDirections(supplier, variant, GardenBlock.FLOWER_VARIANT_3, model3);
			registerPartInAllDirections(supplier, variant, GardenBlock.FLOWER_VARIANT_4, model4);
		}

		// Generate models temorarily.
		// This will likely be replaced once I know what models I'll be creating.
		var texturedModel1 = TexturedModel.FLOWERBED_1.get(ModBlocks.TINY_GARDEN);
		var texturedModel2 = TexturedModel.FLOWERBED_2.get(ModBlocks.TINY_GARDEN);
		var texturedModel3 = TexturedModel.FLOWERBED_3.get(ModBlocks.TINY_GARDEN);
		var texturedModel4 = TexturedModel.FLOWERBED_4.get(ModBlocks.TINY_GARDEN);

		for (FlowerVariant variant : VARIANTS_TO_GENERATE_MODELS_FOR) {
			Identifier itemId = variant.getItemIdentifier();

			Identifier model1 = itemId.withPath(path -> "block/" + path + "_1");
			Identifier model2 = itemId.withPath(path -> "block/" + path + "_2");
			Identifier model3 = itemId.withPath(path -> "block/" + path + "_3");
			Identifier model4 = itemId.withPath(path -> "block/" + path + "_4");

			TextureMap textureMap = new TextureMap()
					.put(TextureKey.FLOWERBED, itemId.withPath(path -> "block/" + path))
					.put(TextureKey.STEM, Identifier.ofVanilla("block/pink_petals_stem"));

			texturedModel1.getModel().upload(model1, textureMap, blockStateModelGenerator.modelCollector);
			texturedModel2.getModel().upload(model2, textureMap, blockStateModelGenerator.modelCollector);
			texturedModel3.getModel().upload(model3, textureMap, blockStateModelGenerator.modelCollector);
			texturedModel4.getModel().upload(model4, textureMap, blockStateModelGenerator.modelCollector);
		}

		blockStateModelGenerator.blockStateCollector.accept(supplier);
	}

	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
		for (FlowerVariant variant : VARIANTS_TO_GENERATE_MODELS_FOR) {
			itemModelGenerator.register(variant.getItem(), Models.GENERATED);
		}
	}

	private void registerPartInAllDirections(MultipartBlockStateSupplier supplier, FlowerVariant variant,
			EnumProperty<FlowerVariant> property, Identifier modelIdentifier) {
		for (Direction direction : DIRECTIONS) {
			supplier.with(
					When.create()
							.set(property, variant)
							.set(Properties.HORIZONTAL_FACING, direction),
					BlockStateVariant.create()
							.put(VariantSettings.MODEL, modelIdentifier)
							.put(VariantSettings.Y, getRotationForDirection(direction)));
		}
	}

	private VariantSettings.Rotation getRotationForDirection(Direction direction) {
		switch (direction) {
			case Direction.NORTH:
				return VariantSettings.Rotation.R0;
			case Direction.EAST:
				return VariantSettings.Rotation.R90;
			case Direction.SOUTH:
				return VariantSettings.Rotation.R180;
			case Direction.WEST:
				return VariantSettings.Rotation.R270;
			default:
				throw new IllegalArgumentException("Unknown direction for model");
		}
	}
}
