package co.secretonline.tinyflowers.mixin;

import java.util.stream.Stream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import co.secretonline.tinyflowers.items.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;

@Mixin(CreativeModeTabs.class)
public class CreativeModeTabsMixin {

	@Inject(method = "streamAllTabs", at = @At("TAIL"))
	private static void sortStreamAllTabs(CallbackInfoReturnable<Stream<CreativeModeTab>> ci) {
		ci.setReturnValue(ci.getReturnValue().sorted(CreativeModeTabsMixin::compareTabs));
	}

	private static int compareTabs(CreativeModeTab a, CreativeModeTab b) {
		return Boolean.compare(
				b.equals(ModItems.TINY_FLOWERS_GROUP),
				a.equals(ModItems.TINY_FLOWERS_GROUP));
	}
}
