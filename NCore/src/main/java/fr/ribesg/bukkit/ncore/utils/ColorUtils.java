/***************************************************************************
 * Project file:    NPlugins - NCore - ColorUtils.java                     *
 * Full Class name: fr.ribesg.bukkit.ncore.utils.ColorUtils                *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.utils;
import org.bukkit.ChatColor;

/**
 * Utility class to handle both Bukkit's and custom color codes.
 *
 * @author Ribesg
 */
public class ColorUtils {

	public static final char   ALTERNATE_COLOR_CHAR        = '&';
	public static final String ALTERNATE_COLOR_CHAR_STRING = Character.toString(ALTERNATE_COLOR_CHAR);

	/**
	 * Transform NPlugins color codes into Bukkit color codes.
	 *
	 * @param toColorize the String to colorize
	 *
	 * @return the modified String
	 */
	public static String colorize(final String toColorize) {
		return ChatColor.translateAlternateColorCodes(ALTERNATE_COLOR_CHAR, toColorize);
	}

	/**
	 * Removes all types of color codes: Bukkit & NPlugins ones.
	 *
	 * @param toStrip the String to strip
	 *
	 * @return the same String without any color codes
	 */
	public static String stripColorCodes(final String toStrip) {
		return ChatColor.stripColor(colorize(toStrip));
	}

}
