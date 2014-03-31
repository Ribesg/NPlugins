/***************************************************************************
 * Project file:    NPlugins - NPermissions - PlayerPermissions.java       *
 * Full Class name: fr.ribesg.bukkit.npermissions.permission.PlayerPermissions
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.npermissions.permission;
import org.bukkit.configuration.ConfigurationSection;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Represents the Permissions attached to a Player.
 *
 * @author Ribesg
 */
public class PlayerPermissions extends PermissionsSet {

	private static final int DEFAULT_PLAYER_PRIORITY = 1;

	/**
	 * This Player's UUID
	 */
	private final String playerUuid;

	/**
	 * The main group of attached to this Player
	 */
	private GroupPermissions mainGroup;

	/**
	 * Groups this Player belongs to, not counting the main one
	 */
	private final Map<String, GroupPermissions> groups;

	/**
	 * Player Permissions constructor.
	 * <p>
	 * This constructor will also add the maingroup.groupname Permission to
	 * this Permissions Set.
	 *
	 * @param playerUuid the Universally Unique Identifier of the Player
	 * @param playerName the last known name of the Player
	 * @param priority   the priority of this Permissions Set
	 * @param mainGroup  the main Group of the Player
	 */
	public PlayerPermissions(final String playerUuid, final String playerName, final int priority, final GroupPermissions mainGroup) {
		super(playerName, priority);
		this.playerUuid = playerUuid;
		this.mainGroup = mainGroup;
		this.groups = new LinkedHashMap<>();

		this.allow.add("maingroup." + mainGroup.getGroupName().toLowerCase());
	}

	/**
	 * Gets the Player's Universally Unique Identifier.
	 *
	 * @return the Player's Universally Unique Identifier
	 */
	public String getPlayerUuid() {
		return this.playerUuid;
	}

	/**
	 * Gets the main Group Permissions Set for this Player.
	 *
	 * @return the main Group Permissions Set for this Player
	 */
	public GroupPermissions getMainGroup() {
		return this.mainGroup;
	}

	/**
	 * Sets the main Group Permissions Set for this Player.
	 *
	 * @param mainGroup the new main Group Permissions Set for this Player
	 */
	public void setMainGroup(final GroupPermissions mainGroup) {
		this.mainGroup = mainGroup;
	}

	/**
	 * Adds a Group to the Groups for this Player
	 *
	 * @param groupPermissions a new additional Group for this Player
	 */
	public void addGroup(final GroupPermissions groupPermissions) {
		this.groups.put(groupPermissions.name, groupPermissions);
	}

	/**
	 * Saves a representation of this PlayerPermissions under a
	 * CongigurationSection, usually the root of a players.yml file.
	 *
	 * @param parentSection the ConfigurationSection under which this
	 *                      PlayerPermissions representation will be saved
	 */
	@Override
	public void save(final ConfigurationSection parentSection) {
		final ConfigurationSection thisSection = parentSection.createSection(this.playerUuid);
		thisSection.set("playerName", this.name);
		thisSection.set("mainGroup", this.mainGroup.getGroupName());
		thisSection.set("groups", new LinkedList<>(this.groups.keySet()));
		super.save(thisSection);
	}

	/**
	 * @see PermissionsSet#getDefaultPriority()
	 */
	@Override
	public int getDefaultPriority() {
		return DEFAULT_PLAYER_PRIORITY;
	}

	/**
	 * TODO: handle priorities
	 *
	 * @see PermissionsSet#computeAllowedPermissions(java.util.Set)
	 */
	@Override
	public Set<String> computeAllowedPermissions(Set<String> resultSet) {
		resultSet = this.mainGroup.computeAllowedPermissions(resultSet);
		for (final GroupPermissions g : this.groups.values()) {
			resultSet = g.computeAllowedPermissions(resultSet);
		}
		resultSet.addAll(this.allow);
		resultSet.removeAll(this.deny);
		return resultSet;
	}

	/**
	 * TODO: handle priorities
	 *
	 * @see PermissionsSet#computeDeniedPermissions(java.util.Set)
	 */
	@Override
	public Set<String> computeDeniedPermissions(Set<String> resultSet) {
		resultSet = this.mainGroup.computeDeniedPermissions(resultSet);
		for (final GroupPermissions g : this.groups.values()) {
			resultSet = g.computeDeniedPermissions(resultSet);
		}
		resultSet.addAll(this.deny);
		return resultSet;
	}
}
