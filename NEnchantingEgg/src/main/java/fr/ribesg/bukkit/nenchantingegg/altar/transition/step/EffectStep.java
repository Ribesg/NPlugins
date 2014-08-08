/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - EffectStep.java            *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.altar.transition.step.EffectStep
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

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
		final Location loc = altar.getCenterLocation().toBukkitLocation().add(this.effect.getRelativeLocation());
		if (this.effect.hasRadius()) {
			loc.getWorld().playEffect(loc, this.effect.getEffect(), this.effect.getEffectData(), this.effect.getRadius());
		} else {
			loc.getWorld().playEffect(loc, this.effect.getEffect(), this.effect.getEffectData());
		}
	}
}
