package co.secretonline.tinyflowers.renderer.blockentity;

import co.secretonline.tinyflowers.block.entity.TinyFlowerPotBlockEntity;
import net.minecraft.resources.ResourceLocation;

public class TinyFlowerPotBlockEntityRenderState extends BaseBlockEntityRenderState<TinyFlowerPotBlockEntity> {
	private ResourceLocation flower = null;

	private int[] tintStack = new int[0];

	public int[] getTintStack() {
		return tintStack;
	}

	public ResourceLocation getFlower() {
		return flower;
	}

	public void setFlower(ResourceLocation flower) {
		this.flower = flower;
	}

	public void setTintStack(int[] tintStack) {
		this.tintStack = tintStack;
	}
}
