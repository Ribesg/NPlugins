package fr.ribesg.bukkit.ncore.utils;

public enum Time {
    DAY,
    NIGHT;

    public static boolean isDayTime(final long time) {
        return time >= 0 && time <= 12000;
    }

    public static boolean isNightTime(final long time) {
        return time >= 12000 && time <= 24000;
    }
}
