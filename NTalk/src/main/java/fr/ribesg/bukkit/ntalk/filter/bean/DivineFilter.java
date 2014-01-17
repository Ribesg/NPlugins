/***************************************************************************
 * Project file:    NPlugins - NTalk - DivineFilter.java                   *
 * Full Class name: fr.ribesg.bukkit.ntalk.filter.bean.DivineFilter        *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntalk.filter.bean;
import fr.ribesg.bukkit.ntalk.filter.ChatFilterResult;

import java.util.Map;

/** @author Ribesg */
public class DivineFilter extends TimedFilter {

	private final int minHealth;
	private final int entityAmount;

	public DivineFilter(final String outputString, final String filteredString, final boolean regex, final long duration, final int minHealth, final int entityAmount) {
		super(outputString, filteredString, regex, ChatFilterResult.DIVINE_PUNISHMENT, duration);
		this.minHealth = minHealth;
		this.entityAmount = entityAmount;
	}

	public int getMinHealth() {
		return minHealth;
	}

	public int getEntityAmount() {
		return entityAmount;
	}

	// ############ //
	// ## Saving ## //
	// ############ //

	@Override
	public Map<String, Object> getConfigMap() {
		final Map<String, Object> map = super.getConfigMap();
		map.put("minHealth", minHealth);
		map.put("entityAmount", entityAmount);
		return map;
	}

	// ############# //
	// ## Loading ## //
	// ############# //

	public static DivineFilter loadFromConfig(final String key, final Map<String, Object> values) {
		try {
			final String filteredString = (String) values.get("filteredString");
			final boolean regex = (boolean) values.get("isRegex");
			final long duration = (int) values.get("duration");
			final int minHealth = (int) values.get("minHealth");
			final int entityAmount = (int) values.get("entityAmount");
			return new DivineFilter(key, filteredString, regex, duration, minHealth, entityAmount);
		} catch (final NullPointerException e) {
			throw new IllegalArgumentException("Missing value", e);
		}
	}
}
