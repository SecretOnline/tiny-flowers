package co.secretonline.tinyflowers.item.component;

import com.mojang.serialization.Codec;

import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.helper.Survivable;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.level.LevelReader;

public record TinyFlowerComponent(Identifier id) implements Survivable {
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

	public static final Codec<TinyFlowerComponent> CODEC = Identifier.CODEC.xmap(TinyFlowerComponent::new,
			TinyFlowerComponent::id);
}
