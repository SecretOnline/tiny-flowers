package co.secretonline.tinyflowers.renderer.block;

import org.jspecify.annotations.Nullable;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.EmptyBlockAndTintGetter;

public class TinyGardenBlockEntityRenderState extends BlockEntityRenderState {
	private Direction direction = Direction.NORTH;

	@Nullable
	private Identifier flower1 = null;
	@Nullable
	private Identifier flower2 = null;
	@Nullable
	private Identifier flower3 = null;
	@Nullable
	private Identifier flower4 = null;
	@Nullable
	private BlockAndTintGetter blockAndTintGetter = null;

	public Direction getDirection() {
		return direction;
	}

	public Identifier getFlower1() {
		return flower1;
	}

	public Identifier getFlower2() {
		return flower2;
	}

	public Identifier getFlower3() {
		return flower3;
	}

	public Identifier getFlower4() {
		return flower4;
	}

	public BlockAndTintGetter getBlockAndTintGetter() {
		if (blockAndTintGetter == null) {
			return EmptyBlockAndTintGetter.INSTANCE;
		}

		return blockAndTintGetter;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void setFlowers(Identifier flower1, Identifier flower2, Identifier flower3, Identifier flower4) {
		this.flower1 = flower1;
		this.flower2 = flower2;
		this.flower3 = flower3;
		this.flower4 = flower4;
	}

	public void setBlockAndTintGetter(BlockAndTintGetter getter) {
		this.blockAndTintGetter = getter;
	}
}
