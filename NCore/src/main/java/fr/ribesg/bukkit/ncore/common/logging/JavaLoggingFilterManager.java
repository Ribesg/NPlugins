/***************************************************************************
 * Project file:    NPlugins - NCore - JavaLoggingFilterManager.java       *
 * Full Class name: fr.ribesg.bukkit.ncore.common.logging.JavaLoggingFilterManager
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.common.logging;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Filter;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.regex.Pattern;

/**
 * Manages Java Logging filters
 *
 * @author Ribesg
 */
public class JavaLoggingFilterManager extends FilterManager {

    /**
     * A Java Logging Filter based on a regex.
     */
    private class RegexFilter implements Filter {

        private final Pattern pattern;

        private RegexFilter(final String regex) {
            this.pattern = Pattern.compile(regex);
        }

        @Override
        public boolean isLoggable(final LogRecord record) {
            return this.pattern.matcher(record.getMessage()).matches();
        }
    }

    /**
     * A Java Logging Filter based on a {@link DenyFilter}.
     */
    private class JavaLoggingDenyFilter implements Filter {

        private final DenyFilter denyFilter;

        private JavaLoggingDenyFilter(final DenyFilter denyFilter) {
            super();
            this.denyFilter = denyFilter;
        }

        @Override
        public boolean isLoggable(final LogRecord record) {
            return this.denyFilter.denies(record.getMessage());
        }
    }

    /**
     * A Java Logging Filter that aggregates multiple Filters.
     */
    private class JavaLoggingMetaFilter implements Filter {

        private final List<Filter> filters;

        private JavaLoggingMetaFilter() {
            super();
            this.filters = new LinkedList<>();
        }

        public void addFilter(final Filter filter) {
            this.filters.add(filter);
        }

        @Override
        public boolean isLoggable(final LogRecord record) {
            for (final Filter f : this.filters) {
                if (!f.isLoggable(record)) {
                    return false;
                }
            }
            return true;
        }
    }

    private final JavaLoggingMetaFilter metaFilter;

    public JavaLoggingFilterManager() {
        super();
        this.metaFilter = new JavaLoggingMetaFilter();
        final LogManager manager = LogManager.getLogManager();
        final Enumeration<String> loggerNames = manager.getLoggerNames();
        while (loggerNames.hasMoreElements()) {
            final String loggerName = loggerNames.nextElement();
            manager.getLogger(loggerName).setFilter(this.metaFilter);
        }
    }

    @Override
    public void addRegexFilter(final String regex) {
        this.addFilter(new RegexFilter(regex));
    }

    @Override
    public void addDenyFilter(final DenyFilter denyFilter) {
        this.addFilter(new JavaLoggingDenyFilter(denyFilter));
    }

    /**
     * Adds a new Java Logging Filter to Java Logging.
     *
     * @param filter a Java Logging filter
     */
    private void addFilter(final Filter filter) {
        this.metaFilter.addFilter(filter);
    }
}
