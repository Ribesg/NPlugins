/***************************************************************************
 * Project file:    NPlugins - NPermissions - LegacyPlayerPermissions.java *
 * Full Class name: fr.ribesg.bukkit.npermissions.permission.LegacyPlayerPermissions
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.npermissions.permission;
import org.bukkit.configuration.ConfigurationSection;

import java.util.LinkedList;

/**
 * Represents the Permissions attached to a Legacy Player.
 *
 * @author Ribesg
 */
public class LegacyPlayerPermissions extends PlayerPermissions {

	/**
	 * Legacy Player Permissions constructor.
	 * <p>
	 * This constructor will also add the maingroup.groupname Permission to
	 * this Permissions Set.
	 *
	 * @param manager    the Permissions Manager
	 * @param playerName the last known name of the Player
	 * @param priority   the priority of this Permissions Set
	 * @param mainGroup  the main Group of the Player
	 */
	public LegacyPlayerPermissions(final PermissionsManager manager, final String playerName, final int priority, final String mainGroup) {
		super(manager, null, playerName, priority, mainGroup);
	}

	/**
	 * Saves a representation of this LegacyPlayerPermissions under a
	 * CongigurationSection, usually the _legacy section of a players.yml
	 * file.
	 *
	 * @param parentSection the ConfigurationSection under which this
	 *                      LegacyPlayerPermissions representation will be
	 *                      saved
	 */
	public void save(final ConfigurationSection parentSection) {
		final ConfigurationSection thisSection = parentSection.createSection(this.getPlayerName());
		thisSection.set("mainGroup", this.getMainGroup());
		if (this.getGroups().size() > 0) {
			thisSection.set("groups", new LinkedList<>(this.getGroups()));
		}
		super.saveCommon(thisSection);
	}
}
