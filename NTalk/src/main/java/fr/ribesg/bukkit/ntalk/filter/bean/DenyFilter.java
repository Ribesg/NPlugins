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

/** @author Ribesg */
public class DenyFilter extends Filter {

	public DenyFilter(final String filteredString, final boolean regex) {
		super(filteredString, regex, ChatFilterResult.DENY);
	}
}
