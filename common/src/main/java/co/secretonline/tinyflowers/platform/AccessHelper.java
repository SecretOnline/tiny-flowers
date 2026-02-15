package co.secretonline.tinyflowers.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

/**
 * Some Minecraft methods are private, but loaders have their own ways of achieving the same thing.
 */
public interface AccessHelper {
	<T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> entityFactory, Block... blocks);
}
