/***************************************************************************
 * Project file:    NPlugins - NTalk - TimedFilter.java                    *
 * Full Class name: fr.ribesg.bukkit.ntalk.filter.bean.TimedFilter         *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntalk.filter.bean;
import fr.ribesg.bukkit.ntalk.filter.ChatFilterResult;

/** @author Ribesg */
public abstract class TimedFilter extends Filter {

	private final long duration;

	protected TimedFilter(final String filteredString, final boolean regex, final ChatFilterResult responseType, final long duration) {
		super(filteredString, regex, responseType);
		this.duration = duration;
	}

	public long getDuration() {
		return duration;
	}
}
