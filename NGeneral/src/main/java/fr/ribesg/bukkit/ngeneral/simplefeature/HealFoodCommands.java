/***************************************************************************
 * Project file:    NPlugins - NGeneral - HealFoodCommands.java            *
 * Full Class name: fr.ribesg.bukkit.ngeneral.simplefeature.HealFoodCommands
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.simplefeature;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.Perms;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealFoodCommands implements CommandExecutor {

    private static final String[] COMMANDS = {
            "heal",
            "feed",
            "health",
            "food"
    };

    private final NGeneral plugin;

    public HealFoodCommands(final NGeneral instance) {
        this.plugin = instance;
        for (final String command : COMMANDS) {
            this.plugin.setCommandExecutor(command, this);
        }
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        switch (cmd.getName()) {
            case "heal":
                if (Perms.hasHeal(sender)) {
                    return this.cmdHeal(sender, args);
                } else {
                    this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    return true;
                }
            case "feed":
                if (Perms.hasFeed(sender)) {
                    return this.cmdFeed(sender, args);
                } else {
                    this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    return true;
                }
            case "health":
                if (Perms.hasHealth(sender)) {
                    return this.cmdHealth(sender, args);
                } else {
                    this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    return true;
                }
            case "food":
                if (Perms.hasFood(sender)) {
                    return this.cmdFood(sender, args);
                } else {
                    this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    return true;
                }
            default:
                return false;
        }
    }

    private boolean cmdHeal(final CommandSender sender, final String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                this.plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
            } else {
                final Player player = (Player)sender;
                player.setHealth(player.getMaxHealth());
                this.plugin.sendMessage(player, MessageId.general_heal_autoHeal);
            }
        } else {
            for (final String arg : args) {
                for (final String playerName : arg.split(",")) {
                    final Player player = Bukkit.getPlayer(playerName);
                    if (player == null) {
                        this.plugin.sendMessage(sender, MessageId.noPlayerFoundForGivenName, playerName);
                    } else {
                        player.setHealth(player.getMaxHealth());
                        this.plugin.sendMessage(player, MessageId.general_heal_healedBy, sender.getName());
                        this.plugin.sendMessage(sender, MessageId.general_heal_healed, player.getName());
                    }
                }
            }
        }
        return true;
    }

    private boolean cmdFeed(final CommandSender sender, final String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                this.plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
            } else {
                final Player player = (Player)sender;
                player.setFoodLevel(20);
                player.setSaturation(20f);
                this.plugin.sendMessage(player, MessageId.general_feed_autoFeed);
            }
        } else {
            for (final String arg : args) {
                for (final String playerName : arg.split(",")) {
                    final Player player = Bukkit.getPlayer(playerName);
                    if (player == null) {
                        this.plugin.sendMessage(sender, MessageId.noPlayerFoundForGivenName, playerName);
                    } else {
                        player.setFoodLevel(20);
                        player.setSaturation(20f);
                        this.plugin.sendMessage(player, MessageId.general_feed_fedBy, sender.getName());
                        this.plugin.sendMessage(sender, MessageId.general_feed_fed, player.getName());
                    }
                }
            }
        }
        return true;
    }

    private boolean cmdHealth(final CommandSender sender, final String[] args) {
        if (args.length == 0) {
            return false;
        } else {
            final int value;
            try {
                value = Integer.parseInt(args[args.length - 1]);
                if (value < 0) {
                    return false;
                }
            } catch (final NumberFormatException e) {
                return false;
            }
            if (args.length == 1) {
                if (!(sender instanceof Player)) {
                    this.plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
                } else {
                    final Player player = (Player)sender;
                    final int realValue = (int)Math.min(value, player.getMaxHealth());
                    player.setHealth(realValue);
                    this.plugin.sendMessage(player, MessageId.general_health_autoSet, Integer.toString(realValue));
                }
            } else {
                for (int i = 0; i < args.length - 1; i++) {
                    final String arg = args[i];
                    for (final String playerName : arg.split(",")) {
                        final Player player = Bukkit.getPlayer(playerName);
                        if (player == null) {
                            this.plugin.sendMessage(sender, MessageId.noPlayerFoundForGivenName, playerName);
                        } else {
                            final int realValue = (int)Math.min(value, player.getMaxHealth());
                            player.setHealth(realValue);
                            this.plugin.sendMessage(player, MessageId.general_health_setBy, sender.getName(), Integer.toString(realValue));
                            this.plugin.sendMessage(sender, MessageId.general_health_set, player.getName(), Integer.toString(realValue));
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean cmdFood(final CommandSender sender, final String[] args) {
        if (args.length == 0) {
            return false;
        } else {
            final int value;
            try {
                value = Math.min(20, Integer.parseInt(args[args.length - 1]));
                if (value < 0) {
                    return false;
                }
            } catch (final NumberFormatException e) {
                return false;
            }
            if (args.length == 1) {
                if (!(sender instanceof Player)) {
                    this.plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
                } else {
                    final Player player = (Player)sender;
                    player.setFoodLevel(value);
                    player.setSaturation(value);
                    this.plugin.sendMessage(player, MessageId.general_food_autoSet, Integer.toString(value));
                }
            } else {
                for (int i = 0; i < args.length - 1; i++) {
                    final String arg = args[i];
                    for (final String playerName : arg.split(",")) {
                        final Player player = Bukkit.getPlayer(playerName);
                        if (player == null) {
                            this.plugin.sendMessage(sender, MessageId.noPlayerFoundForGivenName, playerName);
                        } else {
                            player.setFoodLevel(value);
                            player.setSaturation(value);
                            this.plugin.sendMessage(player, MessageId.general_food_setBy, sender.getName(), Integer.toString(value));
                            this.plugin.sendMessage(sender, MessageId.general_food_set, player.getName(), Integer.toString(value));
                        }
                    }
                }
            }
        }
        return true;
    }
}
