package co.secretonline.tinyflowers.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;

public interface Survivable {
	boolean canSurviveOn(LevelReader level, BlockPos pos);
}
