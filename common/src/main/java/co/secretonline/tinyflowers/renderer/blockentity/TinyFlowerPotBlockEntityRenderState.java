package co.secretonline.tinyflowers.renderer.blockentity;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class TinyFlowerPotBlockEntityRenderState extends BlockEntityRenderState {
	@Nullable
	private Identifier flower = null;

	private int[] tintStack = new int[0];

	public int[] getTintStack() {
		return tintStack;
	}

	public @Nullable Identifier getFlower() {
		return flower;
	}

	public void setFlower(@Nullable Identifier flower) {
		this.flower = flower;
	}

	public void setTintStack(int[] tintStack) {
		this.tintStack = tintStack;
	}
}
