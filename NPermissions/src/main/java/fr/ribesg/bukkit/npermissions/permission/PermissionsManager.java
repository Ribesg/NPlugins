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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

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
	 * Map of per-World Groups with their attached Permissions
	 */
	private final Map<String, Map<String, WorldGroupPermissions>> worldGroups;

	/**
	 * Map of Players with their attached Permissions
	 */
	private final Map<UUID, PlayerPermissions> players;

	/**
	 * Map of per-World Players with their attached Permissions
	 */
	private final Map<String, Map<UUID, WorldPlayerPermissions>> worldPlayers;

	/**
	 * Map of Legacy Players with their attached Permissions
	 */
	private final Map<String, LegacyPlayerPermissions> legacyPlayers;

	/**
	 * Map of per-World Legacy Players with their attached Permissions
	 */
	private final Map<String, Map<String, WorldLegacyPlayerPermissions>> worldLegacyPlayers;

	/**
	 * Map of online Players and their PermissionAttachment
	 */
	private final Map<UUID, PermissionAttachment> attachmentMap;

	/**
	 * Map that enables easy per-String PlayerPermissions get
	 */
	private Map<String, UUID> playerNameToUuidMap = new HashMap<>();

	/**
	 * Permissions Manager constructor.
	 *
	 * @param instance the NPermissions plugin instance
	 */
	public PermissionsManager(final NPermissions instance) {
		this.plugin = instance;
		this.groups = new LinkedHashMap<>();
		this.worldGroups = new LinkedHashMap<>();
		this.players = new LinkedHashMap<>();
		this.worldPlayers = new LinkedHashMap<>();
		this.legacyPlayers = new LinkedHashMap<>();
		this.worldLegacyPlayers = new LinkedHashMap<>();
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
	 * Gets the map of World Groups with their attached Permissions.
	 *
	 * @return the map of World Groups with their attached Permissions
	 */
	public Map<String, Map<String, WorldGroupPermissions>> getWorldGroups() {
		return this.worldGroups;
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
	 * Gets the map of World Players with their attached Permissions.
	 *
	 * @return the map of World Players with their attached Permissions
	 */
	public Map<String, Map<UUID, WorldPlayerPermissions>> getWorldPlayers() {
		return this.worldPlayers;
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
	 * Gets the map of World Legacy Players with their attached Permissions.
	 *
	 * @return the map of World Legacy Players with their attached Permissions
	 */
	public Map<String, Map<String, WorldLegacyPlayerPermissions>> getWorldLegacyPlayers() {
		return this.worldLegacyPlayers;
	}

	/**
	 * Gets a UUID based on a registered Player name.
	 *
	 * @param playerName a Player name
	 *
	 * @return the UUID associated to this Player's name or null if unknown
	 */
	public UUID getByPlayerName(final String playerName) {
		return this.playerNameToUuidMap.get(playerName.toLowerCase());
	}

	/**
	 * Adds a Player name - UUID couple to the map.
	 *
	 * @param playerName a Player name
	 * @param id         the corresponding UUID
	 */
	public void addPlayerByName(final String playerName, final UUID id) {
		this.playerNameToUuidMap.put(playerName.toLowerCase(), id);
	}

	/**
	 * Registers a Player on login.
	 *
	 * Handles Legacy Player -> Player conversion for both general and World
	 * permissions (if any), attach general permissions to the player.
	 *
	 * @param player the Player
	 */
	public void registerPlayer(final Player player) {
		boolean saveNeeded = false;
		final UUID playerUuid = player.getUniqueId();
		final String playerName = player.getName();

		// Get/Generate/Update General permissions
		PlayerPermissions playerPermissionsSet = this.players.get(playerUuid);
		if (playerPermissionsSet == null) {
			final LegacyPlayerPermissions legacyPlayerPermissionsSet = this.legacyPlayers.remove(playerName.toLowerCase());
			if (legacyPlayerPermissionsSet == null) {
				playerPermissionsSet = new PlayerPermissions(this, playerUuid, playerName, 1, plugin.getPluginConfig().getDefaultGroup());
			} else {
				playerPermissionsSet = new PlayerPermissions(playerUuid, legacyPlayerPermissionsSet);
				playerPermissionsSet.setPlayerName(playerName);
			}
			this.players.put(playerUuid, playerPermissionsSet);
			saveNeeded = true;
		}

		// Get/Generate/Update World specific permissions
		if (plugin.getPluginConfig().hasPerWorldPermissions()) {
			for (final String worldName : this.worldPlayers.keySet()) {
				WorldPlayerPermissions worldPlayerPerms = this.worldPlayers.get(worldName).get(playerUuid);
				if (this.worldLegacyPlayers.containsKey(worldName) && this.worldLegacyPlayers.get(worldName).containsKey(playerName.toLowerCase())) {
					final WorldLegacyPlayerPermissions worldLegacyPlayerPerms = this.worldLegacyPlayers.get(worldName).remove(playerName.toLowerCase());
					if (worldPlayerPerms != null) {
						plugin.error(Level.WARNING, "Legacy Player record already has normal Player record for world '" + worldName + "': " + playerName);
					} else {
						worldPlayerPerms = new WorldPlayerPermissions(playerUuid, playerPermissionsSet, worldLegacyPlayerPerms);
						this.worldPlayers.get(worldName).put(playerUuid, worldPlayerPerms);
						saveNeeded = true;
					}
				}
			}
		}

		// Apply general permissions for now, world specific permissions will be handled later, if needed
		final Map<String, Boolean> permissions = playerPermissionsSet.getComputedPermissions();
		final PermissionAttachment playerPermissions = player.addAttachment(plugin);
		for (final Map.Entry<String, Boolean> e : permissions.entrySet()) {
			playerPermissions.setPermission(e.getKey(), e.getValue());
		}
		this.attachmentMap.put(playerUuid, playerPermissions);
		addPlayerByName(playerName, playerUuid);
		if (saveNeeded) {
			this.plugin.savePlayers();
			this.plugin.saveWorldPlayers();
		}
	}

	/**
	 * Handles a Player after login.
	 *
	 * Handles World specific permissions, if any.
	 *
	 * @param player the Player
	 */
	public void registerPlayerForWorld(final Player player) {
		applyWorldPermissions(player);
	}

	/**
	 * Registers a Player again, often after a reload.
	 *
	 * @param uuid the Player's UUID
	 */
	public void reRegisterPlayer(final UUID uuid) {
		final Player player = Bukkit.getPlayer(uuid);
		unRegisterPlayer(player);
		registerPlayer(player);
		applyWorldPermissions(player);
	}

	/**
	 * Handles a Player's logout.
	 *
	 * Detach Player's permissions.
	 *
	 * @param player the Player
	 */
	public void unRegisterPlayer(final Player player) {
		final PermissionAttachment playerAttachment = this.attachmentMap.remove(player.getUniqueId());
		if (playerAttachment != null) {
			playerAttachment.remove();
		}
	}

	/**
	 * Applies world permissions, if enabled.
	 *
	 * If enabled, change the Player's permissions to the new world specific
	 * permissions if any.
	 *
	 * @param player the Player
	 */
	public void applyWorldPermissions(final Player player) {
		if (plugin.getPluginConfig().hasPerWorldPermissions()) {
			final String worldName = player.getWorld().getName();
			final UUID playerUuid = player.getUniqueId();
			PermissionAttachment playerAttachment = this.attachmentMap.remove(playerUuid);
			PlayerPermissions playerPermissions = this.worldPlayers.get(worldName).get(playerUuid);
			if (playerPermissions == null) {
				playerPermissions = this.players.get(playerUuid);
			}
			if (playerAttachment != null) {
				playerAttachment.remove();
			}
			playerAttachment = player.addAttachment(plugin);
			for (final Map.Entry<String, Boolean> e : playerPermissions.getComputedPermissions().entrySet()) {
				playerAttachment.setPermission(e.getKey(), e.getValue());
			}
			this.attachmentMap.put(playerUuid, playerAttachment);
		}
	}

	/**
	 * Unregisters and registers everybody again, nice just after reloading
	 * configs from files.
	 */
	public void reload() {
		for (final Player player : Bukkit.getOnlinePlayers()) {
			this.plugin.getManager().unRegisterPlayer(player);
		}
		this.attachmentMap.clear();
		for (final Player player : Bukkit.getOnlinePlayers()) {
			this.plugin.getManager().registerPlayer(player);
		}
	}
}
