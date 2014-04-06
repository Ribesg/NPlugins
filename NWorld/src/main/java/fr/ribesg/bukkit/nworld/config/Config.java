/***************************************************************************
 * Project file:    NPlugins - NWorld - Config.java                        *
 * Full Class name: fr.ribesg.bukkit.nworld.config.Config                  *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nworld.config;

import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.config.AbstractConfig;
import fr.ribesg.bukkit.ncore.utils.FrameBuilder;
import fr.ribesg.bukkit.nworld.NWorld;
import fr.ribesg.bukkit.nworld.warp.Warp;
import fr.ribesg.bukkit.nworld.warp.Warps;
import fr.ribesg.bukkit.nworld.world.AdditionalSubWorld;
import fr.ribesg.bukkit.nworld.world.AdditionalWorld;
import fr.ribesg.bukkit.nworld.world.GeneralWorld;
import fr.ribesg.bukkit.nworld.world.StockWorld;
import fr.ribesg.bukkit.nworld.world.Worlds;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Config extends AbstractConfig<NWorld> {

	private final Logger log;

	// General
	private       int                 spawnCommandBehaviour;
	private       String              defaultRequiredPermission;
	private       boolean             defaultHidden;
	private final Map<String, String> permissionShortcuts;

	// Messages
	private int broadcastOnWorldCreate;
	private int broadcastOnWorldLoad;
	private int broadcastOnWorldUnload;

	// Worlds
	private final Worlds worlds;

	// Warps
	private final Warps warps;

	public Config(final fr.ribesg.bukkit.nworld.NWorld instance) {
		super(instance);
		log = instance.getLogger();
		permissionShortcuts = new HashMap<>();
		worlds = plugin.getWorlds();
		warps = plugin.getWarps();

		// Set default values for first use
		spawnCommandBehaviour = 1;
		defaultRequiredPermission = "group.admin";
		defaultHidden = true;
		broadcastOnWorldCreate = 0;
		broadcastOnWorldLoad = 0;
		broadcastOnWorldUnload = 0;
	}

	/**
	 * @see fr.ribesg.bukkit.ncore.config.AbstractConfig#handleValues(org.bukkit.configuration.file.YamlConfiguration)
	 */
	@Override
	protected void handleValues(final YamlConfiguration config) throws InvalidConfigurationException {

		// #############
		// ## General ##
		// #############

		// spawnCommandBehaviour. Default: 1.
		// Possible values: 0,1
		setSpawnCommandBehaviour(config.getInt("spawnCommandBehaviour", 1));
		if (getSpawnCommandBehaviour() < 0 || getSpawnCommandBehaviour() > 1) {
			wrongValue("config.yml", "spawnCommandBehaviour", getSpawnCommandBehaviour(), 0);
			setSpawnCommandBehaviour(0);
		}

		// defaultRequiredPermission. Default: group.admin
		setDefaultRequiredPermission(config.getString("defaultRequiredPermission", "group.admin"));

		// defaultHidden. Default: true
		setDefaultHidden(config.getBoolean("defaultHidden", true));

		// ##############
		// ## Messages ##
		// ##############

		// broadcastOnWorldCreate. Default: 0.
		// Possible values: 0,1
		setBroadcastOnWorldCreate(config.getInt("broadcastOnWorldCreate", 0));
		if (getBroadcastOnWorldCreate() < 0 || getBroadcastOnWorldCreate() > 1) {
			wrongValue("config.yml", "broadcastOnWorldCreate", getBroadcastOnWorldCreate(), 0);
			setBroadcastOnWorldCreate(0);
		}

		// broadcastOnWorldLoad. Default: 0.
		// Possible values: 0,1
		setBroadcastOnWorldLoad(config.getInt("broadcastOnWorldLoad", 0));
		if (getBroadcastOnWorldLoad() < 0 || getBroadcastOnWorldLoad() > 1) {
			wrongValue("config.yml", "broadcastOnWorldLoad", getBroadcastOnWorldLoad(), 0);
			setBroadcastOnWorldLoad(0);
		}

		// broadcastOnWorldUnload. Default: 0.
		// Possible values: 0,1
		setBroadcastOnWorldUnload(config.getInt("broadcastOnWorldUnload", 0));
		if (getBroadcastOnWorldUnload() < 0 || getBroadcastOnWorldUnload() > 1) {
			wrongValue("config.yml", "broadcastOnWorldUnload", getBroadcastOnWorldUnload(), 0);
			setBroadcastOnWorldUnload(0);
		}

		// ############
		// ## Worlds ##
		// ############

		final Map<String, GeneralWorld> worldsMap = new HashMap<>();
		if (config.isConfigurationSection("stockWorlds")) {
			final ConfigurationSection stockWorldsSection = config.getConfigurationSection("stockWorlds");
			for (final String worldName : stockWorldsSection.getKeys(false)) {
				final ConfigurationSection worldSection = stockWorldsSection.getConfigurationSection(worldName);
				final GeneralWorld.WorldType type = worldName.endsWith("_the_end") ? GeneralWorld.WorldType.STOCK_END : worldName.endsWith("_nether") ? GeneralWorld.WorldType.STOCK_NETHER : GeneralWorld.WorldType.STOCK;
				boolean malformedWorldSection = false;
				NLocation spawnLocation = null;
				String requiredPermission = null;
				final boolean enabled = Bukkit.getWorld(worldName) != null;
				Boolean hidden = null;
				if (!worldSection.isConfigurationSection("spawnLocation")) {
					malformedWorldSection = true;
					log.severe("Missing or invalid configuration value: stockWorlds." + worldName + ".spawnLocation");
				} else {
					final ConfigurationSection spawnSection = worldSection.getConfigurationSection("spawnLocation");
					if (!spawnSection.isDouble("x")) {
						malformedWorldSection = true;
						log.severe("Missing or invalid configuration value: stockWorlds." + worldName + ".spawnLocation.x");
					} else if (!spawnSection.isDouble("y")) {
						malformedWorldSection = true;
						log.severe("Missing or invalid configuration value: stockWorlds." + worldName + ".spawnLocation.y");
					} else if (!spawnSection.isDouble("z")) {
						malformedWorldSection = true;
						log.severe("Missing or invalid configuration value: stockWorlds." + worldName + ".spawnLocation.z");
					} else if (!spawnSection.isDouble("yaw")) {
						malformedWorldSection = true;
						log.severe("Missing or invalid configuration value: stockWorlds." + worldName + ".spawnLocation.yaw");
					} else if (!spawnSection.isDouble("pitch")) {
						malformedWorldSection = true;
						log.severe("Missing or invalid configuration value: stockWorlds." + worldName + ".spawnLocation.pitch");
					} else {
						final double x = spawnSection.getDouble("x");
						final double y = spawnSection.getDouble("y");
						final double z = spawnSection.getDouble("z");
						final float yaw = (float) spawnSection.getDouble("yaw");
						final float pitch = (float) spawnSection.getDouble("pitch");
						spawnLocation = new NLocation(worldName, x, y, z, yaw, pitch);
					}
				}
				if (!worldSection.isString("requiredPermission")) {
					malformedWorldSection = true;
					log.severe("Missing or invalid configuration value: stockWorlds." + worldName + ".requiredPermission");
				} else {
					requiredPermission = worldSection.getString("requiredPermission");
				}
				if (!worldSection.isBoolean("hidden")) {
					malformedWorldSection = true;
					log.severe("Missing or invalid configuration value: stockWorlds." + worldName + ".hidden");
				} else {
					hidden = worldSection.getBoolean("hidden");
				}
				if (!malformedWorldSection) {
					worldsMap.put(worldName, new StockWorld(plugin, worldName, type, spawnLocation, requiredPermission, enabled, hidden));
				} else {
					throw new InvalidConfigurationException("Malformed Configuration - Stopping everything");
				}
			}
		}
		if (config.isConfigurationSection("additionalWorlds")) {
			final ConfigurationSection additionalWorldsSection = config.getConfigurationSection("additionalWorlds");
			for (final String worldName : additionalWorldsSection.getKeys(false)) {
				final ConfigurationSection worldSection = additionalWorldsSection.getConfigurationSection(worldName);

				// If an error is found in the config
				boolean malformedWorldSection = false;

				// All variables to build the GeneralWorld objects

				// Main
				NLocation spawnLocation = null;
				Long seed = null;
				String requiredPermission = null;
				Boolean enabled = null;
				Boolean hidden = null;
				Boolean hasNether = null;
				Boolean hasEnd = null;

				// Nether
				NLocation netherSpawnLocation = null;
				String netherRequiredPermission = null;
				Boolean netherEnabled = null;
				Boolean netherHidden = null;

				// End
				NLocation endSpawnLocation = null;
				String endRequiredPermission = null;
				Boolean endEnabled = null;
				Boolean endHidden = null;

				if (!worldSection.isConfigurationSection("spawnLocation")) {
					malformedWorldSection = true;
					log.severe("Missing or invalid configuration value: additionalWorlds." + worldName + ".spawnLocation");
				} else {
					final ConfigurationSection spawnSection = worldSection.getConfigurationSection("spawnLocation");
					if (!spawnSection.isDouble("x")) {
						malformedWorldSection = true;
						log.severe("Missing or invalid configuration value: additionalWorlds." + worldName + ".spawnLocation.x");
					} else if (!spawnSection.isDouble("y")) {
						malformedWorldSection = true;
						log.severe("Missing or invalid configuration value: additionalWorlds." + worldName + ".spawnLocation.y");
					} else if (!spawnSection.isDouble("z")) {
						malformedWorldSection = true;
						log.severe("Missing or invalid configuration value: additionalWorlds." + worldName + ".spawnLocation.z");
					} else if (!spawnSection.isDouble("yaw")) {
						malformedWorldSection = true;
						log.severe("Missing or invalid configuration value: additionalWorlds." + worldName + ".spawnLocation.yaw");
					} else if (!spawnSection.isDouble("pitch")) {
						malformedWorldSection = true;
						log.severe("Missing or invalid configuration value: additionalWorlds." + worldName + ".spawnLocation.pitch");
					} else {
						final double x = spawnSection.getDouble("x");
						final double y = spawnSection.getDouble("y");
						final double z = spawnSection.getDouble("z");
						final float yaw = (float) spawnSection.getDouble("yaw");
						final float pitch = (float) spawnSection.getDouble("pitch");
						spawnLocation = new NLocation(worldName, x, y, z, yaw, pitch);
					}
				}
				if (!worldSection.isLong("seed") && !worldSection.isInt("seed")) {
					malformedWorldSection = true;
					log.severe("Missing or invalid configuration value: additionalWorlds." + worldName + ".seed");
				} else {
					seed = worldSection.getLong("seed");
				}
				if (!worldSection.isString("requiredPermission")) {
					malformedWorldSection = true;
					log.severe("Missing or invalid configuration value: additionalWorlds." + worldName + ".requiredPermission");
				} else {
					requiredPermission = worldSection.getString("requiredPermission");
				}
				if (!worldSection.isBoolean("enabled")) {
					malformedWorldSection = true;
					log.severe("Missing or invalid configuration value: additionalWorlds." + worldName + ".enabled");
				} else {
					enabled = worldSection.getBoolean("enabled");
				}
				if (!worldSection.isBoolean("hidden")) {
					malformedWorldSection = true;
					log.severe("Missing or invalid configuration value: additionalWorlds." + worldName + ".hidden");
				} else {
					hidden = worldSection.getBoolean("hidden");
				}
				if (!worldSection.isBoolean("hasNether")) {
					malformedWorldSection = true;
					log.severe("Missing or invalid configuration value: additionalWorlds." + worldName + ".hasNether");
				} else {
					hasNether = worldSection.getBoolean("hasNether");
				}
				if (!worldSection.isConfigurationSection("netherWorld")) {
					malformedWorldSection = true;
					log.severe("Missing or invalid configuration section: additionalWorlds." + worldName + ".netherWorld");
				} else {
					final ConfigurationSection netherSection = worldSection.getConfigurationSection("netherWorld");
					if (!netherSection.isConfigurationSection("spawnLocation")) {
						malformedWorldSection = true;
						log.severe("Missing or invalid configuration value: additionalWorlds." + worldName + ".netherWorld.spawnLocation");
					} else {
						final ConfigurationSection spawnSection = netherSection.getConfigurationSection("spawnLocation");
						if (!spawnSection.isDouble("x")) {
							malformedWorldSection = true;
							log.severe("Missing or invalid configuration value: additionalWorlds." +
							           worldName +
							           ".netherWorld.spawnLocation.x");
						} else if (!spawnSection.isDouble("y")) {
							malformedWorldSection = true;
							log.severe("Missing or invalid configuration value: additionalWorlds." +
							           worldName +
							           ".netherWorld.spawnLocation.y");
						} else if (!spawnSection.isDouble("z")) {
							malformedWorldSection = true;
							log.severe("Missing or invalid configuration value: additionalWorlds." +
							           worldName +
							           ".netherWorld.spawnLocation.z");
						} else if (!spawnSection.isDouble("yaw")) {
							malformedWorldSection = true;
							log.severe("Missing or invalid configuration value: additionalWorlds." +
							           worldName +
							           ".netherWorld.spawnLocation.yaw");
						} else if (!spawnSection.isDouble("pitch")) {
							malformedWorldSection = true;
							log.severe("Missing or invalid configuration value: additionalWorlds." +
							           worldName +
							           ".netherWorld.spawnLocation.pitch");
						} else {
							final double x = spawnSection.getDouble("x");
							final double y = spawnSection.getDouble("y");
							final double z = spawnSection.getDouble("z");
							final float yaw = (float) spawnSection.getDouble("yaw");
							final float pitch = (float) spawnSection.getDouble("pitch");
							netherSpawnLocation = new NLocation(worldName + "_nether", x, y, z, yaw, pitch);
						}
					}
					if (!netherSection.isString("requiredPermission")) {
						malformedWorldSection = true;
						log.severe("Missing or invalid configuration value: additionalWorlds." +
						           worldName +
						           ".netherWorld.requiredPermission");
					} else {
						netherRequiredPermission = netherSection.getString("requiredPermission");
					}
					if (!netherSection.isBoolean("enabled")) {
						malformedWorldSection = true;
						log.severe("Missing or invalid configuration value: additionalWorlds." + worldName + ".netherWorld.enabled");
					} else {
						netherEnabled = netherSection.getBoolean("enabled");
					}
					if (!netherSection.isBoolean("hidden")) {
						malformedWorldSection = true;
						log.severe("Missing or invalid configuration value: additionalWorlds." + worldName + ".netherWorld.hidden");
					} else {
						netherHidden = netherSection.getBoolean("hidden");
					}
				}
				if (!worldSection.isBoolean("hasEnd")) {
					malformedWorldSection = true;
					log.severe("Missing or invalid configuration value: additionalWorlds." + worldName + ".hasEnd");
				} else {
					hasEnd = worldSection.getBoolean("hasEnd");
				}
				if (!worldSection.isConfigurationSection("endWorld")) {
					malformedWorldSection = true;
					log.severe("Missing or invalid configuration section: additionalWorlds." + worldName + ".endWorld");
				} else {
					final ConfigurationSection endSection = worldSection.getConfigurationSection("endWorld");
					if (!endSection.isConfigurationSection("spawnLocation")) {
						malformedWorldSection = true;
						log.severe("Missing or invalid configuration value: additionalWorlds." + worldName + ".endWorld.spawnLocation");
					} else {
						final ConfigurationSection spawnSection = endSection.getConfigurationSection("spawnLocation");
						if (!spawnSection.isDouble("x")) {
							malformedWorldSection = true;
							log.severe("Missing or invalid configuration value: additionalWorlds." +
							           worldName +
							           ".endWorld.spawnLocation.x");
						} else if (!spawnSection.isDouble("y")) {
							malformedWorldSection = true;
							log.severe("Missing or invalid configuration value: additionalWorlds." +
							           worldName +
							           ".endWorld.spawnLocation.y");
						} else if (!spawnSection.isDouble("z")) {
							malformedWorldSection = true;
							log.severe("Missing or invalid configuration value: additionalWorlds." +
							           worldName +
							           ".endWorld.spawnLocation.z");
						} else if (!spawnSection.isDouble("yaw")) {
							malformedWorldSection = true;
							log.severe("Missing or invalid configuration value: additionalWorlds." +
							           worldName +
							           ".endWorld.spawnLocation.yaw");
						} else if (!spawnSection.isDouble("pitch")) {
							malformedWorldSection = true;
							log.severe("Missing or invalid configuration value: additionalWorlds." +
							           worldName +
							           ".endWorld.spawnLocation.pitch");
						} else {
							final double x = spawnSection.getDouble("x");
							final double y = spawnSection.getDouble("y");
							final double z = spawnSection.getDouble("z");
							final float yaw = (float) spawnSection.getDouble("yaw");
							final float pitch = (float) spawnSection.getDouble("pitch");
							endSpawnLocation = new NLocation(worldName + "_the_end", x, y, z, yaw, pitch);
						}
					}
					if (!endSection.isString("requiredPermission")) {
						malformedWorldSection = true;
						log.severe("Missing or invalid configuration value: additionalWorlds." +
						           worldName +
						           ".endWorld.requiredPermission");
					} else {
						endRequiredPermission = endSection.getString("requiredPermission");
					}
					if (!endSection.isBoolean("enabled")) {
						malformedWorldSection = true;
						log.severe("Missing or invalid configuration value: additionalWorlds." + worldName + ".endWorld.enabled");
					} else {
						endEnabled = endSection.getBoolean("enabled");
					}
					if (!endSection.isBoolean("hidden")) {
						malformedWorldSection = true;
						log.severe("Missing or invalid configuration value: additionalWorlds." + worldName + ".endWorld.hidden");
					} else {
						endHidden = endSection.getBoolean("hidden");
					}
				}
				if (!malformedWorldSection) {
					final AdditionalWorld world = new AdditionalWorld(plugin, worldName, seed, spawnLocation, requiredPermission, enabled, hidden, hasNether, hasEnd);
					worldsMap.put(worldName, world);
					if (hasNether) {
						final AdditionalSubWorld nether = new AdditionalSubWorld(plugin, world, netherSpawnLocation, netherRequiredPermission, netherEnabled, netherHidden, World.Environment.NETHER);
						worldsMap.put(worldName + "_nether", nether);
					}

					if (hasEnd) {
						final AdditionalSubWorld end = new AdditionalSubWorld(plugin, world, endSpawnLocation, endRequiredPermission, endEnabled, endHidden, World.Environment.THE_END);
						worldsMap.put(worldName + "_the_end", end);
					}
				} else {
					throw new InvalidConfigurationException("Malformed Configuration - Stopping everything");
				}
			}
		}
		worlds.putAll(worldsMap);

		// ###########
		// ## Warps ##
		// ###########

		final Map<String, Warp> warpsMap = new HashMap<>();
		if (config.isConfigurationSection("warps")) {
			final ConfigurationSection warpsSection = config.getConfigurationSection("warps");
			for (final String warpName : warpsSection.getKeys(false)) {
				final ConfigurationSection warpSection = warpsSection.getConfigurationSection(warpName);
				boolean malformedWarpSection = false;
				NLocation location = null;
				String requiredPermission = null;
				Boolean hidden = null;
				if (!warpSection.isConfigurationSection("location")) {
					malformedWarpSection = true;
					log.severe("Missing or invalid configuration value: warps." + warpName + ".location");
				} else {
					final ConfigurationSection locationSection = warpSection.getConfigurationSection("location");
					if (!locationSection.isString("worldName")) {
						malformedWarpSection = true;
						log.severe("Missing or invalid configuration value: warps." + warpName + ".location.worldName");
					}
					if (!locationSection.isDouble("x")) {
						malformedWarpSection = true;
						log.severe("Missing or invalid configuration value: warps." + warpName + ".location.x");
					}
					if (!locationSection.isDouble("y")) {
						malformedWarpSection = true;
						log.severe("Missing or invalid configuration value: warps." + warpName + ".location.y");
					}
					if (!locationSection.isDouble("z")) {
						malformedWarpSection = true;
						log.severe("Missing or invalid configuration value: warps." + warpName + ".location.z");
					}
					if (!locationSection.isDouble("yaw")) {
						malformedWarpSection = true;
						log.severe("Missing or invalid configuration value: warps." + warpName + ".location.yaw");
					}
					if (!locationSection.isDouble("pitch")) {
						malformedWarpSection = true;
						log.severe("Missing or invalid configuration value: warps." + warpName + ".location.pitch");
					}
					if (!malformedWarpSection) {
						final String worldName = locationSection.getString("worldName");
						final double x = locationSection.getDouble("x");
						final double y = locationSection.getDouble("y");
						final double z = locationSection.getDouble("z");
						final float yaw = (float) locationSection.getDouble("yaw");
						final float pitch = (float) locationSection.getDouble("pitch");
						location = new NLocation(worldName, x, y, z, yaw, pitch);
					}
				}
				if (!warpSection.isString("requiredPermission")) {
					malformedWarpSection = true;
					log.severe("Missing or invalid configuration value: warps." + warpName + ".requiredPermission");
				} else {
					requiredPermission = warpSection.getString("requiredPermission");
				}
				if (!warpSection.isBoolean("hidden")) {
					malformedWarpSection = true;
					log.severe("Missing or invalid configuration value: warps." + warpName + ".hidden");
				} else {
					hidden = warpSection.getBoolean("hidden");
				}
				if (!malformedWarpSection) {
					warpsMap.put(warpName, new Warp(warpName, location, false, requiredPermission, hidden));
				} else {
					throw new InvalidConfigurationException("Malformed Configuration - Stopping everything");
				}
			}
		}
		warps.putAll(warpsMap);
	}

	/**
	 * @see AbstractConfig#getConfigString()
	 */
	@Override
	protected String getConfigString() {
		final StringBuilder content = new StringBuilder();
		FrameBuilder frame;

		// Header
		frame = new FrameBuilder();
		frame.addLine("Config file for NWorld plugin", FrameBuilder.Option.CENTER);
		frame.addLine("If you don't understand something, please ask on dev.bukkit.org");
		frame.addLine("Ribesg", FrameBuilder.Option.RIGHT);
		for (final String line : frame.build()) {
			content.append(line + '\n');
		}
		content.append('\n');

		// #############
		// ## General ##
		// #############

		frame = new FrameBuilder();
		frame.addLine("General", FrameBuilder.Option.CENTER);
		content.append('\n');
		for (final String line : frame.build()) {
			content.append(line + '\n');
		}
		content.append('\n');

		// Spawn command behaviour
		content.append("# Configures the behaviour of the /spawn command. Two possibilities:\n");
		content.append("# - 0 = Teleports the player to the current world's spawn point\n");
		content.append("# - 1 = Teleports the player to a \"Global\" spawn point\n");
		content.append("# Default : 1\n");
		content.append("spawnCommandBehaviour: " + getSpawnCommandBehaviour() + "\n\n");

		// Default required permission
		content.append("# The default permission required to warp to a point, set if\n");
		content.append("# not provided in \"/nworld create\" or \"/setwarp\" commands\n");
		content.append("# Default : group.admin\n");
		content.append("defaultRequiredPermission: \"" + getDefaultRequiredPermission() + "\"\n\n");

		// Default hidden value
		content.append("# The default value for any new World or Warp created, set if\n");
		content.append("# not provided in \"/nworld create\" or \"/setwarp\" commands\n");
		content.append("# Default : true\n");
		content.append("defaultHidden: " + isDefaultHidden() + "\n\n");

		// Permission shortcuts
		content.append("# You can use those shortcuts when setting a required Permission\n");
		content.append("# to a world or a warp >>> VIA IN GAME COMMANDS <<< /!\\\n");
		content.append("# Note: every key should be lowercase.\n");
		content.append("permissionShortcuts:\n");
		if (!permissionShortcuts.containsKey("user")) {
			content.append("  user: \"group.user\"\n");
		}
		if (!permissionShortcuts.containsKey("admin")) {
			content.append("  admin: \"group.admin\"\n");
		}
		for (final Map.Entry<String, String> e : permissionShortcuts.entrySet()) {
			content.append("  " + e.getKey() + ": \"" + e.getValue() + "\"\n");
		}

		// ##############
		// ## Messages ##
		// ##############

		frame = new FrameBuilder();
		frame.addLine("Messages", FrameBuilder.Option.CENTER);
		content.append('\n');
		for (final String line : frame.build()) {
			content.append(line + '\n');
		}
		content.append('\n');

		// Broadcast on world creation
		content.append("# Do we broadcast a message on World creation. Possible values: 0,1\n");
		content.append("# Default : 0\n");
		content.append("broadcastOnWorldCreate: " + getBroadcastOnWorldCreate() + "\n\n");

		// Broadcast on world load
		content.append("# Do we broadcast a message on World load. Possible values: 0,1\n");
		content.append("# Default : 0\n");
		content.append("broadcastOnWorldLoad: " + getBroadcastOnWorldLoad() + "\n\n");

		// Broadcast on world unload
		content.append("# Do we broadcast a message on World unload. Possible values: 0,1\n");
		content.append("# Default : 0\n");
		content.append("broadcastOnWorldUnload: " + getBroadcastOnWorldUnload() + "\n\n");

		// ############
		// ## Worlds ##
		// ############

		frame = new FrameBuilder();
		frame.addLine("Worlds", FrameBuilder.Option.CENTER);
		content.append('\n');
		for (final String line : frame.build()) {
			content.append(line + '\n');
		}
		content.append('\n');

		content.append("# Values for stock worlds (Handled by Bukkit config files)\n");
		content.append("# - Spawn Location\n");
		content.append("# - Required Permission for direct warp to this world's spawn point\n");
		content.append("# - If this world will be hidden to those that are not allowed to directly warp to it\n");
		content.append("stockWorlds:\n");
		for (final StockWorld w : worlds.getStock().values()) {
			content.append("  \"" + w.getWorldName() + "\":\n");
			content.append("    spawnLocation:\n");
			content.append("      x: " + w.getSpawnLocation().getX() + "\n");
			content.append("      y: " + w.getSpawnLocation().getY() + "\n");
			content.append("      z: " + w.getSpawnLocation().getZ() + "\n");
			content.append("      yaw: " + w.getSpawnLocation().getYaw() + "\n");
			content.append("      pitch: " + w.getSpawnLocation().getPitch() + "\n");
			content.append("    requiredPermission: \"" + w.getRequiredPermission() + "\"\n");
			content.append("    hidden: " + w.isHidden() + "\n");
		}
		content.append("\n");

		content.append("# Values for additional worlds\n");
		content.append("# - Spawn Location\n");
		content.append("# - Required Permission for direct warp to this world's spawn point\n");
		content.append("# - If this world:\n");
		content.append("#   * was loaded at last server stop\n");
		content.append("#   * will be loaded at next server start\n");
		content.append("# - If this world will be hidden to those that are not allowed to directly warp to it\n");
		content.append("# - If this world has an associated Nether world, and the associated parameters\n");
		content.append("# - If this world has an associated End world, and the associated parameters\n");
		content.append("additionalWorlds:\n");
		for (final AdditionalWorld w : worlds.getAdditional().values()) {
			content.append("  \"" + w.getWorldName() + "\":\n");
			content.append("    spawnLocation:\n");
			content.append("      x: " + w.getSpawnLocation().getX() + "\n");
			content.append("      y: " + w.getSpawnLocation().getY() + "\n");
			content.append("      z: " + w.getSpawnLocation().getZ() + "\n");
			content.append("      yaw: " + w.getSpawnLocation().getYaw() + "\n");
			content.append("      pitch: " + w.getSpawnLocation().getPitch() + "\n");
			content.append("    seed: " + w.getSeed() + "\n");
			content.append("    requiredPermission: \"" + w.getRequiredPermission() + "\"\n");
			content.append("    enabled: " + w.isEnabled() + "\n");
			content.append("    hidden: " + w.isHidden() + "\n");
			content.append("    hasNether: " + w.hasNether() + "\n");
			content.append("    netherWorld: # Ignore this category if this world has no Nether sub-world\n");
			content.append("      spawnLocation:\n");
			content.append("        x: " + w.getSpawnLocation().getX() + "\n");
			content.append("        y: " + w.getSpawnLocation().getY() + "\n");
			content.append("        z: " + w.getSpawnLocation().getZ() + "\n");
			content.append("        yaw: " + w.getSpawnLocation().getYaw() + "\n");
			content.append("        pitch: " + w.getSpawnLocation().getPitch() + "\n");
			content.append("      requiredPermission: \"" + w.getRequiredPermission() + "\"\n");
			content.append("      enabled: " + w.isEnabled() + "\n");
			content.append("      hidden: " + w.isHidden() + "\n");
			content.append("    hasEnd: " + w.hasEnd() + "\n");
			content.append("    endWorld: # Ignore this category if this world has no End sub-world\n");
			content.append("      spawnLocation:\n");
			content.append("        x: " + w.getSpawnLocation().getX() + "\n");
			content.append("        y: " + w.getSpawnLocation().getY() + "\n");
			content.append("        z: " + w.getSpawnLocation().getZ() + "\n");
			content.append("        yaw: " + w.getSpawnLocation().getYaw() + "\n");
			content.append("        pitch: " + w.getSpawnLocation().getPitch() + "\n");
			content.append("      requiredPermission: \"" + w.getRequiredPermission() + "\"\n");
			content.append("      enabled: " + w.isEnabled() + "\n");
			content.append("      hidden: " + w.isHidden() + "\n");
		}

		// ###########
		// ## Warps ##
		// ###########

		frame = new FrameBuilder();
		frame.addLine("Warps", FrameBuilder.Option.CENTER);
		content.append('\n');
		for (final String line : frame.build()) {
			content.append(line + '\n');
		}
		content.append('\n');

		content.append("# Everything warp related\n");
		content.append("# - Location\n");
		content.append("# - Required Permission to warp\n");
		content.append("# - If it is hidden to those who do not have the required permission\n");
		content.append("warps:\n");
		for (final Warp w : warps) {
			content.append("  \"" + w.getName() + "\":\n");
			content.append("    location:\n");
			content.append("      worldName: " + w.getLocation().getWorldName() + "\n");
			content.append("      x: " + w.getLocation().getX() + "\n");
			content.append("      y: " + w.getLocation().getY() + "\n");
			content.append("      z: " + w.getLocation().getZ() + "\n");
			content.append("      yaw: " + w.getLocation().getYaw() + "\n");
			content.append("      pitch: " + w.getLocation().getPitch() + "\n");
			content.append("    requiredPermission: \"" + w.getRequiredPermission() + "\"\n");
			content.append("    hidden: " + w.isHidden() + "\n");
		}

		return content.toString();
	}

	// Getters and Setters for config values

	public int getSpawnCommandBehaviour() {
		return spawnCommandBehaviour;
	}

	public void setSpawnCommandBehaviour(final int spawnCommandBehaviour) {
		this.spawnCommandBehaviour = spawnCommandBehaviour;
	}

	public boolean isDefaultHidden() {
		return defaultHidden;
	}

	public void setDefaultHidden(final boolean defaultHidden) {
		this.defaultHidden = defaultHidden;
	}

	public String getDefaultRequiredPermission() {
		return defaultRequiredPermission;
	}

	public void setDefaultRequiredPermission(final String defaultRequiredPermission) {
		this.defaultRequiredPermission = defaultRequiredPermission;
	}

	public int getBroadcastOnWorldCreate() {
		return broadcastOnWorldCreate;
	}

	private void setBroadcastOnWorldCreate(final int broadcastOnWorldCreate) {
		this.broadcastOnWorldCreate = broadcastOnWorldCreate;
	}

	public int getBroadcastOnWorldLoad() {
		return broadcastOnWorldLoad;
	}

	private void setBroadcastOnWorldLoad(final int broadcastOnWorldLoad) {
		this.broadcastOnWorldLoad = broadcastOnWorldLoad;
	}

	public int getBroadcastOnWorldUnload() {
		return broadcastOnWorldUnload;
	}

	private void setBroadcastOnWorldUnload(final int broadcastOnWorldUnload) {
		this.broadcastOnWorldUnload = broadcastOnWorldUnload;
	}

	// Getters for worlds and warps objects

	public Warps getWarps() {
		return warps;
	}

	public Worlds getWorlds() {
		return worlds;
	}

	// Permissions shortcuts

	public Map<String, String> getPermissionShortcuts() {
		return permissionShortcuts;
	}
}
