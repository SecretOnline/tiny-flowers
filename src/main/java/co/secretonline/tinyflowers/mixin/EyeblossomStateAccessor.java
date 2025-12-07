package co.secretonline.tinyflowers.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.EyeblossomBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EyeblossomBlock.Type.class)
public interface EyeblossomStateAccessor {
	// EyeblossomState has a getter for the long sound (played on random tick) but
	// not for the short sound (played on scheduled tick).
	@Accessor
	SoundEvent getShortSwitchSound();
}
