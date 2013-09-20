package fr.ribesg.bukkit.ncore.utils;
import java.math.BigInteger;

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
