package co.secretonline.tinyflowers.blocks;

import co.secretonline.tinyflowers.TinyFlowers;
import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.apache.commons.lang3.function.TriConsumer;

import java.util.function.Supplier;

public class ModBlocks {
	private static final Identifier TINY_GARDEN_ID = TinyFlowers.id("tiny_garden");
	public static final Supplier<Block> TINY_GARDEN_BLOCK = Suppliers.memoize(()-> new TinyGardenBlock(BlockBehaviour.Properties.of()
		.mapColor(MapColor.PLANT)
		.noCollision()
		.sound(SoundType.PINK_PETALS)
		.pushReaction(PushReaction.DESTROY)
		.randomTicks()
		.setId(ResourceKey.create(Registries.BLOCK, TINY_GARDEN_ID))));

	public static void register(TriConsumer<Identifier, Supplier<Block>, MapCodec<? extends Block>> register) {
		register.accept(TINY_GARDEN_ID, TINY_GARDEN_BLOCK, TinyGardenBlock.CODEC);
	}
}
