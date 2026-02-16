package co.secretonline.tinyflowers.helper;

import co.secretonline.tinyflowers.TinyFlowersClientState;
import co.secretonline.tinyflowers.block.ModBlocks;
import co.secretonline.tinyflowers.block.entity.TinyGardenBlockEntity;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class ParticleHelper {
	public static @Nullable TextureAtlasSprite getOverrideSprite(@NonNull ClientLevel level, @NonNull BlockPos blockPos) {
		BlockState blockState = level.getBlockState(blockPos);
		if (!(blockState.is(ModBlocks.TINY_GARDEN_BLOCK.get()))) {
			return null;
		}

		if (!(level.getBlockEntity(blockPos) instanceof TinyGardenBlockEntity gardenBlockEntity)) {
			// If there's no block entity, don't do anything
			return null;
		}

		// Select a random flower variant to render as the particle
		List<Identifier> flowers = gardenBlockEntity.getFlowers();
		if (flowers.isEmpty()) {
			return null;
		}

		Identifier flowerId = Util.getRandom(flowers, TinyFlowersClientState.RANDOM);
		TinyFlowerData flowerData = TinyFlowerData.findById(level.registryAccess(), flowerId);
		if (flowerData == null) {
			return null;
		}

		Minecraft client = Minecraft.getInstance();
		ItemStack stack = flowerData.getItemStack(1);

		TinyFlowersClientState.ITEM_RENDER_STATE.clear();
		client.getItemModelResolver()
			.appendItemLayers(TinyFlowersClientState.ITEM_RENDER_STATE, stack,
				ItemDisplayContext.GROUND, level, null, 0);

		return TinyFlowersClientState.ITEM_RENDER_STATE.pickParticleIcon(TinyFlowersClientState.RANDOM);
	}
}
