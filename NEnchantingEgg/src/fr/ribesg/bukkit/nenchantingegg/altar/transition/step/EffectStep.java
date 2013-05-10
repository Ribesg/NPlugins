package fr.ribesg.bukkit.nenchantingegg.altar.transition.step;

import org.bukkit.Location;

import fr.ribesg.bukkit.nenchantingegg.altar.Altar;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeEffect;

public class EffectStep extends Step {

    private final RelativeEffect effect;

    public EffectStep(final int delay, final RelativeEffect effect) {
        super(delay);
        this.effect = effect;
    }

    @Override
    public void doStep(final Altar altar) {
        final Location loc = altar.getCenterLocation().clone().add(effect.getRelativeLocation());
        if (effect.hasRadius()) {
            loc.getWorld().playEffect(loc, effect.getEffect(), effect.getEffectData(), effect.getRadius());
        } else {
            loc.getWorld().playEffect(loc, effect.getEffect(), effect.getEffectData());
        }
    }

}
