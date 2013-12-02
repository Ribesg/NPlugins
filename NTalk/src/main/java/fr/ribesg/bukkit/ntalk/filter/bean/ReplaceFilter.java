/***************************************************************************
 * Project file:    NPlugins - NTalk - ReplaceFilter.java                  *
 * Full Class name: fr.ribesg.bukkit.ntalk.filter.bean.ReplaceFilter       *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntalk.filter.bean;
import fr.ribesg.bukkit.ntalk.filter.ChatFilterResult;

/** @author Ribesg */
public class ReplaceFilter extends Filter {

	private final String replacement;

	public ReplaceFilter(final String filteredString, final boolean regex, final String replacement) {
		super(filteredString, regex, ChatFilterResult.REPLACE);
		this.replacement = replacement;
	}

	public String getReplacement() {
		return replacement;
	}
}
