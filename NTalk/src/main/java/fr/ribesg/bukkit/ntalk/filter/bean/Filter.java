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
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

import java.util.LinkedHashMap;
import java.util.Map;

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

	// ############ //
	// ## Saving ## //
	// ############ //

	/** May be overriden to add stuff */
	public Map<String, Object> getConfigMap() {
		final Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", responseType.name());
		map.put("isRegex", regex);
		return map;
	}

	// ############# //
	// ## Loading ## //
	// ############# //

	public static Filter loadFromConfig(final String key, final ConfigurationSection keySection) throws InvalidConfigurationException {
		try {
			final Map<String, Object> map = keySection.getValues(false);
			switch (ChatFilterResult.valueOf((String) map.get("type"))) {
				case TEMPORARY_BAN:
					return BanFilter.loadFromConfig(key, map);
				case DENY:
					return DenyFilter.loadFromConfig(key, map);
				case DIVINE_PUNISHMENT:
					return DivineFilter.loadFromConfig(key, map);
				case TEMPORARY_JAIL:
					return JailFilter.loadFromConfig(key, map);
				case TEMPORARY_MUTE:
					return MuteFilter.loadFromConfig(key, map);
				case REPLACE:
					return ReplaceFilter.loadFromConfig(key, map);
				default:
					return null;
			}
		} catch (final IllegalArgumentException e) {
			throw new InvalidConfigurationException("Missing value in configuration of filter for '" + key + "'", e);
		}
	}
}
