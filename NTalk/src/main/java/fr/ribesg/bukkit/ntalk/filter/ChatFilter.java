/***************************************************************************
 * Project file:    NPlugins - NTalk - ChatFilter.java                     *
 * Full Class name: fr.ribesg.bukkit.ntalk.filter.ChatFilter               *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntalk.filter;
import fr.ribesg.bukkit.ncore.AbstractConfig;
import fr.ribesg.bukkit.ncore.utils.FrameBuilder;
import fr.ribesg.bukkit.ntalk.NTalk;
import fr.ribesg.bukkit.ntalk.filter.bean.BanFilter;
import fr.ribesg.bukkit.ntalk.filter.bean.DenyFilter;
import fr.ribesg.bukkit.ntalk.filter.bean.Filter;
import fr.ribesg.bukkit.ntalk.filter.bean.JailFilter;
import fr.ribesg.bukkit.ntalk.filter.bean.MuteFilter;
import fr.ribesg.bukkit.ntalk.filter.bean.ReplaceFilter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** @author Ribesg */
public class ChatFilter extends AbstractConfig<NTalk> {

	private final Map<String, Filter> filters;

	public ChatFilter(final NTalk plugin) {
		super(plugin);
		this.filters = new ConcurrentHashMap<>();

		final BanFilter banFilter = new BanFilter("ohai", false, 3600);
		this.filters.put(banFilter.getFilteredString(), banFilter);

		final DenyFilter denyFilter = new DenyFilter("nope", false);
		this.filters.put(denyFilter.getFilteredString(), denyFilter);

		/* TODO Not Implemented Yet
		final DivineFilter divineFilter = new DivineFilter("hitme", false, 120, 1, 7);
		this.filters.put(divineFilter.getFilteredString(), divineFilter);
		*/

		final JailFilter jailFilter = new JailFilter("you can't jail me", false, 3600, "theBadWordJail");
		this.filters.put(jailFilter.getFilteredString(), jailFilter);

		final MuteFilter muteFilter = new MuteFilter("l+[\\s]*o+[\\s]*l+", true, 3600);
		this.filters.put(muteFilter.getFilteredString(), muteFilter);

		final ReplaceFilter replacerFilter = new ReplaceFilter("admins sucks", false, "admins rocks");
		this.filters.put(replacerFilter.getFilteredString(), replacerFilter);
	}

	@Override
	protected void handleValues(final YamlConfiguration config) throws InvalidConfigurationException {
		filters.clear();

		if (!config.isConfigurationSection("filters")) {
			throw new InvalidConfigurationException("Unable to find 'filters' configuration section.");
		} else {
			final ConfigurationSection filtersSection = config.getConfigurationSection("filters");
			for (final String key : filtersSection.getKeys(false)) {
				if (!filtersSection.isConfigurationSection(key)) {
					throw new InvalidConfigurationException("Config for key '" + key + "' is invalid");
				} else {
					final Filter filter = Filter.loadFromConfig(key, filtersSection.getConfigurationSection(key));
					filters.put(filter.getFilteredString(), filter);
				}
			}
		}
	}

	@Override
	protected String getConfigString() {
		final StringBuilder content = new StringBuilder();
		FrameBuilder frame;

		// Header
		frame = new FrameBuilder();
		frame.addLine("Chat Filters list for NTalk plugin", FrameBuilder.Option.CENTER);
		frame.addLine("If you don't understand something, please ask on dev.bukkit.org");
		frame.addLine("Ribesg", FrameBuilder.Option.RIGHT);
		for (final String line : frame.build()) {
			content.append(line + '\n');
		}

		content.append("# You can define your filters here.\n");
		content.append("#\n");
		content.append("# For all Filter types, you have to specify its type and if it's a regex.\n");
		content.append("# Each type may require additional informations.\n");
		content.append("#\n");
		content.append("# Here are all the available Filter types and examples of use:\n");
		content.append("# - TempBan the player\n");
		content.append("#   \"ohai\":\n");
		content.append("#     type: \"TEMPORARY_BAN\"\n");
		content.append("#     isRegex: false\n");
		content.append("#     duration: 3600\n");
		content.append("#\n");
		content.append("# - Deny the message\n");
		content.append("#   \"nope\":\n");
		content.append("#     type: \"DENY\"\n");
		content.append("#     isRegex: false\n");
		content.append("#\n");
		/* TODO Not Implemented Yet
		content.append("# - The Divine Punishment\n");
		content.append("#   \"hitme\":\n");
		content.append("#     type: \"DIVINE_PUNISHMENT\"\n");
		content.append("#     isRegex: false\n");
		content.append("#     duration: 120\n");
		content.append("#     minHealth: 1\n");
		content.append("#     entityAmount: 7\n");
		content.append("#\n");
		*/
		content.append("# - TempJail the player\n");
		content.append("#   \"you can't jail me\":\n");
		content.append("#     type: \"TEMPORARY_JAIL\"\n");
		content.append("#     isRegex: false\n");
		content.append("#     duration: 3600\n");
		content.append("#     jailName: theBadWordJail\n");
		content.append("#\n");
		content.append("# - TempMute the player (This example matches all kind of 'lol', 'l ooo l', etc)\n");
		content.append("#   \"l+[\\s]*o+[\\s]*l+\":\n");
		content.append("#     type: \"TEMPORARY_MUTE\"\n");
		content.append("#     isRegex: true\n");
		content.append("#     duration: 3600\n");
		content.append("#\n");
		content.append("# - Replace the occurence in the message\n");
		content.append("#   \"admins sucks\":\n");
		content.append("#     type: \"REPLACE\"\n");
		content.append("#     isRegex: false\n");
		content.append("#     replacement: \"admins rocks\"\n");
		content.append("#\n");
		content.append("# Be sure to write everything correctly, or the plugin will fail\n");
		content.append("# to load. Remember, use only spaces, no tabs!\n");
		content.append("filters:\n");

		for (final Filter filter : filters.values()) {
			content.append("  \"" + filter.getFilteredString() + "\":\n");
			for (final Map.Entry<String, Object> e : filter.getConfigMap().entrySet()) {
				content.append("    " + e.getKey() + ": \"" + e.getValue().toString() + "\"\n");
			}
		}

		return content.toString();
	}

	public Filter check(final String message) {
		final String[] split = message.split(" ");
		for (final String word : split) {
			for (final Map.Entry<String, Filter> e : filters.entrySet()) {
				if (e.getValue().isRegex()) {
					if (word.matches(e.getKey())) {
						return e.getValue();
					}
				} else {
					if (e.getKey().equals(word)) {
						return e.getValue();
					}
				}
			}
		}
		return null;
	}
}
