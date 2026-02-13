package co.secretonline.tinyflowers.renderer.item;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import co.secretonline.tinyflowers.components.ModComponents;
import co.secretonline.tinyflowers.components.TinyFlowerComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public record TinyFlowerProperty() implements SelectItemModelProperty<TinyFlowerComponent> {
	public static final Codec<TinyFlowerComponent> VALUE_CODEC = Identifier.CODEC.xmap(
			TinyFlowerComponent::new,
			TinyFlowerComponent::id);
	public static final SelectItemModelProperty.Type<TinyFlowerProperty, TinyFlowerComponent> TYPE = Type
			.create(MapCodec.unit(new TinyFlowerProperty()), VALUE_CODEC);

	@Override
	public @Nullable TinyFlowerComponent get(ItemStack itemStack, @Nullable ClientLevel clientLevel,
																					 @Nullable LivingEntity entity, int i, @NonNull ItemDisplayContext ctx) {
		return itemStack.get(ModComponents.TINY_FLOWER);
	}

	@Override
	public @NonNull Type<? extends SelectItemModelProperty<TinyFlowerComponent>, TinyFlowerComponent> type() {
		return TYPE;
	}

	@Override
	public @NonNull Codec<TinyFlowerComponent> valueCodec() {
		return VALUE_CODEC;
	}

}
