/***************************************************************************
 * Project file:    NPlugins - NCuboid - FlagAttributes.java               *
 * Full Class name: fr.ribesg.bukkit.ncuboid.beans.FlagAttributes          *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.beans;

import fr.ribesg.bukkit.ncore.common.NLocation;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.EnumMap;

import static fr.ribesg.bukkit.ncuboid.beans.FlagAtt.isIntFlagAtt;
import static fr.ribesg.bukkit.ncuboid.beans.FlagAtt.isLocFlagAtt;
import static fr.ribesg.bukkit.ncuboid.beans.FlagAtt.isVectFlagAtt;

public class FlagAttributes {

	// Default map
	private static EnumMap<FlagAtt, Object> getDefaultFlagAttMap() {
		final EnumMap<FlagAtt, Object> defaultFlagMap = new EnumMap<>(FlagAtt.class);

		// We do not put anything in the map, we do not want to store useless objects / references

		return defaultFlagMap;
	}

	private EnumMap<FlagAtt, Object> atts;

	public FlagAttributes() {
		atts = null;
	}

	private void newMap() {
		atts = getDefaultFlagAttMap();
	}

	// Integers handling
	public Integer getIntFlagAtt(final FlagAtt f) {
		if (atts == null) {
			return null;
		} else if (isIntFlagAtt(f)) {
			return (Integer) atts.get(f);
		} else {
			new IllegalArgumentException(f.name()).printStackTrace();
			return null;
		}
	}

	public void setIntFlagAtt(final FlagAtt fa, final Integer i) {
		if (atts == null) {
			newMap();
		}
		if (isIntFlagAtt(fa)) {
			atts.put(fa, i);
			checkIntFlagAttCorrectness();
		} else {
			new IllegalArgumentException(fa == null ? "null" : fa.name()).printStackTrace();
		}
	}

	private void setIntFlagAttNoCheck(final FlagAtt fa, final Integer i) {
		atts.put(fa, i);
	}

	private void checkIntFlagAttCorrectness() {
		if (getIntFlagAtt(FlagAtt.HEAL_TIMER) != null && getIntFlagAtt(FlagAtt.HEAL_TIMER) < 5) {
			setIntFlagAttNoCheck(FlagAtt.HEAL_TIMER, 5);
		}
		if (getIntFlagAtt(FlagAtt.FEED_TIMER) != null && getIntFlagAtt(FlagAtt.FEED_TIMER) < 5) {
			setIntFlagAttNoCheck(FlagAtt.FEED_TIMER, 5);
		}
		if (getIntFlagAtt(FlagAtt.HEAL_AMOUNT) != null && getIntFlagAtt(FlagAtt.HEAL_AMOUNT) < -20) {
			setIntFlagAttNoCheck(FlagAtt.HEAL_AMOUNT, -20);
		} else if (getIntFlagAtt(FlagAtt.HEAL_AMOUNT) != null && getIntFlagAtt(FlagAtt.HEAL_AMOUNT) > 20) {
			setIntFlagAttNoCheck(FlagAtt.HEAL_AMOUNT, 20);
		}
		if (getIntFlagAtt(FlagAtt.FEED_AMOUNT) != null && getIntFlagAtt(FlagAtt.FEED_AMOUNT) < -20) {
			setIntFlagAttNoCheck(FlagAtt.FEED_AMOUNT, -20);
		} else if (getIntFlagAtt(FlagAtt.FEED_AMOUNT) != null && getIntFlagAtt(FlagAtt.FEED_AMOUNT) > 20) {
			setIntFlagAttNoCheck(FlagAtt.FEED_AMOUNT, 20);
		}
		for (final FlagAtt f : new FlagAtt[] {
				FlagAtt.HEAL_MIN_HEALTH,
				FlagAtt.HEAL_MAX_HEALTH,
				FlagAtt.FEED_MIN_FOOD,
				FlagAtt.FEED_MAX_FOOD
		}) {
			if (getIntFlagAtt(f) != null && getIntFlagAtt(f) < 0) {
				setIntFlagAttNoCheck(f, 0);
			} else if (getIntFlagAtt(f) != null && getIntFlagAtt(f) > 20) {
				setIntFlagAttNoCheck(f, 20);
			}
		}
		if (getIntFlagAtt(FlagAtt.EXPLOSION_BLOCK_DROP) != null && getIntFlagAtt(FlagAtt.EXPLOSION_BLOCK_DROP) < 0) {
			setIntFlagAtt(FlagAtt.EXPLOSION_BLOCK_DROP, 0);
		} else if (getIntFlagAtt(FlagAtt.EXPLOSION_BLOCK_DROP) != null && getIntFlagAtt(FlagAtt.EXPLOSION_BLOCK_DROP) > 100) {
			setIntFlagAtt(FlagAtt.EXPLOSION_BLOCK_DROP, 100);
		}
	}

	// Locations handling
	public Location getLocFlagAtt(final FlagAtt fa) {
		if (atts == null) {
			return null;
		} else if (isLocFlagAtt(fa)) {
			return (Location) atts.get(fa);
		} else {
			new IllegalArgumentException(fa.name()).printStackTrace();
			return null;
		}
	}

	public void setLocFlagAtt(final FlagAtt fa, final Location loc) {
		if (atts == null) {
			newMap();
		}
		if (isLocFlagAtt(fa)) {
			atts.put(fa, loc);
			checkLocFlagAttCorrectness();
		} else {
			new IllegalArgumentException(fa.name()).printStackTrace();
		}
	}

	private void checkLocFlagAttCorrectness() {
		// Nothing to do yet
	}

	// Vectors handling
	public Vector getVectFlagAtt(final FlagAtt fa) {
		if (atts == null) {
			return null;
		} else if (isVectFlagAtt(fa)) {
			return (Vector) atts.get(fa);
		} else {
			new IllegalArgumentException(fa.name()).printStackTrace();
			return null;
		}
	}

	public void setVectFlagAtt(final FlagAtt fa, final Vector v) {
		if (atts == null) {
			newMap();
		}
		if (isVectFlagAtt(fa)) {
			atts.put(fa, v);
			checkVectFlagAttCorrectness();
		} else {
			new IllegalArgumentException(fa.name()).printStackTrace();
		}
	}

	private void checkVectFlagAttCorrectness() {
		if (getVectFlagAtt(FlagAtt.BOOSTER_VECTOR) != null && getVectFlagAtt(FlagAtt.BOOSTER_VECTOR).lengthSquared() > 100) {
			// XXX: Bukkit does not allow > 10 m/s Velocity
			setVectFlagAtt(FlagAtt.BOOSTER_VECTOR, getVectFlagAtt(FlagAtt.BOOSTER_VECTOR).normalize().multiply(9.95D));
		}
	}

	public String getStringRepresentation(final FlagAtt fa) {
		if (isIntFlagAtt(fa)) {
			return Integer.toString(getIntFlagAtt(fa));
		} else if (isVectFlagAtt(fa)) {
			final Vector v = getVectFlagAtt(fa);
			return "<" + v.getX() + ";" + v.getY() + ";" + v.getZ() + ">";
		} else if (isLocFlagAtt(fa)) {
			return NLocation.toStringPlus(getLocFlagAtt(fa));
		} else {
			throw new UnsupportedOperationException("Not yet implemented for " + fa.name());
		}
	}
}
