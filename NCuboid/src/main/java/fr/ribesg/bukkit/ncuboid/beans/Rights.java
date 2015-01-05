/***************************************************************************
 * Project file:    NPlugins - NCuboid - Rights.java                       *
 * Full Class name: fr.ribesg.bukkit.ncuboid.beans.Rights                  *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.beans;

import fr.ribesg.bukkit.ncuboid.Perms;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

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
        if (Perms.isAdmin(player) || this.isUserId(player.getUniqueId())) {
            return true;
        } else if (this.allowedGroups != null) {
            for (final String groupName : this.allowedGroups) {
                final String permission = "group." + groupName;
                if (Perms.has(player, permission)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isUserId(final UUID id) {
        return this.users != null && this.users.contains(id);
    }

    public boolean isAdmin(final Player player) {
        return Perms.isAdmin(player) || this.isAdminId(player.getUniqueId());
    }

    public boolean isAdminId(final UUID id) {
        return this.admins != null && this.admins.contains(id);
    }

    public boolean isAllowedGroup(final String groupName) {
        return this.allowedGroups != null && this.allowedGroups.contains(groupName.toLowerCase());
    }

    public boolean isAllowedCommand(final String command) {
        return this.disallowedCommands == null || !this.disallowedCommands.contains(command.toLowerCase());
    }

    public void addUser(final UUID id) {
        if (this.users == null) {
            this.users = new HashSet<>();
        }
        this.users.add(id);
    }

    public void removeUser(final UUID id) {
        this.removeAdmin(id);
        if (this.users != null) {
            this.users.remove(id);
        }
    }

    public void addAdmin(final UUID id) {
        this.addUser(id);
        if (this.admins == null) {
            this.admins = new HashSet<>();
        }
        this.admins.add(id);
    }

    public void removeAdmin(final UUID id) {
        if (this.admins != null) {
            this.admins.remove(id);
        }
    }

    public void allowGroup(final String groupName) {
        if (this.allowedGroups == null) {
            this.allowedGroups = new HashSet<>();
        }
        this.allowedGroups.add(groupName.toLowerCase());
    }

    public void denyGroup(final String groupName) {
        if (this.allowedGroups != null) {
            this.allowedGroups.remove(groupName.toLowerCase());
        }
    }

    public void allowCommand(final String command) {
        if (this.disallowedCommands != null) {
            this.disallowedCommands.remove(command.toLowerCase());
        }
    }

    public void denyCommand(final String command) {
        if (this.disallowedCommands == null) {
            this.disallowedCommands = new HashSet<>();
        }
        this.disallowedCommands.add(command.toLowerCase());
    }

    public Set<String> getAllowedGroups() {
        return this.allowedGroups;
    }

    public Set<UUID> getUsers() {
        return this.users;
    }

    public Set<UUID> getAdmins() {
        return this.admins;
    }

    public Set<String> getDisallowedCommands() {
        return this.disallowedCommands;
    }
}
