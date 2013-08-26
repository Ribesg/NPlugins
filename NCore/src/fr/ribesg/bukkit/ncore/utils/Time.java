package fr.ribesg.bukkit.ncore.utils;

public enum Time {
	DAY,
	NIGHT;

	private static final int EVENING = 13500;
	private static final int MORNING = 22500;

	public static boolean isDayTime(final long time) {
		return time > MORNING || time <= EVENING;
	}

	public static boolean isNightTime(final long time) {
		return !isDayTime(time);
	}

	public static Time get(final long time) {
		return isDayTime(time) ? DAY : NIGHT;
	}
}
