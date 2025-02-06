package co.secretonline.tinyflowers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.EyeblossomBlock;
import net.minecraft.sound.SoundEvent;

@Mixin(EyeblossomBlock.EyeblossomState.class)
public interface EyeblossomStateAccessor {
	// EyeblossomState has a getter for the long sound (played on random tick) but
	// not for the short sound (played on scheduled tick).
	@Accessor
	SoundEvent getSound();
}
