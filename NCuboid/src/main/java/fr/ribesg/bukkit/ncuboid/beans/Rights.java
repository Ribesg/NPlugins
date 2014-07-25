/***************************************************************************
 * Project file:    NPlugins - NCuboid - Rights.java                       *
 * Full Class name: fr.ribesg.bukkit.ncuboid.beans.Rights                  *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.beans;

import fr.ribesg.bukkit.ncore.util.AsyncPermAccessor;
import fr.ribesg.bukkit.ncuboid.Perms;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Rights {

	private Set<UUID>   users;
	private Set<UUID>   admins;
	private Set<String> allowedGroups;
	private Set<String> disallowedCommands;

	public Rights() {
	}

	public Rights(final Set<UUID> users, final Set<UUID> admins, final Set<String> allowedGroups, final Set<String> disallowedCommands) {
		this.users = users;
		this.admins = admins;
		this.allowedGroups = allowedGroups;
		this.disallowedCommands = disallowedCommands;
	}

	public boolean isUser(final Player player) {
		return this.isUser(player, false);
	}

	public boolean isUser(final Player player, final boolean async) {
		if (Perms.isAdmin(player, async) || isUserId(player.getUniqueId())) {
			return true;
		} else if (allowedGroups != null) {
			final String playerName = player.getName();
			for (final String groupName : allowedGroups) {
				final String permission = "group." + groupName;
				if (async ? AsyncPermAccessor.has(playerName, permission) : player.hasPermission(permission)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isUserId(final UUID id) {
		return users != null && users.contains(id);
	}

	public boolean isAdmin(final Player player) {
		return Perms.isAdmin(player, false) || isAdminId(player.getUniqueId());
	}

	public boolean isAdminId(final UUID id) {
		return admins != null && admins.contains(id);
	}

	public boolean isAllowedGroup(final String groupName) {
		return allowedGroups != null && allowedGroups.contains(groupName.toLowerCase());
	}

	public boolean isAllowedCommand(final String command) {
		return disallowedCommands == null || !disallowedCommands.contains(command.toLowerCase());
	}

	public void addUser(final UUID id) {
		if (users == null) {
			users = new HashSet<>();
		}
		users.add(id);
	}

	public void removeUser(final UUID id) {
		removeAdmin(id);
		if (users != null) {
			users.remove(id);
		}
	}

	public void addAdmin(final UUID id) {
		addUser(id);
		if (admins == null) {
			admins = new HashSet<>();
		}
		admins.add(id);
	}

	public void removeAdmin(final UUID id) {
		if (admins != null) {
			admins.remove(id);
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

	public Set<UUID> getUsers() {
		return users;
	}

	public Set<UUID> getAdmins() {
		return admins;
	}

	public Set<String> getDisallowedCommands() {
		return disallowedCommands;
	}
}
