package co.secretonline.tinyflowers.blocks;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class TinyGardenBlockEntity extends BlockEntity {
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

	@Nullable
	public Identifier getFlower1() {
		return flower1;
	}

	public void setFlower1(@Nullable Identifier flower1) {
		this.flower1 = flower1;
	}

	@Nullable
	public Identifier getFlower2() {
		return flower2;
	}

	public void setFlower2(@Nullable Identifier flower2) {
		this.flower2 = flower2;
	}

	@Nullable
	public Identifier getFlower3() {
		return flower3;
	}

	public void setFlower3(@Nullable Identifier flower3) {
		this.flower3 = flower3;
	}

	@Nullable
	public Identifier getFlower4() {
		return flower4;
	}

	public void setFlower4(@Nullable Identifier flower4) {
		this.flower4 = flower4;
	}

	@Override
	protected void saveAdditional(ValueOutput writeView) {
		writeView.storeNullable("flower_1", Identifier.CODEC, flower1);
		writeView.storeNullable("flower_2", Identifier.CODEC, flower2);
		writeView.storeNullable("flower_3", Identifier.CODEC, flower3);
		writeView.storeNullable("flower_4", Identifier.CODEC, flower4);

		super.saveAdditional(writeView);
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
}
