/***************************************************************************
 * Project file:    NPlugins - NTalk - BanFilter.java                      *
 * Full Class name: fr.ribesg.bukkit.ntalk.filter.bean.BanFilter           *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntalk.filter.bean;
import fr.ribesg.bukkit.ntalk.filter.ChatFilterResult;

/** @author Ribesg */
public class BanFilter extends TimedFilter {

	public BanFilter(final String filteredString, final boolean regex, final long duration) {
		super(filteredString, regex, ChatFilterResult.TEMPORARY_BAN, duration);
	}
}
