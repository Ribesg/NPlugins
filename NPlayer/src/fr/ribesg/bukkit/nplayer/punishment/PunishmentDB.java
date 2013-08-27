package fr.ribesg.bukkit.nplayer.punishment;
import fr.ribesg.bukkit.nplayer.NPlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PunishmentDB {

	private final NPlayer plugin;

	private final Map<String, Set<Punishment>> permPunishments;
	private final Map<String, Set<Punishment>> tempPunishments;

	public PunishmentDB(NPlayer instance) {
		this.plugin = instance;
		this.permPunishments = new HashMap<>();
		this.tempPunishments = new HashMap<>();
	}

	public void saveConfig() throws IOException {
		saveConfig(Paths.get(plugin.getDataFolder().getPath(), "punishmentDB.yml"));
	}

	private void saveConfig(Path filePath) throws IOException {
		if (!Files.exists(filePath.getParent())) {
			Files.createDirectories(filePath.getParent());
		}
		if (!Files.exists(filePath)) {
			Files.createFile(filePath);
		}
		final YamlConfiguration config = new YamlConfiguration();

		long l = 0;
		final ConfigurationSection permSection = config.createSection("permanent");
		for (Punishment p : getAllPunishmentsFromMap(getPermPunishments())) {
			final ConfigurationSection pSection = permSection.createSection("perm" + ++l);
		}

		config.save(filePath.toFile());
	}

	public void loadConfig() throws IOException, InvalidConfigurationException {
		loadConfig(Paths.get(plugin.getDataFolder().getPath(), "punishmentDB.yml"));
	}

	private void loadConfig(Path filePath) throws IOException, InvalidConfigurationException {
		if (!Files.exists(filePath)) {
			return; // Nothing to load
		}
		YamlConfiguration config = new YamlConfiguration();
		config.load(filePath.toFile());

		// TODO

	}

	public Map<String, Set<Punishment>> getPermPunishments() {
		return permPunishments;
	}

	private static Set<Punishment> getAllPunishmentsFromMap(Map<String, Set<Punishment>> map) {
		final Set<Punishment> result = new HashSet<>(map.size());
		for (Set<Punishment> set : map.values()) {
			result.addAll(set);
		}
		return result;
	}

	public Map<String, Set<Punishment>> getTempPunishments() {
		return tempPunishments;
	}
}
