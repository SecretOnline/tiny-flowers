package co.secretonline.tinyflowers.renderer.blockentity;

import co.secretonline.tinyflowers.block.entity.TinyGardenBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class TinyGardenBlockEntityRenderState extends BaseBlockEntityRenderState<TinyGardenBlockEntity> {
	private Direction direction = Direction.NORTH;

	private ResourceLocation flower1 = null;
	private ResourceLocation flower2 = null;
	private ResourceLocation flower3 = null;
	private ResourceLocation flower4 = null;

	private int[] tintStack = new int[0];

	public Direction getDirection() {
		return direction;
	}

	public int[] getTintStack() {
		return tintStack;
	}

	public ResourceLocation getFlower1() {
		return flower1;
	}

	public ResourceLocation getFlower2() {
		return flower2;
	}

	public ResourceLocation getFlower3() {
		return flower3;
	}

	public ResourceLocation getFlower4() {
		return flower4;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void setFlowers(ResourceLocation flower1, ResourceLocation flower2, ResourceLocation flower3, ResourceLocation flower4) {
		this.flower1 = flower1;
		this.flower2 = flower2;
		this.flower3 = flower3;
		this.flower4 = flower4;
	}

	public void setTintStack(int[] tintStack) {
		this.tintStack = tintStack;
	}
}
