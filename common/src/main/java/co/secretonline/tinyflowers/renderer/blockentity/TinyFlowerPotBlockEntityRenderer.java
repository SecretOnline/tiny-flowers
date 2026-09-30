package co.secretonline.tinyflowers.renderer.blockentity;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.TinyFlowersClientState;
import co.secretonline.tinyflowers.block.entity.TinyFlowerPotBlockEntity;
import co.secretonline.tinyflowers.data.TinyFlowerResources;
import co.secretonline.tinyflowers.helper.RenderHelper;
import co.secretonline.tinyflowers.platform.ClientServiceLoader;
import co.secretonline.tinyflowers.renderer.block.TinyFlowersColorProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TinyFlowerPotBlockEntityRenderer
	implements BlockEntityRenderer<TinyFlowerPotBlockEntity> {

	private static final ModelResourceLocation FLOWER_POT_MODEL = new ModelResourceLocation(TinyFlowers.id("tiny_flower_pot"), "");

	BlockEntityRendererProvider.Context context;

	public TinyFlowerPotBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		this.context = context;
	}

	public TinyFlowerPotBlockEntityRenderState createRenderState() {
		return new TinyFlowerPotBlockEntityRenderState();
	}

	public void extractRenderState(TinyFlowerPotBlockEntity blockEntity,
																 TinyFlowerPotBlockEntityRenderState state, float tickProgress,
																 int lightCoords, int overlay) {
		state.extractRenderState(blockEntity, tickProgress, lightCoords, overlay);

		state.setFlower(blockEntity.getFlower());
	}

	public void submit(TinyFlowerPotBlockEntityRenderState blockEntityRenderState, PoseStack poseStack,
										 VertexConsumer consumer) {
		ResourceLocation id = blockEntityRenderState.getFlower();
		if (id == null) {
			return;
		}

		TinyFlowerResources resources = TinyFlowersClientState.RESOURCE_INSTANCES.get(id);
		if (resources == null) {
			return;
		}

		poseStack.pushPose();

		Optional<ResourceLocation> modelPotted = resources.modelPotted();
		if (modelPotted.isPresent()) {
			// Render model on top of flower pot
			submitPartId(blockEntityRenderState, poseStack, modelPotted.get(), consumer);
		} else {
			// Fallback for if there's no specific model for this Tiny Flower type
			poseStack.translate(0.25, 0.25, 0.25);
			submitPartId(blockEntityRenderState, poseStack, resources.model1(), consumer);
		}

		poseStack.popPose();
	}

	private void submitPartId(TinyFlowerPotBlockEntityRenderState state, PoseStack poseStack,
														ResourceLocation partId,
														VertexConsumer consumer) {
		Minecraft minecraft = Minecraft.getInstance();
		BakedModel model = ClientServiceLoader.FLOWER_MODELS.getModel(minecraft, partId);
		if (model == null) {
			return;
		}

		// We can only supply one tint index at a time, so just take the first one that's in the model.
		// In 1.21.1 we only have the normal grass tint.
		int tintIndex = 1;

		int tintInt = TinyFlowersColorProvider.getAverageBiomeColor(
			state.blockEntity.getBlockState(),
			minecraft.level,
			state.blockEntity.getBlockPos(),
			tintIndex
		);
		float[] tint = RenderHelper.unpackColorInt(tintInt);

		context.getBlockRenderDispatcher()
			.getModelRenderer()
			.renderModel(
				poseStack.last(), consumer,
				state.blockEntity.getBlockState(), model,
				tint[0], tint[1], tint[2],
				state.lightCoords, state.overlay);
	}

	private void submitFlowerPot(TinyFlowerPotBlockEntityRenderState state, PoseStack poseStack,
															 VertexConsumer consumer) {
		Minecraft minecraft = Minecraft.getInstance();
		BakedModel model = minecraft.getModelManager().getModel(FLOWER_POT_MODEL);

		BlockAndTintGetter level = state.blockEntity.getLevel();
		if (level == null) {
			return;
		}

		context.getBlockRenderDispatcher()
			.getModelRenderer()
			.tesselateWithoutAO(level, model,
				state.blockEntity.getBlockState(), state.blockEntity.getBlockPos(),
				poseStack, consumer, false,
				TinyFlowersClientState.RANDOM, -1, state.overlay);
	}

	@Override
	public void render(@NotNull TinyFlowerPotBlockEntity blockEntity, float tickProgress, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int lightCoords, int overlay) {
		TinyFlowerPotBlockEntityRenderState renderState = createRenderState();
		extractRenderState(blockEntity, renderState, tickProgress, lightCoords, overlay);

		// In 1.21.1 we do need to render the base flowerpot ourselves.
		submitFlowerPot(renderState, poseStack, multiBufferSource.getBuffer(RenderType.solid()));

		submit(renderState, poseStack, multiBufferSource.getBuffer(RenderType.cutout()));
	}

	@Override
	public int getViewDistance() {
		return 64;
	}
}
