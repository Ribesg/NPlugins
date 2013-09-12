package fr.ribesg.bukkit.ncuboid.beans;

import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.utils.StringUtils;
import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.GeneralCuboid.CuboidType;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CuboidDBPersistenceHandler {

	public static final  Charset CHARSET     = StandardCharsets.UTF_8;
	private final static String  DB_FILENAME = "cuboidDB.yml";

	public static CuboidDb reloadDB(final NCuboid plugin) {
		final CuboidDb oldDb = plugin.getDb();
		try {
			return loadDB(plugin);
		} catch (IOException | InvalidConfigurationException e) {
			plugin.getLogger().severe("An error occured, stacktrace follows:");
			e.printStackTrace();
			plugin.getLogger().severe("This error occured when NCuboid tried to reload cuboidDB.yml");
			plugin.getLogger().info("No cuboid has been changed.");
			return oldDb;
		}
	}

	public static CuboidDb loadDB(final NCuboid plugin) throws IOException, InvalidConfigurationException {
		final CuboidDb db = new CuboidDb(plugin);

		final Path pluginFolder = plugin.getDataFolder().toPath();
		if (!Files.exists(pluginFolder)) {
			Files.createDirectories(pluginFolder);
		}

		final Path cuboidDBConfigFile = Paths.get(pluginFolder.toString(), DB_FILENAME);
		if (!Files.exists(cuboidDBConfigFile)) {
			return db;
		} else {
			String configString = null;

			try (BufferedReader reader = Files.newBufferedReader(cuboidDBConfigFile, CHARSET)) {
				final StringBuilder s = new StringBuilder();
				while (reader.ready()) {
					s.append(reader.readLine() + '\n');
				}
				configString = s.toString();
			}

			final YamlConfiguration config = new YamlConfiguration();
			config.loadFromString(configString);

			if (config.isConfigurationSection("worldCuboids")) {
				final ConfigurationSection sec = config.getConfigurationSection("worldCuboids");
				for (final String key : sec.getKeys(false)) {
					final WorldCuboid cuboid = readWorldCuboid(sec, key);
					db.addByWorld(cuboid);
				}
			}

			if (config.isConfigurationSection("playerCuboids")) {
				final ConfigurationSection sec = config.getConfigurationSection("playerCuboids");
				for (final String key : sec.getKeys(false)) {
					final PlayerCuboid cuboid = readPlayerCuboid(sec, key);
					db.add(cuboid);
				}
			}

			return db;
		}
	}

	private static WorldCuboid readWorldCuboid(final ConfigurationSection parent, final String name) {
		final ConfigurationSection worldSection = parent.getConfigurationSection(name);

		final int priority = worldSection.getInt("priority", 0);

		final Flags flags = readFlags(worldSection);
		final FlagAttributes attributes = readFlagAttributes(worldSection);
		final Rights rights = readRights(worldSection);

		return new WorldCuboid(name, rights, priority, flags, attributes);
	}

	private static PlayerCuboid readPlayerCuboid(final ConfigurationSection sec, final String name) {

		// TODO

		return null;
	}

	private static Flags readFlags(final ConfigurationSection sec) {
		final Flags flags = new Flags();
		if (sec.isList("flags")) {
			final List<String> trueFlags = sec.getStringList("flags");
			for (final Flag f : Flag.values()) {
				flags.setFlag(f, trueFlags.contains(f.toString()));
			}
		}
		return flags;
	}

	private static FlagAttributes readFlagAttributes(final ConfigurationSection sec) {
		final FlagAttributes attributes = new FlagAttributes();
		if (sec.isConfigurationSection("flagAttributes")) {
			final ConfigurationSection flagAttributesSection = sec.getConfigurationSection("flagAttributes");
			for (final FlagAtt f : FlagAtt.values()) {
				if (FlagAtt.isIntFlagAtt(f)) {
					final Integer theInteger = flagAttributesSection.getInt(f.toString(), Integer.MIN_VALUE);
					if (theInteger != Integer.MIN_VALUE) {
						attributes.setIntFlagAtt(f, theInteger);
					}
				} else if (FlagAtt.isLocFlagAtt(f)) {
					final Location theLocation = NLocation.toLocation(flagAttributesSection.getString(f.toString(), ""));
					if (theLocation != null) {
						attributes.setLocFlagAtt(f, theLocation);
					}
				} else if (FlagAtt.isVectFlagAtt(f)) {
					final Vector theVector = StringUtils.toVector(flagAttributesSection.getString(f.toString(), ""));
					if (theVector != null) {
						attributes.setVectFlagAtt(f, theVector);
					}
				} else {
					// Hello, future
				}
			}
		}
		return attributes;
	}

	private static Rights readRights(final ConfigurationSection sec) {
		final Rights rights = new Rights();
		if (sec.isConfigurationSection("rights")) {
			final ConfigurationSection rightsSection = sec.getConfigurationSection("rights");
			if (rightsSection.isList("allowedPlayers")) {
				final List<String> allowedPlayers = rightsSection.getStringList("allowedPlayers");
				for (final String playerName : allowedPlayers) {
					rights.allowPlayer(playerName);
				}
			}
			if (rightsSection.isList("allowedGroups")) {
				final List<String> allowedGroups = rightsSection.getStringList("allowedGroups");
				for (final String groupName : allowedGroups) {
					rights.allowGroup(groupName);
				}
			}
			if (rightsSection.isList("disallowedCommands")) {
				final List<String> disallowedCommands = rightsSection.getStringList("disallowedCommands");
				for (final String disallowedCommand : disallowedCommands) {
					rights.denyCommand(disallowedCommand);
				}
			}
		}
		return rights;
	}

	public static void saveDB(final NCuboid plugin, final CuboidDb db) throws IOException {
		final Path pluginFolder = plugin.getDataFolder().toPath();
		if (!Files.exists(pluginFolder)) {
			Files.createDirectories(pluginFolder);
		}

		final Path cuboidDBConfigFile = Paths.get(pluginFolder.toString(), DB_FILENAME);
		if (!Files.exists(cuboidDBConfigFile)) {
			Files.createFile(cuboidDBConfigFile);
		}

		final YamlConfiguration config = new YamlConfiguration();

		final ConfigurationSection worldCuboidsSection = config.createSection("worldCuboids");
		final Iterator<WorldCuboid> worldCuboidIterator = db.worldCuboidIterator();
		while (worldCuboidIterator.hasNext()) {
			final WorldCuboid cuboid = worldCuboidIterator.next();
			writeWorldCuboid(worldCuboidsSection, cuboid);

		}

		final ConfigurationSection playerCuboidsSection = config.createSection("playerCuboids");
		final Iterator<PlayerCuboid> playerCuboidIterator = db.playerCuboidIterator();
		while (playerCuboidIterator.hasNext()) {
			final PlayerCuboid cuboid = playerCuboidIterator.next();
			writePlayerCuboid(playerCuboidsSection, cuboid);
		}

		final String configString = config.saveToString();
		try (BufferedWriter writer = Files.newBufferedWriter(cuboidDBConfigFile,
		                                                     CHARSET,
		                                                     StandardOpenOption.TRUNCATE_EXISTING,
		                                                     StandardOpenOption.WRITE)) {
			writer.write(configString);
		}
	}

	private static void writeWorldCuboid(final ConfigurationSection parent, final WorldCuboid cuboid) {
		final ConfigurationSection sec = parent.createSection(cuboid.getWorldName());
		sec.set("priority", cuboid.getPriority());
		writeFlags(sec, cuboid);
		writeFlagAtts(sec, cuboid);
		writeRights(sec, cuboid);

	}

	private static void writePlayerCuboid(final ConfigurationSection parent, final PlayerCuboid cuboid) {
		final ConfigurationSection sec = parent.createSection(cuboid.getCuboidName());

		// TODO Set everything else here
		sec.set("priority", cuboid.getPriority());

		if (cuboid.getType() == CuboidType.RECT) {
			// Set coords etc
		} else {
			// Hello, future
		}

		writeFlags(sec, cuboid);
		writeFlagAtts(sec, cuboid);
		writeRights(sec, cuboid);
	}

	private static void writeFlags(final ConfigurationSection parent, final GeneralCuboid cuboid) {
		final List<String> flags = new ArrayList<String>();
		for (final Flag f : Flag.values()) {
			if (cuboid.getFlag(f)) {
				flags.add(f.toString());
			}
		}
		if (flags.size() > 0) {
			parent.set("flags", flags);
		}
	}

	private static void writeFlagAtts(final ConfigurationSection parent, final GeneralCuboid cuboid) {
		final ConfigurationSection sec = parent.createSection("flagAttributes");
		boolean used = false;

		for (final FlagAtt f : FlagAtt.values()) {
			if (FlagAtt.isIntFlagAtt(f)) {
				final Integer theInteger = cuboid.getIntFlagAtt(f);
				if (theInteger != null) {
					used = true;
					sec.set(f.toString(), theInteger);
				}
			} else if (FlagAtt.isLocFlagAtt(f)) {
				final Location theLocation = cuboid.getLocFlagAtt(f);
				if (theLocation != null) {
					used = true;
					sec.set(f.toString(), NLocation.toStringPlus(theLocation));
				}
			} else if (FlagAtt.isVectFlagAtt(f)) {
				final Vector theVector = cuboid.getVectFlagAtt(f);
				if (theVector != null) {
					used = true;
					sec.set(f.toString(), StringUtils.toString(theVector));
				}
			} else {
				// Hello, future
			}
		}

		if (!used) {
			parent.set("flagAttributes", null);
		}
	}

	private static void writeRights(final ConfigurationSection parent, final GeneralCuboid cuboid) {
		final ConfigurationSection sec = parent.createSection("rights");
		boolean used = false;

		final Set<String> allowedPlayers = cuboid.getAllowedPlayers();
		if (allowedPlayers != null) {
			final List<String> allowedPlayersStringList = new ArrayList<String>(allowedPlayers);
			sec.set("allowedPlayers", allowedPlayersStringList);
			used = true;
		}

		final Set<String> allowedGroups = cuboid.getAllowedGroups();
		if (allowedGroups != null) {
			final List<String> allowedGroupsStringList = new ArrayList<String>(allowedGroups);
			sec.set("allowedGroups", allowedGroupsStringList);
			used = true;
		}

		final Set<String> disallowedCommands = cuboid.getDisallowedCommands();
		if (disallowedCommands != null) {
			final List<String> disallowedCommandsStringList = new ArrayList<String>(disallowedCommands);
			sec.set("disallowedCommands", disallowedCommandsStringList);
			used = true;
		}

		if (!used) {
			parent.set("rights", null);
		}
	}
}
