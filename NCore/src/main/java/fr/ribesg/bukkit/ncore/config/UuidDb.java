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
import fr.ribesg.com.mojang.api.profiles.HttpProfileRepository;
import fr.ribesg.com.mojang.api.profiles.Profile;
import fr.ribesg.com.mojang.api.profiles.ProfileRepository;
import org.apache.commons.lang.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;

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

	private static final Logger LOGGER = LogManager.getLogger(UuidDb.class);

	private static final ProfileRepository mojangRepo = new HttpProfileRepository("minecraft");
	private static UuidDb instance;

	public static String getName(final UUID id) {
		final PlayerInfo info = instance.byUuid.get(id);
		return info == null ? null : info.lastKnownName;
	}

	public static UUID getId(final String nodeName, final String name) {
		return getId(nodeName, name, false);
	}

	public static UUID getId(final String nodeName, final String name, final boolean askMojangIfUnknown) {
		final PlayerInfo info = instance.byName.get(name.toLowerCase());
		if (info != null) {
			return info.uuid;
		} else {
			UUID id;
			if (!Bukkit.getOnlineMode()) {
				id = PlayerIdsUtil.getOfflineUuid(name);
				register(id, name);
			} else if (!askMojangIfUnknown) {
				id = null;
			} else {
				final Profile profile = getMojangProfile(nodeName, name, 3);
				if (profile == null) {
					id = null;
				} else {
					try {
						id = UUID.fromString(profile.getId());
					} catch (final IllegalArgumentException e) {
						id = PlayerIdsUtil.shortUuidToUuid(profile.getId());
					}
					register(id, profile.getName());
				}
			}
			return id;
		}
	}

	public static List<String> getPreviousNames(final UUID id) {
		final PlayerInfo info = instance.byUuid.get(id);
		if (info == null) {
			return null;
		} else {
			return new LinkedList<>(info.previousNames.values());
		}
	}

	public static long getFirstSeen(final UUID id) {
		return instance.byUuid.containsKey(id) ? instance.byUuid.get(id).firstSeen : -1L;
	}

	public static long getLastSeen(final UUID id) {
		return instance.byUuid.containsKey(id) ? instance.byUuid.get(id).lastSeen : -1L;
	}

	private static Profile getMojangProfile(final String nodeName, final String name, final int tries) {
		Validate.isTrue(tries > 0, "We should at least try once...");
		LOGGER.debug('[' + nodeName + "] [UuidDb] Getting UUID from Mojang for Player name '" + name + "'...");
		for (int i = 0; i < tries; i++) {
			LOGGER.debug('[' + nodeName + "] [UuidDb] Try " + (i + 1) + "...");
			final Profile[] res = mojangRepo.findProfilesByNames(name);
			if (res.length > 0) {
				return res[0];
			}
		}
		LOGGER.warn('[' + nodeName + "] Failed to get Mojang's UUID for Player name '" + name + "'!");
		return null;
	}

	private static void register(final Player player) {
		register(player.getUniqueId(), player.getName());
	}

	private static void register(final UUID id, final String name) {
		final long now = System.currentTimeMillis();
		PlayerInfo info = instance.byUuid.get(id);
		if (info == null) {
			info = new PlayerInfo(id, name, new TreeMap<Long, String>(), now, now);
			instance.byUuid.put(id, info);
			instance.byName.put(name.toLowerCase(), info);
		} else if (!name.equals(info.lastKnownName)) {
			instance.byName.remove(info.lastKnownName.toLowerCase());
			info.previousNames.put(now, info.lastKnownName);
			info.lastKnownName = name;
			info.lastSeen = now;
			instance.byName.put(info.lastKnownName.toLowerCase(), info);
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

	private boolean updated;

	public UuidDb(final NCore instance) {
		super(instance);
		if (UuidDb.instance != null) {
			throw new IllegalStateException();
		} else {
			UuidDb.instance = this;
		}
		this.byUuid = new LinkedHashMap<>();
		this.byName = new LinkedHashMap<>();
		this.updated = false;
		Bukkit.getPluginManager().registerEvents(this, instance);
		Bukkit.getScheduler().runTaskTimerAsynchronously(instance, new BukkitRunnable() {

			@Override
			public void run() {
				if (UuidDb.this.updated) {
					try {
						UuidDb.this.updated = false;
						UuidDb.this.writeConfig();
					} catch (final IOException e) {
						UuidDb.LOGGER.error("[NCore] An error occured when NCore tried to save uuidDb.yml", e);
					}
				}
			}
		}, 5 * 60 * 20L, 30 * 20L);
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
			if (lastKnownName != null) {
				byName.put(lastKnownName.toLowerCase(), info);
			}
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
