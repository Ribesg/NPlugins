/***************************************************************************
 * Project file:    NPlugins - NCore - VersionUtil.java                    *
 * Full Class name: fr.ribesg.bukkit.ncore.util.VersionUtil                *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to play with Plugin versions
 *
 * @author Ribesg
 */
public class VersionUtil {

    private static final Pattern VERSION_REGEX       = Pattern.compile("^(v?\\d+\\.\\d+\\.\\d+).*$");
    private static final Pattern VERSION_PARTS_REGEX = Pattern.compile("^v?(\\d+)\\.(\\d+)\\.(\\d+).*$");

    /**
     * Check if a version represents a Snapshot version or not.
     *
     * @param versionString a version String
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
     * @param versionString a version String
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
        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            return "";
        }
    }

    /**
     * Compares two versions.
     *
     * @param a a version String
     * @param b another version String
     *
     * @return -1 if a is older than b, 1 if b is older than a, 0 otherwise
     */
    public static int compare(final String a, final String b) {
        final Matcher aMatcher = VERSION_PARTS_REGEX.matcher(a);
        final Matcher bMatcher = VERSION_PARTS_REGEX.matcher(b);
        if (!aMatcher.matches()) {
            throw new IllegalArgumentException(a + " isn't a valid version String");
        } else if (!bMatcher.matches()) {
            throw new IllegalArgumentException(b + " isn't a valid version String");
        } else {
            final int a1, a2, a3, b1, b2, b3;
            try {
                a1 = Integer.parseInt(aMatcher.group(1));
                a2 = Integer.parseInt(aMatcher.group(2));
                a3 = Integer.parseInt(aMatcher.group(3));
            } catch (final NumberFormatException | IndexOutOfBoundsException e) {
                throw new IllegalArgumentException(a + " isn't a valid version String");
            }
            try {
                b1 = Integer.parseInt(bMatcher.group(1));
                b2 = Integer.parseInt(bMatcher.group(2));
                b3 = Integer.parseInt(bMatcher.group(3));
            } catch (final NumberFormatException | IndexOutOfBoundsException e) {
                throw new IllegalArgumentException(b + " isn't a valid version String");
            }
            int cmp;
            if ((cmp = Integer.compare(a1, b1)) != 0) {
                return cmp < 0 ? -1 : 1;
            } else if ((cmp = Integer.compare(a2, b2)) != 0) {
                return cmp < 0 ? -1 : 1;
            } else {
                cmp = Integer.compare(a3, b3);
                return cmp < 0 ? -1 : cmp > 0 ? 1 : 0;
            }
        }
    }
}
