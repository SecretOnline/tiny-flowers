package co.secretonline.tinyflowers.datagen.providers;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.blocks.ModBlocks;
import co.secretonline.tinyflowers.blocks.TinyGardenBlock;
import co.secretonline.tinyflowers.items.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.color.item.Dye;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;

public class DefaultModelProvider extends FabricModelProvider {
	private final static Direction[] DIRECTIONS = new Direction[] {
			Direction.NORTH, Direction.EAST,
			Direction.SOUTH, Direction.WEST, };

	public DefaultModelProvider(FabricPackOutput generator) {
		super(generator);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
		MultiPartGenerator definitionCreator = MultiPartGenerator
				.multiPart(ModBlocks.TINY_GARDEN_BLOCK.get());

		for (Direction direction : DIRECTIONS) {
			definitionCreator = definitionCreator.with(
					BlockModelGenerators.condition()
							.term(TinyGardenBlock.FACING, direction),
					BlockModelGenerators.plainVariant(TinyFlowers.id("block/tiny_garden")));
		}

		blockStateModelGenerator.blockStateOutput.accept(definitionCreator);
	}

	@Override
	public void generateItemModels(ItemModelGenerators itemModelGenerator) {
		itemModelGenerator.generateItemWithTintedOverlay(
				ModItems.FLORISTS_SHEARS_ITEM.get(),
				"_handle",
				new Dye(DyeColor.RED.getTextureDiffuseColor()));
	}

	@Override
	public String getName() {
		return "FloristsShearsItemModelProvider";
	}
}
