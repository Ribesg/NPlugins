package fr.ribesg.bukkit.nenchantingegg.altar.transition.step;

import fr.ribesg.bukkit.nenchantingegg.altar.Altar;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeSound;
import org.bukkit.Location;

public class SoundStep extends Step {

    private final RelativeSound sound;

    public SoundStep(final int delay, final RelativeSound effect) {
        super(delay);
        sound = effect;
    }

    @Override
    public void doStep(final Altar altar) {
        final Location loc = altar.getCenterLocation().toBukkitLocation().add(sound.getRelativeLocation());
        loc.getWorld().playSound(loc, sound.getSound(), sound.getVolume(), sound.getPitch());
    }
}
