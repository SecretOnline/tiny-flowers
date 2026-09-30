package co.secretonline.tinyflowers.datagen.providers;

import co.secretonline.tinyflowers.datagen.mods.FlowerProvider;
import co.secretonline.tinyflowers.helper.DataHelper;
import com.google.gson.JsonElement;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class NeoForgeModModelProvider implements DataProvider {
	private final FlowerProvider modData;
	PackOutput.PathProvider blockStatePathProvider;
	PackOutput.PathProvider modelPathProvider;

	public NeoForgeModModelProvider(FlowerProvider modData, PackOutput output) {
		this.blockStatePathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
		this.modelPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");

		this.modData = modData;
	}

	@Override
	public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cachedOutput) {
		Map<ResourceLocation, Supplier<JsonElement>> blockModelMap = new HashMap<>();
		this.modData.generateBlockStateModels(blockModelMap::put);

		Map<ResourceLocation, Supplier<JsonElement>> itemModelMap = new HashMap<>();
		this.modData.generateItemModels(itemModelMap::put);

		return CompletableFuture.allOf(
			DataHelper.saveAll(cachedOutput, Supplier::get, modelPathProvider::json, blockModelMap),
			DataHelper.saveAll(cachedOutput, Supplier::get, modelPathProvider::json, itemModelMap));
	}

	@Override
	public @NotNull String getName() {
		return "Mod models provider [" + this.modData.getModId() + "]";
	}
}
