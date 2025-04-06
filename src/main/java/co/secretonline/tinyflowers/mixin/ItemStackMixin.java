package co.secretonline.tinyflowers.mixin;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import co.secretonline.tinyflowers.components.ModComponents;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

@Mixin(ItemStack.class)
public class ItemStackMixin {
	@Inject(method = "appendTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;appendTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/Item$TooltipContext;Lnet/minecraft/component/type/TooltipDisplayComponent;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V", shift = At.Shift.AFTER))
	private void injectCustomTooltip(
			Item.TooltipContext context,
			TooltipDisplayComponent displayComponent,
			PlayerEntity player,
			TooltipType type,
			Consumer<Text> textConsumer,
			CallbackInfo ci) {
		((ItemStack) (Object) this).appendComponentTooltip(
				ModComponents.TINY_FLOWERS_COMPONENT_TYPE,
				context,
				displayComponent,
				textConsumer,
				type);
	}
}
