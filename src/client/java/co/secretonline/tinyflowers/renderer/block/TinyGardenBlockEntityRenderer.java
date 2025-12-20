package co.secretonline.tinyflowers.renderer.block;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.jspecify.annotations.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import co.secretonline.tinyflowers.blocks.TinyGardenBlock;
import co.secretonline.tinyflowers.blocks.TinyGardenBlockEntity;
import co.secretonline.tinyflowers.resources.TinyFlowerResources;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

public class TinyGardenBlockEntityRenderer
		implements BlockEntityRenderer<TinyGardenBlockEntity, TinyGardenBlockEntityRenderState> {
	private final Map<Identifier, BakedModelHolder> modelsForVariant = new HashMap<>();

	public TinyGardenBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		// This map is re-created when resources are reloaded. Luckily, a new instance
		// of this class is made too.
		Map<Identifier, TinyFlowerResources> resourceMap = TinyFlowerResources.getInstances();

		// Some primitive caching for model baking, since most flowers shoudl use the
		// same models.
		Map<Identifier, ModelPart> bakedModels = new HashMap<>();
		// resourceMap.forEach((id, resource) -> {
		// for (var part : new TinyFlowerResources.Part[] { resource.part1(),
		// resource.part2(),
		// resource.part3(), resource.part4() }) {
		// if (!bakedModels.containsKey(part.model())) {
		// var location = new ModelLayerLocation(part.model(), "main");
		// var baked = context.bakeLayer(location);
		// bakedModels.put(part.model(), baked);
		// }
		// }

		// modelsForVariant.put(id, new BakedModelHolder(
		// bakedModels.get(resource.part1().model()), resource.part1().textures(),
		// bakedModels.get(resource.part2().model()), resource.part2().textures(),
		// bakedModels.get(resource.part3().model()), resource.part3().textures(),
		// bakedModels.get(resource.part4().model()), resource.part4().textures()));
		// });
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

		if (blockEntityRenderState.getFlower1() != null) {
			poseStack.pushPose();
			poseStack.scale(1 / 32f, 1 / 32f, 1 / 32f);
			poseStack.mulPose(Axis.XP.rotationDegrees(180));
			submitNodeCollector.submitText(poseStack, 24f, -20f,
					Component.literal(blockEntityRenderState.getFlower1().toString()).getVisualOrderText(),
					false,
					Font.DisplayMode.SEE_THROUGH,
					blockEntityRenderState.lightCoords,
					0xffffffff,
					1,
					0);
			poseStack.popPose();
		}
		if (blockEntityRenderState.getFlower2() != null) {
			poseStack.pushPose();
			poseStack.scale(1 / 32f, 1 / 32f, 1 / 32f);
			poseStack.mulPose(Axis.XP.rotationDegrees(180));
			poseStack.translate(0, 0, 32);
			submitNodeCollector.submitText(poseStack, 24f, -20f,
					Component.literal(blockEntityRenderState.getFlower2().toString()).getVisualOrderText(),
					false,
					Font.DisplayMode.SEE_THROUGH,
					blockEntityRenderState.lightCoords,
					0xffffffff,
					0,
					0);
			poseStack.popPose();
		}
		if (blockEntityRenderState.getFlower3() != null) {
			poseStack.pushPose();
			poseStack.scale(1 / 32f, 1 / 32f, 1 / 32f);
			poseStack.mulPose(Axis.XP.rotationDegrees(180));
			poseStack.translate(0, 0, 32);
			submitNodeCollector.submitText(poseStack, 8f, -10f,
					Component.literal(blockEntityRenderState.getFlower3().toString()).getVisualOrderText(),
					false,
					Font.DisplayMode.SEE_THROUGH,
					blockEntityRenderState.lightCoords,
					0xffffffff,
					0,
					0);
			poseStack.popPose();
		}
		if (blockEntityRenderState.getFlower4() != null) {
			poseStack.pushPose();
			poseStack.scale(1 / 32f, 1 / 32f, 1 / 32f);
			poseStack.mulPose(Axis.XP.rotationDegrees(180));
			submitNodeCollector.submitText(poseStack, 8f, -10f,
					Component.literal(blockEntityRenderState.getFlower4().toString()).getVisualOrderText(),
					false,
					Font.DisplayMode.SEE_THROUGH,
					blockEntityRenderState.lightCoords,
					0xffffffff,
					0,
					0);
			poseStack.popPose();
		}

		poseStack.popPose();
	}

	@Override
	public int getViewDistance() {
		// Hopefully this is far enough?
		// I know the whole reason this exists is for performance, but I think it's a
		// bit sad if distant gardens aren't rendered in. Expecially since these are
		// mean to be part of the world, which usually doesn't distance culling.
		return 256;
	}

	private static record BakedModelHolder(ModelPart part1, Map<String, Identifier> textures1,
			ModelPart part2, Map<String, Identifier> textures2,
			ModelPart part3, Map<String, Identifier> textures3,
			ModelPart part4, Map<String, Identifier> textures4) {
	}
}
