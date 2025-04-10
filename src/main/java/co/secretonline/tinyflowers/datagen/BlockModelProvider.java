package co.secretonline.tinyflowers.datagen;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.google.gson.JsonElement;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.blocks.FlowerVariant;
import co.secretonline.tinyflowers.blocks.GardenBlock;
import co.secretonline.tinyflowers.blocks.ModBlocks;
import co.secretonline.tinyflowers.datagen.data.ModModels;
import co.secretonline.tinyflowers.datagen.data.ModTextureMap;
import co.secretonline.tinyflowers.items.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.ModelIds;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.MultipartBlockStateSupplier;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.data.client.When;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class BlockModelProvider extends FabricModelProvider {
	private final static Direction[] DIRECTIONS = new Direction[] {
			Direction.NORTH, Direction.EAST,
			Direction.SOUTH, Direction.WEST, };

	private final static ModelGroup[] MODEL_GROUPS = new ModelGroup[] {
			// Single layer, tinted stem
			new ModelGroup(ModModels.Quartet.GARDEN,
					ModTextureMap.flowerbed(),
					new FlowerVariant[] {
							FlowerVariant.DANDELION,
							FlowerVariant.ALLIUM,
							FlowerVariant.AZURE_BLUET,
							FlowerVariant.RED_TULIP,
							FlowerVariant.ORANGE_TULIP,
							FlowerVariant.WHITE_TULIP,
							FlowerVariant.PINK_TULIP,
							FlowerVariant.OXEYE_DAISY,
							FlowerVariant.CORNFLOWER,
					}),
			// Single layer, wither rose stem
			new ModelGroup(ModModels.Quartet.GARDEN_UNTINTED,
					ModTextureMap.flowerbed(TinyFlowers.id("block/tiny_wither_rose_stem")),
					new FlowerVariant[] {
							FlowerVariant.WITHER_ROSE,
					}),
			// Single layer (tall), tinted stem
			new ModelGroup(ModModels.Quartet.GARDEN_TALL,
					ModTextureMap.flowerbed(TinyFlowers.id("block/tall_tiny_flower_stem")),
					new FlowerVariant[] {
							FlowerVariant.POPPY,
					}),
			// Double layer, tinted stem
			new ModelGroup(ModModels.Quartet.GARDEN_DOUBLE,
					ModTextureMap.flowerbedDouble(TinyFlowers.id("block/tall_tiny_flower_stem")),
					new FlowerVariant[] {
							FlowerVariant.BLUE_ORCHID,
							FlowerVariant.LILY_OF_THE_VALLEY,
					}),
			// Triple layer, torchflower stem
			new ModelGroup(ModModels.Quartet.GARDEN_TRIPLE_UNTINTED,
					ModTextureMap.flowerbedTriple(TinyFlowers.id("block/tiny_torchflower_stem")),
					new FlowerVariant[] {
							FlowerVariant.TORCHFLOWER,
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

			Identifier baseId = getVariantBaseModelId(variant);

			Identifier model1 = baseId.withPath(path -> "block/" + path + "_1");
			Identifier model2 = baseId.withPath(path -> "block/" + path + "_2");
			Identifier model3 = baseId.withPath(path -> "block/" + path + "_3");
			Identifier model4 = baseId.withPath(path -> "block/" + path + "_4");

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

		Models.GENERATED_TWO_LAYERS.upload(
				ModelIds.getItemModelId(ModItems.FLORISTS_SHEARS_ITEM),
				TextureMap.layered(
						ModItems.FLORISTS_SHEARS_ITEM_KEY.getValue().withPrefixedPath("item/"),
						ModItems.FLORISTS_SHEARS_ITEM_KEY.getValue().withPrefixedPath("item/").withSuffixedPath("_handle")),
				itemModelGenerator.writer);

		for (FlowerVariant variant : FlowerVariant.values()) {
			if (variant.shouldCreateItem()) {
				itemModelGenerator.register(variant.asItem(), Models.GENERATED);
			}
		}
	}

	private MultipartBlockStateSupplier registerPartInAllDirections(
			MultipartBlockStateSupplier modelDefinitionCreator, FlowerVariant variant,
			EnumProperty<FlowerVariant> property, Identifier modelIdentifier) {
		for (Direction direction : DIRECTIONS) {
			modelDefinitionCreator.with(
					When.create()
							.set(property, variant)
							.set(Properties.HORIZONTAL_FACING, direction),
					BlockStateVariant.create()
							.put(VariantSettings.MODEL, modelIdentifier)
							.put(VariantSettings.Y, getRotationForDirection(direction)));
		}

		return modelDefinitionCreator;
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

	private static Identifier getVariantBaseModelId(FlowerVariant variant) {
		return variant.getItemIdentifier();
	}

	private record ModelGroup(ModModels.Quartet models,
			Function<Identifier, TextureMap> texturesGetter, FlowerVariant[] variants) {

		public void upload(BiConsumer<Identifier, Supplier<JsonElement>> modelCollector) {
			for (FlowerVariant variant : this.variants) {
				Identifier textureId = variant.getItemIdentifier();
				Identifier itemId = getVariantBaseModelId(variant);

				Identifier modelId1 = itemId.withPath(path -> "block/" + path + "_1");
				Identifier modelId2 = itemId.withPath(path -> "block/" + path + "_2");
				Identifier modelId3 = itemId.withPath(path -> "block/" + path + "_3");
				Identifier modelId4 = itemId.withPath(path -> "block/" + path + "_4");

				TextureMap textureMap = this.texturesGetter.apply(textureId);

				this.models.model1().upload(modelId1, textureMap, modelCollector);
				this.models.model2().upload(modelId2, textureMap, modelCollector);
				this.models.model3().upload(modelId3, textureMap, modelCollector);
				this.models.model4().upload(modelId4, textureMap, modelCollector);
			}
		}
	}
}
