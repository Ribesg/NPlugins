/***************************************************************************
 * Project file:    NPlugins - NTalk - DenyFilter.java                     *
 * Full Class name: fr.ribesg.bukkit.ntalk.filter.bean.DenyFilter          *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntalk.filter.bean;
import fr.ribesg.bukkit.ntalk.filter.ChatFilterResult;

import java.util.Map;

/** @author Ribesg */
public class DenyFilter extends Filter {

	public DenyFilter(final String outputString, final String filteredString, final boolean regex) {
		super(outputString, filteredString, regex, ChatFilterResult.DENY);
	}

	// ############# //
	// ## Loading ## //
	// ############# //

	public static DenyFilter loadFromConfig(final String key, final Map<String, Object> values) {
		try {
			final String filteredString = (String) values.get("filteredString");
			final boolean regex = (boolean) values.get("isRegex");
			return new DenyFilter(key, filteredString, regex);
		} catch (final NullPointerException e) {
			throw new IllegalArgumentException("Missing value", e);
		}
	}
}
