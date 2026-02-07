package co.secretonline.tinyflowers.data.special;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import net.minecraft.util.ExtraCodecs;

public interface SpecialFeature {
	abstract MapCodec<? extends SpecialFeature> getMapCodec();

	public static final Codec<SpecialFeature> CODEC = new ExtraCodecs.LateBoundIdMapper<String, MapCodec<? extends SpecialFeature>>()
			.put("transform_day_night", TransformDayNightSpecialFeature.MAP_CODEC)
			.codec(Codec.STRING)
			.dispatch(SpecialFeature::getMapCodec, (mapCodec) -> mapCodec);
}
