package co.secretonline.tinyflowers.data.special;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import co.secretonline.tinyflowers.block.entity.TinyGardenBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.TrailParticleOption;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

/**
 * @param when            When this tiny flower will transform into
 *                        another type.
 * @param turnsInto       The identifier of the flower type this one
 *                        will transform into.
 * @param particleColor   Color of particle to spawn when transforming.
 *                        Set to 0 to disable.
 * @param soundEventLong  The sound event to play when the change is
 *                        triggered by a random tick. This is referred
 *                        to as the long switch sound by Minecraft.
 * @param soundEventShort The sound event to play when the change is
 *                        triggered by a scheduled tick. This is
 *                        referred to as the short switch sound by
 *                        Minecraft.
 */
public record TransformWeatherSpecialFeature(When when, Identifier turnsInto, Integer particleColor,
																						 Optional<Identifier> soundEventLong,
																						 Optional<Identifier> soundEventShort) implements SpecialFeature {

	@Override
	public boolean shouldActivateFeature(TinyGardenBlockEntity entity, int index, BlockState state, ServerLevel level,
																			 BlockPos pos, RandomSource random) {
		return this.when().shouldChange(level, pos);
	}

	@Override
	public void onActivateFeature(TinyGardenBlockEntity entity, int index, BlockState state, ServerLevel level,
																BlockPos pos, RandomSource random) {
		entity.setFlower(index, this.turnsInto());
	}

	@Override
	public boolean hasWorldEffect() {
		return !(this.particleColor == 0 &&
			this.soundEventShort.isEmpty() &&
			this.soundEventLong.isEmpty());
	}

	@Override
	public void doWorldEffect(ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource,
														boolean isRandomTick) {
		if (this.particleColor != 0) {
			Vec3 center = blockPos.getCenter();
			double scale = 0.5 + randomSource.nextDouble();
			Vec3 random = new Vec3(randomSource.nextDouble() - 0.5, randomSource.nextDouble() + 1.0,
				randomSource.nextDouble() - 0.5);
			Vec3 position = center.add(random.scale(scale));
			TrailParticleOption trailParticleOption = new TrailParticleOption(position, this.particleColor,
				(int) (20.0 * scale));
			serverLevel.sendParticles(trailParticleOption, center.x, center.y, center.z, 1, 0.0, 0.0, 0.0, 0.0);
		}

		Optional<Identifier> soundEventId = isRandomTick ? this.soundEventLong : this.soundEventShort;
		if (soundEventId.isPresent()) {
			Optional<SoundEvent> soundEvent = BuiltInRegistries.SOUND_EVENT.getOptional(soundEventId.get());
			if (soundEvent.isPresent()) {
				serverLevel.playSound(null, blockPos, soundEvent.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
			}
		}
	}

	public static final MapCodec<TransformWeatherSpecialFeature> MAP_CODEC = RecordCodecBuilder
		.mapCodec(instance -> instance
			.group(
				When.CODEC.fieldOf("when").forGetter(TransformWeatherSpecialFeature::when),
				Identifier.CODEC.fieldOf("turns_into").forGetter(TransformWeatherSpecialFeature::turnsInto),
				Codec.INT.optionalFieldOf("particle_color", 0).forGetter(TransformWeatherSpecialFeature::particleColor),
				Identifier.CODEC.optionalFieldOf("sound_event_long")
					.forGetter(TransformWeatherSpecialFeature::soundEventLong),
				Identifier.CODEC.optionalFieldOf("sound_event_short")
					.forGetter(TransformWeatherSpecialFeature::soundEventShort))
			.apply(instance, TransformWeatherSpecialFeature::new));

	public MapCodec<TransformWeatherSpecialFeature> getMapCodec() {
		return MAP_CODEC;
	}

	public static enum When implements StringRepresentable {
		ALWAYS("always"),
		RAINING("raining"),
		THUNDERING("thundering"),
		RAINING_ON("raining_on"),
		SNOWING_ON("snowing_on");

		private final String name;

		private When(String name) {
			this.name = name;
		}

		@Override
		public @NonNull String getSerializedName() {
			return this.name;
		}

		public boolean shouldChange(ServerLevel level, BlockPos pos) {
			if (!level.canHaveWeather()) {
				return false;
			}

			if (this.equals(ALWAYS)) {
				return true;
			}

			if ((this.equals(RAINING) && level.isRaining()) ||
				(this.equals(THUNDERING) && level.isThundering())) {
				return true;
			}

			Biome.Precipitation weatherAtPos = level.precipitationAt(pos);
			if ((this.equals(RAINING_ON) && weatherAtPos.equals(Biome.Precipitation.RAIN)) ||
				(this.equals(SNOWING_ON) && weatherAtPos.equals(Biome.Precipitation.SNOW))) {
				return true;
			}

			return false;
		}

		public static final Codec<When> CODEC = StringRepresentable.fromEnum(When::values);
	}
}
