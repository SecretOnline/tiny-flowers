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
	 * Called after resource loading completes. Stores resolved resources and
	 * clears the model registry for the new reload cycle.
	 */
	public static void onResourcesLoaded(Map<Identifier, TinyFlowerResources> resources) {
		TinyFlowersClientState.RESOURCE_INSTANCES = resources;
		TinyFlowerModelHolder.clear();
	}

	/**
	 * Adds all extra model dependencies to the model discovery graph.
	 * Called during model dependency resolution.
	 */
	public static void addModelDependencies(ModelDiscovery discovery) {
		for (var resources : TinyFlowersClientState.RESOURCE_INSTANCES.values()) {
			discovery.addRoot(new TinyFlowerModel(resources.model1()));
			discovery.addRoot(new TinyFlowerModel(resources.model2()));
			discovery.addRoot(new TinyFlowerModel(resources.model3()));
			discovery.addRoot(new TinyFlowerModel(resources.model4()));
		}
	}

	/**
	 * Bakes all registered extra models and stores them in ExtraModelRegistry.
	 * Called after model baking completes.
	 */
	public static void bakeExtraModels(ModelBaker baker) {
		for (var resources : TinyFlowersClientState.RESOURCE_INSTANCES.values()) {
			bakeAndStore(baker, resources.model1());
			bakeAndStore(baker, resources.model2());
			bakeAndStore(baker, resources.model3());
			bakeAndStore(baker, resources.model4());
		}
	}

	private static void bakeAndStore(ModelBaker baker, Identifier modelId) {
		try {
			ResolvedModel resolved = baker.getModel(modelId);
			TextureSlots textures = resolved.getTopTextureSlots();
			BlockStateModel model = new SingleVariant(new SimpleModelWrapper(
					resolved.bakeTopGeometry(textures, baker, BlockModelRotation.IDENTITY),
					resolved.getTopAmbientOcclusion(),
					resolved.resolveParticleSprite(textures, baker)));

			TinyFlowerModelHolder.setModel(modelId, model);
		} catch (Exception e) {
			TinyFlowers.LOGGER.warn("Failed to bake extra model: {}", modelId, e);
		}
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

	public static boolean shouldReplaceItemModel(Identifier itemId) {
		return itemId.equals(ModItems.TINY_FLOWER_ITEM_KEY.identifier());
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
