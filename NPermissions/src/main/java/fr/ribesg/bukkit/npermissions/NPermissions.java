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
import fr.ribesg.bukkit.npermissions.lang.Messages;
import fr.ribesg.bukkit.npermissions.permission.PermissionsManager;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;

public class NPermissions extends NPlugin implements PermissionsNode {

	// Configs
	private Messages messages;
	private Config   pluginConfig;
	private Groups   groupsConfig;
	private Players  playersConfig;

	// Useful Nodes
	// // None

	// Permissions
	private PermissionsManager                manager;

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
			pluginConfig = new Config(this);
			pluginConfig.loadConfig();
		} catch (final IOException | InvalidConfigurationException e) {
			error("An error occured when NPermissions tried to load config.yml", e);
			return false;
		}

		this.manager = new PermissionsManager(this);

		// Groups
		try {
			groupsConfig = new Groups(this);
			groupsConfig.loadConfig("groups.yml");
		} catch (final IOException | InvalidConfigurationException e) {
			error("An error occured when NPermissions tried to load groups.yml", e);
			return false;
		}

		// Players
		try {
			playersConfig = new Players(this);
			playersConfig.loadConfig("players.yml");
		} catch (final IOException | InvalidConfigurationException e) {
			error("An error occured when NPermissions tried to load players.yml", e);
			return false;
		}

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

	@Override
	public Messages getMessages() {
		return messages;
	}

	public Config getPluginConfig() {
		return pluginConfig;
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
