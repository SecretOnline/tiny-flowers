package co.secretonline.tinyflowers.items;

import java.util.Arrays;

import co.secretonline.tinyflowers.blocks.ModBlocks;
import co.secretonline.tinyflowers.blocks.TinyGardenBlock;
import co.secretonline.tinyflowers.blocks.TinyGardenBlockEntity;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SegmentableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public class FloristsShearsItem extends ShearsItem {
	private final static Direction[] DIRECTIONS = new Direction[] {
			Direction.NORTH, Direction.EAST,
			Direction.SOUTH, Direction.WEST, };

	public FloristsShearsItem(Properties settings) {
		super(settings);
	}

	@Override
	public @NonNull InteractionResult useOn(UseOnContext ctx) {
		Level world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		BlockState prevBockState = world.getBlockState(pos);

		Block prevBlock = prevBockState.getBlock();
		TinyFlowerData prevData = TinyFlowerData.findByOriginalBlock(world.registryAccess(), prevBlock);
		if (prevData != null) {
			// The block that was clicked on is the original block of a tiny flower variant.
			// Try and turn into actual tiny flowers.

			// Ensure the block underneath can support the tiny flower variant. This is
			// likely, given the block was previously holding the original block.
			Block supportingBlock = world.getBlockState(pos.below()).getBlock();
			if (!prevData.canSurviveOn(supportingBlock)) {
				return InteractionResult.FAIL;
			}

			BlockState newBlockState = ModBlocks.TINY_GARDEN_BLOCK.defaultBlockState()
					.setValue(TinyGardenBlock.FACING, ctx.getHorizontalDirection().getOpposite());

			world.setBlockAndUpdate(pos, newBlockState);

			if (!(world.getBlockEntity(pos) instanceof TinyGardenBlockEntity gardenBlockEntity)) {
				// If there's no block entity, try undo the change
				world.setBlockAndUpdate(pos, prevBockState);
				return InteractionResult.FAIL;
			}

			gardenBlockEntity.setFromPreviousBlockState(world.registryAccess(), prevBockState);

			// If the block we converted from is not segmentable, then we're done here.
			if (!(prevBlock instanceof SegmentableBlock)) {
				if (ctx.getPlayer() != null) {
					Player player = ctx.getPlayer();
					ctx.getItemInHand().hurtAndBreak(1, player, ctx.getHand());

					world.playSound(player, pos, SoundEvents.GROWING_PLANT_CROP,
							SoundSource.BLOCKS, 1.0F, 1.0F);
				}

				world.gameEvent(GameEvent.BLOCK_CHANGE, pos, Context.of(ctx.getPlayer(),
						newBlockState));

				return InteractionResult.SUCCESS;
			} else {
				// If the previous block was segmentable, then we actually want to remove a
				// flower from it.
				// That's handled by the next if statement, but only for Tiny Gardens. Luckily,
				// we just did
				// the conversion.
				prevBockState = newBlockState;
			}
		}

		if (prevBockState.is(ModBlocks.TINY_GARDEN_BLOCK)) {
			if (!(world.getBlockEntity(pos) instanceof TinyGardenBlockEntity gardenBlockEntity)) {
				// If there's no block entity, don't do anything
				return InteractionResult.FAIL;
			}

			// Remove flower at certain part of garden.
			Vec3 positionInBlock = ctx.getClickLocation().subtract(Vec3.atLowerCornerOf(pos));
			boolean isEast = positionInBlock.x >= 0.5;
			boolean isSouth = positionInBlock.z >= 0.5;

			// Convert block quadrant into the correct property.
			// Writing this was a little bit of trial and a lot of error.
			int index = isSouth ? (isEast ? 2 : 3) : (isEast ? 1 : 0);
			index = Arrays.asList(DIRECTIONS).indexOf(prevBockState.getValue(TinyGardenBlock.FACING)) -
					index;
			index = (index + 4) % 4;
			int oneIndexed = index + 1;

			Identifier idAtIndex = gardenBlockEntity.getFlower(oneIndexed);
			if (idAtIndex == null) {
				// This spot has no flower.
				return InteractionResult.TRY_WITH_EMPTY_HAND;
			}
			TinyFlowerData flowerData = TinyFlowerData.findById(world.registryAccess(), idAtIndex);
			// This condition fails if the garden has an identifier in this spot, but it is
			// no longer registered. This usually happens when a mod is removed. In that
			// case, don't pop an item, but do do the rest.
			if (flowerData != null) {
				Block.popResource(world, pos, flowerData.getItemStack(1));
			}

			if (ctx.getPlayer() != null) {
				Player player = ctx.getPlayer();
				ctx.getItemInHand().hurtAndBreak(1, player, ctx.getHand());

				world.playSound(player, pos, SoundEvents.GROWING_PLANT_CROP,
						SoundSource.BLOCKS, 1.0F, 1.0F);
			}

			gardenBlockEntity.setFlower(oneIndexed, null);

			if (gardenBlockEntity.isEmpty()) {
				world.removeBlock(pos, false);
			}

			world.gameEvent(GameEvent.BLOCK_CHANGE, pos, Context.of(ctx.getPlayer(),
					prevBockState));

			return InteractionResult.SUCCESS;
		}

		return super.useOn(ctx);
	}
}
