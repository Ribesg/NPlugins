/***************************************************************************
 * Project file:    NPlugins - NCore - VersionUtils.java                   *
 * Full Class name: fr.ribesg.bukkit.ncore.utils.VersionUtils              *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.utils;

/**
 * Utility class to play with Plugin versions
 *
 * @author Ribesg
 */
public class VersionUtils {

	private static final String SNAPSHOT_SUFFIX = "-SNAPSHOT";

	/**
	 * Check if a version represents a Snapshot version or not.
	 *
	 * @return true if the provided String represents a Snapshot version,
	 *         false otherwise
	 */
	public static boolean isSnapshot(final String versionString) {
		return versionString.endsWith(SNAPSHOT_SUFFIX);
	}
}
