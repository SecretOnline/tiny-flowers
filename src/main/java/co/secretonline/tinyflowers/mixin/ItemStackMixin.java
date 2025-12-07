package co.secretonline.tinyflowers.mixin;

import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import co.secretonline.tinyflowers.components.ModComponents;

@Mixin(ItemStack.class)
public class ItemStackMixin {
	@Inject(method = "addDetailsToTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;appendHoverText(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/item/component/TooltipDisplay;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V", shift = At.Shift.AFTER))
	private void injectCustomTooltip(
			Item.TooltipContext context,
			TooltipDisplay displayComponent,
			Player player,
			TooltipFlag type,
			Consumer<Component> textConsumer,
			CallbackInfo ci) {
		((ItemStack) (Object) this).addToTooltip(
				ModComponents.TINY_FLOWERS_COMPONENT_TYPE,
				context,
				displayComponent,
				textConsumer,
				type);
	}
}
