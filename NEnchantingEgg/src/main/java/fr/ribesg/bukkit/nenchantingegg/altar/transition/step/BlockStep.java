package fr.ribesg.bukkit.nenchantingegg.altar.transition.step;

import fr.ribesg.bukkit.nenchantingegg.altar.Altar;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeBlock;
import org.bukkit.block.Block;

public class BlockStep extends Step {

	private final RelativeBlock block;

	public BlockStep(final int delay, final RelativeBlock block) {
		super(delay);
		this.block = block;
	}

	@Override
	public void doStep(final Altar altar) {
		final Block b = altar.getCenterLocation().toBukkitLocation().add(block.getRelativeLocation()).getBlock();
		b.setType(block.getBlockMaterial());
		b.setData(block.getBlockData());
		if (block.needAdditionalData()) {
			block.setAdditionalData(b);
		}
	}

}
