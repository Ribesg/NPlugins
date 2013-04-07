package fr.ribesg.bukkit.ncuboid.beans;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

import org.bukkit.entity.Player;

import fr.ribesg.bukkit.ncuboid.Permissions;

public class Rights {
    @Getter private Set<String> allowedPlayers;
    @Getter private Set<String> allowedGroups;
    @Getter private Set<String> disallowedCommands;

    public Rights() {
    }

    public Rights(final Set<String> allowedPlayers,
            final Set<String> allowedGroups,
            final Set<String> disallowedCommands) {
        this.allowedPlayers = allowedPlayers;
        this.allowedGroups = allowedGroups;
        this.disallowedCommands = disallowedCommands;
    }

    public boolean isAllowedPlayer(final Player p) {
        if (p.isOp() || p.hasPermission(Permissions.ADMIN) || isAllowedPlayerName(p.getName())) {
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
}
