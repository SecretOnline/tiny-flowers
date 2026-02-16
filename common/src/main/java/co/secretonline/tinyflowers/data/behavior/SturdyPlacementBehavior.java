package co.secretonline.tinyflowers.data.behavior;

import co.secretonline.tinyflowers.block.entity.TinyGardenBlockEntity;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @param isReallyCool Does nothing, is only here because I couldn't be bothered messing with Codecs.
 */
public record SturdyPlacementBehavior(boolean isReallyCool) implements Behavior {
	@Override
	public boolean shouldActivate(TinyGardenBlockEntity entity, int index, BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		return false;
	}

	@Override
	public void onActivate(TinyGardenBlockEntity entity, int index, BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
	}

	@Override
	public boolean hasWorldEffect() {
		return false;
	}

	@Override
	public void doWorldEffect(ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, boolean isRandomTick) {
	}

	public static final MapCodec<SturdyPlacementBehavior> MAP_CODEC = RecordCodecBuilder
		.mapCodec(instance -> instance
			.group(
				Codec.BOOL.optionalFieldOf("is_really_cool", true).forGetter(SturdyPlacementBehavior::isReallyCool))
			.apply(instance, SturdyPlacementBehavior::new));

	public MapCodec<SturdyPlacementBehavior> getMapCodec() {
		return MAP_CODEC;
	}
}
