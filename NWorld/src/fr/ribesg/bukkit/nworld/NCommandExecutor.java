package fr.ribesg.bukkit.nworld;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.ribesg.bukkit.ncore.lang.MessageId;

/**
 * @author Ribesg
 */
public class NCommandExecutor implements CommandExecutor {

    private final NWorld plugin;

    public NCommandExecutor(final NWorld instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
        if (command.getName().equalsIgnoreCase("nworld")) {
            if (sender.hasPermission(Permissions.CMD_WORLD) || sender.hasPermission(Permissions.USER) || sender.hasPermission(Permissions.ADMIN) || sender.isOp()) {
                return cmdWorld(sender, args);
            } else {
                plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                return true;
            }
        } else if (command.getName().equalsIgnoreCase("spawn")) {
            if (sender.hasPermission(Permissions.CMD_SPAWN) || sender.hasPermission(Permissions.USER) || sender.hasPermission(Permissions.ADMIN) || sender.isOp()) {
                return cmdSpawn(sender);
            } else {
                plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                return true;
            }
        } else if (command.getName().equalsIgnoreCase("setspawn")) {
            if (sender.hasPermission(Permissions.CMD_SETSPAWN) || sender.hasPermission(Permissions.ADMIN) || sender.isOp()) {
                return cmdSetSpawn(sender);
            } else {
                plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                return true;
            }
        } else {
            return false;
        }
    }

