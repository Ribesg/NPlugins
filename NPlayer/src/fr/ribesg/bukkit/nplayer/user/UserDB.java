package fr.ribesg.bukkit.nplayer.user;
import fr.ribesg.bukkit.ncore.common.NLocation;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDb {

	private final NPlayer                 plugin;
	private final Map<String, User>       usersPerName;
	private final Map<String, List<User>> usersPerIp;

	public UserDb(NPlayer plugin) {
		this.plugin = plugin;
		this.usersPerName = new HashMap<>();
		this.usersPerIp = new HashMap<>();
	}

	public boolean isUserKnown(String userName) {
		return usersPerName.containsKey(userName);
	}

	public boolean isIpKnown(String ip) {
		return usersPerIp.containsKey(ip);
	}

	public User get(String userName) {
		return usersPerName.get(userName);
	}

	public List<User> getByIp(String ip) {
		List<User> res = usersPerIp.get(ip);
		return res == null ? new ArrayList<User>() : res;
	}

	public User newUser(String userName, String passwordHash, String currentIp) {
		Date date = new Date();
		User user = new User(plugin.getLoggedOutUserHandler(), userName, passwordHash, currentIp, date);
		usersPerName.put(userName, user);
		addPerIp(currentIp, user);
		return user;
	}

	private void addPerIp(String ip, User user) {
		List<User> usersWithSameIp = usersPerIp.get(ip);
		if (usersWithSameIp == null) {
			usersWithSameIp = new ArrayList<>();
			usersWithSameIp.add(user);
			usersPerIp.put(ip, usersWithSameIp);
		} else {
			usersWithSameIp.add(user);
		}
	}

	public void saveConfig() throws IOException {
		saveConfig(Paths.get(plugin.getDataFolder().getPath(), "userDB.yml"));
	}

	private void saveConfig(Path filePath) throws IOException {
		if (!Files.exists(filePath.getParent())) {
			Files.createDirectories(filePath.getParent());
		}
		if (!Files.exists(filePath)) {
			Files.createFile(filePath);
		}
		YamlConfiguration config = new YamlConfiguration();
		for (String userName : usersPerName.keySet()) {
			User user = usersPerName.get(userName);
			ConfigurationSection userSection = config.createSection(userName);
			userSection.set("passwordHash", user.getPasswordHash());
			userSection.set("lastIp", user.getLastIp());
			userSection.set("knownIps", user.getKnownIps());
			userSection.set("firstJoin", user.getFirstJoin().getTime());
			userSection.set("lastSeen", user.getLastSeen().getTime());
			userSection.set("autoLogout", user.hasAutoLogout());
			userSection.set("home", user.getHome() != null ? new NLocation(user.getHome()).toString() : "null");
		}
		config.save(filePath.toFile());
	}

	public void loadConfig() throws IOException, InvalidConfigurationException {
		loadConfig(Paths.get(plugin.getDataFolder().getPath(), "userDB.yml"));
	}

	private void loadConfig(Path filePath) throws IOException, InvalidConfigurationException {
		if (!Files.exists(filePath)) {
			return; // Nothing to load
		}
		YamlConfiguration config = new YamlConfiguration();
		config.load(filePath.toFile());
		for (String userName : config.getKeys(false)) {
			ConfigurationSection userSection = config.getConfigurationSection(userName);
			String passwordHash = userSection.getString("passwordHash");
			String lastIp = userSection.getString("lastIp");
			List<String> knownIps = userSection.getStringList("knownIps");
			Date firstJoin = new Date(userSection.getLong("firstJoin"));
			Date lastSeen = new Date(userSection.getLong("lastSeen"));
			boolean autoLogout = userSection.getBoolean("autoLogout");
			Location home = NLocation.toLocation(userSection.getString("home"));
			User user = new User(plugin.getLoggedOutUserHandler(),
			                     lastIp,
			                     firstJoin,
			                     knownIps,
			                     lastSeen,
			                     passwordHash,
			                     userName,
			                     autoLogout,
			                     home);
			usersPerName.put(userName, user);
			for (String ip : user.getKnownIps()) {
				addPerIp(ip, user);
			}
		}
	}
}
