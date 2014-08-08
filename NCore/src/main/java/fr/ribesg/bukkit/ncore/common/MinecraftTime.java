/***************************************************************************
 * Project file:    NPlugins - NCore - MinecraftTime.java                  *
 * Full Class name: fr.ribesg.bukkit.ncore.common.MinecraftTime            *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.common;

/**
 * This class can be used to work with the Time in Minecraft.
 * It contains some magic values about "real" day/night time.
 *
 * @author Ribesg
 */
public enum MinecraftTime {

	/**
	 * Moment between "full dark" and "full bright"
	 */
	MORNING(ExactTime.NIGHT_END, ExactTime.DAY_START),

	/**
	 * Always "full bright"
	 */
	DAY(ExactTime.DAY_START, ExactTime.DAY_END),

	/**
	 * Moment between "full bright" and "full dark"
	 */
	EVENING(ExactTime.DAY_END, ExactTime.NIGHT_START),

	/**
	 * Always "full dark"
	 */
	NIGHT(ExactTime.NIGHT_START, ExactTime.NIGHT_END);

	/**
	 * This little static class is used to pre-define magic values,
	 * so that they can be used in the Enum constructor.
	 */
	private static class ExactTime {

		private static final long NIGHT_START = 13500;
		private static final long NIGHT_END   = 22500;
		private static final long DAY_START   = 23500;
		private static final long DAY_END     = 12500;
	}

	public static final long DAY_LENGTH = 24_000;

	/**
	 * @param time some absolute or relative time
	 *
	 * @return true if the provided time represents Morning, false otherwise
	 */
	public static boolean isMorningTime(final long time) {
		return MORNING.is(time);
	}

	/**
	 * @param time some absolute or relative time
	 *
	 * @return true if the provided time represents Day, false otherwise
	 */
	public static boolean isDayTime(final long time) {
		return DAY.is(time);
	}

	/**
	 * @param time some absolute or relative time
	 *
	 * @return true if the provided time represents Evening, false otherwise
	 */
	public static boolean isEveningTime(final long time) {
		return EVENING.is(time);
	}

	/**
	 * @param time some absolute or relative time
	 *
	 * @return true if the provided time represents Night, false otherwise
	 */
	public static boolean isNightTime(final long time) {
		return NIGHT.is(time);
	}

	/**
	 * @param time some absolute or relative time
	 *
	 * @return the MinecraftTime value for the provided time
	 */
	public static MinecraftTime get(final long time) {
		for (final MinecraftTime t : MinecraftTime.values()) {
			if (t.is(time)) {
				return t;
			}
		}
		// Dead code
		return null;
	}

	private final long start;
	private final long end;

	private MinecraftTime(final long start, final long end) {
		this.start = start;
		this.end = end;
	}

	/**
	 * Get the time at which this period starts
	 *
	 * @return this period's start time
	 */
	public long start() {
		return this.start;
	}

	/**
	 * Get the time at which this period ends
	 *
	 * @return this period's end time
	 */
	public long end() {
		return this.end;
	}

	/**
	 * Checks if the provided time corresponds to this Enum value
	 *
	 * @param time some absolute or relative time
	 *
	 * @return true if the provided time corresponds to this Enum value
	 */
	private boolean is(final long time) {
		final long notFullTime = time % DAY_LENGTH;
		if (this.start < this.end) {
			return this.start <= notFullTime && notFullTime < this.end;
		} else {
			return this.start <= notFullTime || notFullTime < this.end;
		}
	}
}
