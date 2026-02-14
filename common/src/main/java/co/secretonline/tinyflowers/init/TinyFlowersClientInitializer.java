package co.secretonline.tinyflowers.init;

import co.secretonline.tinyflowers.renderer.block.ModBlockEntityRenderers;
import co.secretonline.tinyflowers.renderer.item.ModSelectItemModelProperties;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class TinyFlowersClientInitializer {
	public void initializeClient() {
		ModSelectItemModelProperties.registerClient(this::registerSelectItemModelProperty);
		ModBlockEntityRenderers.registerClient(this::registerBlockEntityRenderer);
	}

	abstract void registerSelectItemModelProperty(Identifier id, SelectItemModelProperty.Type<?, ?> propertyType);

	abstract <T extends BlockEntity, S extends BlockEntityRenderState> void registerBlockEntityRenderer(ModBlockEntityRenderers.BlockEntityRenderInfo<T,S> info);
}
