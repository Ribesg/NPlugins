/***************************************************************************
 * Project file:    NPlugins - NPlayer - PunishmentDb.java                 *
 * Full Class name: fr.ribesg.bukkit.nplayer.punishment.PunishmentDb       *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nplayer.punishment;
import fr.ribesg.bukkit.ncore.config.UuidDb;
import fr.ribesg.bukkit.ncore.util.PlayerIdsUtil;
import fr.ribesg.bukkit.nplayer.NPlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class PunishmentDb {

	private final NPlayer plugin;

	private final Map<String, Set<Punishment>> permPunishments;
	private final Map<String, Set<Punishment>> tempPunishments;
	private final SortedMap<Long, Punishment>  tempPunishmentEndDateMap;

	private final Map<UUID, String> leaveMessages;

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
			final String key = p.getPunished().replaceAll("\\.", "-") + '-' + p.getType().toString(); // Should be unique // TODO What if I want to ban 24 hours and mute 7 days?
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
				final String punishedString = pSection.getString("punished");
				final String punished;
				if (PlayerIdsUtil.isValidUuid(punishedString)) {
					punished = punishedString;
				} else if (PlayerIdsUtil.isValidMinecraftUserName(punishedString)) {
					punished = UuidDb.getId(punishedString, true).toString();
				} else {
					throw new InvalidConfigurationException("Unknown punished '" + punishedString + "' found in punishmentDB.yml");
				}
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

	public Map<UUID, String> getLeaveMessages() {
		return leaveMessages;
	}

	// ############## //
	// ## UUID Ban ## //
	// ############## //

	public boolean tempBanId(final UUID id, final long duration, final String reason) {
		plugin.entering(getClass(), "tempBanId");

		final long newBanEndDate = System.currentTimeMillis() + duration * 1000;
		final Ban ban = (Ban) get(id.toString(), PunishmentType.BAN);
		if (ban != null) {
			if (ban.isPermanent() || ban.getEndDate() > newBanEndDate) {
				plugin.exiting(getClass(), "unBanId", "Failed: already banned with longer duration");
				return false;
			} else {
				remove(ban);
			}
		}
		add(new Ban(id.toString(), reason, newBanEndDate));

		plugin.exiting(getClass(), "tempBanId");
		return true;
	}

	public boolean permBanId(final UUID id, final String reason) {
		plugin.entering(getClass(), "permBanId");

		if (get(id.toString(), PunishmentType.BAN) != null) {
			plugin.exiting(getClass(), "unBanId", "Failed: already banned");
			return false;
		}
		add(new Ban(id.toString(), reason));

		plugin.exiting(getClass(), "permBanId");
		return true;
	}

	public boolean unBanId(final UUID id) {
		plugin.entering(getClass(), "unBanId");

		final Punishment p = get(id.toString(), PunishmentType.BAN);
		if (p == null) {
			plugin.exiting(getClass(), "unBanId", "Failed: not banned");
			return false;
		}
		remove(p);

		plugin.exiting(getClass(), "unBanId");
		return true;
	}

	public boolean isIdBanned(final UUID id) {
		return get(id.toString(), PunishmentType.BAN) != null;
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
				plugin.exiting(getClass(), "tempBanIp", "Failed: already banned with longer duration");
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

	// ############### //
	// ## UUID Mute ## //
	// ############### //

	public boolean tempMuteId(final UUID id, final long duration, final String reason) {
		plugin.entering(getClass(), "tempMuteId");

		final long newMuteEndDate = System.currentTimeMillis() + duration * 1000;
		final Mute mute = (Mute) get(id.toString(), PunishmentType.MUTE);
		if (mute != null) {
			if (mute.isPermanent() || mute.getEndDate() > newMuteEndDate) {
				plugin.exiting(getClass(), "tempMuteId", "Failed: already muted with longer duration");
				return false;
			} else {
				remove(mute);
			}
		}
		add(new Mute(id.toString(), reason, newMuteEndDate));

		plugin.exiting(getClass(), "tempMuteId");
		return true;
	}

	public boolean permMuteId(final UUID id, final String reason) {
		plugin.entering(getClass(), "permMuteId");

		if (get(id.toString(), PunishmentType.MUTE) != null) {
			plugin.exiting(getClass(), "permMuteId", "Failed: already muted");
			return false;
		}
		add(new Mute(id.toString(), reason));

		plugin.exiting(getClass(), "permMuteId");
		return true;
	}

	public boolean unMuteId(final UUID id) {
		plugin.entering(getClass(), "unMuteId");

		final Punishment p = get(id.toString(), PunishmentType.MUTE);
		if (p == null) {
			plugin.exiting(getClass(), "unMuteId", "Failed: not muted");
			return false;
		}
		remove(p);

		plugin.exiting(getClass(), "unMuteId");
		return true;
	}

	public boolean isIdMuted(final UUID id) {
		return get(id.toString(), PunishmentType.MUTE) != null;
	}

	// ############### //
	// ## UUID Jail ## //
	// ############### //

	public boolean tempJailId(final UUID id, final long duration, final String reason, final String jailPointName) {
		plugin.entering(getClass(), "tempJailId");

		final long newJailEndDate = System.currentTimeMillis() + duration * 1000;
		final Jail jail = (Jail) get(id.toString(), PunishmentType.JAIL);
		if (jail != null) {
			if (jail.isPermanent() || jail.getEndDate() > newJailEndDate) {
				plugin.exiting(getClass(), "tempJailId", "Failed: already jailed with longer duration");
				return false;
			} else {
				if (!plugin.getCuboidNode().unJail(id)) {
					plugin.error("Failed to unjail already-jailed player in NCuboid!");
				}
				remove(jail);
			}
		}
		add(new Jail(id.toString(), reason, jailPointName, newJailEndDate));

		if (!plugin.getCuboidNode().jail(id, jailPointName)) {
			plugin.error("Failed to jail player in NCuboid!");
		}

		plugin.exiting(getClass(), "tempJailId");
		return true;
	}

	public boolean permJailId(final UUID id, final String reason, final String jailPointName) {
		plugin.entering(getClass(), "permJailId");

		if (get(id.toString(), PunishmentType.JAIL) != null) {
			plugin.exiting(getClass(), "permJailId", "Failed: already jailed");
			return false;
		}
		add(new Jail(id.toString(), reason, jailPointName));

		if (!plugin.getCuboidNode().jail(id, jailPointName)) {
			plugin.error("Failed to jail player in NCuboid!");
		}

		plugin.exiting(getClass(), "permJailId");
		return true;
	}

	public boolean unJailId(final UUID id) {
		plugin.entering(getClass(), "unJailId");

		final Punishment p = get(id.toString(), PunishmentType.JAIL);
		if (p == null) {
			plugin.exiting(getClass(), "unJailId", "Failed: not jailed");
			return false;
		}
		remove(p);

		if (!plugin.getCuboidNode().unJail(id)) {
			plugin.error("Failed to unjail player in NCuboid!");
		}

		plugin.exiting(getClass(), "unJailId");
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
						plugin.getCuboidNode().unJail(UuidDb.getId(p.getPunished()));
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
						plugin.getCuboidNode().unJail(UuidDb.getId(toBeRemoved.getPunished()));
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
						plugin.getCuboidNode().unJail(UuidDb.getId(toBeRemoved.getPunished()));
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
