package co.secretonline.tinyflowers.data.behavior;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import co.secretonline.tinyflowers.block.entity.TinyGardenBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public interface Behavior {
	boolean shouldActivate(TinyGardenBlockEntity entity, int index, BlockState state, ServerLevel world, BlockPos pos, RandomSource random);

	void onActivate(TinyGardenBlockEntity entity, int index, BlockState state, ServerLevel world, BlockPos pos, RandomSource random);

	boolean hasWorldEffect();

	void doWorldEffect(ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, boolean isRandomTick);

	MapCodec<? extends Behavior> getMapCodec();

	Codec<Behavior> CODEC = new ExtraCodecs.LateBoundIdMapper<String, MapCodec<? extends Behavior>>()
		.put("transform_day_night", TransformDayNightBehavior.MAP_CODEC)
		.put("transform_weather", TransformWeatherBehavior.MAP_CODEC)
		.put("sturdy_placement", SturdyPlacementBehavior.MAP_CODEC)
		.codec(Codec.STRING)
		.dispatch(Behavior::getMapCodec, (mapCodec) -> mapCodec);
}
