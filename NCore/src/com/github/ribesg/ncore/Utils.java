package com.github.ribesg.ncore;

import org.bukkit.Location;

public class Utils {

    public static String toString(final Location loc) {
        return new StringBuilder().append('<').append(loc.getBlockX()).append(',').append(loc.getBlockY()).append(',').append(loc.getBlockZ()).append('>').toString();
    }
}
