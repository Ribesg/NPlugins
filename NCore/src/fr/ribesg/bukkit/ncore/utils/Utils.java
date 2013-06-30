package fr.ribesg.bukkit.ncore.utils;

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
     * @param loc a Location
     *
     * @return A human-readable String representation of this Location
     */
    public static String toString(final Location loc) {
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

    /**
     * @param loc a Location
     *
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
        s.append('>');
        return s.toString();
    }

    /**
     * @param string A String representing a location, returned by {@link #toString(Location)} or {@link #toStringPlus(Location)}
     *
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
                    return new Location(world, x, y, z);
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
                    return new Location(world, x, y, z, yaw, pitch);
                } catch (final NumberFormatException e) {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    /**
     * @param vect a Vector
     *
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
        s.append('>');
        return s.toString();
    }

    /**
     * @param string A String representing a vector, returned by {@link #toString(Vector)}
     *
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
                return new Vector(x, y, z);
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
     * @param originalString "A RANDOM_string__wiTh_ strange  cASe"
     *
     * @return "aRandomStringWithStrangeCase"
     */
    public static String toLowerCamelCase(final String originalString) {
        final String workingString = originalString.replace('_', ' ');
        final StringBuilder s = new StringBuilder();
        for (final String word : workingString.split(" ")) {
            if (word.length() > 0) {
                s.append(word.substring(0, 1).toUpperCase());
                s.append(word.substring(1).toLowerCase());
            }
        }
        final String result = s.toString();
        return result.substring(0, 1).toLowerCase() + result.substring(1);
    }

    /**
     * @param nb The wanted String size
     *
     * @return A String of size nb containing spaces
     */
    private static String spaces(final int nb) {
        return multipleChars(nb, ' ');
    }

    /**
     * @param nb Returned String length
     * @param c  Character to fill the String with
     *
     * @return A String containing nb occurrences of c (and nothing else)
     */
    private static String multipleChars(final int nb, final char c) {
        final StringBuilder s = new StringBuilder(nb);
        for (int i = 0; i < nb; i++) {
            s.append(c);
        }
        return s.toString();
    }

    /**
     * Frames a text into a ## box
     *
     * @param messages Lines of the text
     *
     * @return New lines with additional ###
     */
    public static String[] frame(final String... messages) {
        final String[] result = new String[messages.length + 2];
        int maxLength = 0;
        for (final String s : messages) {
            maxLength = Math.max(maxLength, s.length());
        }
        final int length = maxLength + 6;
        result[0] = multipleChars(length, '#');
        for (int i = 0; i < messages.length; i++) {
            result[i + 1] = "## " + messages[i] + spaces(maxLength - messages[i].length()) + " ##";
        }
        result[result.length - 1] = result[0];
        return result;
    }

    public static String ipToString(String string) {
        string = string.replace('.', '-');
        return string;
    }

    public static String stringToIp(String string) {
        string = string.replace('-', '.');
        return string;
    }

    public static String joinStrings(String... strings) {
        return joinStrings(" ", strings);
    }

    public static String joinStrings(String joinString, String... strings) {
        if (strings.length == 0) {
            return "";
        } else {
            StringBuilder builder = new StringBuilder(strings[0]);
            for (int i = 1; i < strings.length; i++) {
                builder.append(joinString);
                builder.append(strings[i]);
            }
            return builder.toString();
        }
    }
}
