package fr.ribesg.bukkit.ntheendagain;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.ribesg.bukkit.ncore.Utils;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ntheendagain.world.EndChunk;
import fr.ribesg.bukkit.ntheendagain.world.EndChunks;
import fr.ribesg.bukkit.ntheendagain.world.EndWorldHandler;

public class NCommandExecutor implements CommandExecutor {

    private final NTheEndAgain plugin;

    public NCommandExecutor(final NTheEndAgain instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
        if (commandLabel.equalsIgnoreCase("end")) {
            if (args.length == 0 || args.length == 1 && (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h"))) {
                if (sender.hasPermission(Permissions.CMD_HELP) || sender.hasPermission(Permissions.USER) || sender.hasPermission(Permissions.ADMIN)) {
                    return cmdHelp(sender);
                } else {
                    plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    return true;
                }
            } else {
                switch (args[0].toLowerCase()) {
                    case "regen":
                        if (sender.hasPermission(Permissions.CMD_REGEN) || sender.hasPermission(Permissions.ADMIN)) {
                            return cmdRegen(sender, Arrays.copyOfRange(args, 1, args.length));
                        } else {
                            plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                            return true;
                        }
                    case "respawnenderdragon":
                    case "respawned":
                        if (sender.hasPermission(Permissions.CMD_RESPAWN) || sender.hasPermission(Permissions.ADMIN)) {
                            return cmdRespawn(sender, Arrays.copyOfRange(args, 1, args.length));
                        } else {
                            plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                            return true;
                        }
                    case "nbenderdragon":
                    case "nbed":
                        if (sender.hasPermission(Permissions.CMD_NB) || sender.hasPermission(Permissions.USER) || sender.hasPermission(Permissions.ADMIN)) {
                            return cmdNb(sender, Arrays.copyOfRange(args, 1, args.length));
                        } else {
                            plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                            return true;
                        }
                    case "chunk":
                        if (args.length < 2) {
                            return false;
                        } else {
                            switch (args[1]) {
                                case "info":
                                case "i":
                                    if (sender.hasPermission(Permissions.CMD_CHUNKINFO) || sender.hasPermission(Permissions.USER) || sender.hasPermission(Permissions.ADMIN)) {
                                        return cmdChunkInfo(sender);
                                    } else {
                                        plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                                        return true;
                                    }
                                case "protect":
                                case "p":
                                    if (sender.hasPermission(Permissions.CMD_CHUNKPROTECT) || sender.hasPermission(Permissions.ADMIN)) {
                                        return cmdChunkProtect(sender);
                                    } else {
                                        plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                                        return true;
                                    }
                                case "unprotect":
                                case "up":
                                    if (sender.hasPermission(Permissions.CMD_CHUNKUNPROTECT) || sender.hasPermission(Permissions.ADMIN)) {
                                        return cmdChunkUnprotect(sender);
                                    } else {
                                        plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                                        return true;
                                    }
                                default:
                                    sender.sendMessage("Unknown subcommand: " + args[1]);
                                    return true;

                            }
                        }
                    default:
                        sender.sendMessage("Unknown subcommand: " + args[0]);
                        return true;
                }
            }
        } else {
            return false;
        }
    }

    private boolean cmdHelp(final CommandSender sender) { // TODO Messages
        sender.sendMessage("Available subcommands: help, regen, respawn, nb, chunk info, chunk protect, chunk unprotect");
        return true;
    }

    private boolean cmdRegen(final CommandSender sender, final String[] args) {
        try {
            final EndWorldHandler handler = getWorldHandlerRelatedTo(sender, args);
            if (handler == null) {
                sender.sendMessage("Unknown world"); // TODO Messages
            } else {
                sender.sendMessage("Regenerating " + handler.getEndWorld().getName()); // TODO Messages
                handler.regen();
            }
            return true;
        } catch (final Exception e) {
            return true;
        }
    }

    private boolean cmdRespawn(final CommandSender sender, final String[] args) {
        try {
            final EndWorldHandler handler = getWorldHandlerRelatedTo(sender, args);
            if (handler == null) {
                sender.sendMessage("Unknown world"); // TODO Messages
            } else {
                sender.sendMessage("Respawned " + handler.respawnDragons() + " dragons");
            }
            return true;
        } catch (final Exception e) {
            return true;
        }
    }

    private boolean cmdNb(final CommandSender sender, final String[] args) {
        try {
            final EndWorldHandler handler = getWorldHandlerRelatedTo(sender, args);
            if (handler == null) {
                sender.sendMessage("Unknown world"); // TODO Messages
            } else {
                sender.sendMessage("There are " + handler.getNumberOfAliveEDs() + " dragons alive in " + handler.getEndWorld().getName()); // TODO Messages
            }
            return true;
        } catch (final Exception e) {
            return true;
        }
    }

    private boolean cmdChunkInfo(final CommandSender sender) {
        // TODO Allow x, y, worldName parameters and so all CommandSender as sender
        if (!(sender instanceof Player)) {
            plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
            return true;
        } else {
            final Player player = (Player) sender;
            final String worldName = player.getWorld().getName();
            final EndWorldHandler handler = plugin.getHandler(Utils.toLowerCamelCase(worldName));
            if (handler == null) {
                sender.sendMessage("Not in an End world"); // TODO Messages
                return true;
            } else {
                final EndChunks chunks = handler.getChunks();
                final EndChunk chunk = chunks.getChunk(worldName, player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ());
                // TODO Messages
                player.sendMessage("Chunk (" + chunk.getX() + ", " + chunk.getZ() + ") in world " + worldName + " is " + (chunk.isProtected() ? "" : "not ") + "protected");
                return true;
            }
        }
    }

    private boolean cmdChunkProtect(final CommandSender sender) {
        // TODO Allow x, y, worldName parameters and so all CommandSender as sender
        if (!(sender instanceof Player)) {
            plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
            return true;
        } else {
            final Player player = (Player) sender;
            final String worldName = player.getWorld().getName();
            final EndWorldHandler handler = plugin.getHandler(Utils.toLowerCamelCase(worldName));
            if (handler == null) {
                sender.sendMessage("Not in an End world"); // TODO Messages
                return true;
            } else {
                final EndChunks chunks = handler.getChunks();
                final EndChunk chunk = chunks.getChunk(worldName, player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ());
                // TODO Messages
                player.sendMessage("Chunk (" + chunk.getX() + ", " + chunk.getZ() + ") in world " + worldName + " is " + (chunk.isProtected() ? "already" : "now") + " protected");
                chunk.setProtected(true);
                return true;
            }
        }
    }

    private boolean cmdChunkUnprotect(final CommandSender sender) {
        // TODO Allow x, y, worldName parameters and so all CommandSender as sender
        if (!(sender instanceof Player)) {
            plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
            return true;
        } else {
            final Player player = (Player) sender;
            final String worldName = player.getWorld().getName();
            final EndWorldHandler handler = plugin.getHandler(Utils.toLowerCamelCase(worldName));
            if (handler == null) {
                sender.sendMessage("Not in an End world"); // TODO Messages
                return true;
            } else {
                final EndChunks chunks = handler.getChunks();
                final EndChunk chunk = chunks.getChunk(worldName, player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ());
                // TODO Messages
                player.sendMessage("Chunk (" + chunk.getX() + ", " + chunk.getZ() + ") in world " + worldName + " is " + (chunk.isProtected() ? "no longer" : "already not") + " protected");
                chunk.setProtected(true);
                return true;
            }
        }
    }

    private EndWorldHandler getWorldHandlerRelatedTo(final CommandSender sender, final String[] args) throws Exception {
        String lowerCamelCaseWorldName = null;
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Non-player should give a world name"); // TODO Messages
                throw new Exception(); // We handle it locally so we don't care about having a special Exception name/message
            } else {
                lowerCamelCaseWorldName = Utils.toLowerCamelCase(((Player) sender).getWorld().getName());
            }
        } else {
            final StringBuilder s = new StringBuilder();
            for (final String word : args) {
                s.append(word + " ");
            }
            lowerCamelCaseWorldName = Utils.toLowerCamelCase(s.toString());
        }
        return plugin.getHandler(lowerCamelCaseWorldName);
    }
}
