/***************************************************************************
 * Project file:    NPlugins - NGeneral - TeleportCommands.java            *
 * Full Class name: fr.ribesg.bukkit.ngeneral.simplefeature.TeleportCommands
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral.simplefeature;

import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.node.world.WorldNode;
import fr.ribesg.bukkit.ncore.util.PlayerUtil;
import fr.ribesg.bukkit.ngeneral.NGeneral;
import fr.ribesg.bukkit.ngeneral.Perms;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Ribesg
 */
public class TeleportCommands implements CommandExecutor {

    private static final String COMMAND_TP      = "tp";
    private static final String COMMAND_TPPOS   = "tppos";
    private static final String COMMAND_TPHERE  = "tphere";
    private static final String COMMAND_TPTHERE = "tpthere";
    private static final String COMMAND_TPWORLD = "tpworld";
    private static final String COMMAND_TPBACK  = "tpback";

    private final NGeneral               plugin;
    private final Map<String, NLocation> backMap;

    public TeleportCommands(final NGeneral instance) {
        this.plugin = instance;
        this.backMap = new HashMap<>();
        this.plugin.setCommandExecutor(COMMAND_TP, this);
        this.plugin.setCommandExecutor(COMMAND_TPPOS, this);
        this.plugin.setCommandExecutor(COMMAND_TPHERE, this);
        this.plugin.setCommandExecutor(COMMAND_TPTHERE, this);
        this.plugin.setCommandExecutor(COMMAND_TPWORLD, this);
        this.plugin.setCommandExecutor(COMMAND_TPBACK, this);
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        switch (cmd.getName()) {
            case COMMAND_TP:
                if (Perms.hasTp(sender)) {
                    return this.execTpCommand(sender, args);
                } else {
                    this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    return true;
                }
            case COMMAND_TPPOS:
                if (Perms.hasTpPos(sender)) {
                    return this.execTpPosCommand(sender, args);
                } else {
                    this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    return true;
                }
            case COMMAND_TPHERE:
                if (Perms.hasTpHere(sender)) {
                    return this.execTpHereCommand(sender, args);
                } else {
                    this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    return true;
                }
            case COMMAND_TPTHERE:
                if (Perms.hasTpThere(sender)) {
                    return this.execTpThereCommand(sender, args);
                } else {
                    this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    return true;
                }
            case COMMAND_TPWORLD:
                if (Perms.hasTpWorld(sender)) {
                    return this.execTpWorldCommand(sender, args);
                } else {
                    this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    return true;
                }
            case COMMAND_TPBACK:
                if (Perms.hasTpBack(sender)) {
                    return this.execTpBackCommand(sender, args);
                } else {
                    this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    return true;
                }
            default:
                return false;
        }
    }

