/***************************************************************************
 * Project file:    NPlugins - NCore - Log4jFilterManager.java             *
 * Full Class name: fr.ribesg.bukkit.ncore.common.logging.Log4jFilterManager
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.common.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.core.filter.RegexFilter;

/**
 * Manages log4j2 filters
 *
 * @author Ribesg
 */
/* package */ class Log4jFilterManager extends FilterManager {

    /**
     * A Log4j2 Filter based on a {@link DenyFilter}.
     */
    private class Log4j2DenyFilter extends AbstractFilter {

        private final DenyFilter denyFilter;

        private Log4j2DenyFilter(final DenyFilter denyFilter) {
            super();
            this.denyFilter = denyFilter;
        }

        @Override
        public Result filter(final LogEvent event) {
            return this.denyFilter.denies(event.getMessage().getFormattedMessage()) ? Result.DENY : Result.NEUTRAL;
        }
    }

    @Override
    public void addRegexFilter(final String regex) {
        this.addFilter(RegexFilter.createFilter(regex, "FALSE", "DENY", "NEUTRAL"));
    }

    @Override
    public void addDenyFilter(final DenyFilter denyFilter) {
        this.addFilter(new Log4j2DenyFilter(denyFilter));
    }

    /**
     * Adds a new log4j2 Filter to log4j.
     *
     * @param filter a log4j2 filter
     */
    private void addFilter(final Filter filter) {
        final LoggerContext context = (LoggerContext)LogManager.getContext(false);
        final Configuration config = context.getConfiguration();
        for (final LoggerConfig loggerConfig : config.getLoggers().values()) {
            loggerConfig.addFilter(filter);
        }
    }
}
