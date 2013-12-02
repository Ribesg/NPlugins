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
import fr.ribesg.bukkit.ncore.common.FrameBuilder;
import fr.ribesg.bukkit.ntalk.NTalk;
import fr.ribesg.bukkit.ntalk.filter.bean.Filter;
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
	}

	@Override
	protected void handleValues(YamlConfiguration config) throws InvalidConfigurationException {
		// TODO Load filters
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

		// TODO Write filters

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
