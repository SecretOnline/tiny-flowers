package co.secretonline.tinyflowers.platform;

import co.secretonline.tinyflowers.platform.services.AccessHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public class NeoForgeAccessHelper implements AccessHelper {
	@Override
	public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> entityFactory, Block... blocks) {
		return new BlockEntityType<>(entityFactory::apply, blocks);
	}
}
