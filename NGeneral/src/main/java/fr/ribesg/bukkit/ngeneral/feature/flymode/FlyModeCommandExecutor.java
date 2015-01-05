/***************************************************************************
 * Project file:    NPlugins - NGeneral - FlyModeCommandExecutor.java      *
 * Full Class name: fr.ribesg.bukkit.ngeneral.feature.flymode.FlyModeCommandExecutor
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.feature.flymode;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.util.ArgumentParser;
import fr.ribesg.bukkit.ngeneral.Perms;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyModeCommandExecutor implements CommandExecutor {

    private final FlyModeFeature feature;

    public FlyModeCommandExecutor(final FlyModeFeature feature) {
        this.feature = feature;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
        if ("fly".equals(command.getName())) {
            if (!Perms.hasFly(sender)) {
                this.feature.getPlugin().sendMessage(sender, MessageId.noPermissionForCommand);
                return true;
            } else if (args.length == 0) {
                if (!(sender instanceof Player)) {
                    this.feature.getPlugin().sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
                } else if (this.feature.hasFlyMode((Player)sender)) {
                    this.feature.setFlyMode((Player)sender, false);
                    this.feature.getPlugin().sendMessage(sender, MessageId.general_fly_disabled);
                } else {
                    this.feature.setFlyMode((Player)sender, true);
                    this.feature.getPlugin().sendMessage(sender, MessageId.general_fly_enabled);
                }
                return true;
            } else if (args.length == 1) {
                final Boolean value = ArgumentParser.parseBoolean(args[0]);
                if (value == null) {
                    if (Perms.hasFlyOthers(sender)) {
                        final String[] names = args[0].split(",");
                        this.setAll(sender, names, null);
                    } else {
                        this.feature.getPlugin().sendMessage(sender, MessageId.noPermissionForCommand);
                    }
                } else if (value) {
                    if (!(sender instanceof Player)) {
                        this.feature.getPlugin().sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
                    } else {
                        this.feature.setFlyMode((Player)sender, true);
                        this.feature.getPlugin().sendMessage(sender, MessageId.general_fly_enabled);
                    }
                } else {
                    if (!(sender instanceof Player)) {
                        this.feature.getPlugin().sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
                    } else {
                        this.feature.setFlyMode((Player)sender, false);
                        this.feature.getPlugin().sendMessage(sender, MessageId.general_fly_disabled);
                    }
                }
                return true;
            } else if (args.length == 2) {
                if (!Perms.hasFlyOthers(sender)) {
                    this.feature.getPlugin().sendMessage(sender, MessageId.noPermissionForCommand);
                } else {
                    final Boolean value = ArgumentParser.parseBoolean(args[0]);
                    if (value == null) {
                        return false;
                    }
                    final String[] names = args[1].split(",");
                    this.setAll(sender, names, value);
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Change FlyMode state of all players provided.
     *
     * @param playerNames Names of players
     * @param value       Null for toggle, actual value for a set
     */
    private void setAll(final CommandSender sender, final String[] playerNames, final Boolean value) {
        for (final String name : playerNames) {
            final Player p = Bukkit.getPlayer(name);
            if (p == null) {
                this.feature.getPlugin().sendMessage(sender, MessageId.noPlayerFoundForGivenName, name);
            } else {
                final boolean actualValue = value == null ? !this.feature.hasFlyMode(p) : value;
                this.feature.setFlyMode(p, actualValue);
                if (actualValue) {
                    this.feature.getPlugin().sendMessage(sender, MessageId.general_fly_enabledFor, p.getName());
                    this.feature.getPlugin().sendMessage(p, MessageId.general_fly_enabledBy, sender.getName());
                } else {
                    this.feature.getPlugin().sendMessage(sender, MessageId.general_fly_disabledFor, p.getName());
                    this.feature.getPlugin().sendMessage(p, MessageId.general_fly_disabledBy, sender.getName());
                }
            }
        }
    }
}
