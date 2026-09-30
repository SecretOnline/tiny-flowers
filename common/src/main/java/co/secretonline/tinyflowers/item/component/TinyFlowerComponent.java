package co.secretonline.tinyflowers.item.component;

import com.mojang.serialization.Codec;

import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.data.Survivable;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.Util;
import net.minecraft.world.level.LevelReader;

public record TinyFlowerComponent(ResourceLocation id) implements Survivable {
	public String getTranslationKey() {
		return Util.makeDescriptionId("block", this.id());
	}

	@Override
	public boolean canSurviveOn(LevelReader level, BlockPos pos) {
		TinyFlowerData flowerData = TinyFlowerData.findById(level.registryAccess(), id);
		if (flowerData == null) {
			return true;
		}

		return flowerData.canSurviveOn(level, pos);
	}

	public static final Codec<TinyFlowerComponent> CODEC = ResourceLocation.CODEC.xmap(TinyFlowerComponent::new,
			TinyFlowerComponent::id);
}
