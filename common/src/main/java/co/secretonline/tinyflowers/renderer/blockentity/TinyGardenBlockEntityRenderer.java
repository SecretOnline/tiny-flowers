package co.secretonline.tinyflowers.renderer.blockentity;

import co.secretonline.tinyflowers.TinyFlowersClientState;
import co.secretonline.tinyflowers.block.TinyGardenBlock;
import co.secretonline.tinyflowers.block.entity.TinyGardenBlockEntity;
import co.secretonline.tinyflowers.helper.RenderHelper;
import co.secretonline.tinyflowers.platform.ClientServiceLoader;
import co.secretonline.tinyflowers.data.TinyFlowerResources;
import co.secretonline.tinyflowers.renderer.block.TinyFlowersColorProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
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

import java.util.List;
import java.util.Optional;

public class TinyGardenBlockEntityRenderer
	implements BlockEntityRenderer<TinyGardenBlockEntity, TinyGardenBlockEntityRenderState> {

	public TinyGardenBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public @NonNull TinyGardenBlockEntityRenderState createRenderState() {
		return new TinyGardenBlockEntityRenderState();
	}

	@Override
	public void extractRenderState(@NonNull TinyGardenBlockEntity blockEntity,
																 @NonNull TinyGardenBlockEntityRenderState state, float tickProgress, @NonNull Vec3 cameraPos,
																 @Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);

		Optional<Direction> facingDirection = blockEntity.getBlockState().getOptionalValue(TinyGardenBlock.FACING);
		facingDirection.ifPresent(state::setDirection);

		state.setFlowers(blockEntity.getFlower(0), blockEntity.getFlower(1),
			blockEntity.getFlower(2), blockEntity.getFlower(3));
	}

	@Override
	public void submit(TinyGardenBlockEntityRenderState blockEntityRenderState, PoseStack poseStack,
										 @NonNull SubmitNodeCollector submitNodeCollector, @NonNull CameraRenderState cameraRenderState) {
		poseStack.pushPose();

		poseStack.translate(0.5, 0, 0.5);
		float rotationDegrees = Direction.getYRot(blockEntityRenderState.getDirection());
		poseStack.mulPose(Axis.YP.rotationDegrees(180 - rotationDegrees));
		poseStack.translate(-0.5, 0, -0.5);

		submitPartForFlowerIndex(blockEntityRenderState, poseStack, submitNodeCollector, 0);
		submitPartForFlowerIndex(blockEntityRenderState, poseStack, submitNodeCollector, 1);
		submitPartForFlowerIndex(blockEntityRenderState, poseStack, submitNodeCollector, 2);
		submitPartForFlowerIndex(blockEntityRenderState, poseStack, submitNodeCollector, 3);

		poseStack.popPose();
	}

	private void submitPartForFlowerIndex(TinyGardenBlockEntityRenderState state, PoseStack poseStack,
																				SubmitNodeCollector submitNodeCollector, int index) {
		Identifier id = switch (index) {
			case 0 -> state.getFlower1();
			case 1 -> state.getFlower2();
			case 2 -> state.getFlower3();
			case 3 -> state.getFlower4();
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
			case 0 -> resources.model1();
			case 1 -> resources.model2();
			case 2 -> resources.model3();
			case 3 -> resources.model4();
			default -> throw new IllegalArgumentException("Invalid flower index " + index);
		};
		if (partId == null) {
			return;
		}

		Minecraft minecraft = Minecraft.getInstance();
		BlockStateModel model = ClientServiceLoader.FLOWER_MODELS.getModel(minecraft, partId);
		if (model == null) {
			return;
		}

		// We can only supply one tint index at a time, so just take the first one that's in the model.
		int tintIndex = 0;
		List<BlockModelPart> parts = model.collectParts(TinyFlowersClientState.RANDOM);
		for (BlockModelPart blockModelPart : parts) {
			List<BakedQuad> quads = blockModelPart.getQuads(null);

			for (BakedQuad bakedQuad : quads) {
				if (bakedQuad.isTinted()) {
					tintIndex = bakedQuad.tintIndex();
					break;
				}
			}

			if (tintIndex != 0) {
				break;
			}
		}

		int tintInt = TinyFlowersColorProvider.getAverageBiomeColor(
			state.blockState,
			minecraft.level,
			state.blockPos,
			tintIndex
		);
		float[] tint = RenderHelper.unpackColorInt(tintInt);

		submitNodeCollector.submitBlockModel(poseStack, RenderTypes.cutoutMovingBlock(), model,
			tint[0], tint[1], tint[2],
			state.lightCoords, 0, 0);
	}

	@Override
	public int getViewDistance() {
		// Hopefully this is far enough?
		// I know the whole reason this exists is for performance, but I think it's a
		// bit sad if distant gardens aren't rendered in. Especially since these are
		// meant to be part of the world, which usually doesn't distance culling.
		return 256;
	}
}
