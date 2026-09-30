package co.secretonline.tinyflowers.mixin.item;

import co.secretonline.tinyflowers.item.ModItems;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.component.DyedItemColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemColors.class)
public class ItemColorsMixin {
	@Unique
	private static final int DEFAULT_COLOR = DyeColor.RED.getTextureDiffuseColor();

	@WrapMethod(method = "createDefault")
	private static ItemColors tinyFlowers$createDefault(BlockColors colors, Operation<ItemColors> original) {
		ItemColors itemColors = original.call(colors);

		itemColors.register((itemStack, layer) -> layer == 0 ? -1 : DyedItemColor.getOrDefault(itemStack, DEFAULT_COLOR), ModItems.FLORISTS_SHEARS_ITEM.get());

		return itemColors;
	}
}
