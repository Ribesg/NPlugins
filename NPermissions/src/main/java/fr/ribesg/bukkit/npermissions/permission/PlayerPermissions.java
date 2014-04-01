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

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Represents the Permissions attached to a Player.
 *
 * @author Ribesg
 */
public class PlayerPermissions extends PermissionsSet {

	/**
	 * The default priority for players is 1
	 */
	private static final int DEFAULT_PLAYER_PRIORITY = 1;

	/**
	 * This Player's UUID
	 */
	private final UUID playerUuid;

	/**
	 * The main group of attached to this Player
	 */
	private String mainGroup;

	/**
	 * Groups this Player belongs to, not counting the main one
	 */
	private final Set<String> groups;

	/**
	 * Player Permissions constructor.
	 * <p/>
	 * This constructor will also add the maingroup.groupname Permission to
	 * this Permissions Set.
	 *
	 * @param manager    the Permissions Manager
	 * @param playerUuid the Universally Unique Identifier of the Player
	 * @param playerName the last known name of the Player
	 * @param priority   the priority of this Permissions Set
	 * @param mainGroup  the main Group of the Player
	 */
	public PlayerPermissions(final PermissionsManager manager, final UUID playerUuid, final String playerName, final int priority, final String mainGroup) {
		super(manager, playerName, priority);
		this.playerUuid = playerUuid;
		this.mainGroup = mainGroup;
		this.groups = new LinkedHashSet<>();

		this.allow.add("maingroup." + mainGroup.toLowerCase());
	}

	/**
	 * Gets the Player's Universally Unique Identifier.
	 *
	 * @return the Player's Universally Unique Identifier
	 */
	public UUID getPlayerUuid() {
		return this.playerUuid;
	}

	/**
	 * Gets the name of the Player this PlayerPermissions represents.
	 *
	 * @return the name of the Player this PlayerPermissions represents
	 */
	public String getPlayerName() {
		return this.getName();
	}

	/**
	 * Gets the main Group Permissions Set for this Player.
	 *
	 * @return the main Group Permissions Set for this Player
	 */
	public String getMainGroup() {
		return this.mainGroup;
	}

	/**
	 * Sets the main Group Permissions Set for this Player.
	 *
	 * @param mainGroup the new main Group Permissions Set for this Player
	 */
	public void setMainGroup(final String mainGroup) {
		this.mainGroup = mainGroup;
	}

	/**
	 * Adds a Group to the Groups for this Player
	 *
	 * @param group a new additional Group for this Player
	 */
	public void addGroup(final String group) {
		this.groups.add(group);
	}

	/**
	 * Gets the Groups for this Player, main Group excluded.
	 *
	 * @return the Groups for this Player, main Group excluded
	 */
	public Set<String> getGroups() {
		return groups;
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
		final ConfigurationSection thisSection = parentSection.createSection(this.playerUuid.toString());
		thisSection.set("playerName", this.name);
		thisSection.set("mainGroup", this.mainGroup);
		thisSection.set("groups", new LinkedList<>(this.groups));
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
	 * @see PermissionsSet#computeAllowedPermissions(java.util.Set)
	 */
	@Override
	public Set<String> computeAllowedPermissions(final Set<String> resultSet) {
		// Create some nice data structure
		final SortedMap<Integer, Set<PermissionsSet>> prioritizedPermissions = new TreeMap<>();

		// Populate it with all the things
		final GroupPermissions mainGroupPermissionsSet = this.manager.getGroups().get(this.mainGroup);
		Set<PermissionsSet> set = new HashSet<>();
		set.add(mainGroupPermissionsSet);
		prioritizedPermissions.put(mainGroupPermissionsSet.getPriority(), set);

		for (final String groupName : this.groups) {
			final GroupPermissions group = this.manager.getGroups().get(groupName);
			set = prioritizedPermissions.get(group.getPriority());
			if (set == null) {
				set = new HashSet<>();
			}
			set.add(group);
			prioritizedPermissions.put(group.getPriority(), set);
		}

		set = prioritizedPermissions.get(this.getPriority());
		if (set == null) {
			set = new HashSet<>();
		}
		set.add(this);
		prioritizedPermissions.put(this.getPriority(), set);

		// For each priority level, allows are added THEN denies are removed,
		// overriding lower priority action if needed
		for (final Set<PermissionsSet> permsSet : prioritizedPermissions.values()) {
			for (final PermissionsSet perms : permsSet) {
				resultSet.addAll(perms.getComputedAllowed());
			}
			for (final PermissionsSet perms : permsSet) {
				resultSet.removeAll(perms.getComputedDenied());
			}
		}

		return resultSet;
	}

	/**
	 * @see PermissionsSet#computeDeniedPermissions(java.util.Set)
	 */
	@Override
	public Set<String> computeDeniedPermissions(final Set<String> resultSet) {
		// Create some nice data structure
		final SortedMap<Integer, Set<PermissionsSet>> prioritizedPermissions = new TreeMap<>();

		// Populate it with all the things
		final GroupPermissions mainGroupPermissionsSet = this.manager.getGroups().get(this.mainGroup);
		Set<PermissionsSet> set = new HashSet<>();
		set.add(mainGroupPermissionsSet);
		prioritizedPermissions.put(mainGroupPermissionsSet.getPriority(), set);

		for (final String groupName : this.groups) {
			final GroupPermissions group = this.manager.getGroups().get(groupName);
			set = prioritizedPermissions.get(group.getPriority());
			if (set == null) {
				set = new HashSet<>();
			}
			set.add(group);
			prioritizedPermissions.put(group.getPriority(), set);
		}

		set = prioritizedPermissions.get(this.getPriority());
		if (set == null) {
			set = new HashSet<>();
		}
		set.add(this);
		prioritizedPermissions.put(this.getPriority(), set);

		// For each priority level, allows are added THEN denies are removed,
		// overriding lower priority action if needed
		for (final Set<PermissionsSet> permsSet : prioritizedPermissions.values()) {
			for (final PermissionsSet perms : permsSet) {
				resultSet.removeAll(perms.getComputedAllowed());
			}
			for (final PermissionsSet perms : permsSet) {
				resultSet.addAll(perms.getComputedDenied());
			}
		}

		return resultSet;
	}
}
