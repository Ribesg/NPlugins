/***************************************************************************
 * Project file:    NPlugins - NCore - StringUtils.java                    *
 * Full Class name: fr.ribesg.bukkit.ncore.utils.StringUtils               *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.utils;

import org.bukkit.util.Vector;

import java.util.Arrays;

/**
 * Some simple methods that could be used in every plugin
 *
 * @author Ribesg
 */
public class StringUtils {

	private static final char   SEPARATOR_CHAR        = ';';
	private static final String SEPARATOR_CHAR_STRING = Character.toString(SEPARATOR_CHAR);

	/**
	 * @param vect a Vector
	 *
	 * @return A human-readable String representation of this Vector
	 */
	public static String toString(final Vector vect) {
		if (vect == null) {
			return "null";
		}
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
		if (string == null || "null".equals(string)) {
			return null;
		}
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

	public static String joinStrings(final String[] array, final int start) {
		return joinStrings(array, start, array.length);
	}

	public static String joinStrings(final String[] array, final int start, final int end) {
		return joinStrings(" ", array, start, end);
	}

	public static String joinStrings(final String joinString, final String[] array, final int start) {
		return joinStrings(joinString, array, start, array.length);
	}

	public static String joinStrings(final String joinString, final String[] array, final int start, final int end) {
		return joinStrings(joinString, Arrays.copyOfRange(array, start, end));
	}

	public static String joinStrings(final String... strings) {
		return joinStrings(" ", strings);
	}

	public static String joinStrings(final String joinString, final String... strings) {
		if (strings.length == 0) {
			return "";
		} else {
			final StringBuilder builder = new StringBuilder(strings[0]);
			for (int i = 1; i < strings.length; i++) {
				builder.append(joinString);
				builder.append(strings[i]);
			}
			return builder.toString();
		}
	}
}
