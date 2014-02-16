/***************************************************************************
 * Project file:    NPlugins - NPlayer - PunishmentDb.java                 *
 * Full Class name: fr.ribesg.bukkit.nplayer.punishment.PunishmentDb       *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nplayer.punishment;
import fr.ribesg.bukkit.nplayer.NPlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class PunishmentDb {

	private final NPlayer plugin;

	private final Map<String, Set<Punishment>> permPunishments;
	private final Map<String, Set<Punishment>> tempPunishments;
	private final SortedMap<Long, Punishment>  tempPunishmentEndDateMap;

	private final Map<String, String> leaveMessages;

	public PunishmentDb(final NPlayer instance) {
		this.plugin = instance;
		this.permPunishments = new HashMap<>();
		this.tempPunishments = new HashMap<>();
		this.tempPunishmentEndDateMap = new TreeMap<>();
		this.leaveMessages = new HashMap<>();
	}

	// ##################### //
	// ## Config handling ## //
	// ##################### //

	public void saveConfig() throws IOException {
		saveConfig(Paths.get(plugin.getDataFolder().getPath(), "punishmentDB.yml"));
	}

	private void saveConfig(final Path filePath) throws IOException {
		if (!Files.exists(filePath.getParent())) {
			Files.createDirectories(filePath.getParent());
		}
		if (!Files.exists(filePath)) {
			Files.createFile(filePath);
		}
		final YamlConfiguration config = new YamlConfiguration();

		final List<Punishment> punishments = getAllPunishmentsFromMaps(getPermPunishments(), getTempPunishments());

		for (final Punishment p : punishments) {
			final String key = p.getPunished().replaceAll("\\.", "-") + '-' + p.getType().toString(); // Should be unique
			final ConfigurationSection pSection = config.createSection(key);
			pSection.set("punished", p.getPunished());
			pSection.set("type", p.getType().toString());
			pSection.set("endDate", p.getEndDate());
			pSection.set("reason", p.getReason());
			if (p.getType() == PunishmentType.JAIL) {
				pSection.set("jailPointName", ((Jail) p).getJailPointName());
			}
		}

		config.save(filePath.toFile());
	}

	public void loadConfig() throws IOException, InvalidConfigurationException {
		loadConfig(Paths.get(plugin.getDataFolder().getPath(), "punishmentDB.yml"));
	}

	private void loadConfig(final Path filePath) throws IOException, InvalidConfigurationException {
		if (!Files.exists(filePath)) {
			return; // Nothing to load
		}
		final YamlConfiguration config = new YamlConfiguration();
		config.load(filePath.toFile());

		for (final String key : config.getKeys(false)) {
			if (config.isConfigurationSection(key)) {
				final ConfigurationSection pSection = config.getConfigurationSection(key);
				Punishment p = null;
				final String punished = pSection.getString("punished");
				final PunishmentType type = PunishmentType.valueOf(pSection.getString("type"));
				final long endDate = Long.parseLong(pSection.getString("endDate"));
				final String reason = pSection.getString("reason");
				switch (type) {
					case BAN:
						p = new Ban(punished, reason, endDate);
						break;
					case IPBAN:
						p = new IpBan(punished.toLowerCase(), reason, endDate);
						break;
					case MUTE:
						p = new Mute(punished, reason, endDate);
						break;
					case JAIL:
						final String jailPointName = pSection.getString("jailPointName");
						p = new Jail(punished, reason, jailPointName, endDate);
						break;
				}
				add(p);
			}
		}

	}

	// ############# //
	// ## Getters ## //
	// ############# //

	@SafeVarargs
	private static List<Punishment> getAllPunishmentsFromMaps(final Map<String, Set<Punishment>>... maps) {
		final List<Punishment> result = new ArrayList<>();
		for (final Map<String, Set<Punishment>> map : maps) {
			for (final Set<Punishment> set : map.values()) {
				result.addAll(set);
			}
		}
		return result;
	}

	public List<Punishment> getAllPunishments() {
		return getAllPunishmentsFromMaps(permPunishments, tempPunishments);
	}

	public Map<String, Set<Punishment>> getPermPunishments() {
		return permPunishments;
	}

	public Map<String, Set<Punishment>> getTempPunishments() {
		return tempPunishments;
	}

	public SortedMap<Long, Punishment> getTempPunishmentEndDateMap() {
		return tempPunishmentEndDateMap;
	}

	public Map<String, String> getLeaveMessages() {
		return leaveMessages;
	}

	// ################## //
	// ## Nickname Ban ## //
	// ################## //

	public boolean tempBanNick(final String playerName, final long duration, final String reason) {
		plugin.entering(getClass(), "tempBanNick");

		final long newBanEndDate = System.currentTimeMillis() + duration * 1000;
		final Ban ban = (Ban) get(playerName, PunishmentType.BAN);
		if (ban != null) {
			if (ban.isPermanent() || ban.getEndDate() > newBanEndDate) {
				plugin.exiting(getClass(), "unBanNick", "Failed: already banned with longer duration");
				return false;
			} else {
				remove(ban);
			}
		}
		add(new Ban(playerName, reason, newBanEndDate));

		plugin.exiting(getClass(), "tempBanNick");
		return true;
	}

	public boolean permBanNick(final String playerName, final String reason) {
		plugin.entering(getClass(), "permBanNick");

		if (get(playerName, PunishmentType.BAN) != null) {
			plugin.exiting(getClass(), "unBanNick", "Failed: already banned");
			return false;
		}
		add(new Ban(playerName, reason));

		plugin.exiting(getClass(), "permBanNick");
		return true;
	}

	public boolean unBanNick(final String playerName) {
		plugin.entering(getClass(), "unBanNick");

		final Punishment p = get(playerName, PunishmentType.BAN);
		if (p == null) {
			plugin.exiting(getClass(), "unBanNick", "Failed: not banned");
			return false;
		}
		remove(p);

		plugin.exiting(getClass(), "unBanNick");
		return true;
	}

	public boolean isNickBanned(final String playerName) {
		return get(playerName, PunishmentType.BAN) != null;
	}

	// ############ //
	// ## IP Ban ## //
	// ############ //

	public boolean tempBanIp(final String ip, final long duration, final String reason) {
		plugin.entering(getClass(), "tempBanIp");

		final long newIpBanEndDate = System.currentTimeMillis() + duration * 1000;
		final IpBan ipBan = (IpBan) get(ip, PunishmentType.IPBAN);
		if (ipBan != null) {
			if (ipBan.isPermanent() || ipBan.getEndDate() > newIpBanEndDate) {
				plugin.exiting(getClass(), "unBanNick", "Failed: already banned with longer duration");
				return false;
			} else {
				remove(ipBan);
			}
		}
		add(new IpBan(ip.toLowerCase(), reason, newIpBanEndDate));

		plugin.exiting(getClass(), "tempBanIp");
		return true;
	}

	public boolean permBanIp(final String ip, final String reason) {
		plugin.entering(getClass(), "permBanIp");

		if (get(ip, PunishmentType.IPBAN) != null) {
			plugin.exiting(getClass(), "permBanIp", "Failed: already banned");
			return false;
		}
		add(new IpBan(ip.toLowerCase(), reason));

		plugin.exiting(getClass(), "permBanIp");
		return true;
	}

	public boolean unBanIp(final String ip) {
		plugin.entering(getClass(), "unBanIp");

		final Punishment p = get(ip, PunishmentType.IPBAN);
		if (p == null) {
			plugin.exiting(getClass(), "unBanIp", "Failed: not banned");
			return false;
		}
		remove(p);

		plugin.exiting(getClass(), "unBanIp");
		return true;
	}

	public boolean isIpBanned(final String ip) {
		return get(ip, PunishmentType.IPBAN) != null;
	}

	// ################### //
	// ## Nickname Mute ## //
	// ################### //

	public boolean tempMuteNick(final String playerName, final long duration, final String reason) {
		plugin.entering(getClass(), "tempMuteNick");

		final long newMuteEndDate = System.currentTimeMillis() + duration * 1000;
		final Mute mute = (Mute) get(playerName, PunishmentType.MUTE);
		if (mute != null) {
			if (mute.isPermanent() || mute.getEndDate() > newMuteEndDate) {
				plugin.exiting(getClass(), "tempMuteNick", "Failed: already muted with longer duration");
				return false;
			} else {
				remove(mute);
			}
		}
		add(new Mute(playerName, reason, newMuteEndDate));

		plugin.exiting(getClass(), "tempMuteNick");
		return true;
	}

	public boolean permMuteNick(final String playerName, final String reason) {
		plugin.entering(getClass(), "permMuteNick");

		if (get(playerName, PunishmentType.MUTE) != null) {
			plugin.exiting(getClass(), "permMuteNick", "Failed: already muted");
			return false;
		}
		add(new Mute(playerName, reason));

		plugin.exiting(getClass(), "permMuteNick");
		return true;
	}

	public boolean unMuteNick(final String playerName) {
		plugin.entering(getClass(), "unMuteNick");

		final Punishment p = get(playerName, PunishmentType.MUTE);
		if (p == null) {
			plugin.exiting(getClass(), "unMuteNick", "Failed: not muted");
			return false;
		}
		remove(p);

		plugin.exiting(getClass(), "unMuteNick");
		return true;
	}

	public boolean isNickMuted(final String playerName) {
		return get(playerName, PunishmentType.MUTE) != null;
	}

	// ########## //
	// ## Jail ## //
	// ########## //

	public boolean tempJailNick(final String playerName, final long duration, final String reason, final String jailPointName) {
		plugin.entering(getClass(), "tempJailNick");

		final long newJailEndDate = System.currentTimeMillis() + duration * 1000;
		final Jail jail = (Jail) get(playerName, PunishmentType.JAIL);
		if (jail != null) {
			if (jail.isPermanent() || jail.getEndDate() > newJailEndDate) {
				plugin.exiting(getClass(), "tempJailNick", "Failed: already jailed with longer duration");
				return false;
			} else {
				if (!plugin.getCuboidNode().unJail(playerName)) {
					plugin.error("Failed to unjail already-jailed player in NCuboid!");
				}
				remove(jail);
			}
		}
		add(new Jail(playerName, reason, jailPointName, newJailEndDate));

		if (!plugin.getCuboidNode().jail(playerName, jailPointName)) {
			plugin.error("Failed to jail player in NCuboid!");
		}

		plugin.exiting(getClass(), "tempJailNick");
		return true;
	}

	public boolean permJailNick(final String playerName, final String reason, final String jailPointName) {
		plugin.entering(getClass(), "permJailNick");

		if (get(playerName, PunishmentType.JAIL) != null) {
			plugin.exiting(getClass(), "permJailNick", "Failed: already jailed");
			return false;
		}
		add(new Jail(playerName, reason, jailPointName));

		if (!plugin.getCuboidNode().jail(playerName, jailPointName)) {
			plugin.error("Failed to jail player in NCuboid!");
		}

		plugin.exiting(getClass(), "permJailNick");
		return true;
	}

	public boolean unJailNick(final String playerName) {
		plugin.entering(getClass(), "unJailNick");

		final Punishment p = get(playerName, PunishmentType.JAIL);
		if (p == null) {
			plugin.exiting(getClass(), "unJailNick", "Failed: not jailed");
			return false;
		}
		remove(p);

		if (!plugin.getCuboidNode().unJail(playerName)) {
			plugin.error("Failed to unjail player in NCuboid!");
		}

		plugin.exiting(getClass(), "unJailNick");
		return true;
	}

	// ######################## //
	// ## Convenient methods ## //
	// ######################## //

	public Punishment get(final String key, final PunishmentType type) {
		plugin.entering(getClass(), "get", "key=" + key + ";type=" + type);
		Punishment result = null;

		final Set<Punishment> temporary = getTempPunishments().get(key.toLowerCase());
		if (temporary != null) {
			final Iterator<Punishment> it = temporary.iterator();
			while (it.hasNext()) {
				final Punishment p = it.next();
				if (!p.isStillActive()) {
					if (plugin.getCuboidNode() != null && p.getType() == PunishmentType.JAIL) {
						plugin.getCuboidNode().unJail(p.getPunished());
					}
					it.remove();
				} else if (p.getType() == type) {
					result = p;
					break;
				}
			}
			if (temporary.isEmpty()) {
				getTempPunishments().remove(key.toLowerCase());
			}
		}
		if (result == null) {
			final Set<Punishment> permanent = getPermPunishments().get(key.toLowerCase());
			if (permanent != null) {
				for (final Punishment p : permanent) {
					if (p.getType() == type) {
						result = p;
						break;
					}
				}
			}
		}

		plugin.exiting(getClass(), "get", "result=" + result);
		return result;
	}

	public void add(final Punishment punishment) {
		if (punishment.isPermanent()) {
			Set<Punishment> playerPunishments = getPermPunishments().get(punishment.getPunished().toLowerCase());
			if (playerPunishments == null) {
				playerPunishments = new HashSet<>();
			}
			playerPunishments.add(punishment);
			getPermPunishments().put(punishment.getPunished().toLowerCase(), playerPunishments);
		} else {
			Set<Punishment> playerPunishments = getTempPunishments().get(punishment.getPunished().toLowerCase());
			if (playerPunishments == null) {
				playerPunishments = new HashSet<>();
			}
			playerPunishments.add(punishment);
			getTempPunishments().put(punishment.getPunished().toLowerCase(), playerPunishments);
			getTempPunishmentEndDateMap().put(punishment.getEndDate(), punishment);
		}
	}

	public Punishment remove(final Punishment toBeRemoved) {
		plugin.entering(getClass(), "remove", "toBeRemoved=" + toBeRemoved);
		Punishment result = null;

		final String key = toBeRemoved.getPunished();
		if (toBeRemoved.isPermanent()) {
			final Set<Punishment> permanent = getPermPunishments().get(key.toLowerCase());
			if (permanent != null) {
				if (permanent.remove(toBeRemoved)) {
					if (permanent.isEmpty()) {
						getPermPunishments().remove(key.toLowerCase());
					}
					if (plugin.getCuboidNode() != null && toBeRemoved.getType() == PunishmentType.JAIL) {
						plugin.getCuboidNode().unJail(toBeRemoved.getPunished());
					}
					result = toBeRemoved;
				}
			}
		} else {
			final Set<Punishment> temporary = getTempPunishments().get(key.toLowerCase());
			if (temporary != null) {
				if (temporary.remove(toBeRemoved)) {
					if (temporary.isEmpty()) {
						getTempPunishments().remove(key.toLowerCase());
					}
					if (plugin.getCuboidNode() != null && toBeRemoved.getType() == PunishmentType.JAIL) {
						plugin.getCuboidNode().unJail(toBeRemoved.getPunished());
					}
					result = toBeRemoved;
				}
			}
			final Iterator<Map.Entry<Long, Punishment>> it = getTempPunishmentEndDateMap().entrySet().iterator();
			while (it.hasNext()) {
				if (it.next().getKey() == toBeRemoved.getEndDate()) {
					it.remove();
					break;
				}
			}
		}

		plugin.exiting(getClass(), "remove", "result=" + result);
		return result;
	}
}
