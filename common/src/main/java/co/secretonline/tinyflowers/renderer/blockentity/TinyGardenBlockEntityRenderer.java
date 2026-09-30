package co.secretonline.tinyflowers.renderer.blockentity;

import co.secretonline.tinyflowers.TinyFlowersClientState;
import co.secretonline.tinyflowers.block.TinyGardenBlock;
import co.secretonline.tinyflowers.block.entity.TinyGardenBlockEntity;
import co.secretonline.tinyflowers.helper.RenderHelper;
import co.secretonline.tinyflowers.platform.ClientServiceLoader;
import co.secretonline.tinyflowers.data.TinyFlowerResources;
import co.secretonline.tinyflowers.renderer.block.TinyFlowersColorProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TinyGardenBlockEntityRenderer
	implements BlockEntityRenderer<TinyGardenBlockEntity> {

	BlockEntityRendererProvider.Context context;

	public TinyGardenBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		this.context = context;
	}

	public TinyGardenBlockEntityRenderState createRenderState() {
		return new TinyGardenBlockEntityRenderState();
	}

	public void extractRenderState(TinyGardenBlockEntity blockEntity,
																 TinyGardenBlockEntityRenderState state, float tickProgress,
																 int lightCoords, int overlay) {
		state.extractRenderState(blockEntity, tickProgress, lightCoords, overlay);

		Optional<Direction> facingDirection = blockEntity.getBlockState().getOptionalValue(TinyGardenBlock.FACING);
		facingDirection.ifPresent(state::setDirection);

		state.setFlowers(blockEntity.getFlower(0), blockEntity.getFlower(1),
			blockEntity.getFlower(2), blockEntity.getFlower(3));
	}

	public void submit(TinyGardenBlockEntityRenderState blockEntityRenderState, PoseStack poseStack,
										 VertexConsumer consumer) {
		poseStack.pushPose();

		poseStack.translate(0.5, 0, 0.5);
		float rotationDegrees = blockEntityRenderState.getDirection().toYRot();
		poseStack.mulPose(Axis.YP.rotationDegrees(180 - rotationDegrees));
		poseStack.translate(-0.5, 0, -0.5);

		submitPartForFlowerIndex(blockEntityRenderState, poseStack, consumer, 0);
		submitPartForFlowerIndex(blockEntityRenderState, poseStack, consumer, 1);
		submitPartForFlowerIndex(blockEntityRenderState, poseStack, consumer, 2);
		submitPartForFlowerIndex(blockEntityRenderState, poseStack, consumer, 3);

		poseStack.popPose();
	}

	private void submitPartForFlowerIndex(TinyGardenBlockEntityRenderState state, PoseStack poseStack,
																				VertexConsumer consumer, int index) {
		ResourceLocation id = switch (index) {
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

		ResourceLocation partId = switch (index) {
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

	@Override
	public void render(@NotNull TinyGardenBlockEntity blockEntity, float tickProgress, @NotNull PoseStack poseStack, MultiBufferSource multiBufferSource, int lightCoords, int overlay) {
		TinyGardenBlockEntityRenderState renderState = createRenderState();
		extractRenderState(blockEntity, renderState, tickProgress, lightCoords, overlay);
		submit(renderState, poseStack, multiBufferSource.getBuffer(RenderType.cutout()));
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
