/***************************************************************************
 * Project file:    NPlugins - NCore - TimeUtils.java                      *
 * Full Class name: fr.ribesg.bukkit.ncore.utils.TimeUtils                 *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Defines a new Time format that can be used in commands
 * <p/>
 * Example command argument: "4d5hours8s"
 * Example obtainable output: 363608 seconds
 *
 * @author Ribesg
 */
public class TimeUtils {

	/**
	 * This enum is used to:
	 * - Define to-seconds multiplier for each unit
	 * * Define acceptable String representation for each unit
	 */
	private enum TimeUnits {

		/** Base unit */
		SECOND(1L, "s", "sec", "second", "seconds"),

		/** 60 * {@link #SECOND} */
		MINUTE(60L, "m", "min", "minute", "minutes"),

		/** 60 * {@link #MINUTE} */
		HOUR(3600L, "h", "hour", "hours"),

		/** 24 * {@link #HOUR} */
		DAY(86_400L, "d", "day", "days"),

		/** 7 * {@link #DAY} */
		WEEK(604_800L, "w", "week", "weeks"),

		/** Using the average 30 {@link #DAY days} per month value */
		MONTH(2_592_000L, "month", "months"),

		/** Using the average 365.2425 {@link #DAY days} per year value */
		YEAR(31_556_952L, "y", "year", "years");

		/**
		 * Gets a TimeUnit corresponding to this String representation.
		 *
		 * @param representation the string representation
		 *
		 * @return the corresponding TimeUnits enum value or null if not found
		 */
		public static TimeUnits get(String representation) {
			return getMapping().get(representation);
		}

		/**
		 * Maps String representations to their TimeUnits enum value counterpart.
		 * User should use the lazy {@see #getMapping()} getter.
		 */
		private static Map<String, TimeUnits> mapping;

		/**
		 * Lazy Getter for {@link #mapping}
		 *
		 * @return {@link #mapping}
		 */
		private static Map<String, TimeUnits> getMapping() {
			if (mapping == null) {
				mapping = new HashMap<>();
				for (final TimeUnits t : TimeUnits.values()) {
					for (final String representation : t.getStringRepresentations()) {
						mapping.put(representation, t);
					}
				}
			}
			return mapping;
		}

		/** The acceptable String representations of this TimeUnits value */
		private final String[] stringRepresentations;

		/** How many seconds in this unit of time */
		private final long multiplier;

		private TimeUnits(final long multiplier, final String... stringRepresentations) {
			this.multiplier = multiplier;
			this.stringRepresentations = stringRepresentations;
		}

		/** @return {@link #multiplier} */
		private long getMultiplier() {
			return this.multiplier;
		}

		/** @return {@link #stringRepresentations} */
		private String[] getStringRepresentations() {
			return this.stringRepresentations;
		}
	}

	/** Message of the exception thrown when the provided String is invalid */
	private static final String MALFORMED_STRING_MSG = "String was not correctly formatted";

