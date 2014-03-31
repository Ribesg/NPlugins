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
import fr.ribesg.bukkit.npermissions.lang.Messages;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.permissions.PermissionAttachment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NPermissions extends NPlugin implements PermissionsNode {

	// Configs
	private Messages messages;
	private Config   pluginConfig;

	// Useful Nodes
	// // None

	// Permissions
	private Map<String, PermissionAttachment> permissions;

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
			getLogger().severe("An error occured, stacktrace follows:");
			e.printStackTrace();
			getLogger().severe("This error occured when NTalk tried to load config.yml");
			return false;
		}

		this.permissions = new HashMap<>();

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

	// API for other nodes

	@Override
	public String getNodeName() {
		return PERMISSIONS;
	}
}
