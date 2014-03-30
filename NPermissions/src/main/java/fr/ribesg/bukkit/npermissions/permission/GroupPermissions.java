/***************************************************************************
 * Project file:    NPlugins - NPermissions - GroupPermissions.java        *
 * Full Class name: fr.ribesg.bukkit.npermissions.permission.GroupPermissions
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.npermissions.permission;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the Permissions attached to a Group.
 *
 * @author Ribesg
 */
public class GroupPermissions extends PermissionsSet {

	/**
	 * A Collection of all N+1 Groups in this Group's hierarchy
	 */
	private final Map<String, GroupPermissions> superGroups;

	/**
	 * Group Permissions constructor.
	 * <p>
	 * This constructor will also add the group.groupname Permission to
	 * thi Permissions Set.
	 *
	 * @param groupName the name of this Group
	 * @param priority  the priority of this Permissions Set
	 */
	public GroupPermissions(final String groupName, final int priority) {
		super(groupName, priority);
		this.superGroups = new HashMap<>();

		// Add this Group's permission
		this.allow.add("group." + groupName.toLowerCase());
	}

	/**
	 * Adds a N+1 Group in this Group's hierarchy.
	 *
	 * @param groupPermissions the N+1 Group to add
	 */
	public void addSuperGroup(final GroupPermissions groupPermissions) {
		this.superGroups.put(groupPermissions.name, groupPermissions);
	}

	/**
	 * Checks if this Group Permissions Set explicitly allows a Permission.
	 * <p>
	 * This implementation checks if the Permission is allowed by the
	 * whole Group hierarchy, considering it as a single entity.
	 *
	 * @param permission the Permission to check
	 *
	 * @return true if this Group Permissions Set explicitly allows the
	 * provided Permission, false otherwise
	 */
	@Override
	public boolean allows(final String permission) {
		if (super.allows(permission)) {
			return true;
		} else {
			for (final GroupPermissions superGroup : this.superGroups.values()) {
				if (superGroup.allows(permission)) {
					return true;
				}
			}
			return false;
		}
	}
}
