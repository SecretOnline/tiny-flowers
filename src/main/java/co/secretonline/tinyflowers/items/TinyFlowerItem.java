package co.secretonline.tinyflowers.items;

// import co.secretonline.tinyflowers.blocks.ModBlocks;S
import co.secretonline.tinyflowers.components.ModComponents;
import co.secretonline.tinyflowers.components.TinyFlowerComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class TinyFlowerItem extends BlockItem {
	public TinyFlowerItem(Item.Properties properties) {
		// super(ModBlocks.TINY_GARDEN, properties);
		super(Blocks.DIRT, properties);
	}

	@Override
	public Component getName(ItemStack itemStack) {
		TinyFlowerComponent tinyFlowerComponent = itemStack.getComponents().get(ModComponents.TINY_FLOWER);
		if (tinyFlowerComponent == null) {
			return super.getName(itemStack);
		}

		return Component.translatable(tinyFlowerComponent.getTranslationKey());
	}
}
