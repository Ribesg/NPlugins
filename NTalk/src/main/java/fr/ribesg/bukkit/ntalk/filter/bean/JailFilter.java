/***************************************************************************
 * Project file:    NPlugins - NTalk - JailFilter.java                     *
 * Full Class name: fr.ribesg.bukkit.ntalk.filter.bean.JailFilter          *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntalk.filter.bean;
import fr.ribesg.bukkit.ntalk.filter.ChatFilterResult;

/** @author Ribesg */
public class JailFilter extends TimedFilter {

	private final String jailName;

	public JailFilter(final String filteredString, final boolean regex, final long duration, final String jailName) {
		super(filteredString, regex, ChatFilterResult.TEMPORARY_JAIL, duration);
		this.jailName = jailName;
	}

	public String getJailName() {
		return jailName;
	}
}
