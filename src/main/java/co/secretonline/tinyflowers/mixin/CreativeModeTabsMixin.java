package co.secretonline.tinyflowers.mixin;

import java.util.stream.Stream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import co.secretonline.tinyflowers.items.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;

@Mixin(CreativeModeTabs.class)
public class CreativeModeTabsMixin {

	@ModifyReturnValue(method = "streamAllTabs", at = @At("RETURN"))
	private static Stream<CreativeModeTab> sortStreamAllTabs(Stream<CreativeModeTab> tabStream) {
		return tabStream.sorted((a, b) -> Boolean.compare(
				a.equals(ModItems.TINY_FLOWERS_GROUP),
				b.equals(ModItems.TINY_FLOWERS_GROUP)));
	}
}
