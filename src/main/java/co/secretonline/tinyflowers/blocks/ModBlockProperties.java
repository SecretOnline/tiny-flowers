package co.secretonline.tinyflowers.blocks;

import net.minecraft.world.level.block.state.properties.EnumProperty;

public class ModBlockProperties {
	public static final EnumProperty<FlowerVariant> FLOWER_VARIANT_1 = EnumProperty.create(
			"flower_variant_1",
			FlowerVariant.class);
	public static final EnumProperty<FlowerVariant> FLOWER_VARIANT_2 = EnumProperty.create(
			"flower_variant_2",
			FlowerVariant.class);
	public static final EnumProperty<FlowerVariant> FLOWER_VARIANT_3 = EnumProperty.create(
			"flower_variant_3",
			FlowerVariant.class);
	public static final EnumProperty<FlowerVariant> FLOWER_VARIANT_4 = EnumProperty.create(
			"flower_variant_4",
			FlowerVariant.class);
}
