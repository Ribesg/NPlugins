/***************************************************************************
 * Project file:    NPlugins - NCore - FilterManager.java                  *
 * Full Class name: fr.ribesg.bukkit.ncore.common.logging.FilterManager    *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.common.logging;

/**
 * Manages log filters
 *
 * @author Ribesg
 */
public abstract class FilterManager {

    private static FilterManager manager;

    /**
     * Creates a FilterManager.
     *
     * @return a FilterManager
     */
    public static FilterManager create() {
        if (FilterManager.manager == null) {
            try {
                Class.forName("org.apache.logging.log4j.LogManager");
                // No exception? Log4j!
                FilterManager.manager = new Log4jFilterManager();
            } catch (final ClassNotFoundException e) {
                // Exception? Java Logging!
                FilterManager.manager = new JavaLoggingFilterManager();
            }
        }
        return FilterManager.manager;
    }

    /**
     * Adds a new RegexFilter to the logging system.
     *
     * @param regex a regex String
     */
    public abstract void addRegexFilter(final String regex);

    /**
     * Adds a new DenyFilter to the logging system.
     *
     * @param denyFilter the filter
     */
    public abstract void addDenyFilter(final DenyFilter denyFilter);
}
