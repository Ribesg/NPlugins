/***************************************************************************
 * Project file:    NPlugins - NCore - NLocation.java                      *
 * Full Class name: fr.ribesg.bukkit.ncore.common.NLocation                *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.common;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.logging.Logger;

/**
 * Represents a Location, without reference to Bukkit's World.
 * It stores the world name as a String instead of the reference.
 */
public class NLocation {

	private static final Logger LOGGER = Logger.getLogger(NLocation.class.getName());

	private static final char   SEPARATOR_CHAR        = ';';
	private static final String SEPARATOR_CHAR_STRING = Character.toString(SEPARATOR_CHAR);

	private String worldName;
	private double x;
	private double y;
	private double z;
	private float  yaw;
	private float  pitch;

	public NLocation(final String worldName, final double x, final double y, final double z, final float yaw, final float pitch) {
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public NLocation(final String worldName, final double x, final double y, final double z) {
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = 0f;
		this.pitch = 0f;
	}

	public NLocation(final Location loc) {
		this.worldName = loc.getWorld().getName();
		this.x = loc.getX();
		this.y = loc.getY();
		this.z = loc.getZ();
		this.yaw = loc.getYaw();
		this.pitch = loc.getPitch();
	}

	public Location toBukkitLocation() {
		final World world = Bukkit.getWorld(worldName);
		if (world == null) {
			return null;
		} else {
			return new Location(world, x, y, z, yaw, pitch);
		}
	}

	public Vector toVector() {
		return new Vector(x, y, z);
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(final float pitch) {
		this.pitch = pitch;
	}

	public String getWorldName() {
		return worldName;
	}

	public World getWorld() {
		return Bukkit.getWorld(getWorldName());
	}

	public void setWorldName(final String worldName) {
		this.worldName = worldName;
	}

	public double getX() {
		return x;
	}

	public int getBlockX() {
		return (int) Math.floor(x);
	}

	public void setX(final double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public int getBlockY() {
		return (int) Math.floor(y);
	}

	public void setY(final double y) {
		this.y = y;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(final float yaw) {
		this.yaw = yaw;
	}

	public double getZ() {
		return z;
	}

	public int getBlockZ() {
		return (int) Math.floor(z);
	}

	public void setZ(final double z) {
		this.z = z;
	}

	public NLocation getBlockLocation() {
		final int x = getBlockX();
		final int y = getBlockY();
		final int z = getBlockZ();
		return new NLocation(getWorldName(), x, y, z);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final NLocation nLocation = (NLocation) o;

		return Double.compare(nLocation.x, x) == 0 &&
		       Double.compare(nLocation.y, y) == 0 &&
		       Double.compare(nLocation.z, z) == 0 &&
		       worldName.equals(nLocation.worldName);

	}

	public boolean equalsPlus(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final NLocation nLocation = (NLocation) o;

		return Float.compare(nLocation.pitch, pitch) == 0 &&
		       Double.compare(nLocation.x, x) == 0 &&
		       Double.compare(nLocation.y, y) == 0 &&
		       Float.compare(nLocation.yaw, yaw) == 0 &&
		       Double.compare(nLocation.z, z) == 0 &&
		       worldName.equals(nLocation.worldName);

	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		result = worldName.hashCode();
		temp = Double.doubleToLongBits(x);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		result = 31 * result + (yaw != +0.0f ? Float.floatToIntBits(yaw) : 0);
		result = 31 * result + (pitch != +0.0f ? Float.floatToIntBits(pitch) : 0);
		return result;
	}

	public NLocation clone() {
		return new NLocation(getWorldName(), getX(), getY(), getZ(), getYaw(), getPitch());
	}

	public String toString() {
		final StringBuilder s = new StringBuilder();
		s.append('<');
		s.append(getWorldName());
		s.append(SEPARATOR_CHAR);
		s.append(getX());
		s.append(SEPARATOR_CHAR);
		s.append(getY());
		s.append(SEPARATOR_CHAR);
		s.append(getZ());
		s.append('>');
		return s.toString();
	}

	/**
	 * @param loc a Location
	 *
	 * @return a human-readable String representation of this Location
	 */
	public static String toString(final Location loc) {
		if (loc == null) {
			return "null";
		}
		final StringBuilder s = new StringBuilder();
		s.append('<');
		s.append(loc.getWorld().getName());
		s.append(SEPARATOR_CHAR);
		s.append(loc.getX());
		s.append(SEPARATOR_CHAR);
		s.append(loc.getY());
		s.append(SEPARATOR_CHAR);
		s.append(loc.getZ());
		s.append('>');
		return s.toString();
	}

	public String toStringPlus() {
		final StringBuilder s = new StringBuilder();
		s.append('<');
		s.append(getWorldName());
		s.append(SEPARATOR_CHAR);
		s.append(getX());
		s.append(SEPARATOR_CHAR);
		s.append(getY());
		s.append(SEPARATOR_CHAR);
		s.append(getZ());
		s.append(SEPARATOR_CHAR);
		s.append(getYaw());
		s.append(SEPARATOR_CHAR);
		s.append(getPitch());
		s.append('>');
		return s.toString();
	}

	/**
	 * @param loc a Location
	 *
	 * @return a human-readable String representation of this Location, including Yaw and Pitch
	 */
	public static String toStringPlus(final Location loc) {
		if (loc == null) {
			return "null";
		}
		final StringBuilder s = new StringBuilder();
		s.append('<');
		s.append(loc.getWorld().getName());
		s.append(SEPARATOR_CHAR);
		s.append(loc.getX());
		s.append(SEPARATOR_CHAR);
		s.append(loc.getY());
		s.append(SEPARATOR_CHAR);
		s.append(loc.getZ());
		s.append(SEPARATOR_CHAR);
		s.append(loc.getYaw());
		s.append(SEPARATOR_CHAR);
		s.append(loc.getPitch());
		s.append('>');
		return s.toString();
	}

	/**
	 * @param string A String representing a location, returned by {@link #toString(Location)} or {@link #toStringPlus(Location)}
	 *
	 * @return The actual Location or null if the string was malformed
	 */
	public static Location toLocation(final String string) {
		if (string == null || "null".equals(string)) {
			return null;
		}
		if (string.length() < 2) {
			return null;
		}
		final String[] split = string.substring(1, string.length() - 1).split(SEPARATOR_CHAR_STRING);
		if (split.length == 4) {
			final String worldName = split[0];
			final World world = Bukkit.getWorld(worldName);
			if (world == null) {
				LOGGER.warning("Unable to convert the provided String to Location, world '" + worldName + "' not found");
				LOGGER.warning("String was: " + string);
				return null;
			} else {
				try {
					final Double x = Double.parseDouble(split[1]);
					final Double y = Double.parseDouble(split[2]);
					final Double z = Double.parseDouble(split[3]);
					return new Location(world, x, y, z);
				} catch (final NumberFormatException e) {
					LOGGER.warning("Unable to convert the provided String to Location, caught NumberFormatException while parsing x,y,z");
					LOGGER.warning("String was: " + string);
					return null;
				}
			}
		} else if (split.length == 6) {
			final String worldName = split[0];
			final World world = Bukkit.getWorld(worldName);
			if (world == null) {
				return null;
			} else {
				try {
					final Double x = Double.parseDouble(split[1]);
					final Double y = Double.parseDouble(split[2]);
					final Double z = Double.parseDouble(split[3]);
					final Float yaw = Float.parseFloat(split[4]);
					final Float pitch = Float.parseFloat(split[5]);
					return new Location(world, x, y, z, yaw, pitch);
				} catch (final NumberFormatException e) {
					LOGGER.warning("Unable to convert the provided String to Location, " +
					            "caught NumberFormatException while parsing x,y,z,yaw,pitch");
					LOGGER.warning("String was: " + string);
					return null;
				}
			}
		} else {
			return null;
		}
	}

	/**
	 * @param string A String representing a location, returned by {@link #toString(Location)} or {@link #toStringPlus(Location)}
	 *
	 * @return the actual NLocation or null if the string was malformed
	 */
	public static NLocation toNLocation(final String string) {
		if (string == null || "null".equals(string)) {
			return null;
		}
		if (string.length() < 2) {
			return null;
		}
		final String[] split = string.substring(1, string.length() - 1).split(SEPARATOR_CHAR_STRING);
		if (split.length == 4) {
			final String worldName = split[0];
			try {
				final Double x = Double.parseDouble(split[1]);
				final Double y = Double.parseDouble(split[2]);
				final Double z = Double.parseDouble(split[3]);
				return new NLocation(worldName, x, y, z);
			} catch (final NumberFormatException e) {
				LOGGER.warning("Unable to convert the provided String to NLocation, caught NumberFormatException while parsing x,y,z");
				LOGGER.warning("String was: " + string);
				return null;
			}
		} else if (split.length == 6) {
			final String worldName = split[0];
			try {
				final Double x = Double.parseDouble(split[1]);
				final Double y = Double.parseDouble(split[2]);
				final Double z = Double.parseDouble(split[3]);
				final Float yaw = Float.parseFloat(split[4]);
				final Float pitch = Float.parseFloat(split[5]);
				return new NLocation(worldName, x, y, z, yaw, pitch);
			} catch (final NumberFormatException e) {
				LOGGER.warning("Unable to convert the provided String to NLocation, " +
				            "caught NumberFormatException while parsing x,y,z,yaw,pitch");
				LOGGER.warning("String was: " + string);
				return null;
			}
		} else {
			return null;
		}
	}

	public double distance(final NLocation o) {
		return Math.sqrt(distanceSquared(o));
	}

	public double distanceSquared(final NLocation o) {
		return distance2DSquared(o) + Math.pow(y - o.y, 2);
	}

	public double distance2D(final NLocation o) {
		return Math.sqrt(distance2DSquared(o));
	}

	public double distance2DSquared(final NLocation o) {
		if (o == null) {
			throw new IllegalArgumentException("Cannot measure distance to a null location");
		} else if (!o.getWorldName().equals(getWorldName())) {
			throw new IllegalArgumentException("Cannot measure distance between " + getWorldName() + " and " + o.getWorldName());
		}
		return Math.pow(x - o.x, 2) + Math.pow(z - o.z, 2);
	}
}
