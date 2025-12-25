package co.secretonline.tinyflowers.items;

import java.util.Optional;

import org.jspecify.annotations.Nullable;

import co.secretonline.tinyflowers.blocks.ModBlocks;
import co.secretonline.tinyflowers.blocks.TinyGardenBlock;
import co.secretonline.tinyflowers.components.GardenContentsComponent;
import co.secretonline.tinyflowers.components.ModComponents;
import co.secretonline.tinyflowers.components.TinyFlowerComponent;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;

public class TinyFlowerItem extends BlockItem {
	public TinyFlowerItem(Item.Properties properties) {
		super(ModBlocks.TINY_GARDEN_BLOCK, properties);
	}

	@Override
	protected @Nullable BlockState getPlacementState(BlockPlaceContext blockPlaceContext) {
		BlockState newBlockState = super.getPlacementState(blockPlaceContext);
		if (newBlockState == null || newBlockState.isAir()) {
			return newBlockState;
		}

		BlockState currentBlockState = blockPlaceContext.getLevel().getBlockState(blockPlaceContext.getClickedPos());
		Optional<Direction> currentDirection = currentBlockState.getOptionalValue(TinyGardenBlock.FACING);
		Direction newDirection = currentDirection.orElse(blockPlaceContext.getHorizontalDirection().getOpposite());

		return newBlockState.setValue(TinyGardenBlock.FACING, newDirection);
	}

	@Override
	public Component getName(ItemStack itemStack) {
		GardenContentsComponent gardenComponent = itemStack.get(ModComponents.GARDEN_CONTENTS);
		if (gardenComponent != null) {
			return Component.translatable(GardenContentsComponent.GARDEN_TEXT);
		}

		TinyFlowerComponent tinyFlowerComponent = itemStack.get(ModComponents.TINY_FLOWER);
		if (tinyFlowerComponent != null) {
			return Component.translatable(tinyFlowerComponent.getTranslationKey());
		}

		return super.getName(itemStack);
	}
}
