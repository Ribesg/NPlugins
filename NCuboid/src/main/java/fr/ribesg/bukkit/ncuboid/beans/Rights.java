/***************************************************************************
 * Project file:    NPlugins - NCuboid - Rights.java                       *
 * Full Class name: fr.ribesg.bukkit.ncuboid.beans.Rights                  *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.beans;

import fr.ribesg.bukkit.ncuboid.Perms;
import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.Set;

public class Rights {

	private Set<String> users;
	private Set<String> admins;
	private Set<String> allowedGroups;
	private Set<String> disallowedCommands;

	public Rights() {
	}

	public Rights(final Set<String> users, final Set<String> admins, final Set<String> allowedGroups, final Set<String> disallowedCommands) {
		this.users = users;
		this.admins = admins;
		this.allowedGroups = allowedGroups;
		this.disallowedCommands = disallowedCommands;
	}

	public boolean isUser(final CommandSender sender) {
		if (Perms.isAdmin(sender) || isUserName(sender.getName())) {
			return true;
		} else if (allowedGroups != null) {
			for (final String groupName : allowedGroups) {
				if (sender.hasPermission("group." + groupName)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isUserName(final String playerName) {
		return users != null && users.contains(playerName.toLowerCase()) || isAdminName(playerName);
	}

	public boolean isAdmin(final CommandSender sender) {
		return Perms.isAdmin(sender) || isAdminName(sender.getName());
	}

	public boolean isAdminName(final String playerName) {
		return admins != null && admins.contains(playerName.toLowerCase());
	}

	public boolean isAllowedGroupName(final String groupName) {
		return allowedGroups != null && allowedGroups.contains(groupName.toLowerCase());
	}

	public boolean isAllowedCommand(final String command) {
		return disallowedCommands == null || !disallowedCommands.contains(command.toLowerCase());
	}

	public void addUser(final String playerName) {
		if (users == null) {
			users = new HashSet<>();
		}
		users.add(playerName.toLowerCase());
	}

	public void removeUser(final String playerName) {
		removeAdmin(playerName);
		if (users != null) {
			users.remove(playerName.toLowerCase());
		}
	}

	public void addAdmin(final String playerName) {
		addUser(playerName);
		if (admins == null) {
			admins = new HashSet<>();
		}
		admins.add(playerName.toLowerCase());
	}

	public void removeAdmin(final String playerName) {
		if (admins != null) {
			admins.remove(playerName.toLowerCase());
		}
	}

	public void allowGroup(final String groupName) {
		if (allowedGroups == null) {
			allowedGroups = new HashSet<>();
		}
		allowedGroups.add(groupName.toLowerCase());
	}

	public void denyGroup(final String groupName) {
		if (allowedGroups != null) {
			allowedGroups.remove(groupName.toLowerCase());
		}
	}

	public void allowCommand(final String command) {
		if (disallowedCommands != null) {
			disallowedCommands.remove(command.toLowerCase());
		}
	}

	public void denyCommand(final String command) {
		if (disallowedCommands == null) {
			disallowedCommands = new HashSet<>();
		}
		disallowedCommands.add(command.toLowerCase());
	}

	public Set<String> getAllowedGroups() {
		return allowedGroups;
	}

	public Set<String> getUsers() {
		return users;
	}

	public Set<String> getAdmins() {
		return admins;
	}

	public Set<String> getDisallowedCommands() {
		return disallowedCommands;
	}
}
