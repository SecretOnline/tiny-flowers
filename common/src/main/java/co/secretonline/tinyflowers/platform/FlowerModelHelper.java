package co.secretonline.tinyflowers.platform;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface FlowerModelHelper {
	<T> void registerModel(@NonNull Identifier id, @NonNull T context);

	void clear();

	@Nullable BlockStateModel getModel(@NonNull Minecraft client, @NonNull Identifier id);
}
