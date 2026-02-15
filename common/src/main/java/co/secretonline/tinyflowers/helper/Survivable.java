package co.secretonline.tinyflowers.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

public interface Survivable {
	boolean canSurviveOn(BlockState state, LevelReader level, BlockPos pos);
}
