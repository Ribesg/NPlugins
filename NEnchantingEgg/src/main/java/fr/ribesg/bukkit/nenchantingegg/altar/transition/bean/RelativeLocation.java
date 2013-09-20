package fr.ribesg.bukkit.nenchantingegg.altar.transition.bean;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class RelativeLocation {

	private final Vector relativeLocation;

	public RelativeLocation(final double x, final double y, final double z) {
		relativeLocation = new Vector(x, y, z);
	}

	public Location getLocation(final Location origin) {
		return origin.clone().add(relativeLocation);
	}

	public Vector getRelativeLocation() {
		return relativeLocation;
	}
}
