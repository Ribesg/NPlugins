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
                                    plugin.sendMessage(sender, MessageId.theEndAgain_unkownSubCmd, args[1]);
                                    return true;

                            }
                        }
                    default:
                        plugin.sendMessage(sender, MessageId.theEndAgain_unkownSubCmd, args[0]);
                        return true;
                }
            }
        } else {
            return false;
        }
    }

    private boolean cmdHelp(final CommandSender sender) {
        // TODO We will create some kind of great Help thing later for the whole NPlugins suite
        sender.sendMessage("Available subcommands: help, regen, respawn, nb, chunk info, chunk protect, chunk unprotect");
        return true;
    }

    private boolean cmdRegen(final CommandSender sender, final String[] args) {
        try {
            final EndWorldHandler handler = getWorldHandlerRelatedTo(sender, args);
            if (handler == null) {
                plugin.sendMessage(sender, MessageId.theEndAgain_unknownWorld);
            } else {
                plugin.sendMessage(sender, MessageId.theEndAgain_regenerating, handler.getEndWorld().getName());
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
                plugin.sendMessage(sender, MessageId.theEndAgain_unknownWorld);
            } else {
                Integer respawned = handler.respawnDragons();
                plugin.sendMessage(sender, MessageId.theEndAgain_respawned, respawned.toString(), handler.getEndWorld().getName());
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
                plugin.sendMessage(sender, MessageId.theEndAgain_unknownWorld);
            } else {
                Integer nb = handler.getNumberOfAliveEDs();
                plugin.sendMessage(sender, MessageId.theEndAgain_nbAlive, nb.toString(), handler.getEndWorld().getName());
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
                plugin.sendMessage(player, MessageId.theEndAgain_notInAnEndWorld);
                return true;
            } else {
                final EndChunks chunks = handler.getChunks();
                final EndChunk chunk = chunks.getChunk(worldName, player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ());
                Integer x = chunk.getX();
                Integer z = chunk.getZ();
                MessageId id = chunk.isProtected() ? MessageId.theEndAgain_protectedChunkInfo : MessageId.theEndAgain_unprotectedChunkInfo;
                plugin.sendMessage(player, id, x.toString(), z.toString(), worldName);
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
                plugin.sendMessage(player, MessageId.theEndAgain_notInAnEndWorld);
                return true;
            } else {
                final EndChunks chunks = handler.getChunks();
                final EndChunk chunk = chunks.getChunk(worldName, player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ());
                Integer x = chunk.getX();
                Integer z = chunk.getZ();
                MessageId id = chunk.isProtected() ? MessageId.theEndAgain_protectedChunkProtect : MessageId.theEndAgain_unprotectedChunkProtect;
                plugin.sendMessage(player, id, x.toString(), z.toString(), worldName);
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
                plugin.sendMessage(player, MessageId.theEndAgain_notInAnEndWorld);
                return true;
            } else {
                final EndChunks chunks = handler.getChunks();
                final EndChunk chunk = chunks.getChunk(worldName, player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ());
                Integer x = chunk.getX();
                Integer z = chunk.getZ();
                MessageId id = chunk.isProtected() ? MessageId.theEndAgain_protectedChunkUnprotect : MessageId.theEndAgain_unprotectedChunkUnprotect;
                plugin.sendMessage(player, id, x.toString(), z.toString(), worldName);
                chunk.setProtected(true);
                return true;
            }
        }
    }

    private EndWorldHandler getWorldHandlerRelatedTo(final CommandSender sender, final String[] args) throws Exception {
        String lowerCamelCaseWorldName = null;
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                plugin.sendMessage(sender, MessageId.theEndAgain_missingWorldArg);
                throw new Exception(); // We handle it locally so we don't care about having a special Exception name/message
            } else {
                lowerCamelCaseWorldName = Utils.toLowerCamelCase(((Player) sender).getWorld().getName());
            }
        } else {
            final StringBuilder s = new StringBuilder();
            for (final String word : args) {
                s.append(word);
                s.append(' ');
            }
            lowerCamelCaseWorldName = Utils.toLowerCamelCase(s.toString());
        }
        return plugin.getHandler(lowerCamelCaseWorldName);
    }
}
