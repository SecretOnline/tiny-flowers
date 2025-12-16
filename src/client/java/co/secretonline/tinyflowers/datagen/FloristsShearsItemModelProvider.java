package co.secretonline.tinyflowers.datagen;

import co.secretonline.tinyflowers.items.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.color.item.Dye;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.world.item.DyeColor;

public class FloristsShearsItemModelProvider extends FabricModelProvider {

	public FloristsShearsItemModelProvider(FabricDataOutput generator) {
		super(generator);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
	}

	@Override
	public void generateItemModels(ItemModelGenerators itemModelGenerator) {
		itemModelGenerator.generateItemWithTintedOverlay(
				ModItems.FLORISTS_SHEARS_ITEM,
				"_handle",
				new Dye(DyeColor.RED.getTextureDiffuseColor()));
	}

	@Override
	public String getName() {
		return "FloristsShearsItemModelProvider";
	}
}
