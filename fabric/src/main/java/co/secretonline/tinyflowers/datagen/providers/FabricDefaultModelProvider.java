package co.secretonline.tinyflowers.datagen.providers;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.block.ModBlocks;
import co.secretonline.tinyflowers.block.TinyGardenBlock;
import co.secretonline.tinyflowers.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.core.Direction;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;

public class FabricDefaultModelProvider extends FabricModelProvider {
	private final static Direction[] DIRECTIONS = new Direction[] {
			Direction.NORTH, Direction.EAST,
			Direction.SOUTH, Direction.WEST, };

	public FabricDefaultModelProvider(FabricDataOutput generator) {
		super(generator);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
		MultiPartGenerator definitionCreator = MultiPartGenerator
				.multiPart(ModBlocks.TINY_GARDEN_BLOCK.get());

		for (Direction direction : DIRECTIONS) {
			definitionCreator = definitionCreator.with(
					Condition.condition()
							.term(TinyGardenBlock.FACING, direction),
					Variant.variant().with(VariantProperties.MODEL, TinyFlowers.id("block/tiny_garden")));
		}

		blockStateModelGenerator.blockStateOutput.accept(definitionCreator);

		MultiVariantGenerator flowerPotGenerator = BlockModelGenerators.createSimpleBlock(
			ModBlocks.TINY_FLOWER_POT_BLOCK.get(),
			TinyFlowers.id("block/tiny_flower_pot"));
		blockStateModelGenerator.blockStateOutput.accept(flowerPotGenerator);
	}

	@Override
	public void generateItemModels(ItemModelGenerators itemModelGenerator) {
		ModelTemplates.FLAT_ITEM.create(
			ModelLocationUtils.getModelLocation(ModItems.TINY_FLOWER_ITEM.get()),
			TextureMapping.layer0(TinyFlowers.id("item/tiny_garden")),
			itemModelGenerator.output);

		ResourceLocation shears = ModelLocationUtils.getModelLocation(ModItems.FLORISTS_SHEARS_ITEM.get());
		itemModelGenerator.generateLayeredItem(shears, shears, shears.withSuffix("_handle"));
	}

	@Override
	public String getName() {
		return "FloristsShearsItemModelProvider";
	}
}
