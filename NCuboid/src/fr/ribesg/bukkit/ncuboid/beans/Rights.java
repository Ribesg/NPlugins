package fr.ribesg.bukkit.ncuboid.beans;

import fr.ribesg.bukkit.ncuboid.Perms;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class Rights {

    private Set<String> allowedPlayers;
    private Set<String> allowedGroups;
    private Set<String> disallowedCommands;

    public Rights() {
    }

    public Rights(final Set<String> allowedPlayers, final Set<String> allowedGroups, final Set<String> disallowedCommands) {
        this.allowedPlayers = allowedPlayers;
        this.allowedGroups = allowedGroups;
        this.disallowedCommands = disallowedCommands;
    }

    public boolean isAllowedPlayer(final Player p) {
        if (Perms.isAdmin(p) || isAllowedPlayerName(p.getName())) {
            return true;
        } else if (allowedGroups != null) {
            for (final String groupName : allowedGroups) {
                if (p.hasPermission("group." + groupName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isAllowedPlayerName(final String playerName) {
        return allowedPlayers != null && allowedPlayers.contains(playerName);
    }

    public boolean isAllowedGroupName(final String groupName) {
        return allowedGroups != null && allowedGroups.contains(groupName);
    }

    public boolean isAllowedCommand(final String command) {
        return disallowedCommands == null || !disallowedCommands.contains(command);
    }

    public void allowPlayer(final String playerName) {
        if (allowedPlayers == null) {
            allowedPlayers = new HashSet<String>();
        }
        allowedPlayers.add(playerName.toLowerCase());
    }

    public void denyPlayer(final String playerName) {
        if (allowedPlayers != null) {
            allowedPlayers.remove(playerName.toLowerCase());
        }
    }

    public void allowGroup(final String groupName) {
        if (allowedGroups == null) {
            allowedGroups = new HashSet<String>();
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
            disallowedCommands = new HashSet<String>();
        }
        disallowedCommands.add(command.toLowerCase());
    }

    public Set<String> getAllowedGroups() {
        return allowedGroups;
    }

    public Set<String> getAllowedPlayers() {
        return allowedPlayers;
    }

    public Set<String> getDisallowedCommands() {
        return disallowedCommands;
    }
}
