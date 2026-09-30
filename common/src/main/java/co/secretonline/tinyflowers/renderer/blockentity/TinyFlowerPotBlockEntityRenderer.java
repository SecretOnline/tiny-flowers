package co.secretonline.tinyflowers.renderer.blockentity;

import co.secretonline.tinyflowers.TinyFlowersClientState;
import co.secretonline.tinyflowers.block.entity.TinyFlowerPotBlockEntity;
import co.secretonline.tinyflowers.data.TinyFlowerResources;
import co.secretonline.tinyflowers.helper.RenderHelper;
import co.secretonline.tinyflowers.platform.ClientServiceLoader;
import co.secretonline.tinyflowers.renderer.block.TinyFlowersColorProvider;
import com.mojang.blaze3d.vertex.PoseStack;
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

public class TinyFlowerPotBlockEntityRenderer
	implements BlockEntityRenderer<TinyFlowerPotBlockEntity, TinyFlowerPotBlockEntityRenderState> {

	public TinyFlowerPotBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public @NonNull TinyFlowerPotBlockEntityRenderState createRenderState() {
		return new TinyFlowerPotBlockEntityRenderState();
	}

	@Override
	public void extractRenderState(@NonNull TinyFlowerPotBlockEntity blockEntity,
	                               @NonNull TinyFlowerPotBlockEntityRenderState state, float tickProgress, @NonNull Vec3 cameraPos,
	                               @Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);

		state.setFlower(blockEntity.getFlower());
	}

	@Override
	public void submit(@NonNull TinyFlowerPotBlockEntityRenderState blockEntityRenderState, @NonNull PoseStack poseStack,
										 @NonNull SubmitNodeCollector submitNodeCollector, @NonNull CameraRenderState cameraRenderState) {
		Identifier id = blockEntityRenderState.getFlower();
		if (id == null) {
			return;
		}

		TinyFlowerResources resources = TinyFlowersClientState.RESOURCE_INSTANCES.get(id);
		if (resources == null) {
			return;
		}

		poseStack.pushPose();

		Optional<Identifier> modelPotted = resources.modelPotted();
		if (modelPotted.isPresent()) {
			// Render model on top of flower pot
			submitPartId(blockEntityRenderState, poseStack, modelPotted.get(), submitNodeCollector);
		} else {
			// Fallback for if there's no specific model for this Tiny Flower type
			poseStack.translate(0.25, 0.25, 0.25);
			submitPartId(blockEntityRenderState, poseStack, resources.model1(), submitNodeCollector);
		}

		poseStack.popPose();
	}

	private void submitPartId(TinyFlowerPotBlockEntityRenderState state, PoseStack poseStack,
														Identifier partId,
														SubmitNodeCollector submitNodeCollector) {
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
		return 64;
	}
}