    private boolean execTpCommand(final CommandSender sender, final String[] args) {
        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                this.plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
                return true;
            } else {
                final Player player = (Player)sender;
                final Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    this.plugin.sendMessage(player, MessageId.noPlayerFoundForGivenName, args[0]);
                    return true;
                } else {
                    this.backMap.put(player.getName(), new NLocation(player.getLocation()));
                    player.teleport(target);
                    this.plugin.sendMessage(player, MessageId.general_tp_youToTarget, target.getName());
                    return true;
                }
            }
        } else if (args.length == 2) {
            final Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                this.plugin.sendMessage(sender, MessageId.noPlayerFoundForGivenName, args[0]);
                return true;
            } else {
                for (final String playerName : args[0].split(",")) {
                    final Player player = Bukkit.getPlayer(playerName);
                    if (player == null) {
                        this.plugin.sendMessage(sender, MessageId.noPlayerFoundForGivenName, args[0]);
                    } else {
                        this.backMap.put(player.getName(), new NLocation(player.getLocation()));
                        player.teleport(target);
                        this.plugin.sendMessage(player, MessageId.general_tp_somebodyToTarget, sender.getName(), target.getName());
                        this.plugin.sendMessage(sender, MessageId.general_tp_youSomebodyToTarget, player.getName(), target.getName());
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    private boolean execTpPosCommand(final CommandSender sender, final String[] args) {
        if (!(sender instanceof Player)) {
            this.plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
            return true;
        } else if (args.length == 1 || args.length == 3) {
            final Player player = (Player)sender;
            final Location loc = player.getLocation();
            final Location dest = new Location(loc.getWorld(), 0, 0, 0, loc.getYaw(), loc.getPitch());
            if (args.length == 1) {
                try {
                    final String[] split = args[0].split(";");
                    final double x = Double.parseDouble(split[0]);
                    final double y = Double.parseDouble(split[1]);
                    final double z = Double.parseDouble(split[2]);
                    dest.add(x, y, z);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    return false;
                }
            } else {
                try {
                    final double x = Double.parseDouble(args[0]);
                    final double y = Double.parseDouble(args[1]);
                    final double z = Double.parseDouble(args[2]);
                    dest.add(x, y, z);
                } catch (final NumberFormatException e) {
                    return false;
                }
            }
            this.backMap.put(player.getName(), new NLocation(player.getLocation()));
            player.teleport(dest);
            this.plugin.sendMessage(player, MessageId.general_tp_youToLocation, "<" + dest.getX() +
                                                                                ';' + dest.getY() +
                                                                                ';' + dest.getZ() +
                                                                                '>');
            return true;
        } else if (args.length == 2 || args.length == 4) {
            final Player player = (Player)sender;
            final double x;
            final double y;
            final double z;
            if (args.length == 2) {
                try {
                    final String[] split = args[1].split(";");
                    x = Double.parseDouble(split[0]);
                    y = Double.parseDouble(split[1]);
                    z = Double.parseDouble(split[2]);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    return false;
                }
            } else {
                try {
                    x = Double.parseDouble(args[1]);
                    y = Double.parseDouble(args[2]);
                    z = Double.parseDouble(args[3]);
                } catch (final NumberFormatException e) {
                    return false;
                }
            }
            final World world = player.getWorld();
            for (final String playerName : args[0].split(",")) {
                final Player playerToTeleport = Bukkit.getPlayer(playerName);
                if (playerToTeleport == null) {
                    this.plugin.sendMessage(sender, MessageId.noPlayerFoundForGivenName, args[0]);
                } else {
                    this.backMap.put(playerToTeleport.getName(), new NLocation(playerToTeleport.getLocation()));
                    final Location loc = playerToTeleport.getLocation();
                    final Location dest = new Location(world, x, y, z, loc.getYaw(), loc.getPitch());
                    playerToTeleport.teleport(dest);
                    this.plugin.sendMessage(playerToTeleport, MessageId.general_tp_somebodyToLocation, sender.getName());
                    this.plugin.sendMessage(sender, MessageId.general_tp_youSomebodyToLocation, playerToTeleport.getName(), "<" + x + ';' + y + ';' + z + '>');
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean execTpHereCommand(final CommandSender sender, final String[] args) {
        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                this.plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
                return true;
            } else {
                final Player target = (Player)sender;
                for (final String playerName : args[0].split(",")) {
                    final Player player = Bukkit.getPlayer(playerName);
                    if (player == null) {
                        this.plugin.sendMessage(sender, MessageId.noPlayerFoundForGivenName, args[0]);
                    } else {
                        this.backMap.put(player.getName(), new NLocation(player.getLocation()));
                        player.teleport(target);
                        this.plugin.sendMessage(player, MessageId.general_tp_somebodyToHim, sender.getName());
                        this.plugin.sendMessage(sender, MessageId.general_tp_youSomebodyToYou, player.getName());
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    private boolean execTpWorldCommand(final CommandSender sender, final String[] args) {
        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                this.plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
            } else {
                final Player player = (Player)sender;
                final String worldName = args[0];
                final World world = Bukkit.getWorld(worldName);
                if (world == null) {
                    this.plugin.sendMessage(player, MessageId.general_tp_worldNotFound, worldName);
                } else {
                    final WorldNode worldNode = this.plugin.getCore().getWorldNode();
                    final Location spawnLoc;
                    if (worldNode == null) {
                        spawnLoc = world.getSpawnLocation();
                    } else {
                        spawnLoc = worldNode.getWorldSpawnLocation(world.getName());
                    }
                    this.backMap.put(player.getName(), new NLocation(spawnLoc));
                    player.teleport(spawnLoc);
                    this.plugin.sendMessage(player, MessageId.general_tp_youToWorld, world.getName());
                }
            }
            return true;
        } else if (args.length == 2) {
            final String worldName = args[1];
            final World world = Bukkit.getWorld(worldName);
            if (world == null) {
                this.plugin.sendMessage(sender, MessageId.general_tp_worldNotFound, worldName);
            } else {
                final WorldNode worldNode = this.plugin.getCore().getWorldNode();
                final Location spawnLoc;
                if (worldNode == null) {
                    spawnLoc = world.getSpawnLocation();
                } else {
                    spawnLoc = worldNode.getWorldSpawnLocation(world.getName());
                }
                for (final String playerName : args[0].split(",")) {
                    final Player toTeleport = Bukkit.getPlayer(playerName);
                    if (toTeleport == null) {
                        this.plugin.sendMessage(sender, MessageId.noPlayerFoundForGivenName, args[0]);
                    } else {
                        this.backMap.put(toTeleport.getName(), new NLocation(toTeleport.getLocation()));
                        toTeleport.teleport(spawnLoc);
                        this.plugin.sendMessage(sender, MessageId.general_tp_youSomebodyToWorld, toTeleport.getName(), world.getName());
                        this.plugin.sendMessage(toTeleport, MessageId.general_tp_somebodyToWorld, sender.getName(), world.getName());
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean execTpThereCommand(final CommandSender sender, final String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                this.plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
                return true;
            } else {
                final Player player = (Player)sender;
                final Block targetBlock = PlayerUtil.getTargetBlock(player, null, Integer.MAX_VALUE);
                if (targetBlock == null) {
                    this.plugin.sendMessage(player, MessageId.general_tp_noTarget);
                    return true;
                } else {
                    final Location loc = targetBlock.getLocation();
                    loc.add(0.5, 0.05, 0.5);
                    while (loc.getBlock().getType().isSolid() || loc.getBlock().getRelative(BlockFace.UP).getType().isSolid()) {
                        loc.add(0, 1, 0);
                    }
                    loc.setPitch(player.getLocation().getPitch());
                    loc.setYaw(player.getLocation().getYaw());
                    this.backMap.put(player.getName(), new NLocation(player.getLocation()));
                    player.teleport(loc);
                    this.plugin.sendMessage(player, MessageId.general_tp_youToLocation, "<" + loc.getX() +
                                                                                        ';' + loc.getY() +
                                                                                        ';' + loc.getZ() +
                                                                                        '>');
                    return true;
                }
            }
        } else if (args.length == 1) {
            if (!(sender instanceof Player)) {
                this.plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
                return true;
            } else {
                final Player player = (Player)sender;
                final Block targetBlock = PlayerUtil.getTargetBlock(player, null, Integer.MAX_VALUE);
                if (targetBlock == null) {
                    this.plugin.sendMessage(player, MessageId.general_tp_noTarget);
                    return true;
                } else {
                    final Location loc = targetBlock.getLocation();
                    loc.add(0.5, 0.05, 0.5);
                    while (loc.getBlock().getType().isSolid() || loc.getBlock().getRelative(BlockFace.UP).getType().isSolid()) {
                        loc.add(0, 1, 0);
                    }
                    for (final String playerName : args[0].split(",")) {
                        final Player toTeleport = Bukkit.getPlayer(playerName);
                        if (toTeleport == null) {
                            this.plugin.sendMessage(sender, MessageId.noPlayerFoundForGivenName, args[0]);
                        } else {
                            this.backMap.put(toTeleport.getName(), new NLocation(toTeleport.getLocation()));
                            loc.setPitch(toTeleport.getLocation().getPitch());
                            loc.setYaw(toTeleport.getLocation().getYaw());
                            toTeleport.teleport(loc);
                            this.plugin.sendMessage(toTeleport, MessageId.general_tp_somebodyToLocation, sender.getName());
                            this.plugin.sendMessage(sender, MessageId.general_tp_youSomebodyToLocation, toTeleport.getName(), "<" + loc.getX() +
                                                                                                                              ';' + loc.getY() +
                                                                                                                              ';' + loc.getZ() +
                                                                                                                              '>');
                        }
                    }
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    private boolean execTpBackCommand(final CommandSender sender, final String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                this.plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
                return true;
            } else {
                final Player player = (Player)sender;
                final NLocation loc = this.backMap.remove(player.getName());
                if (loc == null) {
                    this.plugin.sendMessage(player, MessageId.general_tp_youNoKnownBack);
                    return true;
                }
                final Location bukkitLoc = loc.toBukkitLocation();
                if (bukkitLoc == null) {
                    this.plugin.sendMessage(player, MessageId.general_tp_youBackWorldUnloaded, loc.getWorldName());
                    return true;
                }
                player.teleport(bukkitLoc);
                this.plugin.sendMessage(player, MessageId.general_tp_youTeleportedBack);
                return true;
            }
        } else if (args.length == 1) {
            for (final String playerName : args[0].split(",")) {
                final Player player = Bukkit.getPlayer(playerName);
                if (player == null) {
                    this.plugin.sendMessage(sender, MessageId.noPlayerFoundForGivenName, args[0]);
                } else {
                    final NLocation loc = this.backMap.remove(player.getName());
                    if (loc == null) {
                        this.plugin.sendMessage(sender, MessageId.general_tp_somebodyNoKnownBack, player.getName());
                        return true;
                    }
                    final Location bukkitLoc = loc.toBukkitLocation();
                    if (bukkitLoc == null) {
                        this.plugin.sendMessage(sender, MessageId.general_tp_somebodyBackWorldUnloaded, player.getName(), loc.getWorldName());
                        return true;
                    }
                    player.teleport(bukkitLoc);
                    this.plugin.sendMessage(player, MessageId.general_tp_somebodyTeleportedYouBack, sender.getName());
                    this.plugin.sendMessage(sender, MessageId.general_tp_youTeleportedSomebodyBack, player.getName());
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
