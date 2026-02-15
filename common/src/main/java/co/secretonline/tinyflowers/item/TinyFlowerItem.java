package co.secretonline.tinyflowers.item;

import co.secretonline.tinyflowers.block.ModBlocks;
import co.secretonline.tinyflowers.block.TinyGardenBlock;
import co.secretonline.tinyflowers.item.component.GardenContentsComponent;
import co.secretonline.tinyflowers.item.component.ModComponents;
import co.secretonline.tinyflowers.item.component.TinyFlowerComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class TinyFlowerItem extends BlockItem {
	public TinyFlowerItem(Item.Properties properties) {
		super(ModBlocks.TINY_GARDEN_BLOCK.get(), properties);
	}

	@Override
	protected @Nullable BlockState getPlacementState(@NonNull BlockPlaceContext blockPlaceContext) {
		BlockState newBlockState = super.getPlacementState(blockPlaceContext);
		if (newBlockState == null || newBlockState.isAir()) {
			return null;
		}

		if (!this.checkSupportingBlock(blockPlaceContext)) {
			return null;
		}

		BlockState currentBlockState = blockPlaceContext.getLevel().getBlockState(blockPlaceContext.getClickedPos());
		Optional<Direction> currentDirection = currentBlockState.getOptionalValue(TinyGardenBlock.FACING);
		Direction newDirection = currentDirection.orElse(blockPlaceContext.getHorizontalDirection().getOpposite());

		return newBlockState.setValue(TinyGardenBlock.FACING, newDirection);
	}

	private boolean checkSupportingBlock(BlockPlaceContext context) {
		Level level = context.getLevel();
		RegistryAccess registryAccess = level.registryAccess();
		ItemStack itemStack = context.getItemInHand();
		BlockPos supportingPos = context.getClickedPos().below();
		BlockState supportingBlockState = level.getBlockState(supportingPos);

		GardenContentsComponent gardenComponent = itemStack.get(ModComponents.GARDEN_CONTENTS.get());
		if (gardenComponent != null) {
			return gardenComponent.canSurviveOn(supportingBlockState, level, supportingPos);
		}

		TinyFlowerComponent tinyFlowerComponent = itemStack.get(ModComponents.TINY_FLOWER.get());
		if (tinyFlowerComponent != null) {
			return tinyFlowerComponent.canSurviveOn(supportingBlockState, level, supportingPos);
		}

		return false;
	}

	@Override
	public @NonNull Component getName(ItemStack itemStack) {
		GardenContentsComponent gardenComponent = itemStack.get(ModComponents.GARDEN_CONTENTS.get());
		if (gardenComponent != null) {
			return Component.translatable(GardenContentsComponent.GARDEN_TEXT);
		}

		TinyFlowerComponent tinyFlowerComponent = itemStack.get(ModComponents.TINY_FLOWER.get());
		if (tinyFlowerComponent != null) {
			return Component.translatable(tinyFlowerComponent.getTranslationKey());
		}

		return super.getName(itemStack);
	}
}
