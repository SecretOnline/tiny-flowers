package co.secretonline.tinyflowers.init;

import co.secretonline.tinyflowers.renderer.block.ModBlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;

public class FabricTinyFlowersClientInitializer extends TinyFlowersClientInitializer {
	@Override
	void registerSelectItemModelProperty(Identifier id, SelectItemModelProperty.Type<?, ?> propertyType) {
		SelectItemModelProperties.ID_MAPPER.put(id, propertyType);
	}

	@Override
	<T extends BlockEntity, S extends BlockEntityRenderState> void registerBlockEntityRenderer(ModBlockEntityRenderers.BlockEntityRenderInfo<T, S> info) {
		BlockEntityRenderers.register(info.typeSupplier().get(), info.renderer());
	}
}
