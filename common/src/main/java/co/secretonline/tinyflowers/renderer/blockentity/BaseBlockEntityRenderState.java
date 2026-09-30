package co.secretonline.tinyflowers.renderer.blockentity;

import net.minecraft.world.phys.Vec3;

public abstract class BaseBlockEntityRenderState<T> {
	public T blockEntity;
	public float tickProgress;
	public int lightCoords;
	public int overlay;

	public void extractRenderState(T blockEntity, float tickProgress, int lightCoords, int overlay) {
		this.blockEntity = blockEntity;
		this.tickProgress = tickProgress;
		this.lightCoords = lightCoords;
		this.overlay = overlay;
	}
}
