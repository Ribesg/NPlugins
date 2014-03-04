/***************************************************************************
 * Project file:    NPlugins - NCore - UsernameUtils.java                  *
 * Full Class name: fr.ribesg.bukkit.ncore.utils.UsernameUtils             *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.utils;
import java.math.BigInteger;
import java.util.regex.Pattern;

/**
 * Utility class that allows to create 13-chars player ids
 * with a very low chance of collision.
 *
 * @author Ribesg
 */
public class UsernameUtils {

	private static final Pattern USERNAME_REGEX = Pattern.compile("^[a-z0-9_]{2,16}$/i");

	/**
	 * Returns a 13-chars (or less) "unique" ID based on a 2-16 chars Username
	 *
	 * @param username The user name
	 *
	 * @return a 2-13 chars String
	 */
	public static String getId(final String username) {
		if (username == null) {
			return null;
		} else if (username.length() <= 13) {
			return username;
		} else {
			final StringBuilder result = new StringBuilder(13);
			result.append(username.substring(0, 10));
			final BigInteger hash = BigInteger.valueOf(username.hashCode());
			final String hashString = new String(hash.toByteArray());
			result.append('-');
			result.append(hashString.substring(0, 2));
			return result.toString();
		}
	}

	/**
	 * Checks that a String can represent a valid Minecraft UserName.
	 *
	 * @param userName the UserName to check
	 *
	 * @return true if the provided String can represent a valid Minecraft
	 * UserName, false otherwise
	 */
	public static boolean isValidMinecraftUserName(final String userName) {
		return USERNAME_REGEX.matcher(userName).matches();
	}

	/**
	 * Checks that a String can represent a valid NickName.
	 * A valid NickName is something that stripped from any color, can be a
	 * valid Minecraft UserName, according to
	 * {@link #isValidMinecraftUserName(String)}.
	 *
	 * @param nickName the NickName to check
	 *
	 * @return true if the provided String can represent a valid NickName,
	 * false otherwise
	 */
	public static boolean isValidNickName(final String nickName) {
		return USERNAME_REGEX.matcher(ColorUtils.stripColorCodes(nickName)).matches();
	}
}
