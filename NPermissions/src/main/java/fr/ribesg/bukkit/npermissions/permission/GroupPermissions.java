/***************************************************************************
 * Project file:    NPlugins - NPermissions - GroupPermissions.java        *
 * Full Class name: fr.ribesg.bukkit.npermissions.permission.GroupPermissions
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.npermissions.permission;
import org.bukkit.configuration.ConfigurationSection;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Represents the Permissions attached to a Group.
 *
 * @author Ribesg
 */
public class GroupPermissions extends PermissionsSet {

	/**
	 * The default priority for groups is 0
	 */
	private static final int DEFAULT_GROUP_PRIORITY = 0;

	/**
	 * A Collection of all N+1 Groups in this Group's hierarchy
	 */
	private final Set<String> superGroups;

	/**
	 * Group Permissions constructor.
	 * <p>
	 * This constructor will also add the group.groupname Permission to
	 * this Permissions Set.
	 *
	 * @param manager   the Permissions Manager
	 * @param groupName the name of this Group
	 * @param priority  the priority of this Permissions Set
	 */
	public GroupPermissions(final PermissionsManager manager, final String groupName, final int priority) {
		super(manager, groupName, priority);
		this.superGroups = new LinkedHashSet<>();

		// Add this Group's permission
		this.allow.add("group." + groupName.toLowerCase());
	}

	/**
	 * Gets the name of the Group this GroupPermissions represents.
	 *
	 * @return the name of the Group this GroupPermissions represents
	 */
	public String getGroupName() {
		return this.getName();
	}

	/**
	 * Adds a N+1 Group in this Group's hierarchy.
	 *
	 * @param group the N+1 Group to add
	 */
	public void addSuperGroup(final String group) {
		this.superGroups.add(group);
	}

	/**
	 * Saves a representation of this GroupPermissions under a
	 * CongigurationSection, usually the root of a groups.yml file.
	 *
	 * @param parentSection the ConfigurationSection under which this
	 *                      GroupPermissions representation will be saved
	 */
	@Override
	public void save(final ConfigurationSection parentSection) {
		final ConfigurationSection thisSection = parentSection.createSection(this.name);
		thisSection.set("extends", new LinkedList<>(this.superGroups));
		super.save(thisSection);
	}

	/**
	 * @see PermissionsSet#getDefaultPriority()
	 */
	@Override
	public int getDefaultPriority() {
		return DEFAULT_GROUP_PRIORITY;
	}

	/**
	 * Priorities does not count vertically.
	 *
	 * @see PermissionsSet#computeAllowedPermissions(java.util.Set)
	 */
	@Override
	public Set<String> computeAllowedPermissions(Set<String> resultSet) {
		for (final String groupName : this.superGroups) {
			final GroupPermissions group = manager.getGroups().get(groupName);
			resultSet = group.computeAllowedPermissions(resultSet);
		}
		resultSet.addAll(this.allow);
		resultSet.removeAll(this.deny);
		return resultSet;
	}

	/**
	 * Priorities does not count vertically.
	 *
	 * @see PermissionsSet#computeDeniedPermissions(java.util.Set)
	 */
	@Override
	public Set<String> computeDeniedPermissions(Set<String> resultSet) {
		for (final String groupName : this.superGroups) {
			final GroupPermissions group = manager.getGroups().get(groupName);
			resultSet = group.computeDeniedPermissions(resultSet);
		}
		resultSet.addAll(this.deny);
		return resultSet;
	}
}
