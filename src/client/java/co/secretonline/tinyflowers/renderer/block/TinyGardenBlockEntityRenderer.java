package co.secretonline.tinyflowers.renderer.block;

import java.util.Optional;

import org.jspecify.annotations.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;

import co.secretonline.tinyflowers.blocks.TinyGardenBlock;
import co.secretonline.tinyflowers.blocks.TinyGardenBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class TinyGardenBlockEntityRenderer
		implements BlockEntityRenderer<TinyGardenBlockEntity, TinyGardenBlockEntityRenderState> {

	public TinyGardenBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public TinyGardenBlockEntityRenderState createRenderState() {
		return new TinyGardenBlockEntityRenderState();
	}

	@Override
	public void extractRenderState(TinyGardenBlockEntity blockEntity,
			TinyGardenBlockEntityRenderState state, float tickProgress, Vec3 cameraPos,
			@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);

		Optional<Direction> facingDirection = state.blockState.getOptionalValue(TinyGardenBlock.FACING);
		if (facingDirection.isPresent()) {
			state.setDirection(facingDirection.get());
		}

		state.setFlowers(state.getFlower1(), state.getFlower2(), state.getFlower3(), state.getFlower4());
	}

	@Override
	public void submit(TinyGardenBlockEntityRenderState blockEntityRenderState, PoseStack poseStack,
			SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
	}

	@Override
	public int getViewDistance() {
		// Hopefully this is far enough?
		// I know the whole reason this exists is for performance, but I think it's a
		// bit sad if distant gardens aren't rendered in. Expecially since these are
		// mean to be part of the world, which usually doesn't distance culling.
		return 256;
	}
}
