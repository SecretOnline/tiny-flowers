package co.secretonline.tinyflowers.items;

import java.util.Arrays;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SegmentableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import net.minecraft.world.phys.Vec3;
import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.blocks.FlowerVariant;
import co.secretonline.tinyflowers.blocks.GardenBlock;
import co.secretonline.tinyflowers.blocks.ModBlocks;

public class FloristsShearsItem extends ShearsItem {
	private final static Direction[] DIRECTIONS = new Direction[] {
			Direction.NORTH, Direction.EAST,
			Direction.SOUTH, Direction.WEST, };

	public FloristsShearsItem(Properties settings) {
		super(settings);
	}

	@Override
	public ItemStack getRecipeRemainder(ItemStack stack) {
		if (stack.getDamageValue() < stack.getMaxDamage() - 1) {
			ItemStack moreDamaged = stack.copy();
			moreDamaged.setDamageValue(stack.getDamageValue() + 1);
			return moreDamaged;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		BlockState blockState = world.getBlockState(pos);

		if (blockState.getBlock() instanceof SegmentableBlock) {
			// Try convert segmented block to gardens so that shears can remove flowers from
			// them.
			try {
				blockState = ((GardenBlock) ModBlocks.TINY_GARDEN).getStateFromSegmented(blockState);
			} catch (IllegalStateException ex) {
				// Segmented could not be converted to garden.
				TinyFlowers.LOGGER.warn("Could not convert segmented block to garden. Ignoring action.");

				return InteractionResult.TRY_WITH_EMPTY_HAND;
			}
		}

		if (blockState.is(ModBlocks.TINY_GARDEN)) {
			// Remove flower at certain part of garden.
			Vec3 positionInBlock = ctx.getClickLocation().subtract(Vec3.atLowerCornerOf(pos));
			boolean isEast = positionInBlock.x >= 0.5;
			boolean isSouth = positionInBlock.z >= 0.5;

			// Convert block quadrant into the correct property.
			// Writing this was a little bit of trial and a lot of error.
			int index = isSouth ? (isEast ? 2 : 3) : (isEast ? 1 : 0);
			index = Arrays.asList(DIRECTIONS).indexOf(blockState.getValue(GardenBlock.FACING)) - index;
			index = (index + 4) % 4;
			EnumProperty<FlowerVariant> property = GardenBlock.FLOWER_VARIANT_PROPERTIES[index];

			FlowerVariant variant = blockState.getValue(property);
			if (variant.isEmpty()) {
				return InteractionResult.TRY_WITH_EMPTY_HAND;
			}

			Block.popResource(world, pos, new ItemStack(variant));

			// TODO: Figure out if there's a scenario where the player is null
			if (ctx.getPlayer() != null) {
				Player player = ctx.getPlayer();
				ctx.getItemInHand().hurtAndBreak(1, player, ctx.getHand());

				world.playSound(player, pos, SoundEvents.GROWING_PLANT_CROP, SoundSource.BLOCKS, 1.0F, 1.0F);
			}

			BlockState newBlockState = blockState.setValue(property, FlowerVariant.EMPTY);
			if (GardenBlock.isEmpty(newBlockState)) {
				world.removeBlock(pos, false);
			} else {
				world.setBlockAndUpdate(pos, newBlockState);
			}

			world.gameEvent(GameEvent.BLOCK_CHANGE, pos, Context.of(ctx.getPlayer(), newBlockState));

			return InteractionResult.SUCCESS;
		}

		Block block = blockState.getBlock();
		FlowerVariant variant = FlowerVariant.fromOriginalBlock(block);
		if (!variant.isEmpty()) {
			BlockState newBlockState = ((GardenBlock) ModBlocks.TINY_GARDEN).defaultBlockState()
					.setValue(GardenBlock.FACING, ctx.getHorizontalDirection().getOpposite())
					.setValue(GardenBlock.FLOWER_VARIANT_1, variant)
					.setValue(GardenBlock.FLOWER_VARIANT_2, variant)
					.setValue(GardenBlock.FLOWER_VARIANT_3, variant)
					.setValue(GardenBlock.FLOWER_VARIANT_4, variant);

			if (ctx.getPlayer() != null) {
				Player player = ctx.getPlayer();
				ctx.getItemInHand().hurtAndBreak(1, player, ctx.getHand());

				world.playSound(player, pos, SoundEvents.GROWING_PLANT_CROP, SoundSource.BLOCKS, 1.0F, 1.0F);
			}

			world.setBlockAndUpdate(pos, newBlockState);
			world.gameEvent(GameEvent.BLOCK_CHANGE, pos, Context.of(ctx.getPlayer(), newBlockState));

			return InteractionResult.SUCCESS;
		}

		return super.useOn(ctx);
	}
}
