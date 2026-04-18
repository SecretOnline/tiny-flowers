package co.secretonline.tinyflowers.renderer.blockentity;

import co.secretonline.tinyflowers.block.entity.ModBlockEntities;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModBlockEntityRenderers {
	public static void registerClient(Consumer<BlockEntityRenderInfo<?, ?>> register) {
		register.accept(new BlockEntityRenderInfo<>(ModBlockEntities.TINY_GARDEN_BLOCK_ENTITY, TinyGardenBlockEntityRenderer::new));
	}

	public record BlockEntityRenderInfo<T extends BlockEntity, S extends BlockEntityRenderState>(
		Supplier<? extends BlockEntityType<? extends T>> typeSupplier,
		BlockEntityRendererProvider<T, S> renderer) {
	}
}
