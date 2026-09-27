package co.secretonline.tinyflowers.renderer.blockentity;

import co.secretonline.tinyflowers.TinyFlowersClientState;
import co.secretonline.tinyflowers.block.entity.TinyFlowerPotBlockEntity;
import co.secretonline.tinyflowers.data.TinyFlowerResources;
import co.secretonline.tinyflowers.platform.ClientServiceLoader;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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

		state.setTintStack(getTintStack(blockEntity));
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

		List<BlockStateModelPart> parts = new ArrayList<>();
		model.collectParts(TinyFlowersClientState.RANDOM, parts);

		submitNodeCollector.submitBlockModel(poseStack, RenderTypes.cutoutMovingBlock(), parts,
			state.getTintStack(), state.lightCoords, 0, 0);
	}

	@Override
	public int getViewDistance() {
		return 64;
	}

	private int[] getTintStack(BlockEntity blockEntity) {
		Level level = blockEntity.getLevel();
		BlockState blockState = blockEntity.getBlockState();
		BlockPos pos = blockEntity.getBlockPos();

		Minecraft minecraft = Minecraft.getInstance();
		BlockColors blockColors = minecraft.getBlockColors();

		List<BlockTintSource> sources = blockColors.getTintSources(blockState);

		Function<BlockTintSource, Integer> mapper = level instanceof ClientLevel clientLevel
			? source -> source.colorInWorld(blockState, clientLevel, pos)
			: source -> source.color(blockState);

		return sources.stream().map(mapper).mapToInt(Integer::intValue).toArray();
	}
}
