/***************************************************************************
 * Project file:    NPlugins - NCore - PlayerIdsUtils.java                 *
 * Full Class name: fr.ribesg.bukkit.ncore.utils.PlayerIdsUtils            *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.utils;
import java.math.BigInteger;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Utility class that allows to create 13-chars player ids
 * with a very low chance of collision.
 *
 * @author Ribesg
 */
public class PlayerIdsUtils {

	private static final Pattern USERNAME_REGEX   = Pattern.compile("^[a-zA-Z0-9_]{2,16}$");
	private static final Pattern SHORT_UUID_REGEX = Pattern.compile("^[a-zA-Z0-9]{32}$");

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

	/**
	 * Converts a UUID to a short UUID.
	 * <p>
	 * Example:
	 * <ul>
	 * <li>Input: 069a79f4-44e9-4726-a5be-fca90e38aaf5</li>
	 * <li>Output: 069a79f444e94726a5befca90e38aaf5</li>
	 * </ul>
	 *
	 * @param uuid the UUID to convert
	 *
	 * @return a short UUID representing the same UUID than input
	 */
	public static String uuidToShortUuid(final UUID uuid) {
		return uuid.toString().replaceAll("-", "");
	}

	/**
	 * Converts a short UUID to a UUID.
	 * <p>
	 * Example:
	 * <ul>
	 * <li>Input: 069a79f444e94726a5befca90e38aaf5</li>
	 * <li>Output: 069a79f4-44e9-4726-a5be-fca90e38aaf5</li>
	 * </ul>
	 *
	 * @param shortUuid the short UUID to convert
	 *
	 * @return a UUID representing the same UUID than input
	 */
	public static UUID shortUuidToUuid(final String shortUuid) {
		if (!SHORT_UUID_REGEX.matcher(shortUuid).matches()) {
			throw new IllegalArgumentException("Not a short UUID: " + shortUuid);
		}
		return UUID.fromString(shortUuid.substring(0, 8) + '-' +
		                       shortUuid.substring(8, 12) + '-' +
		                       shortUuid.substring(12, 16) + '-' +
		                       shortUuid.substring(16, 20) + '-' +
		                       shortUuid.substring(20, 32));
	}
}
