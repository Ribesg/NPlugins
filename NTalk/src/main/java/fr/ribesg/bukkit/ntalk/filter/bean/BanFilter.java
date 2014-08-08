/***************************************************************************
 * Project file:    NPlugins - NTalk - BanFilter.java                      *
 * Full Class name: fr.ribesg.bukkit.ntalk.filter.bean.BanFilter           *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntalk.filter.bean;

import fr.ribesg.bukkit.ntalk.filter.ChatFilterResult;

import java.util.Map;

/**
 * @author Ribesg
 */
public class BanFilter extends TimedFilter {

	public BanFilter(final String outputString, final String filteredString, final boolean regex, final long duration) {
		super(outputString, filteredString, regex, ChatFilterResult.TEMPORARY_BAN, duration);
	}

	// ############# //
	// ## Loading ## //
	// ############# //

	public static BanFilter loadFromConfig(final String key, final Map<String, Object> values) {
		try {
			final String filteredString = (String)values.get("filteredString");
			final boolean regex = (boolean)values.get("isRegex");
			final long duration = (int)values.get("duration");
			return new BanFilter(key, filteredString, regex, duration);
		} catch (final NullPointerException e) {
			throw new IllegalArgumentException("Missing value", e);
		}
	}
}
