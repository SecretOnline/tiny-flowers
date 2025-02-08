package co.secretonline.tinyflowers.gametest.blocks;

import co.secretonline.tinyflowers.blocks.FlowerVariant;
import co.secretonline.tinyflowers.blocks.GardenBlock;
import co.secretonline.tinyflowers.blocks.ModBlocks;
import co.secretonline.tinyflowers.gametest.TinyFlowersTest;
import co.secretonline.tinyflowers.items.ModItems;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.AfterBatch;
import net.minecraft.test.BeforeBatch;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class GardenBlockTest implements FabricGameTest {
	private static final BlockPos GARDEN_POS = new BlockPos(0, 1, 0);

	@BeforeBatch(batchId = "defaultBatch")
	public void beforeDayBatch(ServerWorld world) {
		world.setTimeOfDay(6000);
	}

	@BeforeBatch(batchId = "night")
	public void beforeNightBatch(ServerWorld world) {
		world.setTimeOfDay(18000);
	}

	@AfterBatch(batchId = "night")
	public void afterNightBatch(ServerWorld world) {
		world.setTimeOfDay(6000);
	}

	@GameTest(templateName = TinyFlowersTest.STRUCTURE_0_FLOWERS)
	public void addsTinyFlowersToGarden(TestContext context) {
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

	@GameTest(templateName = TinyFlowersTest.STRUCTURE_1_PETAL)
	public void addsTinyFlowersToPinkPetals(TestContext context) {
		PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);

		ItemStack stack = new ItemStack(ModItems.TINY_DANDELION);
		player.setStackInHand(Hand.MAIN_HAND, stack);
		context.useBlock(GARDEN_POS, player);

		context.expectBlock(ModBlocks.TINY_GARDEN, GARDEN_POS);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_1, FlowerVariant.PINK_PETALS);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_2, FlowerVariant.DANDELION);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_3, FlowerVariant.EMPTY);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_4, FlowerVariant.EMPTY);
		context.assertTrue(stack.isEmpty(), "Expected item stack to be used");

		context.complete();
	}

	@GameTest(templateName = TinyFlowersTest.STRUCTURE_0_FLOWERS)
	public void placingGardenItem(TestContext context) {
		PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);

		BlockStateComponent blockStateComponent = BlockStateComponent.DEFAULT
				.with(GardenBlock.FLOWER_VARIANT_1, FlowerVariant.DANDELION)
				.with(GardenBlock.FLOWER_VARIANT_2, FlowerVariant.ALLIUM)
				.with(GardenBlock.FLOWER_VARIANT_3, FlowerVariant.WITHER_ROSE)
				.with(GardenBlock.FLOWER_VARIANT_4, FlowerVariant.PINK_PETALS);

		ItemStack stack = new ItemStack(ModItems.TINY_GARDEN_ITEM);
		stack.set(DataComponentTypes.BLOCK_STATE, blockStateComponent);

		player.setStackInHand(Hand.MAIN_HAND, stack);
		context.useBlock(GARDEN_POS, player);

		context.expectBlock(ModBlocks.TINY_GARDEN, GARDEN_POS);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_1, FlowerVariant.DANDELION);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_2, FlowerVariant.ALLIUM);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_3, FlowerVariant.WITHER_ROSE);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_4, FlowerVariant.PINK_PETALS);
		context.assertTrue(stack.isEmpty(), "Expected item stack to be used");

		context.complete();
	}

	@GameTest(templateName = TinyFlowersTest.STRUCTURE_0_FLOWERS)
	public void placingEmptyGardenItem(TestContext context) {
		PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);

		ItemStack stack = new ItemStack(ModItems.TINY_GARDEN_ITEM);
		// No block state component

		player.setStackInHand(Hand.MAIN_HAND, stack);
		context.useBlock(GARDEN_POS, player);

		context.expectBlock(Blocks.AIR, GARDEN_POS);

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

	@GameTest(templateName = TinyFlowersTest.STRUCTURE_1_FLOWER_LAST)
	public void bonemealAddsFlowerWhenSpaceAvailableWithHoles(TestContext context) {
		PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);

		ItemStack stack = new ItemStack(Items.BONE_MEAL);
		player.setStackInHand(Hand.MAIN_HAND, stack);
		context.useBlock(GARDEN_POS, player);

		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_1, FlowerVariant.DANDELION);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_2, FlowerVariant.EMPTY);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_3, FlowerVariant.EMPTY);
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_4, FlowerVariant.DANDELION);
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

	@GameTest(templateName = TinyFlowersTest.STRUCTURE_EYEBLOSSOMS)
	public void tinyEyeblossomsCloseAtDayWhenNotifiedByEyeblossoms(TestContext context) {
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_1, FlowerVariant.OPEN_EYEBLOSSOM);
		context.expectBlock(Blocks.OPEN_EYEBLOSSOM, GARDEN_POS.east());

		context.forceRandomTick(GARDEN_POS.east());

		context.expectBlock(Blocks.CLOSED_EYEBLOSSOM, GARDEN_POS.east());

		// Maximum time is ~42 ticks
		context.waitAndRun(50, () -> {
			context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_1, FlowerVariant.CLOSED_EYEBLOSSOM);

			context.complete();
		});
	}

	@GameTest(templateName = TinyFlowersTest.STRUCTURE_EYEBLOSSOMS)
	public void eyeblossomsCloseAtDayWhenNotifiedByTinyEyeblossoms(TestContext context) {
		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_1, FlowerVariant.OPEN_EYEBLOSSOM);
		context.expectBlock(Blocks.OPEN_EYEBLOSSOM, GARDEN_POS.east());

		context.forceRandomTick(GARDEN_POS);

		context.expectBlockProperty(GARDEN_POS, GardenBlock.FLOWER_VARIANT_1, FlowerVariant.CLOSED_EYEBLOSSOM);

		// Maximum time is ~42 ticks
		context.waitAndRun(50, () -> {
			context.expectBlock(Blocks.CLOSED_EYEBLOSSOM, GARDEN_POS.east());

			context.complete();
		});
	}

	@GameTest(templateName = TinyFlowersTest.STRUCTURE_EYEBLOSSOMS, batchId = "night")
	public void tinyEyeblossomsOpenAtNightWhenNotifiedByEyeblossoms(TestContext context) {
		context.expectBlockProperty(GARDEN_POS.south(), GardenBlock.FLOWER_VARIANT_1, FlowerVariant.CLOSED_EYEBLOSSOM);
		context.expectBlock(Blocks.CLOSED_EYEBLOSSOM, GARDEN_POS.south().east());

		context.useNightTime();

		context.forceRandomTick(GARDEN_POS.south().east());

		context.expectBlock(Blocks.OPEN_EYEBLOSSOM, GARDEN_POS.south().east());

		// Maximum time is ~42 ticks
		context.waitAndRun(50, () -> {
			context.expectBlockProperty(GARDEN_POS.south(), GardenBlock.FLOWER_VARIANT_1, FlowerVariant.OPEN_EYEBLOSSOM);

			context.complete();
		});
	}

	@GameTest(templateName = TinyFlowersTest.STRUCTURE_EYEBLOSSOMS, batchId = "night")
	public void eyeblossomsOpenAtNightWhenNotifiedByTinyEyeblossoms(TestContext context) {
		context.expectBlockProperty(GARDEN_POS.south(), GardenBlock.FLOWER_VARIANT_1, FlowerVariant.CLOSED_EYEBLOSSOM);
		context.expectBlock(Blocks.CLOSED_EYEBLOSSOM, GARDEN_POS.south().east());

		context.forceRandomTick(GARDEN_POS.south());

		context.expectBlockProperty(GARDEN_POS.south(), GardenBlock.FLOWER_VARIANT_1, FlowerVariant.OPEN_EYEBLOSSOM);

		// Maximum time is ~42 ticks
		context.waitAndRun(50, () -> {
			context.expectBlock(Blocks.OPEN_EYEBLOSSOM, GARDEN_POS.south().east());

			context.complete();
		});
	}
}
