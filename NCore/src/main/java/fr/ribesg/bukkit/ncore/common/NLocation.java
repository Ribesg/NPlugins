/***************************************************************************
 * Project file:    NPlugins - NCore - NLocation.java                      *
 * Full Class name: fr.ribesg.bukkit.ncore.common.NLocation                *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.common;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

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
        final World world = Bukkit.getWorld(this.worldName);
        if (world == null) {
            return null;
        } else {
            return new Location(world, this.x, this.y, this.z, this.yaw, this.pitch);
        }
    }

    public Vector toVector() {
        return new Vector(this.x, this.y, this.z);
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }

    public String getWorldName() {
        return this.worldName;
    }

    public World getWorld() {
        return Bukkit.getWorld(this.worldName);
    }

    public void setWorldName(final String worldName) {
        this.worldName = worldName;
    }

    public double getX() {
        return this.x;
    }

    public int getBlockX() {
        return (int)Math.floor(this.x);
    }

    public void setX(final double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public int getBlockY() {
        return (int)Math.floor(this.y);
    }

    public void setY(final double y) {
        this.y = y;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }

    public double getZ() {
        return this.z;
    }

    public int getBlockZ() {
        return (int)Math.floor(this.z);
    }

    public void setZ(final double z) {
        this.z = z;
    }

    public NLocation getBlockLocation() {
        final int x = this.getBlockX();
        final int y = this.getBlockY();
        final int z = this.getBlockZ();
        return new NLocation(this.worldName, x, y, z);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        final NLocation nLocation = (NLocation)o;

        return Double.compare(nLocation.x, this.x) == 0 &&
               Double.compare(nLocation.y, this.y) == 0 &&
               Double.compare(nLocation.z, this.z) == 0 &&
               this.worldName.equals(nLocation.worldName);
    }

    public boolean equalsPlus(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        final NLocation nLocation = (NLocation)o;

        return Float.compare(nLocation.pitch, this.pitch) == 0 &&
               Double.compare(nLocation.x, this.x) == 0 &&
               Double.compare(nLocation.y, this.y) == 0 &&
               Float.compare(nLocation.yaw, this.yaw) == 0 &&
               Double.compare(nLocation.z, this.z) == 0 &&
               this.worldName.equals(nLocation.worldName);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = this.worldName.hashCode();
        temp = Double.doubleToLongBits(this.x);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.y);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.z);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        result = 31 * result + (this.yaw != +0.0f ? Float.floatToIntBits(this.yaw) : 0);
        result = 31 * result + (this.pitch != +0.0f ? Float.floatToIntBits(this.pitch) : 0);
        return result;
    }

    public NLocation clone() {
        return new NLocation(this.worldName, this.x, this.y, this.z, this.yaw, this.pitch);
    }

    public String toString() {
        final StringBuilder s = new StringBuilder();
        s.append('<');
        s.append(this.worldName);
        s.append(SEPARATOR_CHAR);
        s.append(this.x);
        s.append(SEPARATOR_CHAR);
        s.append(this.y);
        s.append(SEPARATOR_CHAR);
        s.append(this.z);
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
        s.append(this.worldName);
        s.append(SEPARATOR_CHAR);
        s.append(this.x);
        s.append(SEPARATOR_CHAR);
        s.append(this.y);
        s.append(SEPARATOR_CHAR);
        s.append(this.z);
        s.append(SEPARATOR_CHAR);
        s.append(this.yaw);
        s.append(SEPARATOR_CHAR);
        s.append(this.pitch);
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
                    LOGGER.warning("Unable to convert the provided String to Location, caught NumberFormatException while parsing x,y,z,yaw,pitch");
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
                LOGGER.warning("Unable to convert the provided String to NLocation, caught NumberFormatException while parsing x,y,z,yaw,pitch");
                LOGGER.warning("String was: " + string);
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Used for "smooth" teleportation.
     * Applies the same direction as directionLocation to
     * positionLocation, and returns it.
     *
     * @param positionLocation  the position Location
     * @param directionLocation the direction Location
     *
     * @return a Location with world, x, y, z of positionLocation and pitch, yaw of directionLocation
     */
    public static Location fixDirection(final NLocation positionLocation, final Location directionLocation) {
        return fixDirection(positionLocation.toBukkitLocation(), directionLocation);
    }

    /**
     * Used for "smooth" teleportation.
     * Applies the same direction as directionLocation to
     * positionLocation, and returns it.
     *
     * @param positionLocation  the position Location
     * @param directionLocation the direction Location
     *
     * @return a Location with world, x, y, z of positionLocation and pitch, yaw of directionLocation
     */
    public static Location fixDirection(final Location positionLocation, final Location directionLocation) {
        return positionLocation.setDirection(directionLocation.getDirection());
    }

    public double distance(final NLocation o) throws IllegalArgumentException {
        return Math.sqrt(this.distanceSquared(o));
    }

    public double distanceSquared(final NLocation o) throws IllegalArgumentException {
        final double dy = this.y - o.y;
        return this.distance2DSquared(o) + dy * dy;
    }

    public double distance2D(final NLocation o) throws IllegalArgumentException {
        return Math.sqrt(this.distance2DSquared(o));
    }

    public double distance2DSquared(final NLocation o) throws IllegalArgumentException {
        if (o == null) {
            throw new IllegalArgumentException("Cannot measure distance to a null location");
        } else if (!o.worldName.equals(this.worldName)) {
            throw new IllegalArgumentException("Cannot measure distance between " + this.worldName + " and " + o.worldName);
        }
        final double dx = this.x - o.x;
        final double dz = this.z - o.z;
        return dx * dx + dz * dz;
    }
}
