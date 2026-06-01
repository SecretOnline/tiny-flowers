package co.secretonline.tinyflowers.renderer.blockentity;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;

public class TinyGardenBlockEntityRenderState extends BlockEntityRenderState {
	private Direction direction = Direction.NORTH;

	@Nullable
	private Identifier flower1 = null;
	@Nullable
	private Identifier flower2 = null;
	@Nullable
	private Identifier flower3 = null;
	@Nullable
	private Identifier flower4 = null;

	private int[] tintStack = new int[0];

	public Direction getDirection() {
		return direction;
	}

	public int[] getTintStack() {
		return tintStack;
	}

	public @Nullable Identifier getFlower1() {
		return flower1;
	}

	public @Nullable Identifier getFlower2() {
		return flower2;
	}

	public @Nullable Identifier getFlower3() {
		return flower3;
	}

	public @Nullable Identifier getFlower4() {
		return flower4;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void setFlowers(Identifier flower1, Identifier flower2, Identifier flower3, Identifier flower4) {
		this.flower1 = flower1;
		this.flower2 = flower2;
		this.flower3 = flower3;
		this.flower4 = flower4;
	}

	public void setTintStack(int[] tintStack) {
		this.tintStack = tintStack;
	}
}
