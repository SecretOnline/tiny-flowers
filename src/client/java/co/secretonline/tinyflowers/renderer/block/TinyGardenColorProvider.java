package co.secretonline.tinyflowers.renderer.block;

import org.jspecify.annotations.Nullable;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.DryFoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.state.BlockState;

public class TinyGardenColorProvider implements BlockColor {
	private static TinyGardenColorProvider INSTANCE = null;

	public static TinyGardenColorProvider getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TinyGardenColorProvider();
		}

		return INSTANCE;
	}

	@Override
	public int getColor(BlockState state, @Nullable BlockAndTintGetter world, @Nullable BlockPos pos, int tintIndex) {
		// Loosely based on Pink Petals in net.minecraft.client.color.block.BlockColors

		boolean useDefaultColors = world == null || pos == null;

		switch (tintIndex) {
			case 1 -> {
				if (useDefaultColors) {
					return GrassColor.getDefaultColor();
				} else {
					return BiomeColors.getAverageGrassColor(world, pos);
				}
			}
			case 2 -> {
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
