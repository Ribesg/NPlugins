/***************************************************************************
 * Project file:    NPlugins - NPermissions - NPermissions.java            *
 * Full Class name: fr.ribesg.bukkit.npermissions.NPermissions             *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.npermissions;

import fr.ribesg.bukkit.ncore.node.NPlugin;
import fr.ribesg.bukkit.ncore.node.permissions.PermissionsNode;
import fr.ribesg.bukkit.npermissions.config.Config;
import fr.ribesg.bukkit.npermissions.config.Groups;
import fr.ribesg.bukkit.npermissions.config.Players;
import fr.ribesg.bukkit.npermissions.config.WorldGroups;
import fr.ribesg.bukkit.npermissions.config.WorldPlayers;
import fr.ribesg.bukkit.npermissions.lang.Messages;
import fr.ribesg.bukkit.npermissions.permission.PermissionsManager;
import fr.ribesg.bukkit.npermissions.permission.WorldGroupPermissions;
import fr.ribesg.bukkit.npermissions.permission.WorldLegacyPlayerPermissions;
import fr.ribesg.bukkit.npermissions.permission.WorldPlayerPermissions;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class NPermissions extends NPlugin implements PermissionsNode {

	// Configs
	private Messages                  messages;
	private Config                    pluginConfig;
	private Groups                    groupsConfig;
	private Players                   playersConfig;
	private Map<String, WorldGroups>  worldGroupsConfigs;
	private Map<String, WorldPlayers> worldPlayersConfigs;

	// Useful Nodes
	// // None

	// Permissions
	private PermissionsManager manager;

	@Override
	protected String getMinCoreVersion() {
		return "0.6.4";
	}

	@Override
	protected void loadMessages() throws IOException {
		debug("Loading plugin Messages...");
		if (!getDataFolder().isDirectory()) {
			getDataFolder().mkdir();
		}

		final Messages messages = new Messages();
		messages.loadMessages(this);

		this.messages = messages;
	}

	@Override
	public boolean onNodeEnable() {
		// Config
		try {
			this.pluginConfig = new Config(this);
			this.pluginConfig.loadConfig();
		} catch (final IOException | InvalidConfigurationException e) {
			error("An error occured when NPermissions tried to load config.yml", e);
			return false;
		}

		this.manager = new PermissionsManager(this);

		// Groups
		try {
			this.groupsConfig = new Groups(this);
			this.groupsConfig.loadConfig("groups.yml");
		} catch (final IOException | InvalidConfigurationException e) {
			error("An error occured when NPermissions tried to load groups.yml", e);
			return false;
		}

		// Players
		try {
			this.playersConfig = new Players(this);
			this.playersConfig.loadConfig("players.yml");
		} catch (final IOException | InvalidConfigurationException e) {
			error("An error occured when NPermissions tried to load players.yml", e);
			return false;
		}

		// World Groups & World Players
		this.worldGroupsConfigs = new LinkedHashMap<>();
		this.worldPlayersConfigs = new LinkedHashMap<>();
		for (final World world : Bukkit.getWorlds()) {
			// Groups
			try {
				this.manager.getWorldGroups().put(world.getName(), new LinkedHashMap<String, WorldGroupPermissions>());
				final WorldGroups worldGroupsConfig = new WorldGroups(this, world.getName());
				worldGroupsConfig.loadConfig(world.getName() + File.pathSeparator + "groups.yml");
				this.worldGroupsConfigs.put(world.getName(), worldGroupsConfig);
			} catch (final IOException | InvalidConfigurationException e) {
				error("An error occured when NPermissions tried to load " + world.getName() + "/groups.yml", e);
				return false;
			}

			// Players
			try {
				this.manager.getWorldPlayers().put(world.getName(), new LinkedHashMap<UUID, WorldPlayerPermissions>());
				this.manager.getWorldLegacyPlayers().put(world.getName(), new LinkedHashMap<String, WorldLegacyPlayerPermissions>());
				final WorldPlayers worldPlayersConfig = new WorldPlayers(this, world.getName());
				worldPlayersConfig.loadConfig(world.getName() + File.pathSeparator + "players.yml");
				this.worldPlayersConfigs.put(world.getName(), worldPlayersConfig);
			} catch (final IOException | InvalidConfigurationException e) {
				error("An error occured when NPermissions tried to load players.yml", e);
				return false;
			}
		}

		final PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new NListener(this), this);

		final NCommandExecutor executor = new NCommandExecutor(this);
		setCommandExecutor("npermissions", executor);
		setCommandExecutor("setgroup", executor);

		return true;
	}

	/**
	 * @see fr.ribesg.bukkit.ncore.node.NPlugin#handleOtherNodes()
	 */
	@Override
	protected void handleOtherNodes() {
		// Nothing to do here for now
	}

	@Override
	public void onNodeDisable() {
	}

	public void saveGroups() {
		try {
			groupsConfig.writeConfig("groups.yml");
		} catch (final IOException e) {
			error("An error occured when NPermissions tried to save groups.yml", e);
		}
	}

	public void savePlayers() {
		try {
			playersConfig.writeConfig("players.yml");
		} catch (final IOException e) {
			error("An error occured when NPermissions tried to save players.yml", e);
		}
	}

	public void saveWorldGroups() {
		for (final Map.Entry<String, WorldGroups> entry : this.worldGroupsConfigs.entrySet()) {
			try {
				entry.getValue().writeConfig(entry.getKey() + File.pathSeparator + "groups.yml");
			} catch (final IOException e) {
				error("An error occured when NPermissions tried to save " + entry.getKey() + "/groups.yml", e);
			}
		}
	}

	public void saveWorldPlayers() {
		for (final Map.Entry<String, WorldPlayers> entry : this.worldPlayersConfigs.entrySet()) {
			try {
				entry.getValue().writeConfig(entry.getKey() + File.pathSeparator + "players.yml");
			} catch (final IOException e) {
				error("An error occured when NPermissions tried to save " + entry.getKey() + "/players.yml", e);
			}
		}
	}

	@Override
	public Messages getMessages() {
		return messages;
	}

	public Config getPluginConfig() {
		return pluginConfig;
	}

	public Groups getGroupsConfig() {
		return groupsConfig;
	}

	public Players getPlayersConfig() {
		return playersConfig;
	}

	public PermissionsManager getManager() {
		return manager;
	}

	// API for other nodes

	@Override
	public String getNodeName() {
		return PERMISSIONS;
	}
}
