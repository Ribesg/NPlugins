/***************************************************************************
 * Project file:    NPlugins - NTalk - Filter.java                         *
 * Full Class name: fr.ribesg.bukkit.ntalk.filter.bean.Filter              *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntalk.filter.bean;
import fr.ribesg.bukkit.ntalk.filter.ChatFilterResult;

/** @author Ribesg */
public abstract class Filter {

	private final String           filteredString;
	private final boolean          regex;
	private final ChatFilterResult responseType;

	protected Filter(final String filteredString, final boolean regex, final ChatFilterResult responseType) {
		this.filteredString = filteredString;
		this.regex = regex;
		this.responseType = responseType;
	}

	public String getFilteredString() {
		return filteredString;
	}

	public boolean isRegex() {
		return regex;
	}

	public ChatFilterResult getResponseType() {
		return responseType;
	}
}
