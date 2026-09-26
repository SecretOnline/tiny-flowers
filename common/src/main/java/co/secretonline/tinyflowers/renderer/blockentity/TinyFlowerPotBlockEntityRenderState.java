package co.secretonline.tinyflowers.renderer.blockentity;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class TinyFlowerPotBlockEntityRenderState extends BlockEntityRenderState {
	@NonNull
	private Identifier flower = null;

	private int[] tintStack = new int[0];

	public int[] getTintStack() {
		return tintStack;
	}

	public @NonNull Identifier getFlower() {
		return flower;
	}

	public void setFlower(@NonNull Identifier flower) {
		this.flower = flower;
	}

	public void setTintStack(int[] tintStack) {
		this.tintStack = tintStack;
	}
}
