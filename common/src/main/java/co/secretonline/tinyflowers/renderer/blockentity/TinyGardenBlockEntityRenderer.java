package co.secretonline.tinyflowers.renderer.blockentity;

import co.secretonline.tinyflowers.TinyFlowers;
import co.secretonline.tinyflowers.TinyFlowersClientState;
import co.secretonline.tinyflowers.block.TinyGardenBlock;
import co.secretonline.tinyflowers.block.entity.TinyGardenBlockEntity;
import co.secretonline.tinyflowers.platform.Services;
import co.secretonline.tinyflowers.data.TinyFlowerResources;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
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

		state.setBlockState(blockEntity.getBlockState());

		Optional<Direction> facingDirection = state.getBlockState() != null ? state.getBlockState().getOptionalValue(TinyGardenBlock.FACING) : Optional.empty();
		facingDirection.ifPresent(state::setDirection);

		state.setFlowers(blockEntity.getFlower(1), blockEntity.getFlower(2),
			blockEntity.getFlower(3), blockEntity.getFlower(4));

		if (blockEntity.getLevel() != null && blockEntity.getLevel().isClientSide()) {
			state.setBlockAndTintGetter((ClientLevel) blockEntity.getLevel());
		}
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

		int[] tintStack = state.getBlockState() != null
			? new int[]{TinyGardenColorProvider.getColor(state.getBlockState(), state.getBlockAndTintGetter(), state.blockPos, resources.tintSource())}
			: new int[0];

		List<BlockStateModelPart> parts = new ArrayList<>();
		model.collectParts(TinyFlowersClientState.RANDOM, parts);

		submitNodeCollector.submitBlockModel(poseStack, RenderTypes.cutoutMovingBlock(), parts,
			tintStack, state.lightCoords, 0, 0);
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
