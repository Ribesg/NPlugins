package fr.ribesg.bukkit.nenchantingegg.altar.transition.step;

import org.bukkit.block.Block;

import fr.ribesg.bukkit.nenchantingegg.altar.Altar;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeBlock;

public class BlockStep extends Step {

    private final RelativeBlock block;

    public BlockStep(final int delay, final RelativeBlock block) {
        super(delay);
        this.block = block;
    }

    @Override
    public void doStep(final Altar altar) {
        final Block b = altar.getCenterLocation().clone().add(block.getRelativeLocation()).getBlock();
        b.setType(block.getBlockMaterial());
        b.setData(block.getBlockData());
        if (block.needSpecialWork()) {
            block.doSpecialWork(b);
        }
    }

}
