/***************************************************************************
 * Project file:    NPlugins - NCore - UsernameUtils.java                  *
 * Full Class name: fr.ribesg.bukkit.ncore.utils.UsernameUtils             *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.utils;
import java.math.BigInteger;

/**
 * Utility class that allows to create 13-chars player ids
 * with a very low chance of collision.
 *
 * @author Ribesg
 */
public class UsernameUtils {

	/**
	 * Returns a 13-chars (or less) "unique" ID based on a 3-16 chars Username
	 *
	 * @param username The user name
	 *
	 * @return a 3-13 chars String
	 */
	public static String getId(String username) {
		if (username == null) {
			return null;
		} else if (username.length() <= 13) {
			return username;
		} else {
			StringBuilder result = new StringBuilder(13);
			result.append(username.substring(0, 10));
			final BigInteger hash = BigInteger.valueOf(username.hashCode());
			final String hashString = new String(hash.toByteArray());
			result.append('-');
			result.append(hashString.substring(0, 2));
			return result.toString();
		}
	}
}
