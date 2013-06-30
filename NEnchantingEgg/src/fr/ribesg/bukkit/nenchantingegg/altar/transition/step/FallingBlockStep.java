package fr.ribesg.bukkit.nenchantingegg.altar.transition.step;

import fr.ribesg.bukkit.nenchantingegg.altar.Altar;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeBlock;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;

public class FallingBlockStep extends Step {

    private final RelativeBlock block;
    private final int           fromHeight;
    private final Vector        initialVelocity;

    public FallingBlockStep(final int delay, final RelativeBlock block, final int fromHeight) {
        this(delay, block, fromHeight, 0f);
    }

    public FallingBlockStep(final int delay, final RelativeBlock block, final int fromHeight, final double initialVerticalVelocity) {
        super(delay);
        this.block = block;
        this.fromHeight = fromHeight < 0 ? 0 : fromHeight;
        initialVelocity = new Vector(0, initialVerticalVelocity, 0);
    }

    public FallingBlockStep(final int delay, final RelativeBlock block, final int fromHeight, final Vector initialVelocity) {
        super(delay);
        this.block = block;
        this.fromHeight = fromHeight < 0 ? 0 : fromHeight;
        this.initialVelocity = initialVelocity;
    }

    @Override
    public void doStep(final Altar altar) {
        final Location locBlock = altar.getCenterLocation().clone().add(block.getRelativeLocation());
        locBlock.getBlock().setType(Material.AIR);
        locBlock.getWorld().playEffect(locBlock, Effect.MOBSPAWNER_FLAMES, (byte) 4);
        locBlock.getWorld().playSound(locBlock, Sound.IRONGOLEM_THROW, 1.0f, 1.0f);

        final Location locSpawn = locBlock.clone().add(0, fromHeight, 0);
        if (locSpawn.getY() >= locSpawn.getWorld().getMaxHeight()) {
            locSpawn.setY(locSpawn.getWorld().getMaxHeight() - 1);
        }
        final FallingBlock fb = locSpawn.getWorld().spawnFallingBlock(locSpawn, block.getBlockMaterial(), block.getBlockData());
        fb.setDropItem(false);
        fb.setVelocity(initialVelocity);

    }

}
