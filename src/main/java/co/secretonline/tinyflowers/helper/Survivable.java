package co.secretonline.tinyflowers.helper;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;

public interface Survivable {
	public boolean canSurviveOn(Block supportingBlock, HolderLookup.Provider provider);
}
