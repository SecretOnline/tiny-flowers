package co.secretonline.tinyflowers.datagen;

import java.util.function.BiConsumer;
import java.util.function.Function;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.blocks.FlowerVariant;
import co.secretonline.tinyflowers.blocks.GardenBlock;
import co.secretonline.tinyflowers.blocks.ModBlocks;
import co.secretonline.tinyflowers.datagen.data.ModModels;
import co.secretonline.tinyflowers.datagen.data.ModTextureMap;
import co.secretonline.tinyflowers.items.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.color.item.Dye;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.block.model.VariantMutator;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

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
			// Single layer, eyeblossom stem
			new ModelGroup(ModModels.Quartet.GARDEN_UNTINTED,
					ModTextureMap.flowerbed(TinyFlowers.id("block/tiny_eyeblossom_stem")),
					new FlowerVariant[] {
							FlowerVariant.CLOSED_EYEBLOSSOM,
					}),
			// Single layer, cactus flower stem
			new ModelGroup(ModModels.Quartet.GARDEN_LOW_UNTINTED,
					ModTextureMap.flowerbed(TinyFlowers.id("block/tiny_cactus_flower_stem")),
					new FlowerVariant[] {
							FlowerVariant.CACTUS_FLOWER,
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
			// Double layer (glow on top), eyeblossom stem
			new ModelGroup(ModModels.Quartet.GARDEN_DOUBLE_UNTINTED_GLOW,
					ModTextureMap.flowerbedDouble(TinyFlowers.id("block/tiny_eyeblossom_stem")),
					new FlowerVariant[] {
							FlowerVariant.OPEN_EYEBLOSSOM,
					}),
			// Triple layer, torchflower stem
			new ModelGroup(ModModels.Quartet.GARDEN_TRIPLE_UNTINTED,
					ModTextureMap.flowerbedTriple(TinyFlowers.id("block/tiny_torchflower_stem")),
					new FlowerVariant[] {
							FlowerVariant.TORCHFLOWER,
					}),
			// Leaf litter, no stem
			new ModelGroup(ModModels.Quartet.GARDEN_LEAF_LITTER,
					ModTextureMap.noStem(),
					new FlowerVariant[] {
							FlowerVariant.LEAF_LITTER,
					}),
	};

	public BlockModelProvider(FabricDataOutput generator) {
		super(generator);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
		MultiPartGenerator definitionCreator = MultiPartGenerator
				.multiPart(ModBlocks.TINY_GARDEN);

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

			registerPartInAllDirections(definitionCreator, variant, GardenBlock.FLOWER_VARIANT_1, model1);
			registerPartInAllDirections(definitionCreator, variant, GardenBlock.FLOWER_VARIANT_2, model2);
			registerPartInAllDirections(definitionCreator, variant, GardenBlock.FLOWER_VARIANT_3, model3);
			registerPartInAllDirections(definitionCreator, variant, GardenBlock.FLOWER_VARIANT_4, model4);
		}

		// Generate all block model definitions.
		for (ModelGroup group : MODEL_GROUPS) {
			group.upload(blockStateModelGenerator.modelOutput);
		}

		blockStateModelGenerator.blockStateOutput.accept(definitionCreator);
	}

	@Override
	public void generateItemModels(ItemModelGenerators itemModelGenerator) {
		itemModelGenerator.generateFlatItem(ModBlocks.TINY_GARDEN.asItem(), ModelTemplates.FLAT_ITEM);
		itemModelGenerator.generateItemWithTintedOverlay(
				ModItems.FLORISTS_SHEARS_ITEM,
				"_handle",
				new Dye(DyeColor.RED.getTextureDiffuseColor()));

		for (FlowerVariant variant : FlowerVariant.values()) {
			if (variant.shouldCreateItem()) {
				itemModelGenerator.generateFlatItem(variant.asItem(), ModelTemplates.FLAT_ITEM);
			}
		}
	}

	private MultiPartGenerator registerPartInAllDirections(
			MultiPartGenerator modelDefinitionCreator, FlowerVariant variant,
			EnumProperty<FlowerVariant> property, Identifier modelIdentifier) {
		for (Direction direction : DIRECTIONS) {
			modelDefinitionCreator = modelDefinitionCreator.with(
					BlockModelGenerators.condition()
							.term(property, variant)
							.term(BlockStateProperties.HORIZONTAL_FACING, direction),
					BlockModelGenerators.plainVariant(modelIdentifier)
							.with(getRotationForDirection(direction)));
		}

		return modelDefinitionCreator;
	}

	private VariantMutator getRotationForDirection(Direction direction) {
		switch (direction) {
			case Direction.NORTH:
				return BlockModelGenerators.NOP;
			case Direction.EAST:
				return BlockModelGenerators.Y_ROT_90;
			case Direction.SOUTH:
				return BlockModelGenerators.Y_ROT_180;
			case Direction.WEST:
				return BlockModelGenerators.Y_ROT_270;
			default:
				throw new IllegalArgumentException("Unknown direction for model");
		}
	}

	private static Identifier getVariantBaseModelId(FlowerVariant variant) {
		Identifier identifier = variant.getItemIdentifier();

		switch (variant) {
			case FlowerVariant.LEAF_LITTER:
				// The base Leaf Litter models utilise a single plane which doesn't quite work
				// with this mod. As such, we need to override the models for Leaf Litter
				// specifically.
				identifier = TinyFlowers.id("leaf_litter");
				break;
			default:
		}

		return identifier;
	}

	private record ModelGroup(ModModels.Quartet models,
			Function<Identifier, TextureMapping> texturesGetter, FlowerVariant[] variants) {

		public void upload(BiConsumer<Identifier, ModelInstance> modelCollector) {
			for (FlowerVariant variant : this.variants) {
				Identifier textureId = variant.getItemIdentifier();
				Identifier itemId = getVariantBaseModelId(variant);

				Identifier modelId1 = itemId.withPath(path -> "block/" + path + "_1");
				Identifier modelId2 = itemId.withPath(path -> "block/" + path + "_2");
				Identifier modelId3 = itemId.withPath(path -> "block/" + path + "_3");
				Identifier modelId4 = itemId.withPath(path -> "block/" + path + "_4");

				TextureMapping textureMap = this.texturesGetter.apply(textureId);

				this.models.model1().create(modelId1, textureMap, modelCollector);
				this.models.model2().create(modelId2, textureMap, modelCollector);
				this.models.model3().create(modelId3, textureMap, modelCollector);
				this.models.model4().create(modelId4, textureMap, modelCollector);
			}
		}
	}
}
