package co.secretonline.tinyflowers.blocks;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import co.secretonline.tinyflowers.components.GardenContentsComponent;
import co.secretonline.tinyflowers.components.ModComponents;
import co.secretonline.tinyflowers.components.TinyFlowerComponent;
import co.secretonline.tinyflowers.data.TinyFlowerData;
import co.secretonline.tinyflowers.helper.Survivable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap.Builder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SegmentableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class TinyGardenBlockEntity extends BlockEntity implements Survivable {
	public static int NUM_SLOTS = 4;

	@Nullable
	private Identifier flower1 = null;
	@Nullable
	private Identifier flower2 = null;
	@Nullable
	private Identifier flower3 = null;
	@Nullable
	private Identifier flower4 = null;

	public TinyGardenBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.TINY_GARDEN_BLOCK_ENTITY, pos, state);
	}

	public List<Identifier> getFlowers() {
		List<Identifier> list = new ArrayList<>(NUM_SLOTS);

		if (flower1 != null) {
			list.add(flower1);
		}
		if (flower2 != null) {
			list.add(flower2);
		}
		if (flower3 != null) {
			list.add(flower3);
		}
		if (flower4 != null) {
			list.add(flower4);
		}

		return list;
	}

	@Override
	public boolean canSurviveOn(Block supportingBlock, HolderLookup.Provider provider) {
		for (Identifier identifier : this.getFlowers()) {
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

	@Nullable
	public Identifier getFlower(int index) {
		switch (index) {
			case 1:
				return flower1;
			case 2:
				return flower2;
			case 3:
				return flower3;
			case 4:
				return flower4;
			default:
				throw new IndexOutOfBoundsException(index);
		}
	}

	public void setFlower(int index, @Nullable Identifier id) {
		switch (index) {
			case 1:
				flower1 = id;
				break;
			case 2:
				flower2 = id;
				break;
			case 3:
				flower3 = id;
				break;
			case 4:
				flower4 = id;
				break;
			default:
				throw new IndexOutOfBoundsException(index);
		}

		this.markUpdated();
	}

	public boolean addFlower(Identifier newId) {
		if (flower1 == null) {
			setFlower(1, newId);
			return true;
		}
		if (flower2 == null) {
			setFlower(2, newId);
			return true;
		}
		if (flower3 == null) {
			setFlower(3, newId);
			return true;
		}
		if (flower4 == null) {
			setFlower(4, newId);
			return true;
		}

		return false;
	}

	public boolean isEmpty() {
		return flower1 == null &&
				flower2 == null &&
				flower3 == null &&
				flower4 == null;
	}

	public boolean isFull() {
		return flower1 != null &&
				flower2 != null &&
				flower3 != null &&
				flower4 != null;
	}

	@Override
	protected void saveAdditional(ValueOutput writeView) {
		super.saveAdditional(writeView);

		writeView.storeNullable("flower_1", Identifier.CODEC, flower1);
		writeView.storeNullable("flower_2", Identifier.CODEC, flower2);
		writeView.storeNullable("flower_3", Identifier.CODEC, flower3);
		writeView.storeNullable("flower_4", Identifier.CODEC, flower4);
	}

	@Override
	protected void loadAdditional(ValueInput readView) {
		super.loadAdditional(readView);

		flower1 = readView.read("flower_1", Identifier.CODEC).orElse(null);
		flower2 = readView.read("flower_2", Identifier.CODEC).orElse(null);
		flower3 = readView.read("flower_3", Identifier.CODEC).orElse(null);
		flower4 = readView.read("flower_4", Identifier.CODEC).orElse(null);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
		return saveWithoutMetadata(registryLookup);
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	protected void applyImplicitComponents(DataComponentGetter dataComponentGetter) {
		super.applyImplicitComponents(dataComponentGetter);

		GardenContentsComponent gardenComponent = dataComponentGetter.get(ModComponents.GARDEN_CONTENTS);
		if (gardenComponent != null) {
			setFlower(1, gardenComponent.flower1());
			setFlower(2, gardenComponent.flower2());
			setFlower(3, gardenComponent.flower3());
			setFlower(4, gardenComponent.flower4());
		} else {
			TinyFlowerComponent itemComponent = dataComponentGetter.get(ModComponents.TINY_FLOWER);
			if (itemComponent != null) {
				addFlower(itemComponent.id());
			}
		}
	}

	@Override
	protected void collectImplicitComponents(Builder builder) {
		super.collectImplicitComponents(builder);

		builder.set(ModComponents.GARDEN_CONTENTS, new GardenContentsComponent(flower1, flower2, flower3, flower4));
	}

	@Override
	public void removeComponentsFromTag(ValueOutput valueOutput) {
		valueOutput.discard("flower1");
		valueOutput.discard("flower2");
		valueOutput.discard("flower3");
		valueOutput.discard("flower4");
	}

	private void markUpdated() {
		this.setChanged();
		this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
	}

	public boolean setFromPreviousBlockState(RegistryAccess registryAccess, BlockState state) {
		Block block = state.getBlock();

		TinyFlowerData tinyFlowerData = TinyFlowerData.findByOriginalBlock(registryAccess, block);
		if (tinyFlowerData == null) {
			return false;
		}

		Identifier id = tinyFlowerData.id();
		int amount = block instanceof SegmentableBlock segmentedBlock
				? state.getValue(segmentedBlock.getSegmentAmountProperty())
				: NUM_SLOTS;

		setFlower(1, amount >= 1 ? id : null);
		setFlower(2, amount >= 2 ? id : null);
		setFlower(3, amount >= 3 ? id : null);
		setFlower(4, amount >= 4 ? id : null);

		return true;
	}
}
