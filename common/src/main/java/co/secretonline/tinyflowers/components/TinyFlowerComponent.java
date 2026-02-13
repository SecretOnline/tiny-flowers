package co.secretonline.tinyflowers.components;

import com.mojang.serialization.Codec;

import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.helper.Survivable;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.level.block.Block;

public record TinyFlowerComponent(Identifier id) implements Survivable {
	public String getTranslationKey() {
		return Util.makeDescriptionId("block", this.id());
	}

	@Override
	public boolean canSurviveOn(Block supportingBlock, HolderLookup.Provider provider) {
		TinyFlowerData flowerData = TinyFlowerData.findById(provider, id);
		if (flowerData == null) {
			return true;
		}

		return flowerData.canSurviveOn(supportingBlock);
	}

	public static final Codec<TinyFlowerComponent> CODEC = Identifier.CODEC.xmap(TinyFlowerComponent::new,
			TinyFlowerComponent::id);
}
