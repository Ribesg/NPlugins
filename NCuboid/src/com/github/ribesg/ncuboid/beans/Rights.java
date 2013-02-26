package com.github.ribesg.ncuboid.beans;

import java.util.Set;

import org.bukkit.entity.Player;

import com.github.ribesg.ncore.Permissions;

public class Rights {
    private Set<String> allowedPlayers;
    private Set<String> allowedGroups;
    private Set<String> disallowedCommands;

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
        allowedPlayers.add(playerName);
    }

    public void denyPlayer(final String playerName) {
        allowedPlayers.remove(playerName);
    }

    public void allowGroup(final String groupName) {
        allowedGroups.add(groupName);
    }

    public void denyGroup(final String groupName) {
        allowedGroups.remove(groupName);
    }

    public void allowCommand(final String command) {
        disallowedCommands.remove(command);
    }

    public void denyCommand(final String command) {
        disallowedCommands.add(command);
    }
}
