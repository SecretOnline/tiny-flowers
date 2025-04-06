package co.secretonline.tinyflowers.components;

import java.util.List;
import java.util.function.Consumer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import co.secretonline.tinyflowers.blocks.FlowerVariant;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Since Minecraft 1.21.5, the tooltip system has been changed to use
 * components, so in order to show the flower variants in a tooltip we need a
 * component.
 */
public record TinyFlowersComponent(List<FlowerVariant> flowers) implements TooltipAppender {
	private static final int MAX_FLOWERS_IN_TOOLTIP = 4;

	public static final TinyFlowersComponent EMPTY = new TinyFlowersComponent(List.of());

	public static final TinyFlowersComponent of(List<FlowerVariant> flowers) {
		return new TinyFlowersComponent(flowers);
	}

	public static final Codec<TinyFlowersComponent> CODEC = RecordCodecBuilder.create(builder -> {
		return builder.group(
				FlowerVariant.CODEC.listOf().fieldOf("flower_variants").forGetter(TinyFlowersComponent::flowers))
				.apply(builder, TinyFlowersComponent::new);
	});

	@Override
	public void appendTooltip(TooltipContext context, Consumer<Text> textConsumer,
			TooltipType type, ComponentsAccess components) {
		for (int i = 0; i < Math.min(MAX_FLOWERS_IN_TOOLTIP, flowers.size()); i++) {
			FlowerVariant flower = flowers.get(i);
			MutableText text = Text.translatable(flower.getTranslationKey());

			if (flower.isEmpty()) {
				text.formatted(Formatting.GRAY);
			}

			textConsumer.accept(text);
		}

		if (flowers.size() > MAX_FLOWERS_IN_TOOLTIP) {
			textConsumer.accept(Text.translatable("item.container.more_items", flowers.size() - MAX_FLOWERS_IN_TOOLTIP)
					.formatted(Formatting.ITALIC));
		}
	}
}
