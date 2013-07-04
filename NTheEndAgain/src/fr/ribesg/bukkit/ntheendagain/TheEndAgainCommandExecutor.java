package fr.ribesg.bukkit.ntheendagain;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.utils.Utils;
import fr.ribesg.bukkit.ntheendagain.world.EndChunk;
import fr.ribesg.bukkit.ntheendagain.world.EndChunks;
import fr.ribesg.bukkit.ntheendagain.world.EndWorldHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class TheEndAgainCommandExecutor implements CommandExecutor {

    private final NTheEndAgain plugin;

    public TheEndAgainCommandExecutor(final NTheEndAgain instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
        if (command.getName().equalsIgnoreCase("nend")) {
            if (args.length == 0 || args.length == 1 && (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h"))) {
                if (Perms.hasHelp(sender)) {
                    return cmdHelp(sender);
                } else {
                    plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    return true;
                }
            } else {
                switch (args[0].toLowerCase()) {
                    case "regen":
                        if (Perms.hasRegen(sender)) {
                            return cmdRegen(sender, Arrays.copyOfRange(args, 1, args.length));
                        } else {
                            plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                            return true;
                        }
                    case "respawnenderdragon":
                    case "respawned":
                    case "respawn":
                        if (Perms.hasRespawn(sender)) {
                            return cmdRespawn(sender, Arrays.copyOfRange(args, 1, args.length));
                        } else {
                            plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                            return true;
                        }
                    case "nbenderdragon":
                    case "nbed":
                    case "nb":
                        if (Perms.hasNb(sender)) {
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
                                    if (Perms.hasChunkInfo(sender)) {
                                        return cmdChunkInfo(sender);
                                    } else {
                                        plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                                        return true;
                                    }
                                case "protect":
                                case "p":
                                    if (Perms.hasChunkProtect(sender)) {
                                        return cmdChunkProtect(sender);
                                    } else {
                                        plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                                        return true;
                                    }
                                case "unprotect":
                                case "up":
                                    if (Perms.hasChunkUnprotect(sender)) {
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
            final String[] parsedArgs = parseArguments(sender, args);
            if (parsedArgs == null) {
                plugin.sendMessage(sender, MessageId.theEndAgain_unknownWorld);
            } else {
                final EndWorldHandler handler = plugin.getHandler(Utils.toLowerCamelCase(parsedArgs[0]));
                if (handler != null) {
                    if (parsedArgs.length > 1 && parsedArgs[1].equalsIgnoreCase("hard")) {
                        plugin.sendMessage(sender, MessageId.theEndAgain_regenerating, handler.getEndWorld().getName());
                        handler.regen(0);
                    } else {
                        plugin.sendMessage(sender, MessageId.theEndAgain_regenerating, handler.getEndWorld().getName());
                        handler.regen();
                    }
                } else {
                    plugin.sendMessage(sender, MessageId.theEndAgain_unknownWorld);
                }
            }
            return true;
        } catch (final Exception e) {
            return true;
        }
    }

    private boolean cmdRespawn(final CommandSender sender, final String[] args) {
        try {
            final String[] parsedArgs = parseArguments(sender, args);
            if (parsedArgs == null) {
                plugin.sendMessage(sender, MessageId.theEndAgain_unknownWorld);
            } else {
                final EndWorldHandler handler = plugin.getHandler(Utils.toLowerCamelCase(parsedArgs[0]));
                if (handler != null) {
                    handler.respawnDragons();
                } else {
                    plugin.sendMessage(sender, MessageId.theEndAgain_unknownWorld);
                }
            }
            return true;
        } catch (final Exception e) {
            return true;
        }
    }

    private boolean cmdNb(final CommandSender sender, final String[] args) {
        try {
            final String[] parsedArgs = parseArguments(sender, args);
            if (parsedArgs == null) {
                plugin.sendMessage(sender, MessageId.theEndAgain_unknownWorld);
            } else {
                final EndWorldHandler handler = plugin.getHandler(Utils.toLowerCamelCase(parsedArgs[0]));
                if (handler != null) {
                    final Integer nb = handler.getNumberOfAliveEnderDragons();
                    plugin.sendMessage(sender, MessageId.theEndAgain_nbAlive, nb.toString(), handler.getEndWorld().getName());
                } else {
                    plugin.sendMessage(sender, MessageId.theEndAgain_unknownWorld);
                }
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
                final Integer x = chunk.getX();
                final Integer z = chunk.getZ();
                final MessageId id = chunk.isProtected() ? MessageId.theEndAgain_protectedChunkInfo : MessageId.theEndAgain_unprotectedChunkInfo;
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
                final Integer x = chunk.getX();
                final Integer z = chunk.getZ();
                final MessageId id = chunk.isProtected() ? MessageId.theEndAgain_protectedChunkProtect : MessageId.theEndAgain_unprotectedChunkProtect;
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
                final Integer x = chunk.getX();
                final Integer z = chunk.getZ();
                final MessageId id = chunk.isProtected() ? MessageId.theEndAgain_protectedChunkUnprotect : MessageId.theEndAgain_unprotectedChunkUnprotect;
                plugin.sendMessage(player, id, x.toString(), z.toString(), worldName);
                chunk.setProtected(true);
                return true;
            }
        }
    }

    private String[] parseArguments(final CommandSender sender, final String[] args) throws Exception {
        String worldName = null;
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                plugin.sendMessage(sender, MessageId.theEndAgain_missingWorldArg);
                throw new Exception(); // We handle it locally so we don't care about having a special Exception name/message
            } else {
                worldName = ((Player) sender).getWorld().getName();
                return new String[] {worldName};
            }
        } else {
            // Find a world name in the arguments
            boolean worldFound = true;
            String concatenation = args[0];
            for (int i = 1; i < args.length; i++) {
                concatenation += ' ' + args[i];
            }
            int nbWords = args.length;
            while (Bukkit.getWorld(concatenation) == null) {
                if (nbWords == 1) {
                    worldFound = false;
                    break;
                }
                concatenation = concatenation.substring(0, 1 + args[--nbWords].length());
            }
            if (worldFound) {
                worldName = concatenation;
                final String[] result = new String[args.length - nbWords + 1];
                result[0] = worldName;
                System.arraycopy(args, nbWords, result, 1, args.length - nbWords + 1);
                return result;
            } else {
                if (!(sender instanceof Player)) {
                    plugin.sendMessage(sender, MessageId.theEndAgain_missingWorldArg);
                    throw new Exception(); // We handle it locally so we don't care about having a special Exception name/message
                } else {
                    worldName = ((Player) sender).getWorld().getName();
                    final String[] result = new String[1 + args.length];
                    result[0] = worldName;
                    System.arraycopy(args, 0, result, 1, args.length);
                    return result;
                }
            }
        }

    }
}
