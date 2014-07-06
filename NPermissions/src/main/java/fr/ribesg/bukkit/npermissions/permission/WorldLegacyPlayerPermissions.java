/***************************************************************************
 * Project file:    NPlugins - NPermissions - WorldLegacyPlayerPermissions.java
 * Full Class name: fr.ribesg.bukkit.npermissions.permission.WorldLegacyPlayerPermissions
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.npermissions.permission;
public class WorldLegacyPlayerPermissions extends LegacyPlayerPermissions {

	private final String                  worldName;
	private final LegacyPlayerPermissions parentPlayer;

	/**
	 * World Legacy Player Permissions constructor.
	 *
	 * @param worldName    the world name
	 * @param parentPlayer the player
	 * @param priority     the priority of this Permissions Set
	 */
	public WorldLegacyPlayerPermissions(final String worldName, final LegacyPlayerPermissions parentPlayer, final int priority) {
		super(parentPlayer.manager, parentPlayer.name, priority, parentPlayer.mainGroup);
		this.worldName = worldName;
		this.parentPlayer = parentPlayer;
	}

	public String getWorldName() {
		return worldName;
	}

	public LegacyPlayerPermissions getParentPlayer() {
		return parentPlayer;
	}
}
