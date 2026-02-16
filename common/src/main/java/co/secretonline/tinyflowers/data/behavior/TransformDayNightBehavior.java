package co.secretonline.tinyflowers.data.behavior;

import co.secretonline.tinyflowers.block.entity.TinyGardenBlockEntity;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.TrailParticleOption;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.TriState;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

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
public record TransformDayNightBehavior(When when, Identifier turnsInto, Integer particleColor,
																				Optional<Identifier> soundEventLong,
																				Optional<Identifier> soundEventShort) implements Behavior {

	@Override
	public boolean shouldActivate(TinyGardenBlockEntity entity, int index, BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		TriState openTriState = level.environmentAttributes().getValue(EnvironmentAttributes.EYEBLOSSOM_OPEN, pos);
		if (openTriState == TriState.DEFAULT) {
			return false;
		}

		return this.when().shouldChange(openTriState);
	}

	@Override
	public void onActivate(TinyGardenBlockEntity entity, int index, BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		entity.setFlower(index, this.turnsInto());
	}

	@Override
	public boolean hasWorldEffect() {
		return !(this.particleColor == 0 &&
			this.soundEventShort.isEmpty() &&
			this.soundEventLong.isEmpty());
	}

	@Override
	public void doWorldEffect(ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, boolean isRandomTick) {
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
		soundEventId.flatMap(BuiltInRegistries.SOUND_EVENT::getOptional)
			.ifPresent(event -> serverLevel.playSound(null, blockPos, event, SoundSource.BLOCKS, 1.0F, 1.0F));
	}

	public static final MapCodec<TransformDayNightBehavior> MAP_CODEC = RecordCodecBuilder
		.mapCodec(instance -> instance
			.group(
				When.CODEC.fieldOf("when").forGetter(TransformDayNightBehavior::when),
				Identifier.CODEC.fieldOf("turns_into").forGetter(TransformDayNightBehavior::turnsInto),
				Codec.INT.optionalFieldOf("particle_color", 0).forGetter(TransformDayNightBehavior::particleColor),
				Identifier.CODEC.optionalFieldOf("sound_event_long")
					.forGetter(TransformDayNightBehavior::soundEventLong),
				Identifier.CODEC.optionalFieldOf("sound_event_short")
					.forGetter(TransformDayNightBehavior::soundEventShort))
			.apply(instance, TransformDayNightBehavior::new));

	public MapCodec<TransformDayNightBehavior> getMapCodec() {
		return MAP_CODEC;
	}

	public enum When implements StringRepresentable {
		ALWAYS("always", TriState.DEFAULT),
		DAY("day", TriState.FALSE),
		NIGHT("night", TriState.TRUE);

		private final String name;
		private final TriState eyeblossomOpen;

		When(String name, TriState eyeblossomOpen) {
			this.name = name;
			this.eyeblossomOpen = eyeblossomOpen;
		}

		@Override
		public @NonNull String getSerializedName() {
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
