package co.secretonline.tinyflowers.renderer.block;

import java.util.Map;
import java.util.Optional;

import org.jspecify.annotations.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import co.secretonline.tinyflowers.blocks.TinyGardenBlock;
import co.secretonline.tinyflowers.blocks.TinyGardenBlockEntity;
import co.secretonline.tinyflowers.resources.TinyFlowerResources;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
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

		state.setFlowers(blockEntity.getFlower(1), blockEntity.getFlower(2),
				blockEntity.getFlower(3), blockEntity.getFlower(4));
	}

	@Override
	public void submit(TinyGardenBlockEntityRenderState blockEntityRenderState, PoseStack poseStack,
			SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
		poseStack.pushPose();

		float rotationDegrees = Direction.getYRot(blockEntityRenderState.getDirection());
		poseStack.mulPose(Axis.YP.rotationDegrees(rotationDegrees));

		submitPartForFlowerIndex(blockEntityRenderState, poseStack, submitNodeCollector, 1);
		submitPartForFlowerIndex(blockEntityRenderState, poseStack, submitNodeCollector, 2);
		submitPartForFlowerIndex(blockEntityRenderState, poseStack, submitNodeCollector, 3);
		submitPartForFlowerIndex(blockEntityRenderState, poseStack, submitNodeCollector, 4);

		poseStack.popPose();
	}

	private void submitPartForFlowerIndex(TinyGardenBlockEntityRenderState blockEntityRenderState, PoseStack poseStack,
			SubmitNodeCollector submitNodeCollector, int index) {
		Identifier id;
		switch (index) {
			case 1:
				id = blockEntityRenderState.getFlower1();
				break;
			case 2:
				id = blockEntityRenderState.getFlower2();
				break;
			case 3:
				id = blockEntityRenderState.getFlower3();
				break;
			case 4:
				id = blockEntityRenderState.getFlower4();
				break;
			default:
				throw new IllegalArgumentException("Invalid flower index " + index);
		}
		if (id == null) {
			return;
		}

		Map<Identifier, TinyFlowerResources> resourceMap = TinyFlowerResources.getInstances();
		TinyFlowerResources resources = resourceMap.get(id);
		if (resources == null) {
			return;
		}

		Identifier modelId;
		switch (index) {
			case 1:
				modelId = resources.modelPart1();
				break;
			case 2:
				modelId = resources.modelPart2();
				break;
			case 3:
				modelId = resources.modelPart3();
				break;
			case 4:
				modelId = resources.modelPart4();
				break;
			default:
				throw new IllegalArgumentException("Invalid flower index " + index);
		}
		if (modelId == null) {
			return;
		}

		ExtraModelKey<ModelPart> key = ExtraModelKey.create(modelId::toString);

		Minecraft minecraft = Minecraft.getInstance();
		ModelManager modelManager = minecraft.getModelManager();

		ModelPart model = modelManager.getModel(key);
		if (model == null) {
			return;
		}

		submitNodeCollector.submitModelPart(model, poseStack, RenderTypes.cutoutMovingBlock(), 0, 0, null);
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
