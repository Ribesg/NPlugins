/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - ExplosionStep.java         *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.altar.transition.step.ExplosionStep
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg.altar.transition.step;

import fr.ribesg.bukkit.nenchantingegg.altar.Altar;
import fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeLocation;
import org.bukkit.Location;

public class ExplosionStep extends Step {

	private final RelativeLocation loc;

	public ExplosionStep(final int delay, final RelativeLocation loc) {
		super(delay);
		this.loc = loc;
	}

	@Override
	public void doStep(final Altar altar) {
		final Location centerLocation = altar.getCenterLocation().toBukkitLocation();
		final double x = centerLocation.getX() + loc.getRelativeLocation().getX();
		final double y = centerLocation.getY() + loc.getRelativeLocation().getY();
		final double z = centerLocation.getZ() + loc.getRelativeLocation().getZ();
		altar.getCenterLocation().getWorld().createExplosion(x, y, z, 5f, false, false);
	}

}
