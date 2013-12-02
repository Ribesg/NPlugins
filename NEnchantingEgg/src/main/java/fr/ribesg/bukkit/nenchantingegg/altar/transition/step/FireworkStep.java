/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - FireworkStep.java          *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.altar.transition.step.FireworkStep
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

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
		final Location loc = altar.getCenterLocation().toBukkitLocation().add(firework.getRelativeLocation());
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
