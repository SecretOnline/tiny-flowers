package co.secretonline.tinyflowers.datagen.providers;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class NeoForgeDefaultItemModelProvider extends ItemModelProvider {
	public NeoForgeDefaultItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, TinyFlowers.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		withExistingParent(ModelLocationUtils.getModelLocation(ModItems.TINY_FLOWER_ITEM.get()).getPath(), mcLoc("item/generated"))
			.texture("layer0", "item/tiny_garden");

		withExistingParent(ModelLocationUtils.getModelLocation(ModItems.FLORISTS_SHEARS_ITEM.get()).getPath(), mcLoc("item/generated"))
			.texture("layer0", "item/florists_shears")
			.texture("layer1", "item/florists_shears_handle");
	}
}
