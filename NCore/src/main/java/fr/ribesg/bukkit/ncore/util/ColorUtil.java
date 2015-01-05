/***************************************************************************
 * Project file:    NPlugins - NCore - ColorUtil.java                      *
 * Full Class name: fr.ribesg.bukkit.ncore.util.ColorUtil                  *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.util;

import java.util.List;

import org.bukkit.ChatColor;

/**
 * Utility class to handle both Bukkit's and custom color codes.
 *
 * @author Ribesg
 */
public class ColorUtil {

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
     * Transform NPlugins color codes into Bukkit color codes.
     *
     * @param toColorize the Strings to colorize
     *
     * @return the modified Strings
     */
    public static String[] colorize(final String[] toColorize) {
        for (int i = 0; i < toColorize.length; i++) {
            toColorize[i] = colorize(toColorize[i]);
        }
        return toColorize;
    }

    /**
     * Transform NPlugins color codes into Bukkit color codes.
     *
     * @param toColorize the Strings to colorize
     *
     * @return the modified Strings
     */
    public static List<String> colorize(final List<String> toColorize) {
        for (int i = 0; i < toColorize.size(); i++) {
            toColorize.set(i, colorize(toColorize.get(i)));
        }
        return toColorize;
    }

    /**
     * Transform Bukkit color codes into NPlugins color codes.
     *
     * @param toDecolorize the String to decolorize
     *
     * @return the modified String
     */
    public static String decolorize(final String toDecolorize) {
        final char[] result = toDecolorize.toCharArray();
        for (int i = 0; i < result.length - 1; i++) {
            if (result[i] == '§') {
                if (ChatColor.getByChar(result[i + 1]) != null) {
                    result[i] = '&';
                }
            }
        }
        return new String(result);
    }

    /**
     * Transform Bukkit color codes into NPlugins color codes.
     *
     * @param toDecolorize the Strings to decolorize
     *
     * @return the modified Strings
     */
    public static String[] decolorize(final String[] toDecolorize) {
        for (int i = 0; i < toDecolorize.length; i++) {
            toDecolorize[i] = decolorize(toDecolorize[i]);
        }
        return toDecolorize;
    }

    /**
     * Transform Bukkit color codes into NPlugins color codes.
     *
     * @param toDecolorize the Strings to decolorize
     *
     * @return the modified Strings
     */
    public static List<String> decolorize(final List<String> toDecolorize) {
        for (int i = 0; i < toDecolorize.size(); i++) {
            toDecolorize.set(i, decolorize(toDecolorize.get(i)));
        }
        return toDecolorize;
    }

    /**
     * Removes all types of color codes: Bukkit and NPlugins ones.
     *
     * @param toStrip the String to strip
     *
     * @return the same String without any color codes
     */
    public static String stripColorCodes(final String toStrip) {
        return ChatColor.stripColor(colorize(toStrip));
    }

    /**
     * Removes all types of color codes: Bukkit and NPlugins ones.
     *
     * @param toStrip the Strings to strip
     *
     * @return the same Strings without any color codes
     */
    public static String[] stripColorCodes(final String[] toStrip) {
        for (int i = 0; i < toStrip.length; i++) {
            toStrip[i] = stripColorCodes(toStrip[i]);
        }
        return toStrip;
    }

    /**
     * Removes all types of color codes: Bukkit and NPlugins ones.
     *
     * @param toStrip the Strings to strip
     *
     * @return the same Strings without any color codes
     */
    public static List<String> stripColorCodes(final List<String> toStrip) {
        for (int i = 0; i < toStrip.size(); i++) {
            toStrip.set(i, stripColorCodes(toStrip.get(i)));
        }
        return toStrip;
    }
}
