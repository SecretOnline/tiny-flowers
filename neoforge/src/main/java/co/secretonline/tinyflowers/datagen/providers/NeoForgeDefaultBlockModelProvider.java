package co.secretonline.tinyflowers.datagen.providers;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.block.ModBlocks;
import co.secretonline.tinyflowers.block.TinyGardenBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.blockstates.Condition;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class NeoForgeDefaultBlockModelProvider extends BlockStateProvider {
	private final static Direction[] DIRECTIONS = new Direction[]{
		Direction.NORTH, Direction.EAST,
		Direction.SOUTH, Direction.WEST,};

	public NeoForgeDefaultBlockModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, TinyFlowers.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		MultiPartBlockStateBuilder gardenBuilder = getMultipartBuilder(ModBlocks.TINY_GARDEN_BLOCK.get());
		for (Direction direction : DIRECTIONS) {
			gardenBuilder
				.part()
				.modelFile(models().getExistingFile(TinyFlowers.id("block/tiny_garden")))
				.addModel()
				.condition(TinyGardenBlock.FACING, direction)
				.end();
		}

		simpleBlock(ModBlocks.TINY_FLOWER_POT_BLOCK.get(),
			models().getExistingFile(TinyFlowers.id("block/tiny_flower_pot")));
	}
}
