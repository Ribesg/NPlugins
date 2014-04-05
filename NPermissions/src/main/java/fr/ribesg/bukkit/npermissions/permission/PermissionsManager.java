/***************************************************************************
 * Project file:    NPlugins - NPermissions - PermissionsManager.java      *
 * Full Class name: fr.ribesg.bukkit.npermissions.permission.PermissionsManager
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.npermissions.permission;
import fr.ribesg.bukkit.npermissions.NPermissions;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This class manages Permissions.
 * Basically, once the configuration files are loaded, this class stores
 * them.
 *
 * @author Ribesg
 */
public class PermissionsManager {

	/**
	 * NPermissions plugin instance
	 */
	private final NPermissions plugin;

	/**
	 * Map of Groups with their attached Permissions
	 */
	private final Map<String, GroupPermissions> groups;

	/**
	 * Map of Players with their attached Permissions
	 */
	private final Map<UUID, PlayerPermissions> players;

	/**
	 * Map of Legacy Players with their attached Permissions
	 */
	private final Map<String, LegacyPlayerPermissions> legacyPlayers;

	/**
	 * Map of online Players and their PermissionAttachment
	 */
	private final Map<UUID, PermissionAttachment> attachmentMap;

	/**
	 * Permissions Manager constructor.
	 *
	 * @param instance the NPermissions plugin instance
	 */
	public PermissionsManager(final NPermissions instance) {
		this.plugin = instance;
		this.groups = new LinkedHashMap<>();
		this.players = new LinkedHashMap<>();
		this.legacyPlayers = new LinkedHashMap<>();
		this.attachmentMap = new HashMap<>();
	}

	/**
	 * Gets the NPermissions plugin instance.
	 *
	 * @return the NPermissions plugin instance
	 */
	public NPermissions getPlugin() {
		return plugin;
	}

	/**
	 * Gets the map of Groups with their attached Permissions.
	 *
	 * @return the map of Groups with their attached Permissions
	 */
	public Map<String, GroupPermissions> getGroups() {
		return this.groups;
	}

	/**
	 * Gets the map of Players with their attached Permissions.
	 *
	 * @return the map of Players with their attached Permissions
	 */
	public Map<UUID, PlayerPermissions> getPlayers() {
		return this.players;
	}

	/**
	 * Gets the map of Legacy Players with their attached Permissions.
	 *
	 * @return the map of Legacy Players with their attached Permissions
	 */
	public Map<String, LegacyPlayerPermissions> getLegacyPlayers() {
		return this.legacyPlayers;
	}

	/**
	 * TODO
	 *
	 * @param player
	 */
	public void registerPlayer(final Player player) {
		boolean saveNeeded = false;
		final UUID playerUuid = player.getUniqueId();
		final String playerName = player.getName();
		PlayerPermissions playerPermissionsSet = this.players.get(playerUuid);
		if (playerPermissionsSet == null) {
			final LegacyPlayerPermissions legacyPlayerPermissionsSet = this.legacyPlayers.remove(playerName);
			if (legacyPlayerPermissionsSet == null) {
				playerPermissionsSet = new PlayerPermissions(this, playerUuid, player.getName(), 1, plugin.getPluginConfig().getDefaultGroup());
			} else {
				playerPermissionsSet = new PlayerPermissions(playerUuid, legacyPlayerPermissionsSet);
			}
			this.players.put(playerUuid, playerPermissionsSet);
			saveNeeded = true;
		}
		final Map<String, Boolean> permissions = playerPermissionsSet.getComputedPermissions();
		final PermissionAttachment playerPermissions = player.addAttachment(plugin);
		for (final Map.Entry<String, Boolean> e : permissions.entrySet()) {
			playerPermissions.setPermission(e.getKey(), e.getValue());
		}
		this.attachmentMap.put(playerUuid, playerPermissions);
		if (saveNeeded) {
			this.plugin.savePlayers();
		}
	}

	/**
	 * TODO
	 *
	 * @param player
	 */
	public void unRegisterPlayer(final Player player) {
		final UUID playerUuid = player.getUniqueId();
		final PermissionAttachment playerAttachment = this.attachmentMap.get(playerUuid);
		if (playerAttachment != null) {
			playerAttachment.remove();
			this.attachmentMap.remove(playerUuid);
		}
	}

	/**
	 * TODO
	 *
	 * @param player
	 */
	public void unRegisterPlayerForWorld(final Player player) {
		final UUID playerUuid = player.getUniqueId();
		final PermissionAttachment playerAttachment = this.attachmentMap.get(playerUuid);
		if (playerAttachment == null) {
			throw new IllegalStateException();
		} else {
			// TODO
		}
	}
}
