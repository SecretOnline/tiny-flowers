package co.secretonline.tinyflowers.datagen.providers;

import net.minecraft.advancements.Advancement;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.MultiRegistryBootstrap;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.Set;

public class NeoForgeFloristsShearsRecipeProvider extends FloristsShearsRecipeProvider {
	private static final Map<DyeColor, TagKey<Item>> COLOR_TAGS = Map.ofEntries(
		Map.entry(DyeColor.WHITE, Tags.Items.DYES_WHITE),
		Map.entry(DyeColor.ORANGE, Tags.Items.DYES_ORANGE),
		Map.entry(DyeColor.MAGENTA, Tags.Items.DYES_MAGENTA),
		Map.entry(DyeColor.LIGHT_BLUE, Tags.Items.DYES_LIGHT_BLUE),
		Map.entry(DyeColor.YELLOW, Tags.Items.DYES_YELLOW),
		Map.entry(DyeColor.LIME, Tags.Items.DYES_LIME),
		Map.entry(DyeColor.PINK, Tags.Items.DYES_PINK),
		Map.entry(DyeColor.GRAY, Tags.Items.DYES_GRAY),
		Map.entry(DyeColor.LIGHT_GRAY, Tags.Items.DYES_LIGHT_GRAY),
		Map.entry(DyeColor.CYAN, Tags.Items.DYES_CYAN),
		Map.entry(DyeColor.PURPLE, Tags.Items.DYES_PURPLE),
		Map.entry(DyeColor.BLUE, Tags.Items.DYES_BLUE),
		Map.entry(DyeColor.BROWN, Tags.Items.DYES_BROWN),
		Map.entry(DyeColor.GREEN, Tags.Items.DYES_GREEN),
		Map.entry(DyeColor.RED, Tags.Items.DYES_RED),
		Map.entry(DyeColor.BLACK, Tags.Items.DYES_BLACK));

	public NeoForgeFloristsShearsRecipeProvider(@NonNull BootstrapContext<Recipe<?>> recipes, @NonNull BootstrapContext<Advancement> advancements) {
		super(recipes, advancements, COLOR_TAGS);
	}

	public static MultiRegistryBootstrap create() {
		return new MultiRegistryBootstrap() {
			@Override
			public Set<ResourceKey<? extends Registry<?>>> requestedRegistries() {
				// Return the registries we are adding entries to.
				return Set.of(Registries.RECIPE, Registries.ADVANCEMENT);
			}

			@Override
			public void run(MultiRegistryBootstrap.BootstrapGetter registries) {
				// Run the recipe provider.
				new NeoForgeFloristsShearsRecipeProvider(registries.get(Registries.RECIPE), registries.get(Registries.ADVANCEMENT)).buildRecipes();
			}
		};
	}
}
