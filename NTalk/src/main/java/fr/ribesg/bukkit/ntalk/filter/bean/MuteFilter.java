/***************************************************************************
 * Project file:    NPlugins - NTalk - MuteFilter.java                     *
 * Full Class name: fr.ribesg.bukkit.ntalk.filter.bean.MuteFilter          *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntalk.filter.bean;
import fr.ribesg.bukkit.ntalk.filter.ChatFilterResult;

import java.util.Map;

/** @author Ribesg */
public class MuteFilter extends TimedFilter {

	public MuteFilter(final String outputString, final String filteredString, final boolean regex, final long duration) {
		super(outputString, filteredString, regex, ChatFilterResult.TEMPORARY_MUTE, duration);
	}

	// ############# //
	// ## Loading ## //
	// ############# //

	public static MuteFilter loadFromConfig(final String key, final Map<String, Object> values) {
		try {
			final String filteredString = (String) values.get("filteredString");
			final boolean regex = (boolean) values.get("isRegex");
			final long duration = (int) values.get("duration");
			return new MuteFilter(key, filteredString, regex, duration);
		} catch (final NullPointerException e) {
			throw new IllegalArgumentException("Missing value", e);
		}
	}
}
