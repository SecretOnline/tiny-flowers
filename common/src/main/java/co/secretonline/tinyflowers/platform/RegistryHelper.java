package co.secretonline.tinyflowers.platform;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

/**
 * Both (Neo)Forge and Fabric have their own ways of registering things to the registry, with not much overlap.
 */
public interface RegistryHelper {
	<T, U extends T> DeferredRegistryObject<U> register(Registry<T> objRegistry, Identifier id, Supplier<U> objSupplier);
}
