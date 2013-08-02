package fr.ribesg.bukkit.nenchantingegg.altar.transition.step;

import fr.ribesg.bukkit.nenchantingegg.altar.Altar;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeLocation;

public class LightningStep extends Step {

    private final RelativeLocation loc;

    public LightningStep(final int delay, final RelativeLocation loc) {
        super(delay);
        this.loc = loc;
    }

    @Override
    public void doStep(final Altar altar) {
        altar.getCenterLocation()
             .getWorld()
             .strikeLightningEffect(altar.getCenterLocation().toBukkitLocation().add(loc.getRelativeLocation()));
    }

}
