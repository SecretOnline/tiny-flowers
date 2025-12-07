package co.secretonline.tinyflowers.components;

import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import co.secretonline.tinyflowers.blocks.FlowerVariant;
import co.secretonline.tinyflowers.blocks.GardenBlock;

/**
 * Since Minecraft 1.21.5, the tooltip system has been changed to use
 * components, so in order to show the flower variants in a tooltip we need a
 * component.
 */
public record TinyFlowersComponent(FlowerVariant flower1, FlowerVariant flower2, FlowerVariant flower3,
		FlowerVariant flower4) implements TooltipProvider {

	public static final TinyFlowersComponent EMPTY = of(
			FlowerVariant.EMPTY, FlowerVariant.EMPTY,
			FlowerVariant.EMPTY, FlowerVariant.EMPTY);

	public static final TinyFlowersComponent of(FlowerVariant flower1, FlowerVariant flower2, FlowerVariant flower3,
			FlowerVariant flower4) {
		return new TinyFlowersComponent(
				flower1 == null ? FlowerVariant.EMPTY : flower1,
				flower2 == null ? FlowerVariant.EMPTY : flower2,
				flower3 == null ? FlowerVariant.EMPTY : flower3,
				flower4 == null ? FlowerVariant.EMPTY : flower4);
	}

	public static final Codec<TinyFlowersComponent> CODEC = RecordCodecBuilder.create(builder -> {
		return builder.group(
				FlowerVariant.CODEC.fieldOf("flower_variant_1").forGetter(TinyFlowersComponent::flower1),
				FlowerVariant.CODEC.fieldOf("flower_variant_2").forGetter(TinyFlowersComponent::flower2),
				FlowerVariant.CODEC.fieldOf("flower_variant_3").forGetter(TinyFlowersComponent::flower3),
				FlowerVariant.CODEC.fieldOf("flower_variant_4").forGetter(TinyFlowersComponent::flower4))
				.apply(builder, TinyFlowersComponent::new);
	});

	public boolean isEmpty() {
		return flower1.isEmpty() && flower2.isEmpty() && flower3.isEmpty() && flower4.isEmpty();
	}

	@Override
	public void addToTooltip(TooltipContext context, Consumer<Component> textConsumer,
			TooltipFlag type, DataComponentGetter components) {
		if (this.isEmpty()) {
			// Since it's possible that garden items were created before this component was
			// added to the mod, we also need to check for variants in the block state.
			BlockItemStateProperties itemBlockState = components.get(DataComponents.BLOCK_STATE);
			if (itemBlockState != null) {
				for (EnumProperty<FlowerVariant> property : GardenBlock.FLOWER_VARIANT_PROPERTIES) {
					FlowerVariant variant = itemBlockState.get(property);
					variant = variant == null ? FlowerVariant.EMPTY : variant;

					MutableComponent text = Component.translatable(variant.getTranslationKey());
					if (variant.isEmpty()) {
						text.withStyle(ChatFormatting.GRAY);
					}

					textConsumer.accept(text);
				}
			}

			return;
		}

		FlowerVariant[] flowers = { flower1, flower2, flower3, flower4 };
		for (int i = 0; i < flowers.length; i++) {
			FlowerVariant variant = flowers[i];

			MutableComponent text = Component.translatable(variant.getTranslationKey());
			if (variant.isEmpty()) {
				text.withStyle(ChatFormatting.GRAY);
			}

			textConsumer.accept(text);
		}
	}
}
