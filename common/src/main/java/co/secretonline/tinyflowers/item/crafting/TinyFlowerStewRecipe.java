package co.secretonline.tinyflowers.item.crafting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mojang.serialization.MapCodec;

import co.secretonline.tinyflowers.item.component.ModComponents;
import co.secretonline.tinyflowers.item.component.TinyFlowerComponent;
import co.secretonline.tinyflowers.data.ModRegistries;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.item.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.item.component.SuspiciousStewEffects.Entry;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class TinyFlowerStewRecipe extends CustomRecipeWithProvider {
	public static final TinyFlowerStewRecipe INSTANCE = new TinyFlowerStewRecipe();
	public static final MapCodec<TinyFlowerStewRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
	public static final StreamCodec<RegistryFriendlyByteBuf, TinyFlowerStewRecipe> STREAM_CODEC = StreamCodec
		.unit(INSTANCE);
	public static final RecipeSerializer<TinyFlowerStewRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC,
		STREAM_CODEC);

	@Override
	public boolean matches(CraftingInput recipeInput, @NonNull Level level) {
		// Quick size check, since the recipe needs a bowl, the two shrooms, and at
		// least one tiny flower.
		if (recipeInput.ingredientCount() < 4) {
			return false;
		}

		boolean hasBowl = false;
		boolean hasBrownMushroom = false;
		boolean hasRedMushroom = false;
		boolean hasAtLeastOneTinyFlower = false;

		for (ItemStack itemStack : recipeInput.items()) {
			// Ensure exactly one of each of bowl, red shroom, and brown shroom are in the
			// list.
			if (itemStack.isEmpty()) {
				continue;
			}

			if (itemStack.is(Items.BOWL)) {
				if (hasBowl) {
					return false;
				}

				hasBowl = true;
				continue;
			}
			if (itemStack.is(Items.BROWN_MUSHROOM)) {
				if (hasBrownMushroom) {
					return false;
				}

				hasBrownMushroom = true;
				continue;
			}
			if (itemStack.is(Items.RED_MUSHROOM)) {
				if (hasRedMushroom) {
					return false;
				}

				hasRedMushroom = true;
				continue;
			}

			if (!itemStack.is(ModItems.TINY_FLOWER_ITEM.get())) {
				return false;
			}

			hasAtLeastOneTinyFlower = true;
		}

		return hasBowl && hasBrownMushroom && hasRedMushroom && hasAtLeastOneTinyFlower;
	}

	@Override
	public ItemStack assemble(CraftingInput recipeInput, Provider provider) {
		RegistryLookup<TinyFlowerData> registry = provider.lookupOrThrow(ModRegistries.TINY_FLOWER);
		Map<Holder<MobEffect>, Integer> effectMap = new HashMap<>();

		for (ItemStack itemStack : recipeInput.items()) {
			if (!itemStack.is(ModItems.TINY_FLOWER_ITEM.get())) {
				continue;
			}

			TinyFlowerComponent tinyFlowerComponent = itemStack.get(ModComponents.TINY_FLOWER.get());
			if (tinyFlowerComponent == null) {
				continue;
			}

			Optional<Reference<TinyFlowerData>> result = registry.get(
				ResourceKey.create(ModRegistries.TINY_FLOWER, tinyFlowerComponent.id()));
			if (result.isEmpty()) {
				continue;
			}

			TinyFlowerData tinyFlowerData = result.get().value();
			for (Entry entry : tinyFlowerData.getSuspiciousEffects().effects()) {
				effectMap.merge(entry.effect(), entry.duration(), Integer::sum);
			}
		}

		List<Entry> effectList = effectMap.entrySet()
			.stream()
			.map((entry) -> new Entry(entry.getKey(), entry.getValue()))
			.toList();
		SuspiciousStewEffects effects = new SuspiciousStewEffects(effectList);

		ItemStack output = new ItemStack(Items.SUSPICIOUS_STEW);
		output.applyComponents(DataComponentPatch.builder()
			.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, effects)
			.build());

		return output;
	}

	@Override
	public @NonNull RecipeSerializer<TinyFlowerStewRecipe> getSerializer() {
		return ModRecipeSerializers.TINY_FLOWER_STEW.get();
	}
}
