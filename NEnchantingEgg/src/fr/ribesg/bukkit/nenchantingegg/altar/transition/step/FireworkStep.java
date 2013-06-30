package fr.ribesg.bukkit.nenchantingegg.altar.transition.step;

import fr.ribesg.bukkit.nenchantingegg.altar.Altar;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeFirework;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkStep extends Step {

    private final RelativeFirework firework;

    public FireworkStep(final int delay, final RelativeFirework firework) {
        super(delay);
        this.firework = firework;
    }

    @Override
    public void doStep(final Altar altar) {
        final Location loc = altar.getCenterLocation().clone().add(firework.getRelativeLocation());
        final Firework f = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        if (f != null) {
            if (firework.hasVelocity()) {
                f.setVelocity(firework.getVelocity());
            }
            final FireworkMeta meta = f.getFireworkMeta();
            meta.addEffects(firework.getEffects());
            meta.setPower(0);
            f.setFireworkMeta(meta);
        }
    }
}
