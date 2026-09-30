package co.secretonline.tinyflowers.mixin.block;

import co.secretonline.tinyflowers.block.ModBlocks;
import co.secretonline.tinyflowers.block.entity.TinyFlowerPotBlockEntity;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(FlowerPotBlock.class)
public class FlowerPotBlockMixin {

	@Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
	private void tinyFlowers$useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos,
																		 Player player, InteractionHand hand, BlockHitResult hitResult,
																		 CallbackInfoReturnable<ItemInteractionResult> cir) {
		// Check vanilla's map first, so that if a mod registers its own segmentable flower as a pottable
		// flower, it'll use vanilla's implementation which feels better than using this mod's solution to
		// completely dynamic flower models.
		Map<Block, Block> pottedByContent = FlowerPotBlockAccessor.tinyFlowers$getPottedByContent();
		if (itemStack.getItem() instanceof BlockItem blockItem && pottedByContent.containsKey(blockItem.getBlock())) {
			// Default to vanilla's behaviour
			return;
		}

		TinyFlowerData flowerData = TinyFlowerData.findByItemStack(level.registryAccess(), itemStack);
		if (flowerData == null || !flowerData.canBePotted()) {
			// No match for tiny flowers either, continue vanilla code
			return;
		}

		if (!((FlowerPotBlockAccessor)this).tinyFlowers$isEmpty()) {
			// Flower Pot is already full, so don't do anything
			cir.setReturnValue(ItemInteractionResult.CONSUME);
			return;
		}

		BlockState newBlockState = ModBlocks.TINY_FLOWER_POT_BLOCK.get().defaultBlockState();
		level.setBlockAndUpdate(pos, newBlockState);

		if (!(level.getBlockEntity(pos) instanceof TinyFlowerPotBlockEntity potBlockEntity)) {
			// Block entity did not get created, try revert change before it's too late
			level.setBlockAndUpdate(pos, state);
			return;
		}

		potBlockEntity.setFlower(flowerData.id());

		level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
		player.awardStat(Stats.POT_FLOWER);
		itemStack.consume(1, player);
		cir.setReturnValue(ItemInteractionResult.SUCCESS);
	}
}
