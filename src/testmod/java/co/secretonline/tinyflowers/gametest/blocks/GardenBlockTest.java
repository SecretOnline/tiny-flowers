package co.secretonline.tinyflowers.gametest.blocks;

import co.secretonline.tinyflowers.blocks.FlowerVariant;
import co.secretonline.tinyflowers.blocks.GardenBlock;
import co.secretonline.tinyflowers.gametest.TinyFlowersTest;
import co.secretonline.tinyflowers.items.ModItems;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class GardenBlockTest implements FabricGameTest {
	private static final BlockPos GARDEN_POS = new BlockPos(0, 1, 0);

	@GameTest(templateName = TinyFlowersTest.STRUCTURE_0_FLOWERS)
	public void addTinyFlowersToGarden(TestContext context) {
		PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);

		ItemStack stack = new ItemStack(ModItems.TINY_DANDELION);
		player.setStackInHand(Hand.MAIN_HAND, stack);
		context.useBlock(GARDEN_POS, player);

		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_1, FlowerVariant.DANDELION);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_2, FlowerVariant.EMPTY);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_3, FlowerVariant.EMPTY);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_4, FlowerVariant.EMPTY);
		context.assertTrue(stack.isEmpty(), "Expected item stack to be used");

		stack = new ItemStack(ModItems.TINY_ALLIUM);
		player.setStackInHand(Hand.MAIN_HAND, stack);
		context.useBlock(GARDEN_POS, player);

		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_1, FlowerVariant.DANDELION);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_2, FlowerVariant.ALLIUM);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_3, FlowerVariant.EMPTY);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_4, FlowerVariant.EMPTY);
		context.assertTrue(stack.isEmpty(), "Expected item stack to be used");

		stack = new ItemStack(ModItems.TINY_WITHER_ROSE);
		player.setStackInHand(Hand.MAIN_HAND, stack);
		context.useBlock(GARDEN_POS, player);

		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_1, FlowerVariant.DANDELION);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_2, FlowerVariant.ALLIUM);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_3, FlowerVariant.WITHER_ROSE);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_4, FlowerVariant.EMPTY);
		context.assertTrue(stack.isEmpty(), "Expected item stack to be used");

		stack = new ItemStack(Items.PINK_PETALS);
		player.setStackInHand(Hand.MAIN_HAND, stack);
		context.useBlock(GARDEN_POS, player);

		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_1, FlowerVariant.DANDELION);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_2, FlowerVariant.ALLIUM);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_3, FlowerVariant.WITHER_ROSE);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_4, FlowerVariant.PINK_PETALS);
		context.assertTrue(stack.isEmpty(), "Expected item stack to be used");

		context.complete();
	}

	@GameTest(templateName = TinyFlowersTest.STRUCTURE_1_FLOWER)
	public void bonemealAddsFlowerWhenSpaceAvailable(TestContext context) {
		PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);

		ItemStack stack = new ItemStack(Items.BONE_MEAL);
		player.setStackInHand(Hand.MAIN_HAND, stack);
		context.useBlock(GARDEN_POS, player);

		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_1, FlowerVariant.DANDELION);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_2, FlowerVariant.DANDELION);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_3, FlowerVariant.EMPTY);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_4, FlowerVariant.EMPTY);
		context.assertTrue(stack.isEmpty(), "Expected item stack to be used");

		context.complete();
	}

	@GameTest(templateName = TinyFlowersTest.STRUCTURE_4_FLOWERS)
	public void bonemealCreatesItemEntityWhenFull(TestContext context) {
		PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);

		ItemStack stack = new ItemStack(Items.BONE_MEAL);
		player.setStackInHand(Hand.MAIN_HAND, stack);
		context.useBlock(GARDEN_POS, player);

		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_1, FlowerVariant.DANDELION);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_2, FlowerVariant.DANDELION);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_3, FlowerVariant.DANDELION);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_4, FlowerVariant.DANDELION);
		context.assertTrue(stack.isEmpty(), "Expected item stack to be used");

		context.expectItemAt(ModItems.TINY_DANDELION, GARDEN_POS, 1);

		context.complete();
	}
}
