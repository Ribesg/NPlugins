/***************************************************************************
 * Project file:    NPlugins - NCore - UuidDb.java                         *
 * Full Class name: fr.ribesg.bukkit.ncore.config.UuidDb                   *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.config;
import fr.ribesg.bukkit.ncore.NCore;
import fr.ribesg.bukkit.ncore.util.DateUtil;
import fr.ribesg.bukkit.ncore.util.FrameBuilder;
import fr.ribesg.bukkit.ncore.util.PlayerIdsUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

/** @author Ribesg */
public class UuidDb extends AbstractConfig<NCore> implements Listener {

	private static UuidDb instance;

	public static String getName(final UUID id) {
		final PlayerInfo info = instance.byUuid.get(id);
		return info == null ? null : info.lastKnownName;
	}

	public static UUID getId(final String name) {
		final PlayerInfo info = instance.byName.get(name.toLowerCase());
		return info == null ? null : info.uuid;
	}

	public static List<String> getPreviousNames(final UUID id) {
		final PlayerInfo info = instance.byUuid.get(id);
		if (info == null) {
			return null;
		} else {
			return new LinkedList<>(info.previousNames.values());
		}
	}

	private static void register(final Player player) {
		final long now = System.currentTimeMillis();
		final UUID id = player.getUniqueId();
		final String name = player.getName();
		PlayerInfo info = instance.byUuid.get(id);
		if (info == null) {
			info = new PlayerInfo(id, name, new TreeMap<Long, String>(), now, now);
			instance.byUuid.put(id, info);
			instance.byName.put(name.toLowerCase(), info);
		} else if (!name.equals(info.lastKnownName)) {
			info.previousNames.put(now, info.lastKnownName);
			info.lastKnownName = name;
			info.lastSeen = now;
		}
	}

	private static class PlayerInfo {

		private final UUID                    uuid;
		private       String                  lastKnownName;
		private final SortedMap<Long, String> previousNames;
		private final long                    firstSeen;
		private       long                    lastSeen;

		private PlayerInfo(final UUID uuid, final String lastKnownName, final SortedMap<Long, String> previousNames, final long firstSeen, final long lastSeen) {
			this.uuid = uuid;
			this.lastKnownName = lastKnownName;
			this.previousNames = previousNames;
			this.firstSeen = firstSeen;
			this.lastSeen = lastSeen;
		}
	}

	private final Map<UUID, PlayerInfo>   byUuid;
	private final Map<String, PlayerInfo> byName;

	public UuidDb(final NCore instance) {
		super(instance);
		if (UuidDb.instance != null) {
			throw new IllegalStateException();
		} else {
			UuidDb.instance = this;
		}
		this.byUuid = new LinkedHashMap<>();
		this.byName = new LinkedHashMap<>();
		Bukkit.getPluginManager().registerEvents(this, instance);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	private void onPlayerLogin(final PlayerLoginEvent event) {
		register(event.getPlayer());
	}

	@Override
	public void loadConfig() throws IOException, InvalidConfigurationException {
		loadConfig("uuidDb.yml");
	}

	@Override
	public void writeConfig() throws IOException {
		writeConfig("uuidDb.yml");
	}

	@Override
	protected void handleValues(final YamlConfiguration config) throws InvalidConfigurationException {
		for (final String uuidString : config.getKeys(false)) {
			final UUID id = UUID.fromString(uuidString);
			final ConfigurationSection section = config.getConfigurationSection(uuidString);
			final String lastKnownName = section.getString("lastKnownName");
			final long firstSeen = section.getLong("firstSeen");
			final long lastSeen = section.getLong("lastSeen");
			final ConfigurationSection previousNamesSection = section.getConfigurationSection("previousNames");
			final SortedMap<Long, String> previousNames = new TreeMap<>();
			if (previousNamesSection != null) {
				for (final String previousName : previousNamesSection.getKeys(false)) {
					previousNames.put(previousNamesSection.getLong(previousName), previousName);
				}
			}
			final PlayerInfo info = new PlayerInfo(id, lastKnownName, previousNames, firstSeen, lastSeen);
			byUuid.put(id, info);
			byName.put(lastKnownName.toLowerCase(), info);
		}
	}

	@Override
	protected String getConfigString() {
		final StringBuilder content = new StringBuilder();
		final FrameBuilder frame;

		// Header
		frame = new FrameBuilder();
		frame.addLine("Database of UUID <-> Player names", FrameBuilder.Option.CENTER);
		frame.addLine("If you don't understand something, please ask on dev.bukkit.org");
		frame.addLine("Ribesg", FrameBuilder.Option.RIGHT);
		for (final String line : frame.build()) {
			content.append(line + '\n');
		}
		content.append('\n');

		// Actual content
		content.append("# Please don't modify this file, you could break everything.\n\n");
		for (final PlayerInfo info : byUuid.values()) {
			content.append("# Player '" + info.lastKnownName + "'\n");
			content.append("# Offline-mode UUID: " + PlayerIdsUtil.getOfflineUuid(info.lastKnownName) + '\n');
			content.append(info.uuid.toString() + ":\n");
			content.append("  lastKnownName: " + info.lastKnownName + '\n');
			content.append("  firstSeen: " + info.firstSeen + " # " + DateUtil.formatDate(info.firstSeen) + '\n');
			content.append("  lastSeen: " + info.lastSeen + " # " + DateUtil.formatDate(info.lastSeen) + '\n');
			if (info.previousNames.size() > 0) {
				content.append("  previousNames:\n");
				for (final Map.Entry<Long, String> e : info.previousNames.entrySet()) {
					content.append("    " + e.getValue() + ": " + e.getKey() + " # Changed on " + DateUtil.formatDate(e.getKey()) + '\n');
				}
			}
			content.append('\n');
		}

		return content.toString();
	}
}