    private boolean cmdWorld(final CommandSender sender, final String[] args) {
        if (args.length == 0) {
            // Lists available worlds
            plugin.sendMessage(sender, MessageId.world_availableWorlds);
            for (final String worldName : plugin.getWorldMap().keySet()) {
                final boolean isTeleportationAllowed = plugin.getWorldMap().get(worldName);
                sender.sendMessage(ChatColor.BLACK + "- " + (isTeleportationAllowed ? ChatColor.GREEN : ChatColor.RED) + worldName);
            }
            return true;
        } else if (args.length == 1) {
            // Warp to world
            if (!(sender instanceof Player)) {
                plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
                return true;
            }
            final Player player = (Player) sender;
            final String worldName = args[0].toLowerCase();
            if (plugin.getWorldMap().containsKey(worldName)) {
                if (plugin.getWorldMap().get(worldName) || player.hasPermission(Permissions.CMD_WORLD_WARP_ALL)) {
                    final World world = plugin.getServer().getWorld(worldName);
                    player.teleport(plugin.getSpawnMap().get(worldName));
                    plugin.sendMessage(sender, MessageId.world_teleportedTo, world.getName());
                    return true;
                } else {
                    plugin.sendMessage(player, MessageId.world_warpToThisWorldDisallowed, plugin.getServer().getWorld(worldName).getName());
                    return true;
                }
            } else {
                plugin.sendMessage(player, MessageId.world_unknownWorld, args[0]);
                return true;
            }
        } else { // Handle subcommands
            // Get worldName
            String worldName = args[1];
            int i = 2;
            if (worldName.startsWith("\"")) {
                while (!worldName.endsWith("\"")) {
                    if (args.length >= i) {
                        return false;
                    }
                    worldName += ' ' + args[i++];
                }
            }

            // Isolate remaing arguments
            final String[] remainingArgs = Arrays.copyOfRange(args, i, args.length);

            // Check if world exists and if it's loaded
            final Path worldFolderPath = plugin.getServer().getWorldContainer().toPath();
            boolean exists = false;
            try {
                for (final Path p : Files.newDirectoryStream(worldFolderPath)) {
                    if (p.getFileName().toString().equalsIgnoreCase(worldName)) {
                        exists = true;
                    }
                }
            } catch (final IOException e) {
                plugin.getLogger().severe("Unable to iterate over Worlds");
                plugin.getLogger().severe("#################################");
                plugin.getLogger().severe("################### Error follows");
                plugin.getLogger().severe("#################################");
                e.printStackTrace();
                plugin.getLogger().severe("################################");
                plugin.getLogger().severe("################### End of error");
                plugin.getLogger().severe("################################");
            }
            final boolean loaded = plugin.getServer().getWorld(worldName) != null;

            // Get real world name if it exists
            String realWorldName = null;
            if (loaded) {
                realWorldName = plugin.getServer().getWorld(worldName).getName();
            } else if (exists) {
                try {
                    for (final Path p : Files.newDirectoryStream(worldFolderPath)) {
                        if (p.getFileName().toString().equalsIgnoreCase(worldName)) {
                            realWorldName = p.getFileName().toString();
                            break;
                        }
                    }
                } catch (final IOException e) {
                    plugin.getLogger().severe("Unable to iterate over Worlds in worldFolder");
                    plugin.getLogger().severe("#################################");
                    plugin.getLogger().severe("################### Error follows");
                    plugin.getLogger().severe("#################################");
                    e.printStackTrace();
                    plugin.getLogger().severe("################################");
                    plugin.getLogger().severe("################### End of error");
                    plugin.getLogger().severe("################################");
                }
            }
            switch (args[0].toLowerCase()) {
                case "create":
                    if (!sender.hasPermission(Permissions.CMD_WORLD_CREATE) && !sender.hasPermission(Permissions.ADMIN) && !sender.isOp()) {
                        plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                        return true;
                    }
                    if (exists) {
                        plugin.sendMessage(sender, MessageId.world_alreadyExists, realWorldName);
                        return true;
                    } else {
                        long seed = new Random().nextLong();
                        if (remainingArgs.length > 0) {
                            String seedString = remainingArgs[0];
                            for (i = 1; i < remainingArgs.length; i++) {
                                seedString += ' ' + remainingArgs[i];
                            }
                            try {
                                seed = Long.parseLong(seedString);
                            } catch (final NumberFormatException e) {
                                seed = seedString.hashCode();
                            }
                        }
                        final WorldCreator newWorld = new WorldCreator(worldName);
                        newWorld.seed(seed);
                        if (plugin.getPluginConfig().getBroadcastOnWorldCreate() == 1) {
                            plugin.broadcastMessage(MessageId.world_creatingWorldMayBeLaggy);
                        } else {
                            plugin.sendMessage(sender, MessageId.world_creatingWorldMayBeLaggy);
                        }
                        final World createdWorld = plugin.getServer().createWorld(newWorld);
                        if (plugin.getPluginConfig().getBroadcastOnWorldCreate() == 1) {
                            plugin.broadcastMessage(MessageId.world_created);
                        } else {
                            plugin.sendMessage(sender, MessageId.world_created);
                        }
                        plugin.getWorldMap().put(createdWorld.getName(), false);
                        return true;
                    }
                case "load":
                    if (!sender.hasPermission(Permissions.CMD_WORLD_LOAD) && !sender.hasPermission(Permissions.ADMIN) && !sender.isOp()) {
                        plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                        return true;
                    }
                    if (!exists) {
                        plugin.sendMessage(sender, MessageId.world_unknownWorld, worldName);
                        return true;
                    } else if (loaded) {
                        plugin.sendMessage(sender, MessageId.world_alreadyLoaded, realWorldName);
                        return true;
                    } else {
                        if (plugin.getPluginConfig().getBroadcastOnWorldLoad() == 1) {
                            plugin.broadcastMessage(MessageId.world_loadingWorldMayBeLaggy);
                        } else {
                            plugin.sendMessage(sender, MessageId.world_loadingWorldMayBeLaggy);
                        }
                        final World createdWorld = plugin.getServer().createWorld(new WorldCreator(realWorldName));
                        if (plugin.getPluginConfig().getBroadcastOnWorldLoad() == 1) {
                            plugin.broadcastMessage(MessageId.world_loaded);
                        } else {
                            plugin.sendMessage(sender, MessageId.world_loaded);
                        }
                        plugin.getWorldMap().put(createdWorld.getName(), false);
                        return true;
                    }
                case "unload":
                    if (!sender.hasPermission(Permissions.CMD_WORLD_UNLOAD) && !sender.hasPermission(Permissions.ADMIN) && !sender.isOp()) {
                        plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                        return true;
                    }
                    if (!exists) {
                        plugin.sendMessage(sender, MessageId.world_unknownWorld, worldName);
                        return true;
                    } else if (!loaded) {
                        plugin.sendMessage(sender, MessageId.world_notLoaded, realWorldName);
                        return true;
                    } else {
                        if (plugin.getPluginConfig().getBroadcastOnWorldUnload() == 1) {
                            plugin.broadcastMessage(MessageId.world_unloadingWorldMayBeLaggy);
                        } else {
                            plugin.sendMessage(sender, MessageId.world_unloadingWorldMayBeLaggy);
                        }
                        plugin.getServer().unloadWorld(realWorldName, true);
                        if (plugin.getPluginConfig().getBroadcastOnWorldUnload() == 1) {
                            plugin.broadcastMessage(MessageId.world_unloaded);
                        } else {
                            plugin.sendMessage(sender, MessageId.world_unloaded);
                        }
                        plugin.getWorldMap().remove(realWorldName);
                        return true;
                    }
                case "allow":
                case "allowwarp":
                    if (!sender.hasPermission(Permissions.CMD_WORLD_WARP_EDIT) && !sender.hasPermission(Permissions.ADMIN) && !sender.isOp()) {
                        plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                        return true;
                    }
                    if (!exists) {
                        plugin.sendMessage(sender, MessageId.world_unknownWorld, worldName);
                        return true;
                    } else if (!loaded) {
                        plugin.sendMessage(sender, MessageId.world_notLoaded, realWorldName);
                        return true;
                    } else {
                        if (plugin.getWorldMap().get(worldName)) {
                            plugin.sendMessage(sender, MessageId.world_alreadyAllowed, realWorldName);
                            return true;
                        } else {
                            plugin.getWorldMap().put(worldName, true);
                            plugin.sendMessage(sender, MessageId.world_allowedWarp, realWorldName);
                            return true;
                        }
                    }
                case "deny":
                case "denywarp":
                    if (!sender.hasPermission(Permissions.CMD_WORLD_WARP_EDIT) && !sender.hasPermission(Permissions.ADMIN) && !sender.isOp()) {
                        plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                        return true;
                    }
                    if (!exists) {
                        plugin.sendMessage(sender, MessageId.world_unknownWorld, worldName);
                        return true;
                    } else if (!loaded) {
                        plugin.sendMessage(sender, MessageId.world_notLoaded, realWorldName);
                        return true;
                    } else {
                        if (plugin.getWorldMap().get(worldName)) {
                            plugin.getWorldMap().put(worldName, false);
                            plugin.sendMessage(sender, MessageId.world_disallowedWarp, realWorldName);
                            return true;
                        } else {
                            plugin.sendMessage(sender, MessageId.world_alreadyDisallowed, realWorldName);
                            return true;
                        }
                    }
                default:
                    return false;
            }
        }
    }

    private boolean cmdSpawn(final CommandSender sender) {
        if (!(sender instanceof Player)) {
            plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
            return true;
        }
        final Player player = (Player) sender;
        player.teleport(plugin.getSpawnMap().get(player.getWorld().getName().toLowerCase()));
        plugin.sendMessage(player, MessageId.world_teleportingToSpawn);
        return true;
    }

    private boolean cmdSetSpawn(final CommandSender sender) {
        if (!(sender instanceof Player)) {
            plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
            return true;
        }
        final Player player = (Player) sender;
        final Location loc = player.getLocation();
        player.getWorld().setSpawnLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        plugin.getSpawnMap().put(player.getWorld().getName().toLowerCase(), loc);
        plugin.sendMessage(player, MessageId.world_settingSpawnPoint, player.getWorld().getName());
        return true;
    }
}
