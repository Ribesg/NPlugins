/***************************************************************************
 * Project file:    NPlugins - NCuboid - RegionDbPersistenceHandler.java   *
 * Full Class name: fr.ribesg.bukkit.ncuboid.beans.RegionDbPersistenceHandler
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.beans;

import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.config.UuidDb;
import fr.ribesg.bukkit.ncore.util.PlayerIdsUtil;
import fr.ribesg.bukkit.ncore.util.StringUtil;
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
import java.util.UUID;
import java.util.logging.Level;

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
	private static final String OWNER_ID   = "owner";
	private static final String TOTAL_SIZE = "totalSize";
	private static final String TYPE       = "type";

	// CUBOID attributes
	private static final String MIN_CORNER = "minCorner";
	private static final String MAX_CORNER = "maxCorner";

	// Common sub-sections
	private static final String FLAGS      = "flags";
	private static final String ATTRIBUTES = "attributes";
	private static final String RIGHTS     = "rights";

	// For rights
	private static final String ADMINS              = "admins";
	private static final String USERS               = "users";
	private static final String ALLOWED_GROUPS      = "allowedGroups";
	private static final String DISALLOWED_COMMANDS = "disallowedCommands";

	private static NCuboid plugin;

	public static RegionDb reloadDb(final NCuboid instance) {
		plugin = instance;
		final RegionDb oldDb = plugin.getDb();
		try {
			return loadDb(plugin);
		} catch (final IOException | InvalidConfigurationException e) {
			plugin.error("An error occured when NCuboid tried to reload regionDB.yml", e);
			plugin.info("No cuboid has been changed.");
			return oldDb;
		}
	}

	public static RegionDb loadDb(final NCuboid instance) throws IOException, InvalidConfigurationException {
		plugin = instance;

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
					if (region != null) {
						db.add(region);
					}
				}
			}

			plugin.getJails().loadJails(db, config);

			return db;
		}
	}

	private static WorldRegion readWorldRegion(final ConfigurationSection parent, final String name) throws InvalidConfigurationException {
		final ConfigurationSection worldSection = parent.getConfigurationSection(name);

		final int priority = worldSection.getInt(PRIORITY, 0);

		final Flags flags = readFlags(worldSection);
		final Attributes attributes = readAttributes(worldSection);
		final Rights rights = readRights(worldSection);

		return new WorldRegion(name, rights, priority, flags, attributes);
	}

	private static PlayerRegion readPlayerRegion(final ConfigurationSection parent, final String name) throws InvalidConfigurationException {
		final ConfigurationSection playerSection = parent.getConfigurationSection(name);

		final String ownerIdString = playerSection.getString(OWNER_ID);
		UUID ownerId = null;
		if (PlayerIdsUtil.isValidUuid(ownerIdString)) {
			ownerId = UUID.fromString(ownerIdString);
		} else if (PlayerIdsUtil.isValidMinecraftUserName(ownerIdString)) {
			ownerId = UuidDb.getId(ownerIdString, true);
		}
		if (ownerId == null) {
			plugin.error(Level.WARNING, "Unknown ownerId '" + ownerIdString + "' found in regionDb.yml under section '" + playerSection.getCurrentPath() + "', ignored");
			return null;
		}
		final String worldName = playerSection.getString(WORLD_NAME);
		final long totalSize = playerSection.getLong(TOTAL_SIZE);
		final int priority = playerSection.getInt(PRIORITY, 0);

		final GeneralRegion.RegionType type = GeneralRegion.RegionType.valueOf(playerSection.getString(TYPE));

		// First load non-specific things that may be written after the specific stuff
		final Flags flags = readFlags(playerSection);
		final Attributes attributes = readAttributes(playerSection);
		final Rights rights = readRights(playerSection);

		// Read specific stuff and return corresponding Region
		switch (type) {
			case CUBOID:
				final NLocation minCorner = NLocation.toNLocation(playerSection.getString(MIN_CORNER));
				final NLocation maxCorner = NLocation.toNLocation(playerSection.getString(MAX_CORNER));
				return new CuboidRegion(name, ownerId, worldName, PlayerRegion.RegionState.NORMAL, totalSize, rights, priority, flags, attributes, minCorner, maxCorner);
			default:
				throw new UnsupportedOperationException();
		}
	}

	private static Flags readFlags(final ConfigurationSection sec) {
		final Flags flags = new Flags();
		if (sec.isList(FLAGS)) {
			final List<String> trueFlags = sec.getStringList(FLAGS);
			for (final Flag f : Flag.values()) {
				flags.setFlag(f, trueFlags.contains(f.name()));
			}
		}
		return flags;
	}

	private static Attributes readAttributes(final ConfigurationSection sec) {
		final Attributes attributes = new Attributes();
		if (sec.isConfigurationSection(ATTRIBUTES)) {
			final ConfigurationSection attributesSection = sec.getConfigurationSection(ATTRIBUTES);
			for (final Attribute att : Attribute.values()) {
				if (Attribute.isStringAttribute(att)) {
					final String theString = attributesSection.getString(att.toString(), null);
					if (theString != null) {
						attributes.setStringAttribute(att, theString);
					}
				} else if (Attribute.isIntegerAttribute(att)) {
					final Integer theInteger = attributesSection.getInt(att.toString(), Integer.MIN_VALUE);
					if (theInteger != Integer.MIN_VALUE) {
						attributes.setIntegerAttribute(att, theInteger);
					}
				} else if (Attribute.isLocationAttribute(att)) {
					final Location theLocation = NLocation.toLocation(attributesSection.getString(att.toString(), ""));
					if (theLocation != null) {
						attributes.setLocationAttribute(att, theLocation);
					}
				} else if (Attribute.isVectorAttribute(att)) {
					final Vector theVector = StringUtil.toVector(attributesSection.getString(att.toString(), ""));
					if (theVector != null) {
						attributes.setVectorAttribute(att, theVector);
					}
				} else {
					// Hello, future
				}
			}
		}

		return attributes;
	}

	private static Rights readRights(final ConfigurationSection sec) throws InvalidConfigurationException {
		final Rights rights = new Rights();
		if (sec.isConfigurationSection(RIGHTS)) {
			final ConfigurationSection rightsSection = sec.getConfigurationSection(RIGHTS);
			if (rightsSection.isList(ADMINS)) {
				final List<String> admins = rightsSection.getStringList(ADMINS);
				for (final String playerName : admins) {
					UUID id = null;
					if (PlayerIdsUtil.isValidUuid(playerName)) {
						id = UUID.fromString(playerName);
					} else if (PlayerIdsUtil.isValidMinecraftUserName(playerName)) {
						id = UuidDb.getId(playerName, true);
					}
					if (id == null) {
						plugin.error(Level.WARNING, "Unknown admin playerId '" + playerName + "' found in regionDb.yml under section '" + rightsSection.getCurrentPath() + "', ignored");
					} else {
						rights.addAdmin(id);
					}
				}
			}
			if (rightsSection.isList(USERS)) {
				final List<String> users = rightsSection.getStringList(USERS);
				for (final String playerName : users) {
					UUID id = null;
					if (PlayerIdsUtil.isValidUuid(playerName)) {
						id = UUID.fromString(playerName);
					} else if (PlayerIdsUtil.isValidMinecraftUserName(playerName)) {
						id = UuidDb.getId(playerName, true);
					}
					if (id == null) {
						plugin.error(Level.WARNING, "Unknown user playerId '" + playerName + "' found in regionDb.yml under section '" + rightsSection.getCurrentPath() + "', ignored");
					} else {
						rights.addUser(id);
					}
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

		plugin.getJails().saveJails(config);

		final String configString = config.saveToString();
		try (BufferedWriter writer = Files.newBufferedWriter(regionDbConfigFile, CHARSET, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
			writer.write(configString);
		}
	}

	private static void writeWorldRegion(final ConfigurationSection parent, final WorldRegion region) {
		final ConfigurationSection sec = parent.createSection(region.getWorldName());
		sec.set(PRIORITY, region.getPriority());
		writeFlags(sec, region);
		writeAttributes(sec, region);
		writeRights(sec, region);
	}

	private static void writePlayerRegion(final ConfigurationSection parent, final PlayerRegion region) {
		final ConfigurationSection sec = parent.createSection(region.getRegionName());

		sec.set(OWNER_ID, region.getOwnerId());
		sec.set(WORLD_NAME, region.getWorldName());
		sec.set(TOTAL_SIZE, region.getTotalSize());
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
		writeAttributes(sec, region);
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

	private static void writeAttributes(final ConfigurationSection parent, final GeneralRegion region) {
		final ConfigurationSection sec = parent.createSection(ATTRIBUTES);
		boolean used = false;

		for (final Attribute att : Attribute.values()) {
			if (Attribute.isStringAttribute(att)) {
				final String theString = region.getStringAttribute(att);
				if (theString != null) {
					used = true;
					sec.set(att.toString(), theString);
				}
			} else if (Attribute.isIntegerAttribute(att)) {
				final Integer theInteger = region.getIntegerAttribute(att);
				if (theInteger != null) {
					used = true;
					sec.set(att.toString(), theInteger);
				}
			} else if (Attribute.isLocationAttribute(att)) {
				final Location theLocation = region.getLocationAttribute(att);
				if (theLocation != null) {
					used = true;
					sec.set(att.toString(), NLocation.toStringPlus(theLocation));
				}
			} else if (Attribute.isVectorAttribute(att)) {
				final Vector theVector = region.getVectorAttribute(att);
				if (theVector != null) {
					used = true;
					sec.set(att.toString(), StringUtil.toString(theVector));
				}
			} else {
				// Hello, future
			}
		}

		if (!used) {
			parent.set(ATTRIBUTES, null);
		}
	}

	private static void writeRights(final ConfigurationSection parent, final GeneralRegion region) {
		final ConfigurationSection sec = parent.createSection(RIGHTS);
		boolean used = false;

		final Set<UUID> admins = region.getAdmins();
		if (admins != null) {
			final List<String> adminsStringList = new ArrayList<>();
			for (final UUID id : admins) {
				adminsStringList.add(id.toString());
			}
			sec.set(ADMINS, adminsStringList);
			used = true;
		}

		final Set<UUID> users = region.getUsers();
		if (users != null) {
			final List<String> usersStringList = new ArrayList<>();
			for (final UUID id : users) {
				usersStringList.add(id.toString());
			}
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
