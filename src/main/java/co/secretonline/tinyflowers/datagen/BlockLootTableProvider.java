package co.secretonline.tinyflowers.datagen;

import java.util.concurrent.CompletableFuture;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.blocks.FlowerVariant;
import co.secretonline.tinyflowers.blocks.GardenBlock;
import co.secretonline.tinyflowers.blocks.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

public class BlockLootTableProvider extends FabricBlockLootTableProvider {
	protected BlockLootTableProvider(FabricDataOutput dataOutput,
			CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(dataOutput, registryLookup);
	}

	@Override
	public void generate() {
		LootTable.Builder lootTableBuilder = LootTable.builder();

		for (FlowerVariant variant : FlowerVariant.values()) {
			if (variant.isEmpty()) {
				continue;
			}

			ItemConvertible item = variant.asItem();
			if (Registries.ITEM.getId(item.asItem()).equals(Identifier.of("air"))) {
				TinyFlowers.LOGGER.error(
						"Variant {} has an invalid item id: {}",
						variant.getItemIdentifier(), Registries.ITEM.getId(item.asItem()));
			}

			for (var property : GardenBlock.FLOWER_VARIANT_PROPERTIES) {
				lootTableBuilder.pool(LootPool.builder()
						.with(ItemEntry.builder(item))
						.conditionally(BlockStatePropertyLootCondition.builder(ModBlocks.TINY_GARDEN)
								.properties(
										StatePredicate.Builder.create().exactMatch(property, variant))));
			}
		}

		addDrop(ModBlocks.TINY_GARDEN, lootTableBuilder);
	}
}
