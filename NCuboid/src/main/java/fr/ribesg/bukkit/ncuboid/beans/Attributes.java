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

import static fr.ribesg.bukkit.ncuboid.beans.Attribute.isIntFlagAtt;
import static fr.ribesg.bukkit.ncuboid.beans.Attribute.isLocFlagAtt;
import static fr.ribesg.bukkit.ncuboid.beans.Attribute.isVectFlagAtt;

public class Attributes {

	// Default map
	private static EnumMap<Attribute, Object> getDefaultFlagAttMap() {
		final EnumMap<Attribute, Object> defaultFlagMap = new EnumMap<>(Attribute.class);

		// We do not put anything in the map, we do not want to store useless objects / references

		return defaultFlagMap;
	}

	private EnumMap<Attribute, Object> attributes;

	public Attributes() {
		attributes = null;
	}

	private void newMap() {
		attributes = getDefaultFlagAttMap();
	}

	// Integers handling
	public Integer getIntAttribute(final Attribute f) {
		if (attributes == null) {
			return null;
		} else if (isIntFlagAtt(f)) {
			return (Integer) attributes.get(f);
		} else {
			new IllegalArgumentException(f.name()).printStackTrace();
			return null;
		}
	}

	public void setIntAttribute(final Attribute att, final Integer i) {
		if (attributes == null) {
			newMap();
		}
		if (isIntFlagAtt(att)) {
			attributes.put(att, i);
			checkIntAttributeCorrectness();
		} else {
			new IllegalArgumentException(att == null ? "null" : att.name()).printStackTrace();
		}
	}

	private void setIntAttributeNoCheck(final Attribute att, final Integer i) {
		attributes.put(att, i);
	}

	private void checkIntAttributeCorrectness() {
		if (getIntAttribute(Attribute.HEAL_TIMER) != null && getIntAttribute(Attribute.HEAL_TIMER) < 5) {
			setIntAttributeNoCheck(Attribute.HEAL_TIMER, 5);
		}
		if (getIntAttribute(Attribute.FEED_TIMER) != null && getIntAttribute(Attribute.FEED_TIMER) < 5) {
			setIntAttributeNoCheck(Attribute.FEED_TIMER, 5);
		}
		if (getIntAttribute(Attribute.HEAL_AMOUNT) != null && getIntAttribute(Attribute.HEAL_AMOUNT) < -20) {
			setIntAttributeNoCheck(Attribute.HEAL_AMOUNT, -20);
		} else if (getIntAttribute(Attribute.HEAL_AMOUNT) != null && getIntAttribute(Attribute.HEAL_AMOUNT) > 20) {
			setIntAttributeNoCheck(Attribute.HEAL_AMOUNT, 20);
		}
		if (getIntAttribute(Attribute.FEED_AMOUNT) != null && getIntAttribute(Attribute.FEED_AMOUNT) < -20) {
			setIntAttributeNoCheck(Attribute.FEED_AMOUNT, -20);
		} else if (getIntAttribute(Attribute.FEED_AMOUNT) != null && getIntAttribute(Attribute.FEED_AMOUNT) > 20) {
			setIntAttributeNoCheck(Attribute.FEED_AMOUNT, 20);
		}
		for (final Attribute f : new Attribute[] {
				Attribute.HEAL_MIN_HEALTH,
				Attribute.HEAL_MAX_HEALTH,
				Attribute.FEED_MIN_FOOD,
				Attribute.FEED_MAX_FOOD
		}) {
			if (getIntAttribute(f) != null && getIntAttribute(f) < 0) {
				setIntAttributeNoCheck(f, 0);
			} else if (getIntAttribute(f) != null && getIntAttribute(f) > 20) {
				setIntAttributeNoCheck(f, 20);
			}
		}
		if (getIntAttribute(Attribute.EXPLOSION_BLOCK_DROP) != null && getIntAttribute(Attribute.EXPLOSION_BLOCK_DROP) < 0) {
			setIntAttribute(Attribute.EXPLOSION_BLOCK_DROP, 0);
		} else if (getIntAttribute(Attribute.EXPLOSION_BLOCK_DROP) != null && getIntAttribute(Attribute.EXPLOSION_BLOCK_DROP) > 100) {
			setIntAttribute(Attribute.EXPLOSION_BLOCK_DROP, 100);
		}
	}

	// Locations handling
	public Location getLocAttribute(final Attribute att) {
		if (attributes == null) {
			return null;
		} else if (isLocFlagAtt(att)) {
			return (Location) attributes.get(att);
		} else {
			new IllegalArgumentException(att.name()).printStackTrace();
			return null;
		}
	}

	public void setLocAttribute(final Attribute att, final Location loc) {
		if (attributes == null) {
			newMap();
		}
		if (isLocFlagAtt(att)) {
			attributes.put(att, loc);
			checkLocAttributeCorrectness();
		} else {
			new IllegalArgumentException(att.name()).printStackTrace();
		}
	}

	private void checkLocAttributeCorrectness() {
		// Nothing to do yet
	}

	// Vectors handling
	public Vector getVectAttribute(final Attribute att) {
		if (attributes == null) {
			return null;
		} else if (isVectFlagAtt(att)) {
			return (Vector) attributes.get(att);
		} else {
			new IllegalArgumentException(att.name()).printStackTrace();
			return null;
		}
	}

	public void setVectAttribute(final Attribute att, final Vector v) {
		if (attributes == null) {
			newMap();
		}
		if (isVectFlagAtt(att)) {
			attributes.put(att, v);
			checkVectAttributeCorrectness();
		} else {
			new IllegalArgumentException(att.name()).printStackTrace();
		}
	}

	private void checkVectAttributeCorrectness() {
		if (getVectAttribute(Attribute.BOOSTER_VECTOR) != null && getVectAttribute(Attribute.BOOSTER_VECTOR).lengthSquared() > 100) {
			// XXX: Bukkit does not allow > 10 m/s Velocity
			setVectAttribute(Attribute.BOOSTER_VECTOR, getVectAttribute(Attribute.BOOSTER_VECTOR).normalize().multiply(9.95D));
		}
	}

	public String getStringRepresentation(final Attribute att) {
		if (isIntFlagAtt(att)) {
			return Integer.toString(getIntAttribute(att));
		} else if (isVectFlagAtt(att)) {
			final Vector v = getVectAttribute(att);
			return "<" + v.getX() + ";" + v.getY() + ";" + v.getZ() + ">";
		} else if (isLocFlagAtt(att)) {
			return NLocation.toStringPlus(getLocAttribute(att));
		} else {
			throw new UnsupportedOperationException("Not yet implemented for " + att.name());
		}
	}
}
