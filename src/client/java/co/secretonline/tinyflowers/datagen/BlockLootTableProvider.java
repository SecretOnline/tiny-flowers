package co.secretonline.tinyflowers.datagen;

import java.util.concurrent.CompletableFuture;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.blocks.FlowerVariant;
import co.secretonline.tinyflowers.blocks.GardenBlock;
import co.secretonline.tinyflowers.blocks.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;

public class BlockLootTableProvider extends FabricBlockLootTableProvider {
	protected BlockLootTableProvider(FabricDataOutput dataOutput,
			CompletableFuture<HolderLookup.Provider> registryLookup) {
		super(dataOutput, registryLookup);
	}

	@Override
	public void generate() {
		LootTable.Builder lootTableBuilder = LootTable.lootTable();

		for (FlowerVariant variant : FlowerVariant.values()) {
			if (variant.isEmpty()) {
				continue;
			}

			Item item = variant.asItem();
			if (BuiltInRegistries.ITEM.getKey(item).equals(ResourceLocation.parse("air"))) {
				TinyFlowers.LOGGER.error(
						"Variant {} has an invalid item id: {}",
						variant.getItemIdentifier(), BuiltInRegistries.ITEM.getKey(item));
			}

			for (var property : GardenBlock.FLOWER_VARIANT_PROPERTIES) {
				lootTableBuilder.withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(item))
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.TINY_GARDEN)
								.setProperties(
										StatePropertiesPredicate.Builder.properties().hasProperty(property, variant))));
			}
		}

		add(ModBlocks.TINY_GARDEN, lootTableBuilder);
	}
}
