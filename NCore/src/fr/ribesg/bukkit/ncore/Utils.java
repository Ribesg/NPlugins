package fr.ribesg.bukkit.ncore;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

/**
 * Some simple methods that could be used in every plugin
 * 
 * @author Ribesg
 */
public class Utils {

    private final static char   SEPARATOR_CHAR        = ';';
    private final static String SEPARATOR_CHAR_STRING = Character.toString(SEPARATOR_CHAR);

    /**
     * @param loc
     *            a Location
     * @return A human-readable String representation of this Location
     */
    public static String toString(final Location loc) {
        final StringBuilder s = new StringBuilder();
        s.append('<');
        s.append(loc.getWorld().getName());
        s.append(SEPARATOR_CHAR);
        s.append(loc.getBlockX());
        s.append(SEPARATOR_CHAR);
        s.append(loc.getBlockY());
        s.append(SEPARATOR_CHAR);
        s.append(loc.getBlockZ());
        s.append('>').toString();
        return s.toString();
    }

    /**
     * @param loc
     *            a Location
     * @return A human-readable String representation of this Location, including Yaw and Pitch
     */
    public static String toStringPlus(final Location loc) {
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
        s.append('>').toString();
        return s.toString();
    }

    /**
     * @param string
     *            A String representing a location, returned by {@link #toString(Location)} or {@link #toStringPlus(Location)}
     * @return The actual Location or null if the string was malformed
     */
    public static Location toLocation(final String string) {
        if (string.length() < 2) {
            return null;
        }
        final String[] split = string.substring(1, string.length() - 1).split(SEPARATOR_CHAR_STRING);
        if (split.length == 4) {
            final String worldName = split[0];
            final World world = Bukkit.getWorld(worldName);
            if (world == null) {
                return null;
            } else {
                try {
                    final Double x = Double.parseDouble(split[1]);
                    final Double y = Double.parseDouble(split[2]);
                    final Double z = Double.parseDouble(split[3]);
                    final Location loc = new Location(world, x, y, z);
                    return loc;
                } catch (final NumberFormatException e) {
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
                    final Location loc = new Location(world, x, y, z, yaw, pitch);
                    return loc;
                } catch (final NumberFormatException e) {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    /**
     * @param vect
     *            a Vector
     * @return A human-readable String representation of this Vector
     */
    public static String toString(final Vector vect) {
        final StringBuilder s = new StringBuilder();
        s.append('<');
        s.append(vect.getX());
        s.append(SEPARATOR_CHAR);
        s.append(vect.getY());
        s.append(SEPARATOR_CHAR);
        s.append(vect.getZ());
        s.append('>').toString();
        return s.toString();
    }

    /**
     * @param string
     *            A String representing a vector, returned by {@link #toString(Vector)}
     * @return The actual Vector or null if the string was malformed
     */
    public static Vector toVector(final String string) {
        if (string.length() < 2) {
            return null;
        }
        final String[] split = string.substring(1, string.length() - 1).split(SEPARATOR_CHAR_STRING);
        if (split.length == 3) {
            try {
                final Double x = Double.parseDouble(split[0]);
                final Double y = Double.parseDouble(split[1]);
                final Double z = Double.parseDouble(split[2]);
                final Vector vect = new Vector(x, y, z);
                return vect;
            } catch (final NumberFormatException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Self Explanatory
     * 
     * @param originalString
     *            "A RANDOM_string__wiTh_ strange  cASe"
     * @return "aRandomStringWithStrangeCase"
     */
    public static String toLowerCamelCase(String originalString) {
        originalString = originalString.replace('_', ' ');
        final StringBuilder s = new StringBuilder();
        for (final String word : originalString.split(" ")) {
            if (word.length() > 0) {
                s.append(word.substring(0, 1).toUpperCase());
                s.append(word.substring(1).toLowerCase());
            }
        }
        final String result = s.toString();
        return new StringBuilder(
                result.substring(0, 1).toLowerCase()).append(
                result.substring(1)).toString();
    }
}
