package co.secretonline.tinyflowers.data.special;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import co.secretonline.tinyflowers.block.entity.TinyGardenBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public interface SpecialFeature {
	abstract boolean shouldActivateFeature(TinyGardenBlockEntity entity, int index, BlockState state, ServerLevel world,
			BlockPos pos, RandomSource random);

	abstract void onActivateFeature(TinyGardenBlockEntity entity, int index, BlockState state, ServerLevel world,
			BlockPos pos, RandomSource random);

	abstract boolean hasWorldEffect();

	abstract void doWorldEffect(ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource,
			boolean isRandomTick);

	abstract MapCodec<? extends SpecialFeature> getMapCodec();

	public static final Codec<SpecialFeature> CODEC = new ExtraCodecs.LateBoundIdMapper<String, MapCodec<? extends SpecialFeature>>()
			.put("transform_day_night", TransformDayNightSpecialFeature.MAP_CODEC)
			.put("transform_weather", TransformWeatherSpecialFeature.MAP_CODEC)
			.codec(Codec.STRING)
			.dispatch(SpecialFeature::getMapCodec, (mapCodec) -> mapCodec);
}
