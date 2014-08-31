/***************************************************************************
 * Project file:    NPlugins - NWorld - WorldCommandExecutor.java          *
 * Full Class name: fr.ribesg.bukkit.nworld.WorldCommandExecutor           *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nworld;

import fr.ribesg.bukkit.ncore.common.NLocation;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.util.ArgumentParser;
import fr.ribesg.bukkit.ncore.util.WorldUtil;
import fr.ribesg.bukkit.nworld.warp.Warp;
import fr.ribesg.bukkit.nworld.world.AdditionalWorld;
import fr.ribesg.bukkit.nworld.world.GeneralWorld;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.ChatPaginator;

/**
 * @author Ribesg
 */
public class WorldCommandExecutor implements CommandExecutor {

    private final NWorld plugin;

    public WorldCommandExecutor(final NWorld instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
        if ("nworld".equals(command.getName())) {
            if (Perms.hasWorld(sender)) {
                return this.cmdWorld(sender, args);
            } else {
                this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                return true;
            }
        } else if ("spawn".equals(command.getName())) {
            if (Perms.hasSpawn(sender)) {
                return this.cmdSpawn(sender);
            } else {
                this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                return true;
            }
        } else if ("setspawn".equals(command.getName())) {
            if (Perms.hasSetSpawn(sender)) {
                return this.cmdSetSpawn(sender);
            } else {
                this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                return true;
            }
        } else if ("warp".equals(command.getName())) {
            if (Perms.hasWarp(sender)) {
                return this.cmdWarp(sender, args);
            } else {
                this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                return true;
            }
        } else if ("setwarp".equals(command.getName())) {
            if (Perms.hasSetWarp(sender)) {
                return this.cmdSetWarp(sender, args);
            } else {
                this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                return true;
            }
        } else if ("delwarp".equals(command.getName())) {
            if (Perms.hasDelWarp(sender)) {
                return this.cmdDelWarp(sender, args);
            } else {
                this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                return true;
            }
        } else {
            return false;
        }
    }

