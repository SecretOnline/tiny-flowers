package co.secretonline.tinyflowers.mixin.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(FlowerPotBlock.class)
public interface FlowerPotBlockAccessor {
	@Invoker("isEmpty")
	boolean tinyFlowers$isEmpty();

	@Accessor("POTTED_BY_CONTENT")
	static Map<Block, Block> tinyFlowers$getPottedByContent()
	{
		throw new AssertionError("Untransformed @Accessor");
	}
}
