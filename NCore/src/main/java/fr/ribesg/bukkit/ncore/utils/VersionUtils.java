/***************************************************************************
 * Project file:    NPlugins - NCore - VersionUtils.java                   *
 * Full Class name: fr.ribesg.bukkit.ncore.utils.VersionUtils              *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to play with Plugin versions
 *
 * @author Ribesg
 */
public class VersionUtils {

	private static final Pattern VERSION_REGEX = Pattern.compile("^(v\\d+.\\d+.\\d+)");

	/**
	 * Check if a version represents a Snapshot version or not.
	 *
	 * @return true if the provided String represents a Snapshot version,
	 * false otherwise
	 */
	public static boolean isSnapshot(final String versionString) {
		return versionString.toLowerCase().contains("-snapshot") && VERSION_REGEX.matcher(versionString).find();
	}

	/**
	 * Check if a version represents a Release version or not.
	 *
	 * @return true if the provided String represents a Release version,
	 * false otherwise
	 */
	public static boolean isRelease(final String versionString) {
		return !versionString.toLowerCase().contains("-snapshot") && VERSION_REGEX.matcher(versionString).find();
	}

	/**
	 * Gets the version part in this String.
	 * Example: for "v0.4.2 (Foo)", returns "v0.4.2"
	 *
	 * @param versionString the version String
	 *
	 * @return the version in this String, or null if there's none
	 */
	public static String getVersion(final String versionString) {
		final Matcher matcher = VERSION_REGEX.matcher(versionString);
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return null;
		}
	}

}
