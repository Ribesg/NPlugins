/***************************************************************************
 * Project file:    NPlugins - NCuboid - Attributes.java                   *
 * Full Class name: fr.ribesg.bukkit.ncuboid.beans.Attributes              *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.beans;

import fr.ribesg.bukkit.ncore.common.NLocation;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.EnumMap;

public class Attributes {

	// Default map
	private static EnumMap<Attribute, Object> getDefaultAttributesMap() {
		final EnumMap<Attribute, Object> defaultAttributesMap = new EnumMap<>(Attribute.class);

		// We do not put anything in the map, we do not want to store useless objects / references

		return defaultAttributesMap;
	}

	private EnumMap<Attribute, Object> attributes;

	public Attributes() {
		attributes = null;
	}

	private void newMap() {
		attributes = getDefaultAttributesMap();
	}

	// Strings handling
	public String getStringAttribute(final Attribute att) {
		if (attributes == null) {
			return null;
		} else if (Attribute.isStringAttribute(att)) {
			return (String) attributes.get(att);
		} else {
			new IllegalArgumentException(att.name()).printStackTrace();
			return null;
		}
	}

	public void setStringAttribute(final Attribute att, final String s) {
		if (attributes == null) {
			newMap();
		}
		if (Attribute.isStringAttribute(att)) {
			attributes.put(att, s);
			checkStringAttributeCorrectness();
		} else {
			new IllegalArgumentException(att == null ? "null" : att.name()).printStackTrace();
		}
	}

	private void setStringAttributeNoCheck(final Attribute att, final String s) {
		attributes.put(att, s);
	}

	private void checkStringAttributeCorrectness() {
		// Nothing to do yet
	}

	// Integers handling
	public Integer getIntegerAttribute(final Attribute att) {
		if (attributes == null) {
			return null;
		} else if (Attribute.isIntegerAttribute(att)) {
			return (Integer) attributes.get(att);
		} else {
			new IllegalArgumentException(att.name()).printStackTrace();
			return null;
		}
	}

	public void setIntegerAttribute(final Attribute att, final Integer i) {
		if (attributes == null) {
			newMap();
		}
		if (Attribute.isIntegerAttribute(att)) {
			attributes.put(att, i);
			checkIntegerAttributeCorrectness();
		} else {
			new IllegalArgumentException(att == null ? "null" : att.name()).printStackTrace();
		}
	}

	private void setIntegerAttributeNoCheck(final Attribute att, final Integer i) {
		attributes.put(att, i);
	}

	private void checkIntegerAttributeCorrectness() {
		if (getIntegerAttribute(Attribute.HEAL_TIMER) != null && getIntegerAttribute(Attribute.HEAL_TIMER) < 5) {
			setIntegerAttributeNoCheck(Attribute.HEAL_TIMER, 5);
		}
		if (getIntegerAttribute(Attribute.FEED_TIMER) != null && getIntegerAttribute(Attribute.FEED_TIMER) < 5) {
			setIntegerAttributeNoCheck(Attribute.FEED_TIMER, 5);
		}
		if (getIntegerAttribute(Attribute.HEAL_AMOUNT) != null && getIntegerAttribute(Attribute.HEAL_AMOUNT) < -20) {
			setIntegerAttributeNoCheck(Attribute.HEAL_AMOUNT, -20);
		} else if (getIntegerAttribute(Attribute.HEAL_AMOUNT) != null && getIntegerAttribute(Attribute.HEAL_AMOUNT) > 20) {
			setIntegerAttributeNoCheck(Attribute.HEAL_AMOUNT, 20);
		}
		if (getIntegerAttribute(Attribute.FEED_AMOUNT) != null && getIntegerAttribute(Attribute.FEED_AMOUNT) < -20) {
			setIntegerAttributeNoCheck(Attribute.FEED_AMOUNT, -20);
		} else if (getIntegerAttribute(Attribute.FEED_AMOUNT) != null && getIntegerAttribute(Attribute.FEED_AMOUNT) > 20) {
			setIntegerAttributeNoCheck(Attribute.FEED_AMOUNT, 20);
		}
		for (final Attribute att : new Attribute[] {
				Attribute.HEAL_MIN_HEALTH,
				Attribute.HEAL_MAX_HEALTH,
				Attribute.FEED_MIN_FOOD,
				Attribute.FEED_MAX_FOOD
		}) {
			if (getIntegerAttribute(att) != null && getIntegerAttribute(att) < 0) {
				setIntegerAttributeNoCheck(att, 0);
			} else if (getIntegerAttribute(att) != null && getIntegerAttribute(att) > 20) {
				setIntegerAttributeNoCheck(att, 20);
			}
		}
		if (getIntegerAttribute(Attribute.EXPLOSION_BLOCK_DROP) != null && getIntegerAttribute(Attribute.EXPLOSION_BLOCK_DROP) < 0) {
			setIntegerAttribute(Attribute.EXPLOSION_BLOCK_DROP, 0);
		} else if (getIntegerAttribute(Attribute.EXPLOSION_BLOCK_DROP) != null && getIntegerAttribute(Attribute.EXPLOSION_BLOCK_DROP) > 100) {
			setIntegerAttribute(Attribute.EXPLOSION_BLOCK_DROP, 100);
		}
	}

	// Locations handling
	public Location getLocationAttribute(final Attribute att) {
		if (attributes == null) {
			return null;
		} else if (Attribute.isLocationAttribute(att)) {
			return (Location) attributes.get(att);
		} else {
			new IllegalArgumentException(att.name()).printStackTrace();
			return null;
		}
	}

	public void setLocationAttribute(final Attribute att, final Location loc) {
		if (attributes == null) {
			newMap();
		}
		if (Attribute.isLocationAttribute(att)) {
			attributes.put(att, loc);
			checkLocationAttributeCorrectness();
		} else {
			new IllegalArgumentException(att.name()).printStackTrace();
		}
	}

	private void checkLocationAttributeCorrectness() {
		// Nothing to do yet
	}

	// Vectors handling
	public Vector getVectorAttribute(final Attribute att) {
		if (attributes == null) {
			return null;
		} else if (Attribute.isVectorAttribute(att)) {
			return (Vector) attributes.get(att);
		} else {
			new IllegalArgumentException(att.name()).printStackTrace();
			return null;
		}
	}

	public void setVectorAttribute(final Attribute att, final Vector v) {
		if (attributes == null) {
			newMap();
		}
		if (Attribute.isVectorAttribute(att)) {
			attributes.put(att, v);
			checkVectorAttributeCorrectness();
		} else {
			new IllegalArgumentException(att.name()).printStackTrace();
		}
	}

	private void checkVectorAttributeCorrectness() {
		if (getVectorAttribute(Attribute.BOOSTER_VECTOR) != null && getVectorAttribute(Attribute.BOOSTER_VECTOR).lengthSquared() > 100) {
			// XXX: Bukkit does not allow > 10 m/s Velocity
			setVectorAttribute(Attribute.BOOSTER_VECTOR, getVectorAttribute(Attribute.BOOSTER_VECTOR).normalize().multiply(9.95D));
		}
	}

	public String getStringRepresentation(final Attribute att) {
		if (Attribute.isStringAttribute(att)) {
			return getStringAttribute(att);
		} else if (Attribute.isIntegerAttribute(att)) {
			return Integer.toString(getIntegerAttribute(att));
		} else if (Attribute.isVectorAttribute(att)) {
			final Vector v = getVectorAttribute(att);
			return "<" + v.getX() + ";" + v.getY() + ";" + v.getZ() + ">";
		} else if (Attribute.isLocationAttribute(att)) {
			return NLocation.toStringPlus(getLocationAttribute(att));
		} else {
			throw new UnsupportedOperationException("Not yet implemented for " + att.name());
		}
	}
}
