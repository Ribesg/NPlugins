/***************************************************************************
 * Project file:    NPlugins - NPlayer - UserDb.java                       *
 * Full Class name: fr.ribesg.bukkit.nplayer.user.UserDb                   *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nplayer.user;
import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.config.UuidDb;
import fr.ribesg.bukkit.ncore.util.PlayerIdsUtil;
import fr.ribesg.bukkit.ncore.util.TimeUtil;
import fr.ribesg.bukkit.nplayer.NPlayer;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserDb {

	private final static long TWO_WEEKS = TimeUtil.getInMilliseconds("2weeks");

	private final NPlayer                 plugin;
	private final Map<UUID, User>         usersPerId;
	private final Map<String, List<User>> usersPerIp;

	public UserDb(final NPlayer plugin) {
		this.plugin = plugin;
		this.usersPerId = new HashMap<>();
		this.usersPerIp = new HashMap<>();
	}

	public boolean isUserKnown(final UUID userId) {
		return usersPerId.containsKey(userId);
	}

	public boolean isIpKnown(final String ip) {
		return usersPerIp.containsKey(ip);
	}

	public User get(final UUID userId) {
		return usersPerId.get(userId);
	}

	public List<User> getByIp(final String ip) {
		final List<User> res = usersPerIp.get(ip);
		return res == null ? new ArrayList<User>() : res;
	}

	public User newUser(final UUID userId, final String passwordHash, final String currentIp) {
		final User user = new User(plugin.getLoggedOutUserHandler(), userId, passwordHash, currentIp);
		usersPerId.put(userId, user);
		addPerIp(currentIp, user);
		return user;
	}

	private void addPerIp(final String ip, final User user) {
		List<User> usersWithSameIp = usersPerIp.get(ip);
		if (usersWithSameIp == null) {
			usersWithSameIp = new ArrayList<>();
			usersWithSameIp.add(user);
			usersPerIp.put(ip, usersWithSameIp);
		} else {
			usersWithSameIp.add(user);
		}
	}

	public int size() {
		return usersPerId.size();
	}

	public int recurrentSize() {
		int size = 0;
		for (final User u : usersPerId.values()) {
			if (System.currentTimeMillis() - UuidDb.getLastSeen(u.getUserId()) < TWO_WEEKS) {
				size++;
			}
		}
		return size;
	}

	public void saveConfig() throws IOException {
		saveConfig(Paths.get(plugin.getDataFolder().getPath(), "userDB.yml"));
	}

	private void saveConfig(final Path filePath) throws IOException {
		if (!Files.exists(filePath.getParent())) {
			Files.createDirectories(filePath.getParent());
		}
		if (!Files.exists(filePath)) {
			Files.createFile(filePath);
		}
		final YamlConfiguration config = new YamlConfiguration();
		for (final UUID userId : usersPerId.keySet()) {
			final User user = usersPerId.get(userId);
			final ConfigurationSection userSection = config.createSection(user.getUserId().toString());
			userSection.set("passwordHash", user.getPasswordHash());
			userSection.set("lastIp", user.getLastIp());
			userSection.set("knownIps", user.getKnownIps());
			userSection.set("autoLogout", user.hasAutoLogout());
			userSection.set("home", user.getHome() != null ? new NLocation(user.getHome()).toString() : "null");
		}
		config.save(filePath.toFile());
	}

	public void loadConfig() throws IOException, InvalidConfigurationException {
		loadConfig(Paths.get(plugin.getDataFolder().getPath(), "userDB.yml"));
	}

	private void loadConfig(final Path filePath) throws IOException, InvalidConfigurationException {
		if (!Files.exists(filePath)) {
			return; // Nothing to load
		}
		final YamlConfiguration config = new YamlConfiguration();
		config.load(filePath.toFile());
		for (final String userIdString : config.getKeys(false)) {
			final ConfigurationSection userSection = config.getConfigurationSection(userIdString);
			UUID id = null;
			if (PlayerIdsUtil.isValidUuid(userIdString)) {
				id = UUID.fromString(userIdString);
			} else if (PlayerIdsUtil.isValidMinecraftUserName(userIdString)) {
				id = UuidDb.getId(userIdString, true);
			}
			if (id == null) {
				throw new InvalidConfigurationException("Unknown userId '" + userIdString + "' found in userDb.yml");
			}
			final String passwordHash = userSection.getString("passwordHash");
			final String lastIp = userSection.getString("lastIp");
			final List<String> knownIps = userSection.getStringList("knownIps");
			final boolean autoLogout = userSection.getBoolean("autoLogout");
			final Location home = NLocation.toLocation(userSection.getString("home"));
			final User user = new User(plugin.getLoggedOutUserHandler(), lastIp, knownIps, passwordHash, id, autoLogout, home);
			usersPerId.put(id, user);
			for (final String ip : user.getKnownIps()) {
				addPerIp(ip, user);
			}
		}
	}
}