	public static String toString(long seconds) {
		StringBuilder builder = null;

		long years = seconds / TimeUnits.YEAR.getMultiplier();
		if (years != 0) {
			builder = new StringBuilder(years + " year" + (years == 1 ? "" : "s"));
		}
		long leftOver = seconds % TimeUnits.YEAR.getMultiplier();

		long months = leftOver / TimeUnits.MONTH.getMultiplier();
		if (months != 0) {
			if (builder == null) {
				builder = new StringBuilder(months + " month" + (months == 1 ? "" : "s"));
			} else {
				builder.append(", ").append(months).append(" month").append(months == 1 ? "" : "s");
			}
		}
		leftOver = leftOver % TimeUnits.MONTH.getMultiplier();

		long weeks = leftOver / TimeUnits.WEEK.getMultiplier();
		if (weeks != 0) {
			if (builder == null) {
				builder = new StringBuilder(weeks + " week" + (weeks == 1 ? "" : "s"));
			} else {
				builder.append(", ").append(weeks).append(" week").append(weeks == 1 ? "" : "s");
			}
		}
		leftOver = leftOver % TimeUnits.WEEK.getMultiplier();

		long days = leftOver / TimeUnits.DAY.getMultiplier();
		if (days != 0) {
			if (builder == null) {
				builder = new StringBuilder(days + " day" + (days == 1 ? "" : "s"));
			} else {
				builder.append(", ").append(days).append(" day").append(days == 1 ? "" : "s");
			}
		}
		leftOver = leftOver % TimeUnits.DAY.getMultiplier();

		long hours = leftOver / TimeUnits.HOUR.getMultiplier();
		if (hours != 0) {
			if (builder == null) {
				builder = new StringBuilder(hours + " hour" + (hours == 1 ? "" : "s"));
			} else {
				builder.append(", ").append(hours).append(" hour").append(hours == 1 ? "" : "s");
			}
		}
		leftOver = leftOver % TimeUnits.HOUR.getMultiplier();

		long minutes = leftOver / TimeUnits.MINUTE.getMultiplier();
		if (minutes != 0) {
			if (builder == null) {
				builder = new StringBuilder(minutes + " minute" + (minutes == 1 ? "" : "s"));
			} else {
				builder.append(", ").append(minutes).append(" minute").append(minutes == 1 ? "" : "s");
			}
		}
		leftOver = leftOver % TimeUnits.MINUTE.getMultiplier();

		if (builder == null) {
			builder = new StringBuilder(leftOver + " second" + (leftOver == 1 ? "" : "s"));
		} else if (leftOver != 0) {
			builder.append(", ").append(leftOver).append(" second").append(leftOver == 1 ? "" : "s");
		}

		return builder.toString();
	}

	/**
	 * This method parses the provided String and extract the number of seconds it
	 * represents.
	 * <p/>
	 * Example input: "4d5hours8s" will return the number of seconds representing
	 * 4 days + 5 hours + 8 seconds = 363608 seconds
	 *
	 * @param stringRepresentation a string representation of an amount of time
	 *
	 * @return the corresponding amount of seconds
	 *
	 * @throws IllegalArgumentException when the provided String is malformed
	 */
	public static long getInSeconds(final String stringRepresentation) throws IllegalArgumentException {
		final String[][] parsed = split(stringRepresentation);

		// Check that the whole String was correct. How? Rebuild it!
		final StringBuilder reBuilder = new StringBuilder(stringRepresentation.length());
		for (String[] s : parsed) {
			reBuilder.append(s[0]).append(s[1]);
		}
		if (!stringRepresentation.equals(reBuilder.toString())) {
			throw new IllegalArgumentException(MALFORMED_STRING_MSG);
		}

		// Do some conversions and calculations
		long time = 0;
		for (final String[] value : parsed) {
			// parseLong should not be able to throw a NFE here, so don't catch it
			final long val = Long.parseLong(value[0]);
			final TimeUnits unit = TimeUnits.get(value[1]);
			if (unit == null) {
				throw new IllegalArgumentException(MALFORMED_STRING_MSG);
			}
			time += val * unit.getMultiplier();
		}

		return time;
	}

	/**
	 * Black magic regex stuff.
	 * Example input: "4d5hours8s"
	 * Example output: [ ["4","d"], ["5","hours"], ["8","s"] ]
	 *
	 * @param stringRepresentation a string representation of an amount of time
	 *
	 * @return An array of String couple, each couple being the value and its unit
	 */
	private static String[][] split(final String stringRepresentation) {
		final List<String[]> result = new ArrayList<>();
		final Pattern pattern = Pattern.compile("(\\d+)([A-Za-z]+)");
		final Matcher matcher = pattern.matcher(stringRepresentation);
		while (matcher.find()) {
			final String[] matched = new String[] {matcher.group(1), matcher.group(2)};
			result.add(matched);
		}
		return result.toArray(new String[result.size()][2]);
	}
}
