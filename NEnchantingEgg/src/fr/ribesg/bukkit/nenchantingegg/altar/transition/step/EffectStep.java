package fr.ribesg.bukkit.nenchantingegg.altar.transition.step;

import fr.ribesg.bukkit.nenchantingegg.altar.Altar;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeEffect;
import org.bukkit.Location;

public class EffectStep extends Step {

	private final RelativeEffect effect;

	public EffectStep(final int delay, final RelativeEffect effect) {
		super(delay);
		this.effect = effect;
	}

	@Override
	public void doStep(final Altar altar) {
		final Location loc = altar.getCenterLocation().toBukkitLocation().add(effect.getRelativeLocation());
		if (effect.hasRadius()) {
			loc.getWorld().playEffect(loc, effect.getEffect(), effect.getEffectData(), effect.getRadius());
		} else {
			loc.getWorld().playEffect(loc, effect.getEffect(), effect.getEffectData());
		}
	}

}
