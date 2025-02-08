package co.secretonline.tinyflowers.gametest.items;

import co.secretonline.tinyflowers.blocks.FlowerVariant;
import co.secretonline.tinyflowers.blocks.GardenBlock;
import co.secretonline.tinyflowers.blocks.ModBlocks;
import co.secretonline.tinyflowers.gametest.TinyFlowersTest;
import co.secretonline.tinyflowers.items.ModItems;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

public class FloristsShearsTest implements FabricGameTest {
	private static final BlockPos GARDEN_POS = new BlockPos(0, 1, 0);

	private void useBlockInQuadrant(TestContext context, BlockPos pos, PlayerEntity player,
			Direction northSouth, Direction eastWest) {
		BlockPos blockPos = context.getAbsolutePos(pos);
		Vec3d hitPosition = Vec3d
				.ofCenter(blockPos)
				.add(eastWest.getOffsetX() * 0.25, 0, northSouth.getOffsetZ() * 0.25);

		context.useBlock(pos, player, new BlockHitResult(hitPosition, Direction.NORTH, blockPos, true));
	}

	@GameTest(templateName = TinyFlowersTest.STRUCTURE_4_FLOWERS)
	public void removesTinyFlowersWhenUsedOnGarden(TestContext context) {
		PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);

		ItemStack stack = new ItemStack(ModItems.FLORISTS_SHEARS_ITEM);
		player.setStackInHand(Hand.MAIN_HAND, stack);

		useBlockInQuadrant(context, GARDEN_POS, player, Direction.NORTH, Direction.EAST);

		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_1, FlowerVariant.DANDELION);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_2, FlowerVariant.DANDELION);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_3, FlowerVariant.DANDELION);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_4, FlowerVariant.EMPTY);
		context.assertTrue(stack.getDamage() == 1, "Expected shears to have taken 1 damage");

		context.expectItemAt(ModItems.TINY_DANDELION, GARDEN_POS, 1);

		// No change when used on already empty spot
		useBlockInQuadrant(context, GARDEN_POS, player, Direction.NORTH, Direction.EAST);

		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_1, FlowerVariant.DANDELION);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_2, FlowerVariant.DANDELION);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_3, FlowerVariant.DANDELION);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_4, FlowerVariant.EMPTY);
		context.assertTrue(stack.getDamage() == 1, "Expected shears to have taken 1 damage");

		// Removes block when used on all quadrants
		useBlockInQuadrant(context, GARDEN_POS, player, Direction.NORTH, Direction.WEST);
		useBlockInQuadrant(context, GARDEN_POS, player, Direction.SOUTH, Direction.EAST);
		useBlockInQuadrant(context, GARDEN_POS, player, Direction.SOUTH, Direction.WEST);

		context.expectBlock(Blocks.AIR, GARDEN_POS);
		context.assertTrue(stack.getDamage() == 4, "Expected shears to have taken 4 damage");

		context.expectItemsAt(ModItems.TINY_DANDELION, GARDEN_POS, 1, 4);

		context.complete();
	}

	@GameTest(templateName = TinyFlowersTest.STRUCTURE_4_PETALS)
	public void removesPetalsWhenUsedOnFlowerbed(TestContext context) {
		PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);

		ItemStack stack = new ItemStack(ModItems.FLORISTS_SHEARS_ITEM);
		player.setStackInHand(Hand.MAIN_HAND, stack);

		useBlockInQuadrant(context, GARDEN_POS, player, Direction.NORTH, Direction.EAST);

		context.expectBlock(ModBlocks.TINY_GARDEN, GARDEN_POS);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_1, FlowerVariant.PINK_PETALS);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_2, FlowerVariant.PINK_PETALS);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_3, FlowerVariant.PINK_PETALS);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_4, FlowerVariant.EMPTY);
		context.assertTrue(stack.getDamage() == 1, "Expected shears to have taken 1 damage");

		context.expectItemAt(Items.PINK_PETALS, GARDEN_POS, 1);

		// Removes block when used on all quadrants
		useBlockInQuadrant(context, GARDEN_POS, player, Direction.NORTH, Direction.WEST);
		useBlockInQuadrant(context, GARDEN_POS, player, Direction.SOUTH, Direction.EAST);
		useBlockInQuadrant(context, GARDEN_POS, player, Direction.SOUTH, Direction.WEST);

		context.expectBlock(Blocks.AIR, GARDEN_POS);
		context.assertTrue(stack.getDamage() == 4, "Expected shears to have taken 4 damage");

		context.expectItemsAt(Items.PINK_PETALS, GARDEN_POS, 1, 4);

		context.complete();
	}

}
