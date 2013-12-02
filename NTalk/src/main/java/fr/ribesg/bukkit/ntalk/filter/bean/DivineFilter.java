/***************************************************************************
 * Project file:    NPlugins - NTalk - DivineFilter.java                   *
 * Full Class name: fr.ribesg.bukkit.ntalk.filter.bean.DivineFilter        *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntalk.filter.bean;
import fr.ribesg.bukkit.ntalk.filter.ChatFilterResult;

/** @author Ribesg */
public class DivineFilter extends TimedFilter {

	private final int minHealth;
	private final int entityAmount;

	public DivineFilter(final String filteredString,
	                    final boolean regex,
	                    final long duration,
	                    final int minHealth,
	                    final int entityAmount) {
		super(filteredString, regex, ChatFilterResult.DIVINE_PUNISHMENT, duration);
		this.minHealth = minHealth;
		this.entityAmount = entityAmount;
	}

	public int getMinHealth() {
		return minHealth;
	}

	public int getEntityAmount() {
		return entityAmount;
	}
}
