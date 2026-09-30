package co.secretonline.tinyflowers.block.entity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import co.secretonline.tinyflowers.data.TinyFlowerHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.PinkPetalsBlock;

import co.secretonline.tinyflowers.item.component.GardenContentsComponent;
import co.secretonline.tinyflowers.item.component.ModComponents;
import co.secretonline.tinyflowers.item.component.TinyFlowerComponent;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.data.Survivable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentMap.Builder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TinyGardenBlockEntity extends BlockEntity implements Survivable, TinyFlowerHolder {
	public static final int NUM_TINY_FLOWER_SLOTS = 4;

	private final ResourceLocation[] flowers = new ResourceLocation[4];

	public TinyGardenBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.TINY_GARDEN_BLOCK_ENTITY.get(), pos, state);
	}

	public List<ResourceLocation> getFlowers() {
		return Arrays.stream(flowers).filter(Objects::nonNull).toList();
	}

	@Override
	public boolean canSurviveOn(LevelReader level, BlockPos pos) {
		for (ResourceLocation identifier : this.getFlowers()) {
			TinyFlowerData flowerData = TinyFlowerData.findById(level.registryAccess(), identifier);
			if (flowerData == null) {
				continue;
			}

			if (!flowerData.canSurviveOn(level, pos)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public int getSize() {
		return NUM_TINY_FLOWER_SLOTS;
	}

	public ResourceLocation getFlower(int index) {
		if (index >= 0 && index < getSize()) {
			return flowers[index];
		}

		throw new IndexOutOfBoundsException(index);
	}

	public void setFlower(int index, ResourceLocation id) {
		if (index >= 0 && index < getSize()) {
			this.flowers[index] = id;

			this.markUpdated();
			return;
		}

		throw new IndexOutOfBoundsException(index);
	}

	public boolean addFlower(ResourceLocation newId) {
		int size = getSize();
		for (int i = 0; i < size; i++) {
			if (flowers[i] == null) {
				setFlower(i, newId);
				return true;
			}
		}

		return false;
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
		super.saveAdditional(tag, registries);

		int size = getSize();
		for (int i = 0; i < size; i++) {
			ResourceLocation id = flowers[i];
			if (id != null) {
				tag.putString("flower_" + (i + 1), id.toString());
			}
		}
	}

	@Override
	protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
		super.loadAdditional(tag, registries);

		int size = getSize();
		for (int i = 0; i < size; i++) {
			String key = "flower_" + (i + 1);
			if (tag.contains(key)) {
				flowers[i] = ResourceLocation.tryParse(tag.getString(key));
			} else {
				flowers[i] = null;
			}
		}
	}

	@Override
	public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registryLookup) {
		return saveWithoutMetadata(registryLookup);
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	protected void applyImplicitComponents(@NotNull DataComponentInput dataComponentGetter) {
		super.applyImplicitComponents(dataComponentGetter);

		GardenContentsComponent gardenComponent = dataComponentGetter.get(ModComponents.GARDEN_CONTENTS.get());
		if (gardenComponent != null) {
			setFlower(0, gardenComponent.flower1());
			setFlower(1, gardenComponent.flower2());
			setFlower(2, gardenComponent.flower3());
			setFlower(3, gardenComponent.flower4());
		} else {
			TinyFlowerComponent itemComponent = dataComponentGetter.get(ModComponents.TINY_FLOWER.get());
			if (itemComponent != null) {
				addFlower(itemComponent.id());
			}
		}
	}

	@Override
	protected void collectImplicitComponents(@NotNull Builder builder) {
		super.collectImplicitComponents(builder);

		builder.set(ModComponents.GARDEN_CONTENTS.get(), new GardenContentsComponent(flowers[0], flowers[1], flowers[2], flowers[3]));
	}

	@Override
	public void removeComponentsFromTag(@NotNull CompoundTag tag) {
		int size = getSize();

		for (int i = 0; i < size; i++) {
			tag.remove("flower_" + (i + 1));
		}
	}

	private void markUpdated() {
		this.setChanged();
		Level level = this.getLevel();
		if (level != null) {
			level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
		}
	}

	public boolean setFromPreviousBlockState(RegistryAccess registryAccess, BlockState state) {
		Block block = state.getBlock();

		TinyFlowerData tinyFlowerData = TinyFlowerData.findByOriginalBlock(registryAccess, block);
		if (tinyFlowerData == null) {
			return false;
		}

		int size = getSize();
		ResourceLocation id = tinyFlowerData.id();
		int amount = block instanceof PinkPetalsBlock segmentedBlock
			? state.getValue(PinkPetalsBlock.AMOUNT)
			: size;


		for (int i = 0; i < size; i++) {
			setFlower(i, amount > i ? id : null);
		}

		return true;
	}
}
