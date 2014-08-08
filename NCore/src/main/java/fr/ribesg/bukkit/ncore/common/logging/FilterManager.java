/***************************************************************************
 * Project file:    NPlugins - NCore - FilterManager.java                  *
 * Full Class name: fr.ribesg.bukkit.ncore.common.logging.FilterManager    *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
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
public class FilterManager {

	/**
	 * Adds a new RegexFilter to log4j2.
	 *
	 * @param regex a regex String
	 */
	public void addRegexFilter(final String regex) {
		this.addFilter(RegexFilter.createFilter(regex, "FALSE", "DENY", "NEUTRAL"));
	}

	/**
	 * Adds a new DenyFilter to log4j2.
	 *
	 * @param denyFilter the filter
	 */
	public void addDenyFilter(final DenyFilter denyFilter) {
		this.addFilter(new Log4jDenyFilter(denyFilter));
	}

	/**
	 * Adds a new log4j Filter to log4j.
	 *
	 * @param log4jFilter a log4j filter
	 */
	private void addFilter(final Filter log4jFilter) {
		final LoggerContext context = (LoggerContext)LogManager.getContext(false);
		final Configuration config = context.getConfiguration();
		for (final LoggerConfig loggerConfig : config.getLoggers().values()) {
			loggerConfig.addFilter(log4jFilter);
		}
	}

	/**
	 * Represents a log4j2 DenyFilter wrapper
	 */
	private class Log4jDenyFilter extends AbstractFilter {

		/**
		 * The actual DenyFilter
		 */
		private final DenyFilter denyFilter;

		/**
		 * Builds a Log4jDenyFilter from a DenyFilter.
		 *
		 * @param denyFilter a DenyFilter
		 */
		private Log4jDenyFilter(final DenyFilter denyFilter) {
			super();
			this.denyFilter = denyFilter;
		}

		@Override
		public Result filter(final LogEvent event) {
			return this.denyFilter.denies(event.getMessage().getFormattedMessage()) ? Result.DENY : Result.NEUTRAL;
		}
	}
}
