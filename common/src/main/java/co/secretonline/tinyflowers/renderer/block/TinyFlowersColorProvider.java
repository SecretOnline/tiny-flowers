package co.secretonline.tinyflowers.renderer.block;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.state.BlockState;

public class TinyFlowersColorProvider {
	public static int getAverageBiomeColor(BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex) {
		boolean hasLevel = level == null || pos == null;

		switch (tintIndex) {
			case 1 -> {
				if (hasLevel) {
					return GrassColor.getDefaultColor();
				} else {
					return BiomeColors.getAverageGrassColor(level, pos);
				}
			}
			default -> {
				return -1;
			}
		}
	}
}
