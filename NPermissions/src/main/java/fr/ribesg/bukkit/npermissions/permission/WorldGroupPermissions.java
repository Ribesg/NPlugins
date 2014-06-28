/***************************************************************************
 * Project file:    NPlugins - NPermissions - WorldGroupPermissions.java   *
 * Full Class name: fr.ribesg.bukkit.npermissions.permission.WorldGroupPermissions
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.npermissions.permission;
import java.util.Map;

public class WorldGroupPermissions extends GroupPermissions {

	private final String           worldName;
	private final GroupPermissions parentGroup;

	/**
	 * World Group Permissions constructor.
	 *
	 * @param worldName   the world name
	 * @param parentGroup the group
	 * @param priority    the priority of this Permissions Set
	 */
	public WorldGroupPermissions(final String worldName, final GroupPermissions parentGroup, final int priority) {
		super(parentGroup.manager, parentGroup.name, priority);
		this.worldName = worldName;
		this.parentGroup = parentGroup;
	}

	/**
	 * Priorities does not count vertically.
	 *
	 * @see PermissionsSet#computePermissions(java.util.Map)
	 */
	@Override
	public Map<String, Boolean> computePermissions(Map<String, Boolean> resultMap) {
		resultMap = this.parentGroup.getComputedPermissions();
		for (final String groupName : this.superGroups) {
			final WorldGroupPermissions worldGroup = manager.getWorldGroups().get(this.worldName).get(groupName);
			resultMap = worldGroup.computePermissions(resultMap);
		}
		resultMap.putAll(this.permissions);
		return resultMap;
	}
}
