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
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Represents the Permissions attached to a Group.
 *
 * @author Ribesg
 */
public class GroupPermissions extends PermissionsSet {

	/**
	 * The default priority for groups is 0
	 */
	protected static final int DEFAULT_GROUP_PRIORITY = 0;

	/**
	 * A Collection of all N+1 Groups in this Group's hierarchy
	 */
	protected final Set<String> superGroups;

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
		this.permissions.put("group." + groupName.toLowerCase(), true);
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
	 * Gets all 'group.x' permissions this Group provides.
	 *
	 * @return all 'group.x' permissions this Group provides
	 */
	public SortedSet<String> getAllGroupPerms() {
		final SortedSet<String> result = new TreeSet<>();
		result.add("group." + this.getGroupName().toLowerCase());
		for (final String superGroup : new TreeSet<>(this.superGroups)) {
			result.addAll(this.manager.getGroups().get(superGroup).getAllGroupPerms());
		}
		return result;
	}

	/**
	 * Saves a representation of this GroupPermissions under a
	 * CongigurationSection, usually the root of a groups.yml file.
	 *
	 * @param parentSection the ConfigurationSection under which this
	 *                      GroupPermissions representation will be saved
	 */
	public void save(final ConfigurationSection parentSection) {
		final ConfigurationSection thisSection = parentSection.createSection(this.name);
		if (this.superGroups.size() > 0) {
			thisSection.set("extends", new LinkedList<>(this.superGroups));
		}
		super.saveCommon(thisSection);
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
	 * @see PermissionsSet#computePermissions(java.util.Map)
	 */
	@Override
	public Map<String, Boolean> computePermissions(Map<String, Boolean> resultMap) {
		for (final String groupName : this.superGroups) {
			final GroupPermissions group = this.manager.getGroups().get(groupName);
			resultMap = group.computePermissions(resultMap);
		}
		resultMap.putAll(this.permissions);
		return resultMap;
	}
}
