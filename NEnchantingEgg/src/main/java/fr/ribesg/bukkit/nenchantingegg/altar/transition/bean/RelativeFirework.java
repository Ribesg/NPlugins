/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - RelativeFirework.java      *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeFirework
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg.altar.transition.bean;

import org.bukkit.FireworkEffect;
import org.bukkit.util.Vector;

import java.util.Set;

public class RelativeFirework extends RelativeLocation {

	private final Set<FireworkEffect> effects;
	private final Vector              velocity;
	final         boolean             hasVelocity;

	public RelativeFirework(final double x, final double y, final double z, final Set<FireworkEffect> effects) {
		this(x, y, z, effects, null);
	}

	public RelativeFirework(final double x, final double y, final double z, final Set<FireworkEffect> effects, final Vector velocity) {
		super(x, y, z);
		this.effects = effects;
		this.velocity = velocity;
		hasVelocity = velocity != null;
	}

	public boolean hasVelocity() {
		return hasVelocity;
	}

	public Set<FireworkEffect> getEffects() {
		return effects;
	}

	public Vector getVelocity() {
		return velocity;
	}
}
