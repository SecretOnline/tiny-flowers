package co.secretonline.tinyflowers.helper;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.TinyFlowersClientState;
import co.secretonline.tinyflowers.item.component.TinyFlowerComponent;
import co.secretonline.tinyflowers.renderer.item.TinyFlowerProperty;
import co.secretonline.tinyflowers.data.TinyFlowerResources;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public final class ItemModelHelper {

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
		return ItemModelUtils.plainModel(id);
	}
}
