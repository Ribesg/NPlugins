/***************************************************************************
 * Project file:    NPlugins - NCuboid - RegionDbPersistenceHandler.java   *
 * Full Class name: fr.ribesg.bukkit.ncuboid.beans.RegionDbPersistenceHandler
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.beans;

import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.utils.StringUtils;
import fr.ribesg.bukkit.ncuboid.NCuboid;
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

public class RegionDbPersistenceHandler {

	public static final Charset CHARSET = StandardCharsets.UTF_8;

	private static final String DB_FILENAME = "regionDB.yml";

	// Sections
	private static final String WORLD_SECTION  = "worldRegions";
	private static final String PLAYER_SECTION = "playerRegions";

	// Common attributes
	private static final String WORLD_NAME = "world";
	private static final String PRIORITY   = "priority";

	// PLAYER attributes
	private static final String OWNER_NAME       = "owner";
	private static final String TOTAL_SIZE       = "totalSize";
	private static final String WELCOME_MESSAGE  = "welcomeMessage";
	private static final String FAREWELL_MESSAGE = "farewellMessage";
	private static final String TYPE             = "type";

	// CUBOID attributes
	private static final String MIN_CORNER = "minCorner";
	private static final String MAX_CORNER = "maxCorner";

	// Common sub-sections
	private static final String FLAGS           = "flags";
	private static final String FLAG_ATTRIBUTES = "flagAttributes";
	private static final String RIGHTS          = "rights";

	// For rights
	private static final String ADMINS              = "admins";
	private static final String USERS               = "users";
	private static final String ALLOWED_GROUPS      = "allowedGroups";
	private static final String DISALLOWED_COMMANDS = "disallowedCommands";

	public static RegionDb reloadDb(final NCuboid plugin) {
		final RegionDb oldDb = plugin.getDb();
		try {
			return loadDb(plugin);
		} catch (IOException | InvalidConfigurationException e) {
			plugin.getLogger().severe("An error occured, stacktrace follows:");
			e.printStackTrace();
			plugin.getLogger().severe("This error occured when NCuboid tried to reload regionDB.yml");
			plugin.getLogger().info("No cuboid has been changed.");
			return oldDb;
		}
	}

	public static RegionDb loadDb(final NCuboid plugin) throws IOException, InvalidConfigurationException {
		final RegionDb db = new RegionDb(plugin);

		final Path pluginFolder = plugin.getDataFolder().toPath();
		if (!Files.exists(pluginFolder)) {
			Files.createDirectories(pluginFolder);
		}

		final Path cuboidDBConfigFile = Paths.get(pluginFolder.toString(), DB_FILENAME);
		if (!Files.exists(cuboidDBConfigFile)) {
			return db;
		} else {
			final String configString;

			try (BufferedReader reader = Files.newBufferedReader(cuboidDBConfigFile, CHARSET)) {
				final StringBuilder s = new StringBuilder();
				while (reader.ready()) {
					s.append(reader.readLine()).append('\n');
				}
				configString = s.toString();
			}

			final YamlConfiguration config = new YamlConfiguration();
			config.loadFromString(configString);

			if (config.isConfigurationSection(WORLD_SECTION)) {
				final ConfigurationSection sec = config.getConfigurationSection(WORLD_SECTION);
				for (final String key : sec.getKeys(false)) {
					final WorldRegion region = readWorldRegion(sec, key);
					db.addByWorld(region);
				}
			}

			if (config.isConfigurationSection(PLAYER_SECTION)) {
				final ConfigurationSection sec = config.getConfigurationSection(PLAYER_SECTION);
				for (final String key : sec.getKeys(false)) {
					final PlayerRegion region = readPlayerRegion(sec, key);
					db.add(region);
				}
			}

			return db;
		}
	}

	private static WorldRegion readWorldRegion(final ConfigurationSection parent, final String name) {
		final ConfigurationSection worldSection = parent.getConfigurationSection(name);

		final int priority = worldSection.getInt(PRIORITY, 0);

		final Flags flags = readFlags(worldSection);
		final FlagAttributes attributes = readFlagAttributes(worldSection);
		final Rights rights = readRights(worldSection);

		return new WorldRegion(name, rights, priority, flags, attributes);
	}

	private static PlayerRegion readPlayerRegion(final ConfigurationSection parent, final String name) {
		final ConfigurationSection playerSection = parent.getConfigurationSection(name);

		final String ownerName = playerSection.getString(OWNER_NAME);
		final String worldName = playerSection.getString(WORLD_NAME);
		final long totalSize = playerSection.getLong(TOTAL_SIZE);
		final String welcomeMessage = playerSection.getString(WELCOME_MESSAGE, "");
		final String farewellMessage = playerSection.getString(FAREWELL_MESSAGE, "");
		final int priority = playerSection.getInt(PRIORITY, 0);

		final GeneralRegion.RegionType type = GeneralRegion.RegionType.valueOf(playerSection.getString(TYPE));

		// First load non-specific things that may be written after the specific stuff
		final Flags flags = readFlags(playerSection);
		final FlagAttributes attributes = readFlagAttributes(playerSection);
		final Rights rights = readRights(playerSection);

		// Read specific stuff and return corresponding Region
		switch (type) {
			case CUBOID:
				final NLocation minCorner = NLocation.toNLocation(playerSection.getString(MIN_CORNER));
				final NLocation maxCorner = NLocation.toNLocation(playerSection.getString(MAX_CORNER));
				return new CuboidRegion(name,
				                        ownerName,
				                        worldName,
				                        PlayerRegion.RegionState.NORMAL,
				                        totalSize,
				                        welcomeMessage,
				                        farewellMessage,
				                        rights,
				                        priority,
				                        flags,
				                        attributes,
				                        minCorner,
				                        maxCorner);
			default:
				throw new UnsupportedOperationException();
		}
	}

	private static Flags readFlags(final ConfigurationSection sec) {
		final Flags flags = new Flags();
		if (sec.isList(FLAGS)) {
			final List<String> trueFlags = sec.getStringList(FLAGS);
			for (final Flag f : Flag.values()) {
				flags.setFlag(f, trueFlags.contains(f.toString()));
			}
		}
		return flags;
	}

	private static FlagAttributes readFlagAttributes(final ConfigurationSection sec) {
		final FlagAttributes attributes = new FlagAttributes();
		if (sec.isConfigurationSection(FLAG_ATTRIBUTES)) {
			final ConfigurationSection flagAttributesSection = sec.getConfigurationSection(FLAG_ATTRIBUTES);
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
		if (sec.isConfigurationSection(RIGHTS)) {
			final ConfigurationSection rightsSection = sec.getConfigurationSection(RIGHTS);
			if (rightsSection.isList(ADMINS)) {
				final List<String> admins = rightsSection.getStringList(ADMINS);
				for (final String playerName : admins) {
					rights.addAdmin(playerName);
				}
			}
			if (rightsSection.isList(USERS)) {
				final List<String> users = rightsSection.getStringList(USERS);
				for (final String playerName : users) {
					rights.addUser(playerName);
				}
			}
			if (rightsSection.isList(ALLOWED_GROUPS)) {
				final List<String> allowedGroups = rightsSection.getStringList(ALLOWED_GROUPS);
				for (final String groupName : allowedGroups) {
					rights.allowGroup(groupName);
				}
			}
			if (rightsSection.isList(DISALLOWED_COMMANDS)) {
				final List<String> disallowedCommands = rightsSection.getStringList(DISALLOWED_COMMANDS);
				for (final String disallowedCommand : disallowedCommands) {
					rights.denyCommand(disallowedCommand);
				}
			}
		}
		return rights;
	}

	public static void saveDb(final NCuboid plugin, final RegionDb db) throws IOException {
		final Path pluginFolder = plugin.getDataFolder().toPath();
		if (!Files.exists(pluginFolder)) {
			Files.createDirectories(pluginFolder);
		}

		final Path regionDbConfigFile = Paths.get(pluginFolder.toString(), DB_FILENAME);
		if (!Files.exists(regionDbConfigFile)) {
			Files.createFile(regionDbConfigFile);
		}

		final YamlConfiguration config = new YamlConfiguration();

		final ConfigurationSection worldRegionsSection = config.createSection(WORLD_SECTION);
		final Iterator<WorldRegion> worldRegionIterator = db.worldRegionsIterator();
		while (worldRegionIterator.hasNext()) {
			final WorldRegion region = worldRegionIterator.next();
			writeWorldRegion(worldRegionsSection, region);

		}

		final ConfigurationSection playerRegionsSection = config.createSection(PLAYER_SECTION);
		final Iterator<PlayerRegion> playerRegionIterator = db.playerRegionsIterator();
		while (playerRegionIterator.hasNext()) {
			final PlayerRegion region = playerRegionIterator.next();
			writePlayerRegion(playerRegionsSection, region);
		}

		final String configString = config.saveToString();
		try (BufferedWriter writer = Files.newBufferedWriter(regionDbConfigFile,
		                                                     CHARSET,
		                                                     StandardOpenOption.TRUNCATE_EXISTING,
		                                                     StandardOpenOption.WRITE)) {
			writer.write(configString);
		}
	}

	private static void writeWorldRegion(final ConfigurationSection parent, final WorldRegion region) {
		final ConfigurationSection sec = parent.createSection(region.getWorldName());
		sec.set(PRIORITY, region.getPriority());
		writeFlags(sec, region);
		writeFlagAtts(sec, region);
		writeRights(sec, region);
	}

	private static void writePlayerRegion(final ConfigurationSection parent, final PlayerRegion region) {
		final ConfigurationSection sec = parent.createSection(region.getRegionName());

		sec.set(OWNER_NAME, region.getOwnerName());
		sec.set(WORLD_NAME, region.getWorldName());
		sec.set(TOTAL_SIZE, region.getTotalSize());
		sec.set(WELCOME_MESSAGE, region.getWelcomeMessage());
		sec.set(FAREWELL_MESSAGE, region.getFarewellMessage());
		sec.set(PRIORITY, region.getPriority());

		sec.set(TYPE, region.getType().toString());
		switch (region.getType()) {
			case CUBOID:
				final CuboidRegion cuboid = (CuboidRegion) region;
				sec.set(MIN_CORNER, cuboid.getMinCorner().toString());
				sec.set(MAX_CORNER, cuboid.getMaxCorner().toString());
				break;
			default:
				// Hello, future
				break;
		}

		writeFlags(sec, region);
		writeFlagAtts(sec, region);
		writeRights(sec, region);
	}

	private static void writeFlags(final ConfigurationSection parent, final GeneralRegion region) {
		final List<String> flags = new ArrayList<>();
		for (final Flag f : Flag.values()) {
			if (region.getFlag(f)) {
				flags.add(f.toString());
			}
		}
		if (flags.size() > 0) {
			parent.set(FLAGS, flags);
		}
	}

	private static void writeFlagAtts(final ConfigurationSection parent, final GeneralRegion region) {
		final ConfigurationSection sec = parent.createSection(FLAG_ATTRIBUTES);
		boolean used = false;

		for (final FlagAtt f : FlagAtt.values()) {
			if (FlagAtt.isIntFlagAtt(f)) {
				final Integer theInteger = region.getIntFlagAtt(f);
				if (theInteger != null) {
					used = true;
					sec.set(f.toString(), theInteger);
				}
			} else if (FlagAtt.isLocFlagAtt(f)) {
				final Location theLocation = region.getLocFlagAtt(f);
				if (theLocation != null) {
					used = true;
					sec.set(f.toString(), NLocation.toStringPlus(theLocation));
				}
			} else if (FlagAtt.isVectFlagAtt(f)) {
				final Vector theVector = region.getVectFlagAtt(f);
				if (theVector != null) {
					used = true;
					sec.set(f.toString(), StringUtils.toString(theVector));
				}
			} else {
				// Hello, future
			}
		}

		if (!used) {
			parent.set(FLAG_ATTRIBUTES, null);
		}
	}

	private static void writeRights(final ConfigurationSection parent, final GeneralRegion region) {
		final ConfigurationSection sec = parent.createSection(RIGHTS);
		boolean used = false;

		final Set<String> admins = region.getAdmins();
		if (admins != null) {
			final List<String> adminsStringList = new ArrayList<>(admins);
			sec.set(ADMINS, adminsStringList);
			used = true;
		}

		final Set<String> users = region.getUsers();
		if (users != null) {
			final List<String> usersStringList = new ArrayList<>(users);
			sec.set(USERS, usersStringList);
			used = true;
		}

		final Set<String> allowedGroups = region.getAllowedGroups();
		if (allowedGroups != null) {
			final List<String> allowedGroupsStringList = new ArrayList<>(allowedGroups);
			sec.set(ALLOWED_GROUPS, allowedGroupsStringList);
			used = true;
		}

		final Set<String> disallowedCommands = region.getDisallowedCommands();
		if (disallowedCommands != null) {
			final List<String> disallowedCommandsStringList = new ArrayList<>(disallowedCommands);
			sec.set(DISALLOWED_COMMANDS, disallowedCommandsStringList);
			used = true;
		}

		if (!used) {
			parent.set(RIGHTS, null);
		}
	}
}
