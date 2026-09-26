package co.secretonline.tinyflowers.block.entity;

import co.secretonline.tinyflowers.data.TinyFlowerHolder;
import co.secretonline.tinyflowers.item.component.ModComponents;
import co.secretonline.tinyflowers.item.component.TinyFlowerComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap.Builder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class TinyFlowerPotBlockEntity extends BlockEntity implements TinyFlowerHolder {
	@Nullable
	private Identifier flower;

	public TinyFlowerPotBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.TINY_FLOWER_POT_BLOCK_ENTITY.get(), pos, state);
	}

	@Override
	public int getSize() {
		return 1;
	}

	@Override
	@Nullable
	public Identifier getFlower(int index) {
		return index == 0 ? flower : null;
	}

	@Override
	public void setFlower(int index, @Nullable Identifier id) {
		if (index == 0) {
			this.setFlower(id);
		}
	}

	@Nullable
	public Identifier getFlower() {
		return flower;
	}

	public void setFlower(@Nullable Identifier id) {
		flower = id;

		this.markUpdated();
	}

	@Override
	protected void saveAdditional(@NonNull ValueOutput writeView) {
		super.saveAdditional(writeView);

		writeView.storeNullable("flower", Identifier.CODEC, flower);
	}

	@Override
	protected void loadAdditional(@NonNull ValueInput readView) {
		super.loadAdditional(readView);

		flower = readView.read("flower", Identifier.CODEC).orElse(null);
	}

	@Override
	public @NonNull CompoundTag getUpdateTag(HolderLookup.@NonNull Provider registryLookup) {
		return saveWithoutMetadata(registryLookup);
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	protected void applyImplicitComponents(@NonNull DataComponentGetter dataComponentGetter) {
		super.applyImplicitComponents(dataComponentGetter);

			TinyFlowerComponent itemComponent = dataComponentGetter.get(ModComponents.TINY_FLOWER.get());
			if (itemComponent != null) {
				flower = itemComponent.id();
			}
	}

	@Override
	protected void collectImplicitComponents(@NonNull Builder builder) {
		super.collectImplicitComponents(builder);

		builder.set(ModComponents.TINY_FLOWER.get(), new TinyFlowerComponent(flower));
	}

	@Override
	public void removeComponentsFromTag(ValueOutput valueOutput) {
		valueOutput.discard("flower");
	}

	private void markUpdated() {
		this.setChanged();
		Level level = this.getLevel();
		if (level != null) {
			level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
		}
	}
}
