/***************************************************************************
 * Project file:    NPlugins - NCore - IPValidator.java                    *
 * Full Class name: fr.ribesg.bukkit.ncore.utils.IPValidator               *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.utils;
import java.util.regex.Pattern;

/**
 * Utility class to validate IPs.
 *
 * @author Ribesg
 */
public class IPValidator {

	/** This Regex recognize any valid IPv4 between 0.0.0.0 and 255.255.255.255 */
	private static final String IP_V4_REGEX = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2" +
	                                          "[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

	/** This Pattern recognize any valid IPv4 between 0.0.0.0 and 255.255.255.255 */
	private static final Pattern IP_V4 = Pattern.compile(IP_V4_REGEX);

	/** This Regex recognize any valid IPv6 (even ::1 and other strange patterns) */
	private static final String IP_V6_REGEX = "^((([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0" +
	                                          "-9A-Fa-f]{1,4}:){5}:([0-9A-Fa-f]{1,4}:)?[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){4}:([0-9A-" +
	                                          "Fa-f]{1,4}:){0,2}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){3}:([0-9A-Fa-f]{1,4}:){0,3}[0-9A-" +
	                                          "Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){2}:([0-9A-Fa-f]{1,4}:){0,4}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-" +
	                                          "f]{1,4}:){6}((b((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))b).){3}(b((25[0-5])|(1d{2})|(2[0-4]d" +
	                                          ")|(d{1,2}))b))|(([0-9A-Fa-f]{1,4}:){0,5}:((b((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))b).){3}" +
	                                          "(b((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))b))|(::([0-9A-Fa-f]{1,4}:){0,5}((b((25[0-5])|(1d{" +
	                                          "2})|(2[0-4]d)|(d{1,2}))b).){3}(b((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))b))|([0-9A-Fa-f]{1," +
	                                          "4}::([0-9A-Fa-f]{1,4}:){0,5}[0-9A-Fa-f]{1,4})|(::([0-9A-Fa-f]{1,4}:){0,6}[0-9A-Fa-f]{1,4}" +
	                                          ")|(([0-9A-Fa-f]{1,4}:){1,7}:))$";

	/** This Pattern recognize any valid IPv6 (even ::1 and other strange patterns) */
	private static final Pattern IP_V6 = Pattern.compile(IP_V6_REGEX);

	/** This Regex recognize any valid IPv4 or IPv6 */
	private static final String IP_ALL_REGEX = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]" +
	                                           "|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))|((([0-9A-Fa-f]{1," +
	                                           "4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){" +
	                                           "5}:([0-9A-Fa-f]{1,4}:)?[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){4}:([0-9A-Fa-f]{1,4}:){0,2" +
	                                           "}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){3}:([0-9A-Fa-f]{1,4}:){0,3}[0-9A-Fa-f]{1,4})|(([" +
	                                           "0-9A-Fa-f]{1,4}:){2}:([0-9A-Fa-f]{1,4}:){0,4}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}((" +
	                                           "b((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))b).){3}(b((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))b)" +
	                                           ")|(([0-9A-Fa-f]{1,4}:){0,5}:((b((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))b).){3}(b((25[0-5])" +
	                                           "|(1d{2})|(2[0-4]d)|(d{1,2}))b))|(::([0-9A-Fa-f]{1,4}:){0,5}((b((25[0-5])|(1d{2})|(2[0-4]" +
	                                           "d)|(d{1,2}))b).){3}(b((25[0-5])|(1d{2})|(2[0-4]d)|(d{1,2}))b))|([0-9A-Fa-f]{1,4}::([0-9A" +
	                                           "-Fa-f]{1,4}:){0,5}[0-9A-Fa-f]{1,4})|(::([0-9A-Fa-f]{1,4}:){0,6}[0-9A-Fa-f]{1,4})|(([0-9A" +
	                                           "-Fa-f]{1,4}:){1,7}:))$";

	/** This Pattern recognize any valid IPv4 or IPv6 */
	private static final Pattern IP_ALL = Pattern.compile(IP_ALL_REGEX);

	/**
	 * @param ip a supposed IPv4
	 *
	 * @return if the provided String is a valid IPv4
	 */
	public static boolean isValidIPv4(final String ip) {
		return IP_V4.matcher(ip).matches();
	}

	/**
	 * @param ip a supposed IPv6
	 *
	 * @return if the provided String is a valid IPv6
	 */
	public static boolean isValidIPv6(final String ip) {
		return IP_V6.matcher(ip).matches();
	}

	/**
	 * @param ip a supposed IPv4 or IPv6
	 *
	 * @return if the provided String is a valid IPv4 or IPv6
	 */
	public static boolean isValidIp(final String ip) {
		return IP_ALL.matcher(ip).matches();
	}
}
