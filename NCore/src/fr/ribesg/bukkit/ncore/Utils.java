package fr.ribesg.bukkit.ncore;

import org.bukkit.Location;

/**
 * Some simple methods that could be used in every plugin
 * 
 * @author Ribesg
 */
public class Utils {

    /**
     * @param loc
     *            a Location
     * @return A human-readable String representation of this Location
     */
    public static String toString(final Location loc) {
        final StringBuilder s = new StringBuilder();
        s.append('<');
        s.append(loc.getWorld().getName());
        s.append(',');
        s.append(loc.getBlockX());
        s.append(',');
        s.append(loc.getBlockY());
        s.append(',');
        s.append(loc.getBlockZ());
        s.append('>').toString();
        return s.toString();
    }

    /**
     * Self Explanatory
     */
    public static String toLowerCamelCase(String originalString) {
        originalString = originalString.replace('_', ' ');
        final StringBuilder s = new StringBuilder();
        for (final String word : originalString.split(" ")) {
            s.append(word.substring(0, 1).toUpperCase());
            s.append(word.substring(1).toLowerCase());
        }
        final String result = s.toString();
        return new StringBuilder(
                result.substring(0, 1).toLowerCase()).append(
                result.substring(1)).toString();
    }
}
