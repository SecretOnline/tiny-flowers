package co.secretonline.tinyflowers.block.entity;

import co.secretonline.tinyflowers.data.TinyFlowerHolder;
import co.secretonline.tinyflowers.item.component.ModComponents;
import co.secretonline.tinyflowers.item.component.TinyFlowerComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap.Builder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TinyFlowerPotBlockEntity extends BlockEntity implements TinyFlowerHolder {
	private ResourceLocation flower;

	public TinyFlowerPotBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.TINY_FLOWER_POT_BLOCK_ENTITY.get(), pos, state);
	}

	@Override
	public int getSize() {
		return 1;
	}

	@Override
	public ResourceLocation getFlower(int index) {
		return index == 0 ? flower : null;
	}

	@Override
	public void setFlower(int index, ResourceLocation id) {
		if (index == 0) {
			this.setFlower(id);
		}
	}

	public ResourceLocation getFlower() {
		return flower;
	}

	public void setFlower(ResourceLocation id) {
		flower = id;

		this.markUpdated();
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
		super.saveAdditional(tag, registries);

		if (flower != null) {
			tag.putString("flower", flower.toString());
		}
	}

	@Override
	protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
		super.loadAdditional(tag, registries);

		if (tag.contains("flower")) {
			flower = ResourceLocation.tryParse(tag.getString("flower"));
		} else {
			flower = null;
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

		TinyFlowerComponent itemComponent = dataComponentGetter.get(ModComponents.TINY_FLOWER.get());
		if (itemComponent != null) {
			flower = itemComponent.id();
		}
	}

	@Override
	protected void collectImplicitComponents(@NotNull Builder builder) {
		super.collectImplicitComponents(builder);

		builder.set(ModComponents.TINY_FLOWER.get(), new TinyFlowerComponent(flower));
	}

	@Override
	public void removeComponentsFromTag(CompoundTag tag) {
		tag.remove("flower");
	}

	private void markUpdated() {
		this.setChanged();
		Level level = this.getLevel();
		if (level != null) {
			level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
		}
	}
}
