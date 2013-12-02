/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - SoundStep.java             *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.altar.transition.step.SoundStep
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

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
