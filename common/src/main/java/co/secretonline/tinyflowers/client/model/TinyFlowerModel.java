package co.secretonline.tinyflowers.client.model;

import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.resources.Identifier;

/**
 * A ResolvableModel that marks a single model identifier as a dependency in
 * the model discovery graph. Used to register extra models that aren't
 * referenced by standard blockstate/item JSON files.
 */
public record TinyFlowerModel(Identifier modelId) implements ResolvableModel {
	@Override
	public void resolveDependencies(Resolver resolver) {
		resolver.markDependency(modelId);
	}
}
