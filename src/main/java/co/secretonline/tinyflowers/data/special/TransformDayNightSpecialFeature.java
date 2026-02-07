package co.secretonline.tinyflowers.data.special;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.TrailParticleOption;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.TriState;
import net.minecraft.world.phys.Vec3;

/**
 * @param when                   When this tiny flower will transform into
 *                               another type.
 * @param turnsInto              The identifier of the flower type this one
 *                               will transform into.
 * @param transformParticleColor Color of particle to spawn when transforming.
 *                               Set to 0 to disable.
 * @param soundEventLong         The sound event to play when the change is
 *                               triggered by a random tick. This is referred
 *                               to as the long switch sound by Minecraft.
 * @param soundEventShort        The sound event to play when the change is
 *                               triggered by a scheduled tick. This is
 *                               referred to as the short switch sound by
 *                               Minecraft.
 */
public record TransformDayNightSpecialFeature(When when, Identifier turnsInto, Integer particleColor,
		Optional<Identifier> soundEventLong,
		Optional<Identifier> soundEventShort) implements SpecialFeature {

	public void spawnTransformParticle(ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
		if (this.particleColor == 0) {
			return;
		}

		Vec3 center = blockPos.getCenter();
		double scale = 0.5 + randomSource.nextDouble();
		Vec3 random = new Vec3(randomSource.nextDouble() - 0.5, randomSource.nextDouble() + 1.0,
				randomSource.nextDouble() - 0.5);
		Vec3 position = center.add(random.scale(scale));
		TrailParticleOption trailParticleOption = new TrailParticleOption(position, this.particleColor,
				(int) (20.0 * scale));
		serverLevel.sendParticles(trailParticleOption, center.x, center.y, center.z, 1, 0.0, 0.0, 0.0, 0.0);
	}

	public void playSound(ServerLevel serverLevel, BlockPos blockPos, boolean isLongSound) {
		Optional<Identifier> soundEventId = isLongSound ? this.soundEventLong : this.soundEventShort;
		if (soundEventId.isEmpty()) {
			return;
		}

		Optional<SoundEvent> soundEvent = BuiltInRegistries.SOUND_EVENT.getOptional(soundEventId.get());
		if (soundEvent.isEmpty()) {
			return;
		}

		serverLevel.playSound(null, blockPos, soundEvent.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
	}

	public static final MapCodec<TransformDayNightSpecialFeature> MAP_CODEC = RecordCodecBuilder
			.mapCodec(instance -> instance
					.group(
							When.CODEC.fieldOf("when").forGetter(TransformDayNightSpecialFeature::when),
							Identifier.CODEC.fieldOf("turns_into").forGetter(TransformDayNightSpecialFeature::turnsInto),
							Codec.INT.optionalFieldOf("particle_color", 0).forGetter(TransformDayNightSpecialFeature::particleColor),
							Identifier.CODEC.optionalFieldOf("sound_event_long")
									.forGetter(TransformDayNightSpecialFeature::soundEventLong),
							Identifier.CODEC.optionalFieldOf("sound_event_short")
									.forGetter(TransformDayNightSpecialFeature::soundEventShort))
					.apply(instance, TransformDayNightSpecialFeature::new));

	public MapCodec<TransformDayNightSpecialFeature> getMapCodec() {
		return MAP_CODEC;
	}

	public static enum When implements StringRepresentable {
		ALWAYS("always", TriState.DEFAULT),
		DAY("day", TriState.FALSE),
		NIGHT("night", TriState.TRUE);

		private final String name;
		private final TriState eyeblossomOpen;

		private When(String name, TriState eyeblossomOpen) {
			this.name = name;
			this.eyeblossomOpen = eyeblossomOpen;
		}

		@Override
		public String getSerializedName() {
			return this.name;
		}

		public boolean shouldChange(TriState eyeblossomOpen) {
			if (this.equals(ALWAYS)) {
				return true;
			}
			if (eyeblossomOpen.equals(TriState.DEFAULT)) {
				return false;
			}

			return this.eyeblossomOpen.equals(eyeblossomOpen);
		}

		public static final Codec<When> CODEC = StringRepresentable.fromEnum(When::values);
	}
}
