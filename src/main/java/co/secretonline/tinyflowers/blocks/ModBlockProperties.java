package co.secretonline.tinyflowers.blocks;

import net.minecraft.state.property.EnumProperty;

public class ModBlockProperties {
	public static final EnumProperty<FlowerVariant> FLOWER_VARIANT_1 = EnumProperty.of(
			"flower_variant_1",
			FlowerVariant.class,
			// First flower variant must not be empty
			(variant) -> variant != FlowerVariant.EMPTY);
	public static final EnumProperty<FlowerVariant> FLOWER_VARIANT_2 = EnumProperty.of(
			"flower_variant_2",
			FlowerVariant.class);
	public static final EnumProperty<FlowerVariant> FLOWER_VARIANT_3 = EnumProperty.of(
			"flower_variant_3",
			FlowerVariant.class);
	public static final EnumProperty<FlowerVariant> FLOWER_VARIANT_4 = EnumProperty.of(
			"flower_variant_4",
			FlowerVariant.class);
}
