package co.secretonline.tinyflowers.datagen.providers;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.block.ModBlocks;
import co.secretonline.tinyflowers.block.TinyGardenBlock;
import co.secretonline.tinyflowers.item.ModItems;
import net.minecraft.client.color.item.Dye;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.DyeColor;
import org.jspecify.annotations.NonNull;

public class NeoForgeDefaultModelProvider extends ModelProvider {
	private final static Direction[] DIRECTIONS = new Direction[] {
		Direction.NORTH, Direction.EAST,
		Direction.SOUTH, Direction.WEST, };

	public NeoForgeDefaultModelProvider(PackOutput output) {
		super(output, TinyFlowers.MOD_ID);
	}

	@Override
	protected void registerModels(@NonNull BlockModelGenerators blockModels, @NonNull ItemModelGenerators itemModels) {
		this.generateBlockStateModels(blockModels);
		this.generateItemModels(itemModels);
	}


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

	public void generateItemModels(ItemModelGenerators itemModelGenerator) {
		itemModelGenerator.generateItemWithTintedOverlay(
			ModItems.FLORISTS_SHEARS_ITEM.get(),
			"_handle",
			new Dye(DyeColor.RED.getTextureDiffuseColor()));
	}
}
