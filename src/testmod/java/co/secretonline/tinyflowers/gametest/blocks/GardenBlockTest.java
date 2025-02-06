package co.secretonline.tinyflowers.gametest.blocks;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;

public class GardenBlockTest implements FabricGameTest {
	@GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
	public void test(TestContext context) {
		context.assertTrue(true, "It should work");
		context.complete();
	}
}
