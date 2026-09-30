package co.secretonline.tinyflowers.data.behavior;

import co.secretonline.tinyflowers.data.TinyFlowerHolder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

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
public record TransformWeatherBehavior(When when, ResourceLocation turnsInto, Integer particleColor,
																			 Optional<ResourceLocation> soundEventLong,
																			 Optional<ResourceLocation> soundEventShort) implements Behavior {

	@Override
	public boolean shouldActivate(TinyFlowerHolder flowerHolder, int index, BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		return this.when().shouldChange(level, pos);
	}

	@Override
	public void onActivate(TinyFlowerHolder flowerHolder, int index, BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		flowerHolder.setFlower(index, this.turnsInto());
	}

	@Override
	public boolean hasWorldEffect() {
		return !(this.particleColor == 0 &&
			this.soundEventShort.isEmpty() &&
			this.soundEventLong.isEmpty());
	}

	@Override
	public void doWorldEffect(ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, boolean isRandomTick) {
		Optional<ResourceLocation> soundEventId = isRandomTick ? this.soundEventLong : this.soundEventShort;
		soundEventId.flatMap(BuiltInRegistries.SOUND_EVENT::getOptional)
			.ifPresent(event -> serverLevel.playSound(null, blockPos, event, SoundSource.BLOCKS, 1.0F, 1.0F));
	}

	public static final MapCodec<TransformWeatherBehavior> MAP_CODEC = RecordCodecBuilder
		.mapCodec(instance -> instance
			.group(
				When.CODEC.fieldOf("when").forGetter(TransformWeatherBehavior::when),
				ResourceLocation.CODEC.fieldOf("turns_into").forGetter(TransformWeatherBehavior::turnsInto),
				Codec.INT.optionalFieldOf("particle_color", 0).forGetter(TransformWeatherBehavior::particleColor),
				ResourceLocation.CODEC.optionalFieldOf("sound_event_long")
					.forGetter(TransformWeatherBehavior::soundEventLong),
				ResourceLocation.CODEC.optionalFieldOf("sound_event_short")
					.forGetter(TransformWeatherBehavior::soundEventShort))
			.apply(instance, TransformWeatherBehavior::new));

	public MapCodec<TransformWeatherBehavior> getMapCodec() {
		return MAP_CODEC;
	}

	public enum When implements StringRepresentable {
		ALWAYS("always"),
		RAINING("raining"),
		THUNDERING("thundering"),
		RAINING_ON("raining_on"),
		SNOWING_ON("snowing_on");

		private final String name;

		When(String name) {
			this.name = name;
		}

		@Override
		public @NotNull String getSerializedName() {
			return this.name;
		}

		public boolean shouldChange(ServerLevel level, BlockPos pos) {
			if (this.equals(ALWAYS)) {
				return true;
			}

			if ((this.equals(RAINING) && level.isRaining()) ||
				(this.equals(THUNDERING) && level.isThundering())) {
				return true;
			}

			Biome biomeAt = level.getBiome(pos).value();
			Biome.Precipitation weatherAtPos = biomeAt.getPrecipitationAt(pos);
			return (this.equals(RAINING_ON) && weatherAtPos.equals(Biome.Precipitation.RAIN)) ||
				(this.equals(SNOWING_ON) && weatherAtPos.equals(Biome.Precipitation.SNOW));
		}

		public static final Codec<When> CODEC = StringRepresentable.fromEnum(When::values);
	}
}
