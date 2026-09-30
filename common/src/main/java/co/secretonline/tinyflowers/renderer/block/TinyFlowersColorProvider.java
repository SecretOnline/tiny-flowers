package co.secretonline.tinyflowers.renderer.block;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.DryFoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class TinyFlowersColorProvider {
	public static int getAverageBiomeColor(@NonNull BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int tintIndex) {
		boolean hasLevel = level == null || pos == null;

		switch (tintIndex) {
			case 1 -> {
				if (hasLevel) {
					return GrassColor.getDefaultColor();
				} else {
					return BiomeColors.getAverageGrassColor(level, pos);
				}
			}
			case 2 -> {
				if (hasLevel) {
					return DryFoliageColor.get(0.5, 1.0);
				} else {
					return BiomeColors.getAverageDryFoliageColor(level, pos);
				}
			}
			default -> {
				return -1;
			}
		}
	}
}
