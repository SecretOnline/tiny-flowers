package co.secretonline.tinyflowers.datagen.providers;

/**
 * Minecraft does validation to ensure that model providers provide models for all variations (items, blocks, block states).
 * Fabric has a mixin to skip entries not added by the mod, or to skip altogether if not in some strict mode.
 * NeoForge has no such mixin, so this interface is here to provide an opportunity to skip that validation.
 */
public interface PartialModelProvider {
	boolean shouldValidateAllEntries();
}
