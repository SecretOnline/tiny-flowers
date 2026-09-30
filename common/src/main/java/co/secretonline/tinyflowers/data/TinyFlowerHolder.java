package co.secretonline.tinyflowers.data;

import net.minecraft.resources.ResourceLocation;

public interface TinyFlowerHolder {
	int getSize();

	ResourceLocation getFlower(int index);

	void setFlower(int index, ResourceLocation id);

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
