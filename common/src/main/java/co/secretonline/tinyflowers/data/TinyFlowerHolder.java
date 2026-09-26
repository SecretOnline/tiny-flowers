package co.secretonline.tinyflowers.data;

import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public interface TinyFlowerHolder {
	int getSize();

	@Nullable
	Identifier getFlower(int index);

	void setFlower(int index, @Nullable Identifier id);

	default boolean isFull() {
		int size = getSize();

		for (int i = 0; i < size; i++) {
			if (getFlower(i) == null) {
				return false;
			}
		}

		return true;
	}

	default boolean isEmpty() {
		int size = getSize();

		for (int i = 0; i < size; i++) {
			if (getFlower(i) != null) {
				return false;
			}
		}

		return true;
	}
}
