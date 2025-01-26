package co.secretonline.tinyflowers.datagen;

import java.util.function.BiConsumer;
import java.util.function.Function;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.blocks.FlowerVariant;
import co.secretonline.tinyflowers.blocks.GardenBlock;
import co.secretonline.tinyflowers.blocks.ModBlocks;
import co.secretonline.tinyflowers.datagen.data.ModModels;
import co.secretonline.tinyflowers.datagen.data.ModTextureMap;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.BlockStateVariant;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.ModelSupplier;
import net.minecraft.client.data.Models;
import net.minecraft.client.data.MultipartBlockStateSupplier;
import net.minecraft.client.data.TextureMap;
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

	private final static ModelGroup[] MODEL_GROUPS = new ModelGroup[] {
			new ModelGroup(ModModels.Quartet.FLOWERBED,
					ModTextureMap.flowerbed(),
					new FlowerVariant[] {
							FlowerVariant.DANDELION,
							FlowerVariant.POPPY,
							FlowerVariant.BLUE_ORCHID,
							FlowerVariant.ALLIUM,
							FlowerVariant.AZURE_BLUET,
							FlowerVariant.OXEYE_DAISY,
							FlowerVariant.CORNFLOWER,
							FlowerVariant.TORCHFLOWER,
					}),
			new ModelGroup(ModModels.Quartet.FLOWERBED_STEM,
					ModTextureMap.flowerbed(TinyFlowers.id("block/tiny_wither_rose_stem")),
					new FlowerVariant[] {
							FlowerVariant.WITHER_ROSE,
					}),
			new ModelGroup(ModModels.Quartet.FLOWERBED_TULIP,
					ModTextureMap.flowerbed(),
					new FlowerVariant[] {
							FlowerVariant.RED_TULIP,
							FlowerVariant.ORANGE_TULIP,
							FlowerVariant.WHITE_TULIP,
							FlowerVariant.PINK_TULIP,
					}),
			new ModelGroup(ModModels.Quartet.FLOWERBED_DOUBLE,
					ModTextureMap.flowerbedDouble(TinyFlowers.id("block/tall_tiny_flower_stem")),
					new FlowerVariant[] {
							FlowerVariant.LILY_OF_THE_VALLEY,
					}),
			new ModelGroup(ModModels.Quartet.FLOWERBED_DOUBLE_GLOW,
					ModTextureMap.flowerbedDouble(TinyFlowers.id("block/tiny_eyeblossom_stem")),
					new FlowerVariant[] {
							FlowerVariant.OPEN_EYEBLOSSOM,
					}),
			new ModelGroup(ModModels.Quartet.FLOWERBED_STEM,
					ModTextureMap.flowerbed(TinyFlowers.id("block/tiny_eyeblossom_stem")),
					new FlowerVariant[] {
							FlowerVariant.CLOSED_EYEBLOSSOM,
					}),
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

		// Generate all block model definitions.
		for (ModelGroup group : MODEL_GROUPS) {
			group.upload(blockStateModelGenerator.modelCollector);
		}

		blockStateModelGenerator.blockStateCollector.accept(supplier);
	}

	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
		itemModelGenerator.register(ModBlocks.TINY_GARDEN.asItem(), Models.GENERATED);

		for (FlowerVariant variant : FlowerVariant.values()) {
			if (variant.shouldCreateItemModel()) {
				itemModelGenerator.register(variant.asItem(), Models.GENERATED);
			}
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

	private record ModelGroup(ModModels.Quartet models,
			Function<Identifier, TextureMap> texturesGetter, FlowerVariant[] variants) {

		public void upload(BiConsumer<Identifier, ModelSupplier> modelCollector) {
			for (FlowerVariant variant : this.variants) {
				Identifier itemId = variant.getItemIdentifier();

				Identifier modelId1 = itemId.withPath(path -> "block/" + path + "_1");
				Identifier modelId2 = itemId.withPath(path -> "block/" + path + "_2");
				Identifier modelId3 = itemId.withPath(path -> "block/" + path + "_3");
				Identifier modelId4 = itemId.withPath(path -> "block/" + path + "_4");

				TextureMap textureMap = this.texturesGetter.apply(itemId);

				this.models.model1().upload(modelId1, textureMap, modelCollector);
				this.models.model2().upload(modelId2, textureMap, modelCollector);
				this.models.model3().upload(modelId3, textureMap, modelCollector);
				this.models.model4().upload(modelId4, textureMap, modelCollector);
			}
		}
	}
}
