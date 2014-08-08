/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - RelativeLocation.java      *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.altar.transition.bean.RelativeLocation
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg.altar.transition.bean;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class RelativeLocation {

	private final Vector relativeLocation;

	public RelativeLocation(final double x, final double y, final double z) {
		this.relativeLocation = new Vector(x, y, z);
	}

	public Location getLocation(final Location origin) {
		return origin.clone().add(this.relativeLocation);
	}

	public Vector getRelativeLocation() {
		return this.relativeLocation;
	}
}
