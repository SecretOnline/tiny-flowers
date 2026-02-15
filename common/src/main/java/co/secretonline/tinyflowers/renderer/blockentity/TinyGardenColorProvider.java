package co.secretonline.tinyflowers.renderer.blockentity;

import co.secretonline.tinyflowers.data.TinyFlowerResources;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.DryFoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.state.BlockState;

public class TinyGardenColorProvider {
	public static int getColor(@NonNull BlockState state, @Nullable BlockAndTintGetter world, @Nullable BlockPos pos, TinyFlowerResources.TintSource source) {
		// Loosely based on Pink Petals in net.minecraft.client.color.block.BlockColors

		boolean useDefaultColors = world == null || pos == null;

		switch (source) {
			case TinyFlowerResources.TintSource.GRASS -> {
				if (useDefaultColors) {
					return GrassColor.getDefaultColor();
				} else {
					return BiomeColors.getAverageGrassColor(world, pos);
				}
			}
			case TinyFlowerResources.TintSource.DRY_FOLIAGE -> {
				if (useDefaultColors) {
					return DryFoliageColor.get(0.5, 1.0);
				} else {
					return BiomeColors.getAverageDryFoliageColor(world, pos);
				}
			}
			default -> {
				return -1;
			}
		}
	}
}
