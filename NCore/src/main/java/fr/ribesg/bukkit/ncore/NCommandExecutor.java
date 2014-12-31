/***************************************************************************
 * Project file:    NPlugins - NCore - NCommandExecutor.java               *
 * Full Class name: fr.ribesg.bukkit.ncore.NCommandExecutor                *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore;

import fr.ribesg.bukkit.ncore.info.Info;
import fr.ribesg.bukkit.ncore.info.Info.Type;
import fr.ribesg.bukkit.ncore.info.InfoCommandHandler;
import fr.ribesg.bukkit.ncore.node.NPlugin;
import fr.ribesg.bukkit.ncore.node.Node;
import fr.ribesg.bukkit.ncore.updater.Updater;
import fr.ribesg.bukkit.ncore.util.ArgumentParser;
import fr.ribesg.bukkit.ncore.util.ColorUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.Plugin;

public class NCommandExecutor implements CommandExecutor {

    private final NCore plugin;

    public NCommandExecutor(final NCore instance) {
        this.plugin = instance;
        this.plugin.getCommand("debug").setExecutor(this);
        this.plugin.getCommand("info").setExecutor(this);
        this.plugin.getCommand("updater").setExecutor(this);
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        switch (cmd.getName()) {
            case "debug":
                return this.onDebugCommand(sender, args);
            case "info":
                return this.onInfoCommand(sender, args);
            case "updater":
                return this.onUpdaterCommand(sender, args);
            default:
                return false;
        }
    }

    private boolean onDebugCommand(final CommandSender sender, final String[] args) {
        if (!Perms.hasDebug(sender)) {
            sender.sendMessage(ColorUtil.colorize("&cYou do not have the permission to use that command"));
            return true;
        }
        if (args.length < 1 || args.length > 2) {
            return false;
        } else {
            final String header = String.valueOf(ChatColor.DARK_GRAY) + ChatColor.BOLD + "DEBUG " + ChatColor.RESET;
            final String nodeName = args[args.length - 1];
            final Plugin plugin = Bukkit.getPluginManager().getPlugin(nodeName);
            if (plugin == null || !(plugin instanceof NPlugin) && plugin != this.plugin) {
                sender.sendMessage(header + ChatColor.RED + '\'' + nodeName + "' is unknown or unloaded!");
            } else {
                final Boolean value;
                if (plugin == this.plugin) {
                    if (args.length == 1) {
                        value = !this.plugin.isDebugEnabled();
                    } else {
                        value = ArgumentParser.parseBoolean(args[0]);
                    }
                    if (value == null) {
                        return false;
                    }
                    this.plugin.setDebugEnabled(value);
                } else {
                    final NPlugin nPlugin;
                    try {
                        nPlugin = (NPlugin)plugin;
                    } catch (final ClassCastException e) {
                        sender.sendMessage(header + ChatColor.RED + '\'' + nodeName + "' is invalid!");
                        return true;
                    }
                    if (args.length == 1) {
                        value = !nPlugin.isDebugEnabled();
                    } else {
                        value = ArgumentParser.parseBoolean(args[0]);
                    }
                    if (value == null) {
                        return false;
                    }
                    nPlugin.setDebugEnabled(value);
                }
                sender.sendMessage(header + ChatColor.GREEN + '\'' + nodeName + "' now has debug mode " + ChatColor.GOLD +
                                   (value ? "enabled" : "disabled") + ChatColor.GREEN + '!');
                try {
                    final List<String> debugEnabledList = this.plugin.getPluginConfig().getDebugEnabled();
                    if (value) {
                        debugEnabledList.add(plugin.getName());
                    } else {
                        debugEnabledList.remove(plugin.getName());
                    }
                    this.plugin.getPluginConfig().loadConfig();
                    this.plugin.getPluginConfig().setDebugEnabled(debugEnabledList);
                    this.plugin.getPluginConfig().writeConfig();
                } catch (final InvalidConfigurationException | IOException ignored) {
                    // Not a real problem
                }
            }
            return true;
        }
    }

    private boolean onInfoCommand(final CommandSender sender, final String[] args) {
        final String query;
        final Type type;
        if (args.length == 1) {
            query = args[0];
            type = Type.FULL;
        } else if (args.length == 2) {
            query = args[1];
            switch (args[0].toLowerCase()) {
                case "player":
                case "p":
                    type = Type.PLAYER;
                    break;
                case "region":
                case "r":
                case "cuboid":
                case "cubo":
                case "c":
                    type = Type.REGION;
                    break;
                case "warp":
                    type = Type.WARP;
                    break;
                case "world":
                    type = Type.WORLD;
                    break;
                default:
                    return false;
            }
        } else {
            return false;
        }
        final List<String> nodes = new ArrayList<>();
        switch (type) {
            case FULL:
                nodes.add(Node.CUBOID);
                nodes.add(Node.GENERAL);
                nodes.add(Node.PERMISSIONS);
                nodes.add(Node.PLAYER);
                nodes.add(Node.TALK);
                nodes.add(Node.WORLD);
                break;
            case PLAYER:
                nodes.add(Node.CUBOID);
                nodes.add(Node.GENERAL);
                nodes.add(Node.PERMISSIONS);
                nodes.add(Node.PLAYER);
                nodes.add(Node.TALK);
                break;
            case REGION:
                nodes.add(Node.CUBOID);
                break;
            case WARP:
                nodes.add(Node.WORLD);
                break;
            case WORLD:
                nodes.add(Node.CUBOID);
                nodes.add(Node.WORLD);
                break;
            default:
                throw new UnsupportedOperationException("Unknown type: " + type);
        }

        final Info info = new Info();
        for (final String nodeName : nodes) {
            final Plugin plugin = Bukkit.getPluginManager().getPlugin(nodeName);
            if (plugin != null && plugin instanceof InfoCommandHandler) {
                ((InfoCommandHandler)plugin).populateInfo(sender, query, info);
            }
        }

        /*
         * TODO Output Info object content
         * Will be done once the Chat API is a thing
         */
        if (info.hasPlayerInfo()) {
            // TODO
        }
        if (info.hasRegionInfo()) {
            // TODO
        }
        if (info.hasWarpInfo()) {
            // TODO
        }
        if (info.hasWorldInfo()) {
            // TODO
        }
        if (info.hasEntryNotes()) {
            // TODO
        }

        return true;
    }

    private boolean onUpdaterCommand(final CommandSender sender, final String[] args) {
        if (!Perms.hasUpdater(sender)) {
            sender.sendMessage(ColorUtil.colorize("&cYou do not have the permission to use that command"));
            return true;
        }
        if (this.plugin.getUpdater() == null) {
            sender.sendMessage(Updater.PREFIX + ChatColor.RED + "Updater is disabled in config");
        } else if (args.length != 2) {
            return false;
        } else {
            final String action = args[0].toLowerCase();
            final String nodeName = args[1];
            final boolean all = "all".equalsIgnoreCase(args[1]);
            if (!all && this.plugin.getUpdater().getPlugins().get(nodeName.toLowerCase()) == null) {
                sender.sendMessage(Updater.PREFIX + ChatColor.RED + "Unknown Node: " + nodeName);
            } else {
                switch (action) {
                    case "check":
                    case "status":
                        this.plugin.getUpdater().checkForUpdates(sender, all ? null : nodeName);
                        break;
                    case "download":
                    case "dl":
                        if (all) {
                            sender.sendMessage(Updater.PREFIX + ChatColor.RED + "Please select a specific Node to download");
                        } else {
                            this.plugin.getUpdater().downloadUpdate(sender, nodeName);
                        }
                        break;
                }
            }
        }
        return true;
    }
}
