package co.secretonline.tinyflowers.data.behavior;

import co.secretonline.tinyflowers.data.TinyFlowerHolder;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;

import co.secretonline.tinyflowers.block.entity.TinyGardenBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public interface Behavior {
	boolean shouldActivate(TinyFlowerHolder flowerHolder, int index, BlockState state, ServerLevel world, BlockPos pos, RandomSource random);

	void onActivate(TinyFlowerHolder flowerHolder, int index, BlockState state, ServerLevel world, BlockPos pos, RandomSource random);

	boolean hasWorldEffect();

	void doWorldEffect(ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, boolean isRandomTick);

	MapCodec<? extends Behavior> getMapCodec();

	Codec<Behavior> CODEC = new LateBoundIdMapper<String, MapCodec<? extends Behavior>>()
		.put("transform_day_night", TransformDayNightBehavior.MAP_CODEC)
		.put("transform_weather", TransformWeatherBehavior.MAP_CODEC)
		.put("sturdy_placement", SturdyPlacementBehavior.MAP_CODEC)
		.codec(Codec.STRING)
		.dispatch(Behavior::getMapCodec, (mapCodec) -> mapCodec);

	class LateBoundIdMapper<I, V> {
		private final BiMap<I, V> idToValue = HashBiMap.create();

		public Codec<V> codec(Codec<I> codec) {
			BiMap<V, I> biMap = this.idToValue.inverse();
			return idResolverCodec(codec, this.idToValue::get, biMap::get);
		}

		public LateBoundIdMapper<I, V> put(I object, V object2) {
			Objects.requireNonNull(object2, () -> "Value for " + object + " is null");
			this.idToValue.put(object, object2);
			return this;
		}

		public Set<V> values() {
			return Collections.unmodifiableSet(this.idToValue.values());
		}

		static <I, E> Codec<E> idResolverCodec(Codec<I> codec, Function<I, @Nullable E> function, Function<E, @Nullable I> function2) {
			return codec.flatXmap(object -> {
				E object2 = (E)function.apply(object);
				return object2 == null ? DataResult.error(() -> "Unknown element id: " + object) : DataResult.success(object2);
			}, object -> {
				I object2 = (I)function2.apply(object);
				return object2 == null ? DataResult.error(() -> "Element with unknown id: " + object) : DataResult.success(object2);
			});
		}
	}
}
