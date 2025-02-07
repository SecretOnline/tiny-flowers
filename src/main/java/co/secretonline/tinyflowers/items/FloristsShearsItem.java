package co.secretonline.tinyflowers.items;

import java.util.Arrays;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.blocks.FlowerVariant;
import co.secretonline.tinyflowers.blocks.GardenBlock;
import co.secretonline.tinyflowers.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerbedBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShearsItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.GameEvent.Emitter;

public class FloristsShearsItem extends ShearsItem {
	private final static Direction[] DIRECTIONS = new Direction[] {
			Direction.NORTH, Direction.EAST,
			Direction.SOUTH, Direction.WEST, };

	public FloristsShearsItem(Settings settings) {
		super(settings);
	}

	@Override
	public ItemStack getRecipeRemainder(ItemStack stack) {
		if (stack.getDamage() < stack.getMaxDamage() - 1) {
			ItemStack moreDamaged = stack.copy();
			moreDamaged.setDamage(stack.getDamage() + 1);
			return moreDamaged;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getBlockPos();
		BlockState blockState = world.getBlockState(pos);

		if (blockState.getBlock() instanceof FlowerbedBlock) {
			// Try convert flowerbeds to gardens so that shears can remove flowers from
			// them.
			try {
				blockState = ((GardenBlock) ModBlocks.TINY_GARDEN).getStateFromFlowerbed(blockState);
			} catch (IllegalStateException ex) {
				// Flowerbed could not be converted to garden.
				TinyFlowers.LOGGER.warn("Could not convert flowerbed to garden. Ignoring action.");

				return ActionResult.PASS;
			}
		}

		if (blockState.isOf(ModBlocks.TINY_GARDEN)) {
			// Remove flower at certain part of garden.
			Vec3d positionInBlock = ctx.getHitPos().subtract(Vec3d.of(pos));
			boolean isEast = positionInBlock.x >= 0.5;
			boolean isSouth = positionInBlock.z >= 0.5;

			// Convert block quadrant into the correct property.
			// Writing this was a little bit of trial and a lot of error.
			int index = isSouth ? (isEast ? 2 : 3) : (isEast ? 1 : 0);
			index = Arrays.asList(DIRECTIONS).indexOf(blockState.get(GardenBlock.FACING)) - index;
			index = (index + 4) % 4;
			EnumProperty<FlowerVariant> property = GardenBlock.FLOWER_VARIANT_PROPERTIES[index];

			FlowerVariant variant = blockState.get(property);
			if (variant.isEmpty()) {
				return ActionResult.PASS;
			}

			Block.dropStack(world, pos, new ItemStack(variant));

			// TODO: Figure out if there's a scenario where the player is null
			if (ctx.getPlayer() != null) {
				PlayerEntity player = ctx.getPlayer();
				ctx.getStack().damage(1, player, LivingEntity.getSlotForHand(ctx.getHand()));

				world.playSound(player, pos, SoundEvents.BLOCK_GROWING_PLANT_CROP, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}

			BlockState newBlockState = blockState.with(property, FlowerVariant.EMPTY);
			if (GardenBlock.isEmpty(newBlockState)) {
				world.removeBlock(pos, false);
			} else {
				world.setBlockState(pos, newBlockState);
			}

			world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, Emitter.of(ctx.getPlayer(), newBlockState));

			return ActionResult.SUCCESS;
		}

		return super.useOnBlock(ctx);
	}
}
