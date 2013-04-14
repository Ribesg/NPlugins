package fr.ribesg.bukkit.nenchantingegg.altar.transitions.steps;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import fr.ribesg.bukkit.nenchantingegg.altar.Altar;
import fr.ribesg.bukkit.nenchantingegg.altar.transitions.beans.RelativeBlock;

public class InactiveToActiveStep1 extends AltarTransitionStep {

    private Set<RelativeBlock> blockChanges;

    public InactiveToActiveStep1() {
        super(0);
    }

    // TODO Add effects
    @Override
    protected void run(final Altar altar) {
        final Location loc = altar.getLoc();
        Block b;
        for (final RelativeBlock r : getBlockChanges()) {
            b = loc.clone().add(r.getRelativeLocation()).getBlock();
            b.setType(r.getBlockMaterial());
            b.setData(r.getBlockData());
            if (r.needSpecialWork()) {
                r.doSpecialWork(b);
            }
        }
    }

    private Set<RelativeBlock> getBlockChanges() {
        if (blockChanges == null) {
            blockChanges = new HashSet<RelativeBlock>();

            // Y = 1
            //
            // -3-2-1 0 1 2 3 Z/X
            //
            //    G       G     2
            //
            // G -> Glowstone

            blockChanges.add(new RelativeBlock(2, 1, -2, Material.GLOWSTONE));
            blockChanges.add(new RelativeBlock(2, 1, 2, Material.GLOWSTONE));

            // ##########################################

            // Y = 2
            //
            // -3-2-1 0 1 2 3 Z/X
            //
            //  G           G   0
            //
            // G -> Glowstone

            blockChanges.add(new RelativeBlock(0, 2, -3, Material.GLOWSTONE));
            blockChanges.add(new RelativeBlock(0, 2, 3, Material.GLOWSTONE));

            // ##########################################

            // Y = 3
            //
            // -2-1 0 1 2 Z/X
            //
            //  G       G  -2
            //
            // G -> Glowstone

            blockChanges.add(new RelativeBlock(-2, 3, -2, Material.GLOWSTONE));
            blockChanges.add(new RelativeBlock(-2, 3, 2, Material.GLOWSTONE));

            // ##########################################

            // Y = 4
            //
            // 0 Z/X
            //
            // G  -3
            //
            // G -> Glowstone

            blockChanges.add(new RelativeBlock(-3, 4, 0, Material.GLOWSTONE));
        }
        return blockChanges;
    }

}
