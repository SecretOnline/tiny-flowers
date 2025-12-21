package co.secretonline.tinyflowers.renderer.block;

import java.util.Map;
import java.util.Optional;

import org.jspecify.annotations.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import co.secretonline.tinyflowers.blocks.TinyGardenBlock;
import co.secretonline.tinyflowers.blocks.TinyGardenBlockEntity;
import co.secretonline.tinyflowers.resources.TinyFlowerResolvedResources;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.model.BlockStateModel;
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

		state.setBlockAndTintGetter(blockEntity.getLevel());
	}

	@Override
	public void submit(TinyGardenBlockEntityRenderState blockEntityRenderState, PoseStack poseStack,
			SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
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
		Identifier id;
		switch (index) {
			case 1:
				id = state.getFlower1();
				break;
			case 2:
				id = state.getFlower2();
				break;
			case 3:
				id = state.getFlower3();
				break;
			case 4:
				id = state.getFlower4();
				break;
			default:
				throw new IllegalArgumentException("Invalid flower index " + index);
		}
		if (id == null) {
			return;
		}

		Map<Identifier, TinyFlowerResolvedResources> resourceMap = TinyFlowerResolvedResources.getInstances();
		TinyFlowerResolvedResources resources = resourceMap.get(id);
		if (resources == null) {
			return;
		}

		ExtraModelKey<BlockStateModel> key;
		switch (index) {
			case 1:
				key = resources.model1().extraModelKey();
				break;
			case 2:
				key = resources.model2().extraModelKey();
				break;
			case 3:
				key = resources.model3().extraModelKey();
				break;
			case 4:
				key = resources.model4().extraModelKey();
				break;
			default:
				throw new IllegalArgumentException("Invalid flower index " + index);
		}
		if (key == null) {
			return;
		}

		Minecraft minecraft = Minecraft.getInstance();
		ModelManager modelManager = minecraft.getModelManager();

		BlockStateModel model = modelManager.getModel(key);
		if (model == null) {
			return;
		}

		submitNodeCollector.submitBlockStateModel(poseStack, (layer) -> RenderTypes.cutoutMovingBlock(), model,
				1, 1, 1,
				state.lightCoords, 0, 0,
				state.getBlockAndTintGetter(), state.blockPos, state.blockState);
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
