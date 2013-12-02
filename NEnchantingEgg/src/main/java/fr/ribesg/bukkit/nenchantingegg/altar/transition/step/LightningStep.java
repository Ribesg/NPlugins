/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - LightningStep.java         *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.altar.transition.step.LightningStep
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

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