    private boolean cmdWorld(final CommandSender sender, final String[] args) {
        String[] parsedArgs = ArgumentParser.joinArgsWithQuotes(args);
        if (parsedArgs.length == 0) {
            // Lists available worlds
            this.plugin.sendMessage(sender, MessageId.world_availableWorlds);
            for (final GeneralWorld world : this.plugin.getWorlds()) {
                final boolean hasPermission = Perms.has(sender, world.getRequiredPermission());
                if (world.isEnabled()) {
                    if (hasPermission) {
                        if (world.isHidden()) {
                            sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + world.getWorldName());
                        } else {
                            sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.GREEN + world.getWorldName());
                        }
                    } else if (!world.isHidden()) {
                        sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.RED + world.getWorldName());
                    }
                } else if (Perms.isAdmin(sender)) {
                    sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.BLACK + world.getWorldName());
                }
            }
            return true;
        } else if (parsedArgs.length == 1) {
            // Warp to world
            return this.subCmdWorldWarp(sender, parsedArgs[0]);
        } else { // Handle subcommands
            final String subCmd = parsedArgs[0].toLowerCase();
            parsedArgs = Arrays.copyOfRange(parsedArgs, 1, parsedArgs.length);
            switch (subCmd) {
                case "create":
                    return this.subCmdWorldCreate(sender, parsedArgs);
                case "load":
                    return this.subCmdWorldLoad(sender, parsedArgs);
                case "unload":
                    return this.subCmdWorldUnload(sender, parsedArgs);
                case "hidden":
                case "sethidden":
                    return this.subCmdWorldSetHidden(sender, parsedArgs);
                case "perm":
                case "setperm":
                    return this.subCmdWorldSetPerm(sender, parsedArgs);
                case "nether":
                case "setnether":
                    return this.subCmdWorldSetNether(sender, parsedArgs);
                case "end":
                case "setend":
                    return this.subCmdWorldSetEnd(sender, parsedArgs);
                case "reload":
                case "rld":
                    return this.subCmdWorldReload(sender, parsedArgs);
                default:
                    return false;
            }
        }
    }

    private boolean subCmdWorldWarp(final CommandSender sender, final String givenWorldName) {
        if (!(sender instanceof Player)) {
            this.plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
            return true;
        }
        final Player player = (Player)sender;
        final GeneralWorld world = this.plugin.getWorlds().get(givenWorldName);
        if (world != null && world.isEnabled()) {
            if (Perms.has(player, world.getRequiredPermission()) || Perms.hasWorldWarpAll(player)) {
                final Location loc = world.getSpawnLocation().toBukkitLocation();
                loc.getChunk().load();
                Bukkit.getScheduler().runTaskLater(this.plugin, new BukkitRunnable() {

                    @Override
                    public void run() {
                        player.teleport(loc);
                    }
                }, 1L);
                this.plugin.sendMessage(sender, MessageId.world_teleportedToWorld, world.getWorldName());
                return true;
            } else if (!world.isHidden()) {
                this.plugin.sendMessage(player, MessageId.world_warpToThisWorldDisallowed, this.plugin.getServer().getWorld(givenWorldName).getName());
                return true;
            } else {
                this.plugin.sendMessage(player, MessageId.unknownWorld, givenWorldName);
                return true;
            }
        } else {
            this.plugin.sendMessage(player, MessageId.unknownWorld, givenWorldName);
            return true;
        }
    }

    private boolean subCmdWorldCreate(final CommandSender sender, final String[] args) {
        if (!Perms.hasWorldCreate(sender)) {
            this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
            return true;
        }
        final String realWorldName = WorldUtil.getRealWorldName(args[0]);
        if (realWorldName != null) {
            this.plugin.sendMessage(sender, MessageId.world_alreadyExists, realWorldName);
            return true;
        } else {
            final String worldName = args[0];
            long seed = new Random().nextLong();
            if (args.length > 1) {
                try {
                    seed = Long.parseLong(args[1]);
                } catch (final NumberFormatException e) {
                    seed = args[1].hashCode();
                }
            }

            String requiredPermission = this.plugin.getPluginConfig().getDefaultRequiredPermission();
            if (args.length > 2) {
                requiredPermission = args[2];
            }

            boolean hidden = this.plugin.getPluginConfig().isDefaultHidden();
            WorldType type = null;

            if (args.length > 3) {
                final Set<String> additionalArguments = new HashSet<>();
                for (int i = 3; i < args.length; i++) {
                    additionalArguments.add(args[i].toLowerCase());
                }

                if (additionalArguments.contains("public")) {
                    hidden = false;
                } else if (additionalArguments.contains("private")) {
                    hidden = true;
                }

                if (additionalArguments.contains("flat")) {
                    type = WorldType.FLAT;
                } else if (additionalArguments.contains("large") || additionalArguments.contains("large_biome")) {
                    type = WorldType.LARGE_BIOMES;
                }
            }

            final AdditionalWorld nWorld = new AdditionalWorld(this.plugin, worldName, seed, null, requiredPermission, true, hidden, false, false);

            if (this.plugin.getPluginConfig().getBroadcastOnWorldCreate() == 1) {
                this.plugin.broadcastMessage(MessageId.world_creatingWorldMayBeLaggy);
            } else {
                this.plugin.sendMessage(sender, MessageId.world_creatingWorldMayBeLaggy);
            }

            nWorld.create(type);

            if (this.plugin.getPluginConfig().getBroadcastOnWorldCreate() == 1) {
                this.plugin.broadcastMessage(MessageId.world_created);
            } else {
                this.plugin.sendMessage(sender, MessageId.world_created);
            }

            this.plugin.getWorlds().put(nWorld.getWorldName(), nWorld);

            return true;
        }
    }

    private boolean subCmdWorldLoad(final CommandSender sender, final String[] args) {
        if (!Perms.hasWorldLoad(sender)) {
            this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
            return true;
        }
        final String realWorldName = WorldUtil.getRealWorldName(args[0]);
        if (realWorldName == null) {
            this.plugin.sendMessage(sender, MessageId.unknownWorld, args[0]);
            return true;
        } else if (this.plugin.getServer().getWorld(realWorldName) != null) {
            this.plugin.sendMessage(sender, MessageId.world_alreadyLoaded, realWorldName);
            return true;
        } else {
            boolean wasKnown = true;
            AdditionalWorld world = this.plugin.getWorlds().getAdditional().get(realWorldName);

            // Main world & messages
            if (this.plugin.getPluginConfig().getBroadcastOnWorldLoad() == 1) {
                this.plugin.broadcastMessage(MessageId.world_loadingWorldMayBeLaggy);
            } else {
                this.plugin.sendMessage(sender, MessageId.world_loadingWorldMayBeLaggy);
            }

            if (world == null) { // Load a never-loaded world
                wasKnown = false;
                final World newLoaded = new WorldCreator(realWorldName).createWorld();
                final long seed = newLoaded.getSeed();
                final NLocation spawn = new NLocation(newLoaded.getSpawnLocation());
                final String requiredPermission = this.plugin.getPluginConfig().getDefaultRequiredPermission();
                final boolean hidden = this.plugin.getPluginConfig().isDefaultHidden();
                world = new AdditionalWorld(this.plugin, realWorldName, seed, spawn, requiredPermission, true, hidden, false, false);
                this.plugin.getWorlds().put(realWorldName, world);
            } else {
                world.load();
            }
            this.plugin.getWarps().worldEnabled(world.getWorldName());
            if (this.plugin.getPluginConfig().getBroadcastOnWorldLoad() == 1) {
                this.plugin.broadcastMessage(MessageId.world_loaded);
            } else {
                this.plugin.sendMessage(sender, MessageId.world_loaded);
            }
            if (!wasKnown) {
                // TODO What if it's a single player adventure world with Nether etc
                return true;
            }

            // Nether world & messages
            if (world.hasNether()) {
                if (this.plugin.getPluginConfig().getBroadcastOnWorldLoad() == 1) {
                    this.plugin.broadcastMessage(MessageId.world_loadingWorldMayBeLaggy);
                } else {
                    this.plugin.sendMessage(sender, MessageId.world_loadingWorldMayBeLaggy);
                }

                world.getNetherWorld().load();
                this.plugin.getWarps().worldEnabled(world.getNetherWorld().getWorldName());

                if (this.plugin.getPluginConfig().getBroadcastOnWorldLoad() == 1) {
                    this.plugin.broadcastMessage(MessageId.world_loaded);
                } else {
                    this.plugin.sendMessage(sender, MessageId.world_loaded);
                }
            }

            // End world & messages
            if (world.hasEnd()) {
                if (this.plugin.getPluginConfig().getBroadcastOnWorldLoad() == 1) {
                    this.plugin.broadcastMessage(MessageId.world_loadingWorldMayBeLaggy);
                } else {
                    this.plugin.sendMessage(sender, MessageId.world_loadingWorldMayBeLaggy);
                }

                world.getEndWorld().load();
                this.plugin.getWarps().worldEnabled(world.getEndWorld().getWorldName());

                if (this.plugin.getPluginConfig().getBroadcastOnWorldLoad() == 1) {
                    this.plugin.broadcastMessage(MessageId.world_loaded);
                } else {
                    this.plugin.sendMessage(sender, MessageId.world_loaded);
                }
            }

            world.setEnabled(true);
            return true;
        }
    }

    private boolean subCmdWorldUnload(final CommandSender sender, final String[] args) {
        if (!Perms.hasWorldUnload(sender)) {
            this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
            return true;
        }
        final String realWorldName = WorldUtil.getRealWorldName(args[0]);
        if (realWorldName == null) {
            this.plugin.sendMessage(sender, MessageId.unknownWorld, args[0]);
            return true;
        } else if (this.plugin.getServer().getWorld(realWorldName) == null) {
            this.plugin.sendMessage(sender, MessageId.world_notLoaded, realWorldName);
            return true;
        } else {
            final AdditionalWorld world = this.plugin.getWorlds().getAdditional().get(realWorldName);
            if (world == null) {
                this.plugin.sendMessage(sender, MessageId.unknownWorld, args[0]);
                return true;
            }

            if (this.plugin.getPluginConfig().getBroadcastOnWorldUnload() == 1) {
                this.plugin.broadcastMessage(MessageId.world_unloadingWorldMayBeLaggy);
            } else {
                this.plugin.sendMessage(sender, MessageId.world_unloadingWorldMayBeLaggy);
            }

            world.unload();
            this.plugin.getWarps().worldDisabled(world.getWorldName());

            if (this.plugin.getPluginConfig().getBroadcastOnWorldUnload() == 1) {
                this.plugin.broadcastMessage(MessageId.world_unloaded);
            } else {
                this.plugin.sendMessage(sender, MessageId.world_unloaded);
            }

            if (world.hasNether()) {
                if (this.plugin.getPluginConfig().getBroadcastOnWorldUnload() == 1) {
                    this.plugin.broadcastMessage(MessageId.world_unloadingWorldMayBeLaggy);
                } else {
                    this.plugin.sendMessage(sender, MessageId.world_unloadingWorldMayBeLaggy);
                }

                world.getNetherWorld().unload();
                this.plugin.getWarps().worldDisabled(world.getNetherWorld().getWorldName());

                if (this.plugin.getPluginConfig().getBroadcastOnWorldUnload() == 1) {
                    this.plugin.broadcastMessage(MessageId.world_unloaded);
                } else {
                    this.plugin.sendMessage(sender, MessageId.world_unloaded);
                }
            }

            if (world.hasEnd()) {
                if (this.plugin.getPluginConfig().getBroadcastOnWorldUnload() == 1) {
                    this.plugin.broadcastMessage(MessageId.world_unloadingWorldMayBeLaggy);
                } else {
                    this.plugin.sendMessage(sender, MessageId.world_unloadingWorldMayBeLaggy);
                }

                world.getEndWorld().unload();
                this.plugin.getWarps().worldDisabled(world.getEndWorld().getWorldName());

                if (this.plugin.getPluginConfig().getBroadcastOnWorldUnload() == 1) {
                    this.plugin.broadcastMessage(MessageId.world_unloaded);
                } else {
                    this.plugin.sendMessage(sender, MessageId.world_unloaded);
                }
            }

            world.setEnabled(false);
            return true;
        }
    }

    private boolean subCmdWorldSetHidden(final CommandSender sender, final String[] args) {
        if (!Perms.hasWorldSetHidden(sender)) {
            this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
            return true;
        }
        if (args.length != 2) {
            return false;
        }
        final String worldName = args[0];
        final Boolean hidden = ArgumentParser.parseBoolean(args[1]);
        if (hidden == null) {
            return false;
        }
        final GeneralWorld world = this.plugin.getWorlds().get(worldName);
        if (world != null) {
            world.setHidden(hidden);
            if (hidden) {
                this.plugin.sendMessage(sender, MessageId.world_worldHiddenTrue, worldName);
            } else {
                this.plugin.sendMessage(sender, MessageId.world_worldHiddenFalse, worldName);
            }
            return true;
        } else {
            this.plugin.sendMessage(sender, MessageId.unknownWorld, worldName);
            return true;
        }
    }

    private boolean subCmdWorldSetPerm(final CommandSender sender, final String[] args) {
        if (!Perms.hasWorldSetPerm(sender)) {
            this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
            return true;
        }
        if (args.length != 2) {
            return false;
        }
        final String worldName = args[0];
        String permission = args[1];
        if (this.plugin.getPluginConfig().getPermissionShortcuts().containsKey(permission.toLowerCase())) {
            permission = this.plugin.getPluginConfig().getPermissionShortcuts().get(permission.toLowerCase());
        }
        final GeneralWorld world = this.plugin.getWorlds().get(worldName);
        if (world != null) {
            world.setRequiredPermission(permission);
            this.plugin.sendMessage(sender, MessageId.world_changedWorldRequiredPermission, worldName, permission);
            return true;
        } else {
            this.plugin.sendMessage(sender, MessageId.unknownWorld, worldName);
            return true;
        }
    }

    private boolean subCmdWorldSetNether(final CommandSender sender, final String[] args) {
        if (!Perms.hasWorldSetNether(sender)) {
            this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
            return true;
        }
        if (args.length != 2) {
            return false;
        }
        final String worldName = args[0];
        final Boolean value = ArgumentParser.parseBoolean(args[1]);
        if (value == null) {
            return false;
        }
        final AdditionalWorld world = this.plugin.getWorlds().getAdditional().get(worldName);
        if (world != null) {
            if (world.hasNether()) {
                if (value) {
                    this.plugin.sendMessage(sender, MessageId.world_worldNetherAlreadyEnabled, worldName);
                } else {
                    this.plugin.sendMessage(sender, MessageId.world_worldNetherDisabled, worldName);
                    world.setNether(false);
                }
            } else {
                if (value) {
                    this.plugin.sendMessage(sender, MessageId.world_worldNetherEnabled, worldName);
                    world.setNether(true);
                } else {
                    this.plugin.sendMessage(sender, MessageId.world_worldNetherAlreadyDisabled, worldName);
                }
            }
            return true;
        } else {
            this.plugin.sendMessage(sender, MessageId.unknownWorld, worldName);
            return true;
        }
    }

    private boolean subCmdWorldSetEnd(final CommandSender sender, final String[] args) {
        if (!Perms.hasWorldSetEnd(sender)) {
            this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
            return true;
        }
        if (args.length != 2) {
            return false;
        }
        final String worldName = args[0];
        final Boolean value = ArgumentParser.parseBoolean(args[1]);
        if (value == null) {
            return false;
        }
        final AdditionalWorld world = this.plugin.getWorlds().getAdditional().get(worldName);
        if (world != null) {
            if (world.hasEnd()) {
                if (value) {
                    this.plugin.sendMessage(sender, MessageId.world_worldEndAlreadyEnabled, worldName);
                } else {
                    this.plugin.sendMessage(sender, MessageId.world_worldEndDisabled, worldName);
                    world.setEnd(false);
                }
            } else {
                if (value) {
                    this.plugin.sendMessage(sender, MessageId.world_worldEndEnabled, worldName);
                    world.setEnd(true);
                } else {
                    this.plugin.sendMessage(sender, MessageId.world_worldEndAlreadyDisabled, worldName);
                }
            }
            return true;
        } else {
            this.plugin.sendMessage(sender, MessageId.unknownWorld, worldName);
            return true;
        }
    }

    private boolean subCmdWorldReload(final CommandSender sender, final String[] args) {
        if (!Perms.hasReload(sender)) {
            this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
            return true;
        }
        if (args.length != 1) {
            return false;
        }
        switch (args[0].toLowerCase()) {
            case "messages":
            case "mess":
            case "mes":
                try {
                    this.plugin.loadMessages();
                    this.plugin.sendMessage(sender, MessageId.cmdReloadMessages);
                } catch (final IOException e) {
                    this.plugin.error("An error occured when NWorld tried to load messages.yml", e);
                    this.plugin.sendMessage(sender, MessageId.cmdReloadError, "messages.yml");
                }
                return true;
            default:
                return false;
        }
    }

    private boolean cmdSpawn(final CommandSender sender) {
        if (!(sender instanceof Player)) {
            this.plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
            return true;
        }
        final Player player = (Player)sender;
        final int spawnBehaviour = this.plugin.getPluginConfig().getSpawnCommandBehaviour();
        final String worldName;
        if (spawnBehaviour == 0) {
            worldName = player.getWorld().getName();
        } else {
            worldName = Bukkit.getWorlds().get(0).getName();
        }
        final GeneralWorld world = this.plugin.getWorlds().get(worldName);
        player.teleport(world.getSpawnLocation().toBukkitLocation());
        this.plugin.sendMessage(player, MessageId.world_teleportingToSpawn);
        return true;
    }

    private boolean cmdSetSpawn(final CommandSender sender) {
        if (!(sender instanceof Player)) {
            this.plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
            return true;
        }
        final Player player = (Player)sender;
        final GeneralWorld world = this.plugin.getWorlds().get(player.getWorld().getName());
        world.setSpawnLocation(player.getLocation());
        this.plugin.sendMessage(player, MessageId.world_settingSpawnPoint, player.getWorld().getName());
        return true;
    }

    private boolean cmdWarp(final CommandSender sender, String[] args) {
        if (args.length == 0) {
            // Lists available warps
            this.plugin.sendMessage(sender, MessageId.world_availableWarps);
            final StringBuilder builder = new StringBuilder();
            for (final Warp warp : this.plugin.getWarps()) {
                if (warp.isEnabled()) {
                    final boolean hasPermission = Perms.has(sender, warp.getRequiredPermission()) || Perms.hasWarpAll(sender);
                    if (hasPermission) {
                        if (warp.isHidden()) {
                            builder.append(ChatColor.GRAY + ", " + ChatColor.DARK_GREEN + warp.getName());
                        } else {
                            builder.append(ChatColor.GRAY + ", " + ChatColor.GREEN + warp.getName());
                        }
                    } else if (!warp.isHidden()) {
                        builder.append(ChatColor.GRAY + ", " + ChatColor.RED + warp.getName());
                    }
                }
            }
            if (builder.length() == 0) {
                sender.sendMessage(ChatColor.RED + "No warps");
            } else {
                final String warps = builder.toString().substring((ChatColor.GRAY + ", ").length());
                final String[] messages = ChatPaginator.wordWrap(warps, ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH);
                for (final String message : messages) {
                    sender.sendMessage(message);
                }
            }
            return true;
        } else if (args.length == 1) {
            // Warp to warp location
            return this.subCmdWarp(sender, args[0]);
        } else { // Handle subcommands
            final String subCmd = args[0].toLowerCase();
            args = Arrays.copyOfRange(args, 1, args.length);
            switch (subCmd) {
                case "hidden":
                case "sethidden":
                    return this.subCmdWarpSetHidden(sender, args);
                case "perm":
                case "setperm":
                    return this.subCmdWarpSetPerm(sender, args);
                default:
                    return false;
            }
        }
    }

    private boolean subCmdWarp(final CommandSender sender, final String givenWarpName) {
        if (!(sender instanceof Player)) {
            this.plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
            return true;
        }
        final Player player = (Player)sender;
        final String warpName = givenWarpName.toLowerCase();
        final Warp warp = this.plugin.getWarps().get(warpName);
        if (warp != null) {
            if (Perms.has(player, warp.getRequiredPermission()) || Perms.hasWarpAll(player)) {
                final Location loc = warp.getLocation().toBukkitLocation();
                if (loc == null) {
                    this.plugin.sendMessage(sender, MessageId.world_warpUnloadedWorld, warp.getLocation().getWorldName(), warp.getName());
                } else {
                    loc.getChunk().load();
                    Bukkit.getScheduler().runTaskLater(this.plugin, new BukkitRunnable() {

                        @Override
                        public void run() {
                            player.teleport(loc);
                        }
                    }, 1L);
                    this.plugin.sendMessage(sender, MessageId.world_teleportedToWarp, warp.getName());
                }
                return true;
            } else if (!warp.isHidden()) {
                this.plugin.sendMessage(player, MessageId.world_warpToThisWarpDisallowed, warp.getName());
                return true;
            } else {
                this.plugin.sendMessage(player, MessageId.world_unknownWarp, givenWarpName);
                return true;
            }
        } else {
            this.plugin.sendMessage(player, MessageId.world_unknownWarp, givenWarpName);
            return true;
        }
    }

    private boolean subCmdWarpSetHidden(final CommandSender sender, final String[] args) {
        if (!Perms.hasWarpSetHidden(sender)) {
            this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
            return true;
        }
        if (args.length != 2) {
            return false;
        }
        final String warpName = args[0];
        final Boolean hidden = ArgumentParser.parseBoolean(args[1]);
        if (hidden == null) {
            return false;
        }
        final Warp warp = this.plugin.getWarps().get(warpName);
        if (warp != null) {
            warp.setHidden(hidden);
            if (hidden) {
                this.plugin.sendMessage(sender, MessageId.world_warpHiddenTrue, warpName);
            } else {
                this.plugin.sendMessage(sender, MessageId.world_warpHiddenFalse, warpName);
            }
            return true;
        } else {
            this.plugin.sendMessage(sender, MessageId.world_unknownWarp, warpName);
            return true;
        }
    }

    private boolean subCmdWarpSetPerm(final CommandSender sender, final String[] args) {
        if (!Perms.hasWarpSetPerm(sender)) {
            this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
            return true;
        }
        if (args.length != 2) {
            return false;
        }
        final String warpName = args[0];
        String permission = args[1];
        if (this.plugin.getPluginConfig().getPermissionShortcuts().containsKey(permission.toLowerCase())) {
            permission = this.plugin.getPluginConfig().getPermissionShortcuts().get(permission.toLowerCase());
        }
        final Warp warp = this.plugin.getWarps().get(warpName);
        if (warp != null) {
            warp.setRequiredPermission(permission);
            this.plugin.sendMessage(sender, MessageId.world_changedWarpRequiredPermission, warpName, permission);
            return true;
        } else {
            this.plugin.sendMessage(sender, MessageId.world_unknownWarp, warpName);
            return true;
        }
    }

    private boolean cmdSetWarp(final CommandSender sender, final String[] args) {
        if (!(sender instanceof Player)) {
            this.plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
            return true;
        }
        if (args.length < 1) {
            return false;
        }
        final Player player = (Player)sender;
        final String warpName = args[0];
        boolean requiredPermissionProvided = false;
        String requiredPermission = this.plugin.getPluginConfig().getDefaultRequiredPermission();
        boolean hiddenProvided = false;
        boolean hidden = this.plugin.getPluginConfig().isDefaultHidden();
        if (args.length > 1) {
            requiredPermission = args[1];
            requiredPermissionProvided = true;
        }
        if (args.length > 2) {
            hidden = Boolean.parseBoolean(args[2]);
            hiddenProvided = true;
        }

        if (this.plugin.getWarps().containsKey(warpName)) {
            final Warp warp = this.plugin.getWarps().get(warpName);
            warp.setLocation(player.getLocation());
            if (requiredPermissionProvided) {
                warp.setRequiredPermission(requiredPermission);
            }
            if (hiddenProvided) {
                warp.setHidden(true);
            }
        } else {
            final Warp warp = new Warp(warpName, new NLocation(player.getLocation()), true, requiredPermission, hidden);
            this.plugin.getWarps().put(warpName, warp);
        }
        this.plugin.sendMessage(player, MessageId.world_settingWarpPoint, warpName);
        return true;
    }

    private boolean cmdDelWarp(final CommandSender sender, final String[] args) {
        if (!Perms.hasDelWarp(sender)) {
            this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
            return true;
        }
        if (args.length != 1) {
            return false;
        }
        final String warpName = args[0];
        final Warp warp = this.plugin.getWarps().get(warpName);
        if (warp == null) {
            this.plugin.sendMessage(sender, MessageId.world_unknownWarp, warpName);
        } else {
            this.plugin.getWarps().remove(warp.getName());
            this.plugin.sendMessage(sender, MessageId.world_warpRemoved, warpName);
        }
        return true;
    }
}
