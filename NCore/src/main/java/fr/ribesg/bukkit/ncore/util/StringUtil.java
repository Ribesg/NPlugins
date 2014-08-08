/***************************************************************************
 * Project file:    NPlugins - NCore - StringUtil.java                     *
 * Full Class name: fr.ribesg.bukkit.ncore.util.StringUtil                 *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.util;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.util.Vector;

/**
 * Some simple methods that could be used in every plugin
 *
 * @author Ribesg
 */
public class StringUtil {

    private static final Logger LOGGER = Logger.getLogger(StringUtil.class.getName());

    private static final char   SEPARATOR_CHAR        = ';';
    private static final String SEPARATOR_CHAR_STRING = Character.toString(SEPARATOR_CHAR);

    /**
     * An arbitrary list of possible separator characters.
     * Used to save Lore to String. Two of them are randomly chosen randomly
     * and the combination of both is used as separator.
     * The only way to break it would be to have Lore Strings contain every
     * single possible 2-length combination of those characters.
     */
    private static final CharSequence SEPARATOR_CHARS = ",;:!?§/.*+-=@_-|#~&$£¤°<>()[]{}";

    private static final Random RANDOM = new Random();

    /**
     * @param vect a Vector
     *
     * @return A human-readable String representation of this Vector
     */
    public static String toString(final Vector vect) {
        if (vect == null) {
            return "null";
        }
        final StringBuilder s = new StringBuilder();
        s.append('<');
        s.append(vect.getX());
        s.append(SEPARATOR_CHAR);
        s.append(vect.getY());
        s.append(SEPARATOR_CHAR);
        s.append(vect.getZ());
        s.append('>');
        return s.toString();
    }

    /**
     * @param string A String representing a vector, returned by {@link #toString(Vector)}
     *
     * @return The actual Vector or null if the string was malformed
     */
    public static Vector toVector(final String string) {
        if (string == null || "null".equals(string)) {
            return null;
        }
        if (string.length() < 2) {
            return null;
        }
        final String[] split = string.substring(1, string.length() - 1).split(SEPARATOR_CHAR_STRING);
        if (split.length == 3) {
            try {
                final Double x = Double.parseDouble(split[0]);
                final Double y = Double.parseDouble(split[1]);
                final Double z = Double.parseDouble(split[2]);
                return new Vector(x, y, z);
            } catch (final NumberFormatException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Self Explanatory
     *
     * @param originalString "A RANDOM_string__wiTh_ strange  cASe"
     *
     * @return "aRandomStringWithStrangeCase"
     */
    public static String toLowerCamelCase(final String originalString) {
        final String workingString = originalString.replace('_', ' ');
        final StringBuilder s = new StringBuilder();
        for (final String word : workingString.split(" ")) {
            if (!word.isEmpty()) {
                s.append(word.substring(0, 1).toUpperCase());
                s.append(word.substring(1).toLowerCase());
            }
        }
        final String result = s.toString();
        return result.substring(0, 1).toLowerCase() + result.substring(1);
    }

    public static String joinStrings(final String[] array, final int start) {
        return joinStrings(array, start, array.length);
    }

    public static String joinStrings(final String[] array, final int start, final int end) {
        return joinStrings(" ", array, start, end);
    }

    public static String joinStrings(final String joinString, final String[] array, final int start) {
        return joinStrings(joinString, array, start, array.length);
    }

    public static String joinStrings(final String joinString, final String[] array, final int start, final int end) {
        return joinStrings(joinString, Arrays.copyOfRange(array, start, end));
    }

    public static String joinStrings(final String... strings) {
        return joinStrings(" ", strings);
    }

    public static String joinStrings(final String joinString, final String... strings) {
        if (strings.length == 0) {
            return "";
        } else {
            final StringBuilder builder = new StringBuilder(strings[0]);
            for (int i = 1; i < strings.length; i++) {
                builder.append(joinString);
                builder.append(strings[i]);
            }
            return builder.toString();
        }
    }

    public static String getPossibleSeparator(final List<String> strings, final int size) {
        int i = 1337; // Maximum tries
        String separator;
        boolean notContained;
        while (i-- > 0) {
            notContained = true;
            separator = getRandomSeparator(size);
            for (final String s : strings) {
                if (s.contains(separator)) {
                    notContained = false;
                    break;
                }
            }
            if (notContained) {
                return separator;
            }
        }
        throw new IllegalStateException("Cannot find a separator for provided list of Strings, it's a trap!");
    }

    private static String getRandomSeparator(final int size) {
        assert size > 0;
        final StringBuilder builder = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            builder.append(getRandomCharacterSeparator());
        }
        return builder.toString();
    }

    private static char getRandomCharacterSeparator() {
        return SEPARATOR_CHARS.charAt(RANDOM.nextInt(SEPARATOR_CHARS.length()));
    }

    /**
     * Better split method than String.split(...)
     * <p>
     * Example:
     * ";;;".split(";"); =&gt; {}
     * splitEmpty(";;;", ";") =&gt; {"","","",""}
     *
     * @param string the String to be splitted
     * @param split  the separator on which we want to split. Not a regex.
     *
     * @return an array of resulting Strings
     */
    public static String[] splitKeepEmpty(final String string, final String split) {
        final String[] result = new String[count(string, split) + 1];
        int from, index = -split.length();
        for (int i = 0; i < result.length; i++) {
            from = index + split.length();
            index = string.indexOf(split, from);
            if (index == -1) {
                index = string.length();
            }
            result[i] = string.substring(from, index);
        }
        return result;
    }

    /**
     * Count the number of occurence of substring in inString.
     *
     * @param substring the substring to count
     * @param inString  the String in which we count
     *
     * @return the number of occurences of substring in inString
     */
    public static int count(final String inString, final String substring) {
        int result = 0;
        int index = inString.indexOf(substring);
        while (index != -1) {
            result++;
            index = inString.indexOf(substring, index + substring.length());
        }
        return result;
    }

    /**
     * Prepend each line of the provided String with the provided String
     *
     * @param string the String to be prefixed
     * @param prefix the prefix to be applied
     *
     * @return the original String with prefix applied
     */
    public static String prependLines(final String string, final String prefix) {
        String result = prefix + string.replaceAll("\r\n", "\n").replaceAll("\n", '\n' + prefix);
        if (result.endsWith(prefix)) {
            result = result.substring(0, result.length() - prefix.length() + 1);
        }
        return result;
    }
}
