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
import fr.ribesg.bukkit.ncore.node.Node;
import fr.ribesg.bukkit.ncore.util.IPValidator;
import fr.ribesg.bukkit.ncore.util.PlayerIdsUtil;
import fr.ribesg.bukkit.nplayer.NPlayer;

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
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

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
		this.saveConfig(Paths.get(this.plugin.getDataFolder().getPath(), "punishmentDB.yml"));
	}

	private void saveConfig(final Path filePath) throws IOException {
		if (!Files.exists(filePath.getParent())) {
			Files.createDirectories(filePath.getParent());
		}
		if (!Files.exists(filePath)) {
			Files.createFile(filePath);
		}
		final YamlConfiguration config = new YamlConfiguration();

		final List<Punishment> punishments = getAllPunishmentsFromMaps(this.permPunishments, this.tempPunishments);

		for (final Punishment p : punishments) {
			final String key = p.getPunished().replaceAll("\\.", "-") + '-' + p.getType().toString(); // Should be unique // TODO What if I want to ban 24 hours and mute 7 days?
			final ConfigurationSection pSection = config.createSection(key);
			pSection.set("punished", p.getPunished());
			pSection.set("type", p.getType().toString());
			pSection.set("endDate", p.getEndDate());
			pSection.set("reason", p.getReason());
			if (p.getType() == PunishmentType.JAIL) {
				pSection.set("jailPointName", ((Jail)p).getJailPointName());
			}
		}

		config.save(filePath.toFile());
	}

	public void loadConfig() throws IOException, InvalidConfigurationException {
		this.loadConfig(Paths.get(this.plugin.getDataFolder().getPath(), "punishmentDB.yml"));
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
				if (PlayerIdsUtil.isValidUuid(punishedString) || IPValidator.isValidIp(punishedString)) {
					punished = punishedString;
				} else if (PlayerIdsUtil.isValidMinecraftUserName(punishedString)) {
					punished = UuidDb.getId(Node.PLAYER, punishedString, true).toString();
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
				this.add(p);
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
		return getAllPunishmentsFromMaps(this.permPunishments, this.tempPunishments);
	}

	public Map<String, Set<Punishment>> getPermPunishments() {
		return this.permPunishments;
	}

	public Map<String, Set<Punishment>> getTempPunishments() {
		return this.tempPunishments;
	}

	public SortedMap<Long, Punishment> getTempPunishmentEndDateMap() {
		return this.tempPunishmentEndDateMap;
	}

	public Map<UUID, String> getLeaveMessages() {
		return this.leaveMessages;
	}

	// ############## //
	// ## UUID Ban ## //
	// ############## //

	public boolean tempBanId(final UUID id, final long duration, final String reason) {
		this.plugin.entering(this.getClass(), "tempBanId");

		final long newBanEndDate = System.currentTimeMillis() + duration * 1000;
		final Ban ban = (Ban)this.get(id.toString(), PunishmentType.BAN);
		if (ban != null) {
			if (ban.isPermanent() || ban.getEndDate() > newBanEndDate) {
				this.plugin.exiting(this.getClass(), "unBanId", "Failed: already banned with longer duration");
				return false;
			} else {
				this.remove(ban);
			}
		}
		this.add(new Ban(id.toString(), reason, newBanEndDate));

		this.plugin.exiting(this.getClass(), "tempBanId");
		return true;
	}

	public boolean permBanId(final UUID id, final String reason) {
		this.plugin.entering(this.getClass(), "permBanId");

		if (this.get(id.toString(), PunishmentType.BAN) != null) {
			this.plugin.exiting(this.getClass(), "unBanId", "Failed: already banned");
			return false;
		}
		this.add(new Ban(id.toString(), reason));

		this.plugin.exiting(this.getClass(), "permBanId");
		return true;
	}

	public boolean unBanId(final UUID id) {
		this.plugin.entering(this.getClass(), "unBanId");

		final Punishment p = this.get(id.toString(), PunishmentType.BAN);
		if (p == null) {
			this.plugin.exiting(this.getClass(), "unBanId", "Failed: not banned");
			return false;
		}
		this.remove(p);

		this.plugin.exiting(this.getClass(), "unBanId");
		return true;
	}

	public boolean isIdBanned(final UUID id) {
		return this.get(id.toString(), PunishmentType.BAN) != null;
	}

	// ############ //
	// ## IP Ban ## //
	// ############ //

	public boolean tempBanIp(final String ip, final long duration, final String reason) {
		this.plugin.entering(this.getClass(), "tempBanIp");

		final long newIpBanEndDate = System.currentTimeMillis() + duration * 1000;
		final IpBan ipBan = (IpBan)this.get(ip, PunishmentType.IPBAN);
		if (ipBan != null) {
			if (ipBan.isPermanent() || ipBan.getEndDate() > newIpBanEndDate) {
				this.plugin.exiting(this.getClass(), "tempBanIp", "Failed: already banned with longer duration");
				return false;
			} else {
				this.remove(ipBan);
			}
		}
		this.add(new IpBan(ip.toLowerCase(), reason, newIpBanEndDate));

		this.plugin.exiting(this.getClass(), "tempBanIp");
		return true;
	}

	public boolean permBanIp(final String ip, final String reason) {
		this.plugin.entering(this.getClass(), "permBanIp");

		if (this.get(ip, PunishmentType.IPBAN) != null) {
			this.plugin.exiting(this.getClass(), "permBanIp", "Failed: already banned");
			return false;
		}
		this.add(new IpBan(ip.toLowerCase(), reason));

		this.plugin.exiting(this.getClass(), "permBanIp");
		return true;
	}

	public boolean unBanIp(final String ip) {
		this.plugin.entering(this.getClass(), "unBanIp");

		final Punishment p = this.get(ip, PunishmentType.IPBAN);
		if (p == null) {
			this.plugin.exiting(this.getClass(), "unBanIp", "Failed: not banned");
			return false;
		}
		this.remove(p);

		this.plugin.exiting(this.getClass(), "unBanIp");
		return true;
	}

	public boolean isIpBanned(final String ip) {
		return this.get(ip, PunishmentType.IPBAN) != null;
	}

	// ############### //
	// ## UUID Mute ## //
	// ############### //

	public boolean tempMuteId(final UUID id, final long duration, final String reason) {
		this.plugin.entering(this.getClass(), "tempMuteId");

		final long newMuteEndDate = System.currentTimeMillis() + duration * 1000;
		final Mute mute = (Mute)this.get(id.toString(), PunishmentType.MUTE);
		if (mute != null) {
			if (mute.isPermanent() || mute.getEndDate() > newMuteEndDate) {
				this.plugin.exiting(this.getClass(), "tempMuteId", "Failed: already muted with longer duration");
				return false;
			} else {
				this.remove(mute);
			}
		}
		this.add(new Mute(id.toString(), reason, newMuteEndDate));

		this.plugin.exiting(this.getClass(), "tempMuteId");
		return true;
	}

	public boolean permMuteId(final UUID id, final String reason) {
		this.plugin.entering(this.getClass(), "permMuteId");

		if (this.get(id.toString(), PunishmentType.MUTE) != null) {
			this.plugin.exiting(this.getClass(), "permMuteId", "Failed: already muted");
			return false;
		}
		this.add(new Mute(id.toString(), reason));

		this.plugin.exiting(this.getClass(), "permMuteId");
		return true;
	}

	public boolean unMuteId(final UUID id) {
		this.plugin.entering(this.getClass(), "unMuteId");

		final Punishment p = this.get(id.toString(), PunishmentType.MUTE);
		if (p == null) {
			this.plugin.exiting(this.getClass(), "unMuteId", "Failed: not muted");
			return false;
		}
		this.remove(p);

		this.plugin.exiting(this.getClass(), "unMuteId");
		return true;
	}

	public boolean isIdMuted(final UUID id) {
		return this.get(id.toString(), PunishmentType.MUTE) != null;
	}

	// ############### //
	// ## UUID Jail ## //
	// ############### //

	public boolean tempJailId(final UUID id, final long duration, final String reason, final String jailPointName) {
		this.plugin.entering(this.getClass(), "tempJailId");

		final long newJailEndDate = System.currentTimeMillis() + duration * 1000;
		final Jail jail = (Jail)this.get(id.toString(), PunishmentType.JAIL);
		if (jail != null) {
			if (jail.isPermanent() || jail.getEndDate() > newJailEndDate) {
				this.plugin.exiting(this.getClass(), "tempJailId", "Failed: already jailed with longer duration");
				return false;
			} else {
				if (!this.plugin.getCuboidNode().unJail(id)) {
					this.plugin.error("Failed to unjail already-jailed player in NCuboid!");
				}
				this.remove(jail);
			}
		}
		this.add(new Jail(id.toString(), reason, jailPointName, newJailEndDate));

		if (!this.plugin.getCuboidNode().jail(id, jailPointName)) {
			this.plugin.error("Failed to jail player in NCuboid!");
		}

		this.plugin.exiting(this.getClass(), "tempJailId");
		return true;
	}

	public boolean permJailId(final UUID id, final String reason, final String jailPointName) {
		this.plugin.entering(this.getClass(), "permJailId");

		if (this.get(id.toString(), PunishmentType.JAIL) != null) {
			this.plugin.exiting(this.getClass(), "permJailId", "Failed: already jailed");
			return false;
		}
		this.add(new Jail(id.toString(), reason, jailPointName));

		if (!this.plugin.getCuboidNode().jail(id, jailPointName)) {
			this.plugin.error("Failed to jail player in NCuboid!");
		}

		this.plugin.exiting(this.getClass(), "permJailId");
		return true;
	}

	public boolean unJailId(final UUID id) {
		this.plugin.entering(this.getClass(), "unJailId");

		final Punishment p = this.get(id.toString(), PunishmentType.JAIL);
		if (p == null) {
			this.plugin.exiting(this.getClass(), "unJailId", "Failed: not jailed");
			return false;
		}
		this.remove(p);

		if (!this.plugin.getCuboidNode().unJail(id)) {
			this.plugin.error("Failed to unjail player in NCuboid!");
		}

		this.plugin.exiting(this.getClass(), "unJailId");
		return true;
	}

	// ######################## //
	// ## Convenient methods ## //
	// ######################## //

	public Punishment get(final String key, final PunishmentType type) {
		this.plugin.entering(this.getClass(), "get", "key=" + key + ";type=" + type);
		Punishment result = null;

		final Set<Punishment> temporary = this.tempPunishments.get(key.toLowerCase());
		if (temporary != null) {
			final Iterator<Punishment> it = temporary.iterator();
			while (it.hasNext()) {
				final Punishment p = it.next();
				if (!p.isStillActive()) {
					if (this.plugin.getCuboidNode() != null && p.getType() == PunishmentType.JAIL) {
						this.plugin.getCuboidNode().unJail(UuidDb.getId(Node.PLAYER, p.getPunished()));
					}
					it.remove();
				} else if (p.getType() == type) {
					result = p;
					break;
				}
			}
			if (temporary.isEmpty()) {
				this.tempPunishments.remove(key.toLowerCase());
			}
		}
		if (result == null) {
			final Set<Punishment> permanent = this.permPunishments.get(key.toLowerCase());
			if (permanent != null) {
				for (final Punishment p : permanent) {
					if (p.getType() == type) {
						result = p;
						break;
					}
				}
			}
		}

		this.plugin.exiting(this.getClass(), "get", "result=" + result);
		return result;
	}

	public void add(final Punishment punishment) {
		if (punishment.isPermanent()) {
			Set<Punishment> playerPunishments = this.permPunishments.get(punishment.getPunished().toLowerCase());
			if (playerPunishments == null) {
				playerPunishments = new HashSet<>();
			}
			playerPunishments.add(punishment);
			this.permPunishments.put(punishment.getPunished().toLowerCase(), playerPunishments);
		} else {
			Set<Punishment> playerPunishments = this.tempPunishments.get(punishment.getPunished().toLowerCase());
			if (playerPunishments == null) {
				playerPunishments = new HashSet<>();
			}
			playerPunishments.add(punishment);
			this.tempPunishments.put(punishment.getPunished().toLowerCase(), playerPunishments);
			this.tempPunishmentEndDateMap.put(punishment.getEndDate(), punishment);
		}
	}

	public Punishment remove(final Punishment toBeRemoved) {
		this.plugin.entering(this.getClass(), "remove", "toBeRemoved=" + toBeRemoved);
		Punishment result = null;

		final String key = toBeRemoved.getPunished();
		if (toBeRemoved.isPermanent()) {
			final Set<Punishment> permanent = this.permPunishments.get(key.toLowerCase());
			if (permanent != null) {
				if (permanent.remove(toBeRemoved)) {
					if (permanent.isEmpty()) {
						this.permPunishments.remove(key.toLowerCase());
					}
					if (this.plugin.getCuboidNode() != null && toBeRemoved.getType() == PunishmentType.JAIL) {
						this.plugin.getCuboidNode().unJail(UuidDb.getId(Node.PLAYER, toBeRemoved.getPunished()));
					}
					result = toBeRemoved;
				}
			}
		} else {
			final Set<Punishment> temporary = this.tempPunishments.get(key.toLowerCase());
			if (temporary != null) {
				if (temporary.remove(toBeRemoved)) {
					if (temporary.isEmpty()) {
						this.tempPunishments.remove(key.toLowerCase());
					}
					if (this.plugin.getCuboidNode() != null && toBeRemoved.getType() == PunishmentType.JAIL) {
						this.plugin.getCuboidNode().unJail(UuidDb.getId(Node.PLAYER, toBeRemoved.getPunished()));
					}
					result = toBeRemoved;
				}
			}
			final Iterator<Map.Entry<Long, Punishment>> it = this.tempPunishmentEndDateMap.entrySet().iterator();
			while (it.hasNext()) {
				if (it.next().getKey() == toBeRemoved.getEndDate()) {
					it.remove();
					break;
				}
			}
		}

		this.plugin.exiting(this.getClass(), "remove", "result=" + result);
		return result;
	}
}
