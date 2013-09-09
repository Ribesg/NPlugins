package fr.ribesg.bukkit.ncore.common;

public enum MinecraftTime {
	MORNING(ExactTime.NIGHT_END, ExactTime.DAY_START),
	DAY(ExactTime.DAY_START, ExactTime.DAY_END),
	EVENING(ExactTime.DAY_END, ExactTime.NIGHT_START),
	NIGHT(ExactTime.NIGHT_START, ExactTime.NIGHT_END);

	private static class ExactTime {

		private static final long NIGHT_START = 13500;
		private static final long NIGHT_END   = 22500;
		private static final long DAY_START   = 23500;
		private static final long DAY_END     = 12500;
	}

	public static final long DAY_LENGTH = 24_000;

	public static boolean isMorningTime(final long time) {
		return MORNING.is(time);
	}

	public static boolean isDayTime(final long time) {
		return DAY.is(time);
	}

	public static boolean isEveningTime(final long time) {
		return EVENING.is(time);
	}

	public static boolean isNightTime(final long time) {
		return NIGHT.is(time);
	}

	public static MinecraftTime get(final long time) {
		for (final MinecraftTime t : MinecraftTime.values()) {
			if (t.is(time)) {
				return t;
			}
		}
		// Dead code
		return null;
	}

	private long start;
	private long end;

	private MinecraftTime(final long start, final long end) {
		this.start = start;
		this.end = end;
	}

	public long start() {
		return this.start;
	}

	public long end() {
		return this.end;
	}

	private boolean is(final long time) {
		final long notFullTime = time % DAY_LENGTH;
		if (this.start < this.end) {
			return this.start <= notFullTime && notFullTime < this.end;
		} else {
			return this.start <= notFullTime || notFullTime < this.end;
		}
	}
}
