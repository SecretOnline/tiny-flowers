package co.secretonline.tinyflowers.components;

import java.util.Optional;
import java.util.function.Consumer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.helper.Survivable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

public record GardenContentsComponent(Identifier flower1, Identifier flower2, Identifier flower3, Identifier flower4)
		implements TooltipProvider, Survivable {
	public static final String GARDEN_TEXT = "block.tiny_flowers.tiny_garden";
	public static final String EMPTY_TEXT = "block.tiny_flowers.tiny_garden.empty";

	@Override
	public boolean canSurviveOn(Block supportingBlock, HolderLookup.Provider provider) {
		for (Identifier identifier : new Identifier[] { flower1, flower2, flower3, flower4 }) {
			if (identifier == null) {
				continue;
			}

			TinyFlowerData flowerData = TinyFlowerData.findById(provider, identifier);
			if (flowerData == null) {
				continue;
			}

			if (!flowerData.canSurviveOn(supportingBlock)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void addToTooltip(@NonNull TooltipContext tooltipContext, @NonNull Consumer<Component> consumer, @NonNull TooltipFlag tooltipFlag,
													 @NonNull DataComponentGetter dataComponentGetter) {

		for (Identifier id : new Identifier[] { flower1, flower2, flower3, flower4 }) {
			if (id == null) {
				MutableComponent empty = Component.translatable(EMPTY_TEXT);
				empty.withStyle(ChatFormatting.GRAY);
				consumer.accept(empty);
				continue;
			}

			MutableComponent text = Component.translatable(Util.makeDescriptionId("block", id));
			consumer.accept(text);
		}
	}

	public static final Codec<GardenContentsComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			Identifier.CODEC.optionalFieldOf("flower_1").forGetter((value) -> Optional.ofNullable(value.flower1())),
			Identifier.CODEC.optionalFieldOf("flower_2").forGetter((value) -> Optional.ofNullable(value.flower2())),
			Identifier.CODEC.optionalFieldOf("flower_3").forGetter((value) -> Optional.ofNullable(value.flower3())),
			Identifier.CODEC.optionalFieldOf("flower_4").forGetter((value) -> Optional.ofNullable(value.flower4())))
			.apply(builder,
					(optional1, optional2, optional3, optional4) -> new GardenContentsComponent(
							optional1.orElse(null), optional2.orElse(null), optional3.orElse(null), optional4.orElse(null))));
}
