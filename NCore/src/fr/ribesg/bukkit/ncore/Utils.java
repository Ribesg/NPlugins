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
        StringBuilder s = new StringBuilder();
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
}
