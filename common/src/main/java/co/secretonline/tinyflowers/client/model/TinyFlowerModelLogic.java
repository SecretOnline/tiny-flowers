package co.secretonline.tinyflowers.client.model;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.TinyFlowersClientState;
import co.secretonline.tinyflowers.components.TinyFlowerComponent;
import co.secretonline.tinyflowers.items.ModItems;
import co.secretonline.tinyflowers.renderer.item.TinyFlowerProperty;
import co.secretonline.tinyflowers.resources.TinyFlowerResources;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.SimpleModelWrapper;
import net.minecraft.client.renderer.block.model.SingleVariant;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDiscovery;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Shared business logic for tiny flower model loading. Called by common mixins
 * into vanilla's ModelManager and ModelBakery.
 */
public final class TinyFlowerModelLogic {

	private TinyFlowerModelLogic() {
	}

	/**
	 * Creates the select item model for the tiny_flower item.
	 */
	public static ItemModel.Unbaked createTinyFlowerItemModel() {
		List<SelectItemModel.SwitchCase<TinyFlowerComponent>> cases = new ArrayList<>();

		for (var entry : TinyFlowersClientState.RESOURCE_INSTANCES.entrySet()) {
			TinyFlowerResources res = entry.getValue();
			cases.add(ItemModelUtils.when(
					new TinyFlowerComponent(res.id()),
					modelForIdentifier(res.itemModel())));
		}

		return ItemModelUtils.select(
				new TinyFlowerProperty(),
				modelForIdentifier(TinyFlowers.id("item/tiny_garden")),
				cases);
	}

	private static ItemModel.Unbaked modelForIdentifier(Identifier id) {
		return ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(
				id,
				TextureMapping.layer0(id),
				TinyFlowerModelLogic::consumeModel));
	}

	private static void consumeModel(Identifier id, ModelInstance model) {
		// No-op: models are consumed by vanilla's model loading system
	}
}
