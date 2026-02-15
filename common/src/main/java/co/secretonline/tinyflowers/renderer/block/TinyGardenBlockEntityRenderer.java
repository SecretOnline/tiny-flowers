package co.secretonline.tinyflowers.renderer.block;

import co.secretonline.tinyflowers.TinyFlowersClientState;
import co.secretonline.tinyflowers.blocks.TinyGardenBlock;
import co.secretonline.tinyflowers.blocks.TinyGardenBlockEntity;
import co.secretonline.tinyflowers.platform.Services;
import co.secretonline.tinyflowers.resources.TinyFlowerResources;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

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
																 TinyGardenBlockEntityRenderState state, float tickProgress, @NonNull Vec3 cameraPos,
																 @Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);

		Optional<Direction> facingDirection = state.blockState.getOptionalValue(TinyGardenBlock.FACING);
		if (facingDirection.isPresent()) {
			state.setDirection(facingDirection.get());
		}

		state.setFlowers(blockEntity.getFlower(1), blockEntity.getFlower(2),
				blockEntity.getFlower(3), blockEntity.getFlower(4));

		state.setBlockAndTintGetter(blockEntity.getLevel());
	}

	@Override
	public void submit(TinyGardenBlockEntityRenderState blockEntityRenderState, PoseStack poseStack,
										 @NonNull SubmitNodeCollector submitNodeCollector, @NonNull CameraRenderState cameraRenderState) {
		poseStack.pushPose();

		poseStack.translate(0.5, 0, 0.5);
		float rotationDegrees = Direction.getYRot(blockEntityRenderState.getDirection());
		poseStack.mulPose(Axis.YP.rotationDegrees(180 - rotationDegrees));
		poseStack.translate(-0.5, 0, -0.5);

		submitPartForFlowerIndex(blockEntityRenderState, poseStack, submitNodeCollector, 1);
		submitPartForFlowerIndex(blockEntityRenderState, poseStack, submitNodeCollector, 2);
		submitPartForFlowerIndex(blockEntityRenderState, poseStack, submitNodeCollector, 3);
		submitPartForFlowerIndex(blockEntityRenderState, poseStack, submitNodeCollector, 4);

		poseStack.popPose();
	}

	private void submitPartForFlowerIndex(TinyGardenBlockEntityRenderState state, PoseStack poseStack,
			SubmitNodeCollector submitNodeCollector, int index) {
		Identifier id = switch (index) {
			case 1 -> state.getFlower1();
			case 2 -> state.getFlower2();
			case 3 -> state.getFlower3();
			case 4 -> state.getFlower4();
			default -> throw new IllegalArgumentException("Invalid flower index " + index);
		};
		if (id == null) {
			return;
		}

		TinyFlowerResources resources = TinyFlowersClientState.RESOURCE_INSTANCES.get(id);
		if (resources == null) {
			return;
		}

		Identifier partId = switch (index) {
			case 1 -> resources.model1();
			case 2 -> resources.model2();
			case 3 -> resources.model3();
			case 4 -> resources.model4();
			default -> throw new IllegalArgumentException("Invalid flower index " + index);
		};
		if (partId == null) {
			return;
		}

		Minecraft minecraft = Minecraft.getInstance();
		BlockStateModel model = Services.FLOWER_MODELS.getModel(minecraft, partId);
		if (model == null) {
			return;
		}

		int packedTint = TinyGardenColorProvider.getColor(state.blockState, state.getBlockAndTintGetter(), state.blockPos, resources.tintSource());
		float r = ((packedTint & 0xFF0000) >> 16) / 255f;
		float g = ((packedTint & 0xFF00) >> 8) / 255f;
		float b = (packedTint & 0xFF) / 255f;

		submitNodeCollector.submitBlockModel(poseStack, RenderTypes.cutoutMovingBlock(), model,
				r, g, b,
				state.lightCoords, 0, 0);
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
