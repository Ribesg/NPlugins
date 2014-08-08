/***************************************************************************
 * Project file:    NPlugins - NTheEndAgain - TheEndAgainCommandExecutor.java
 * Full Class name: fr.ribesg.bukkit.ntheendagain.TheEndAgainCommandExecutor
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntheendagain;

import fr.ribesg.bukkit.ncore.common.ChunkCoord;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.util.StringUtil;
import fr.ribesg.bukkit.ncore.util.WorldUtil;
import fr.ribesg.bukkit.ntheendagain.handler.EndWorldHandler;
import fr.ribesg.bukkit.ntheendagain.world.EndChunk;
import fr.ribesg.bukkit.ntheendagain.world.EndChunks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TheEndAgainCommandExecutor implements CommandExecutor {

    private final NTheEndAgain plugin;

    public TheEndAgainCommandExecutor(final NTheEndAgain instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
        if ("nend".equalsIgnoreCase(command.getName())) {
            if (args.length == 0 || args.length == 1 && ("help".equalsIgnoreCase(args[0]) || "h".equalsIgnoreCase(args[0]))) {
                if (Perms.hasHelp(sender)) {
                    return this.cmdHelp(sender);
                } else {
                    this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    return true;
                }
            } else {
                switch (args[0].toLowerCase()) {
                    case "regen":
                        if (Perms.hasRegen(sender)) {
                            return this.cmdRegen(sender, Arrays.copyOfRange(args, 1, args.length));
                        } else {
                            this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                            return true;
                        }
                    case "respawnenderdragon":
                    case "respawned":
                    case "respawn":
                        if (Perms.hasRespawn(sender)) {
                            return this.cmdRespawn(sender, Arrays.copyOfRange(args, 1, args.length));
                        } else {
                            this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                            return true;
                        }
                    case "nbenderdragon":
                    case "nbed":
                    case "nb":
                        if (Perms.hasNb(sender)) {
                            return this.cmdNb(sender, Arrays.copyOfRange(args, 1, args.length));
                        } else {
                            this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
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
                                        return this.cmdChunkInfo(sender, Arrays.copyOfRange(args, 2, args.length));
                                    } else {
                                        this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                                        return true;
                                    }
                                case "protect":
                                case "p":
                                    if (Perms.hasChunkProtect(sender)) {
                                        return this.cmdChunkProtect(sender, Arrays.copyOfRange(args, 2, args.length));
                                    } else {
                                        this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                                        return true;
                                    }
                                case "unprotect":
                                case "up":
                                    if (Perms.hasChunkUnprotect(sender)) {
                                        return this.cmdChunkUnprotect(sender, Arrays.copyOfRange(args, 2, args.length));
                                    } else {
                                        this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                                        return true;
                                    }
                                default:
                                    this.plugin.sendMessage(sender, MessageId.theEndAgain_unkownSubCmd, args[1]);
                                    return true;
                            }
                        }
                    case "reload":
                    case "rld":
                        if (Perms.hasReload(sender)) {
                            return this.cmdReload(sender, Arrays.copyOfRange(args, 1, args.length));
                        } else {
                            this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                            return true;
                        }
                    default:
                        this.plugin.sendMessage(sender, MessageId.theEndAgain_unkownSubCmd, args[0]);
                        return true;
                }
            }
        } else {
            return false;
        }
    }

    private boolean cmdHelp(final CommandSender sender) {
        // TODO We will create some kind of great Help thing later for the whole NPlugins suite
        //      Or maybe we will just use the Bukkit /help command...
        sender.sendMessage("Available subcommands: help, regen, respawn, nb, chunk info, chunk protect, chunk unprotect, reload messages");
        return true;
    }

    private boolean cmdRegen(final CommandSender sender, final String[] args) {
        final String[] parsedArgs = this.checkWorldArgument(sender, args);
        if (parsedArgs == null) {
            // The sender already received a message
            return true;
        } else {
            final EndWorldHandler handler = this.plugin.getHandler(StringUtil.toLowerCamelCase(parsedArgs[0]));
            if (handler != null) {
                this.plugin.sendMessage(sender, MessageId.theEndAgain_regenerating, handler.getEndWorld().getName());
                if (parsedArgs.length > 1 && "hard".equalsIgnoreCase(parsedArgs[1])) {
                    handler.getRegenHandler().regen(0);
                } else {
                    handler.getRegenHandler().regen();
                }
            } else {
                this.plugin.sendMessage(sender, MessageId.unknownWorld, parsedArgs[0]);
            }
        }
        return true;
    }

    private boolean cmdRespawn(final CommandSender sender, final String[] args) {
        final String[] parsedArgs = this.checkWorldArgument(sender, args);
        if (parsedArgs == null) {
            // The sender already received a message
            return true;
        } else {
            final EndWorldHandler handler = this.plugin.getHandler(StringUtil.toLowerCamelCase(parsedArgs[0]));
            if (handler != null) {
                handler.getRespawnHandler().respawn();
            } else {
                this.plugin.sendMessage(sender, MessageId.unknownWorld, parsedArgs[0]);
            }
        }
        return true;
    }

    private boolean cmdNb(final CommandSender sender, final String[] args) {
        final String[] parsedArgs = this.checkWorldArgument(sender, args);
        if (parsedArgs == null) {
            // The sender already received a message
            return true;
        } else {
            final EndWorldHandler handler = this.plugin.getHandler(StringUtil.toLowerCamelCase(parsedArgs[0]));
            if (handler != null) {
                final Integer nb = handler.getNumberOfAliveEnderDragons();
                if (nb == 0 || nb == 1) {
                    this.plugin.sendMessage(sender, nb == 0 ? MessageId.theEndAgain_nbAlive0 : MessageId.theEndAgain_nbAlive1, handler.getEndWorld().getName());
                } else {
                    this.plugin.sendMessage(sender, MessageId.theEndAgain_nbAliveX, nb.toString(), handler.getEndWorld().getName());
                }
            } else {
                this.plugin.sendMessage(sender, MessageId.unknownWorld, parsedArgs[0]);
            }
        }
        return true;
    }

    private boolean cmdChunkInfo(final CommandSender sender, final String[] args) {
        if (args.length > 0) {
            final List<ChunkCoord> coords = this.getChunks(sender, args);
            if (coords == null) {
                return true;
            } else if (coords.isEmpty()) {
                return false;
            } else {
                final String worldName = coords.get(0).getWorldName();
                final EndWorldHandler handler = this.plugin.getHandler(StringUtil.toLowerCamelCase(worldName));
                if (handler == null) {
                    this.plugin.sendMessage(sender, MessageId.theEndAgain_notInAnEndWorld);
                    return true;
                } else {
                    final EndChunks chunks = handler.getChunks();
                    for (final ChunkCoord coord : coords) {
                        final EndChunk chunk = chunks.getChunk(worldName, coord.getX(), coord.getZ());
                        final Integer x = chunk.getX();
                        final Integer z = chunk.getZ();
                        final MessageId id = chunk.isProtected() ? MessageId.theEndAgain_protectedChunkInfo : MessageId.theEndAgain_unprotectedChunkInfo;
                        this.plugin.sendMessage(sender, id, x.toString(), z.toString(), worldName);
                    }
                    return true;
                }
            }
        } else if (!(sender instanceof Player)) {
            this.plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
            return true;
        } else {
            final Player player = (Player)sender;
            final String worldName = player.getWorld().getName();
            final EndWorldHandler handler = this.plugin.getHandler(StringUtil.toLowerCamelCase(worldName));
            if (handler == null) {
                this.plugin.sendMessage(player, MessageId.theEndAgain_notInAnEndWorld);
                return true;
            } else {
                final EndChunks chunks = handler.getChunks();
                final EndChunk chunk = chunks.getChunk(worldName, player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ());
                final Integer x = chunk.getX();
                final Integer z = chunk.getZ();
                final MessageId id = chunk.isProtected() ? MessageId.theEndAgain_protectedChunkInfo : MessageId.theEndAgain_unprotectedChunkInfo;
                this.plugin.sendMessage(player, id, x.toString(), z.toString(), worldName);
                return true;
            }
        }
    }

    private boolean cmdChunkProtect(final CommandSender sender, final String[] args) {
        if (args.length > 0) {
            final List<ChunkCoord> coords = this.getChunks(sender, args);
            if (coords == null) {
                return true;
            } else if (coords.isEmpty()) {
                return false;
            } else {
                final String worldName = coords.get(0).getWorldName();
                final EndWorldHandler handler = this.plugin.getHandler(StringUtil.toLowerCamelCase(worldName));
                if (handler == null) {
                    this.plugin.sendMessage(sender, MessageId.theEndAgain_notInAnEndWorld);
                    return true;
                } else {
                    final EndChunks chunks = handler.getChunks();
                    for (final ChunkCoord coord : coords) {
                        final EndChunk chunk = chunks.getChunk(worldName, coord.getX(), coord.getZ());
                        final Integer x = chunk.getX();
                        final Integer z = chunk.getZ();
                        final MessageId id = chunk.isProtected() ? MessageId.theEndAgain_protectedChunkProtect : MessageId.theEndAgain_unprotectedChunkProtect;
                        this.plugin.sendMessage(sender, id, x.toString(), z.toString(), worldName);
                        chunk.setProtected(true);
                    }
                    return true;
                }
            }
        } else if (!(sender instanceof Player)) {
            this.plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
            return true;
        } else {
            final Player player = (Player)sender;
            final String worldName = player.getWorld().getName();
            final EndWorldHandler handler = this.plugin.getHandler(StringUtil.toLowerCamelCase(worldName));
            if (handler == null) {
                this.plugin.sendMessage(player, MessageId.theEndAgain_notInAnEndWorld);
                return true;
            } else {
                final EndChunks chunks = handler.getChunks();
                final EndChunk chunk = chunks.getChunk(worldName, player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ());
                final Integer x = chunk.getX();
                final Integer z = chunk.getZ();
                final MessageId id = chunk.isProtected() ? MessageId.theEndAgain_protectedChunkProtect : MessageId.theEndAgain_unprotectedChunkProtect;
                this.plugin.sendMessage(player, id, x.toString(), z.toString(), worldName);
                chunk.setProtected(true);
                return true;
            }
        }
    }

    private boolean cmdChunkUnprotect(final CommandSender sender, final String[] args) {
        if (args.length > 0) {
            final List<ChunkCoord> coords = this.getChunks(sender, args);
            if (coords == null) {
                return true;
            } else if (coords.isEmpty()) {
                return false;
            } else {
                final String worldName = coords.get(0).getWorldName();
                final EndWorldHandler handler = this.plugin.getHandler(StringUtil.toLowerCamelCase(worldName));
                if (handler == null) {
                    this.plugin.sendMessage(sender, MessageId.theEndAgain_notInAnEndWorld);
                    return true;
                } else {
                    final EndChunks chunks = handler.getChunks();
                    for (final ChunkCoord coord : coords) {
                        final EndChunk chunk = chunks.getChunk(worldName, coord.getX(), coord.getZ());
                        final Integer x = chunk.getX();
                        final Integer z = chunk.getZ();
                        final MessageId id = chunk.isProtected() ? MessageId.theEndAgain_protectedChunkUnprotect : MessageId.theEndAgain_unprotectedChunkUnprotect;
                        this.plugin.sendMessage(sender, id, x.toString(), z.toString(), worldName);
                        chunk.setProtected(false);
                    }
                    return true;
                }
            }
        } else if (!(sender instanceof Player)) {
            this.plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
            return true;
        } else {
            final Player player = (Player)sender;
            final String worldName = player.getWorld().getName();
            final EndWorldHandler handler = this.plugin.getHandler(StringUtil.toLowerCamelCase(worldName));
            if (handler == null) {
                this.plugin.sendMessage(player, MessageId.theEndAgain_notInAnEndWorld);
                return true;
            } else {
                final EndChunks chunks = handler.getChunks();
                final EndChunk chunk = chunks.getChunk(worldName, player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ());
                final Integer x = chunk.getX();
                final Integer z = chunk.getZ();
                final MessageId id = chunk.isProtected() ? MessageId.theEndAgain_protectedChunkUnprotect : MessageId.theEndAgain_unprotectedChunkUnprotect;
                this.plugin.sendMessage(player, id, x.toString(), z.toString(), worldName);
                chunk.setProtected(false);
                return true;
            }
        }
    }

    private boolean cmdReload(final CommandSender sender, final String[] args) {
        if (args.length != 1) {
            return false;
        } else {
            switch (args[0].toLowerCase()) {
                case "messages":
                case "mess":
                case "mes":
                    try {
                        this.plugin.loadMessages();
                        this.plugin.sendMessage(sender, MessageId.cmdReloadMessages);
                    } catch (final IOException e) {
                        this.plugin.error("An error occured when NTheEndAgain tried to load messages.yml", e);
                        this.plugin.sendMessage(sender, MessageId.cmdReloadError, "messages.yml");
                    }
                    return true;
                default:
                    return false;
            }
        }
    }

    /**
     * - Checks that a console sender provided a world name
     * - Automagically adds the Player's world name if he "forgot" it as argument
     * - Keeps non-world args
     *
     * @param sender the sender of the command
     * @param args   the arguments used by the sender
     *
     * @return a new args String[] containing the new arguments, or null if the
     * sender did not provide a World (as a ConsoleSender)
     */
    private String[] checkWorldArgument(final CommandSender sender, final String[] args) {
        final String[] result;

        final boolean senderIsAPlayer = sender instanceof Player;
        if (args.length == 0) {
            if (!senderIsAPlayer) {
                this.plugin.sendMessage(sender, MessageId.missingWorldArg);
                return null;
            } else {
                result = new String[args.length + 1];
                result[0] = ((Player)sender).getWorld().getName();
                System.arraycopy(args, 0, result, 1, args.length);
            }
        } else {
            final String supposedWorldName = args[0];
            final String realWorldName = WorldUtil.getRealWorldName(supposedWorldName);
            if (realWorldName == null) { // No world argument provided, use Player's world
                if (senderIsAPlayer) {
                    result = new String[args.length + 1];
                    result[0] = ((Player)sender).getWorld().getName();
                    System.arraycopy(args, 0, result, 1, args.length);
                } else {
                    this.plugin.sendMessage(sender, MessageId.missingWorldArg);
                    return null;
                }
            } else {
                result = args;
            }
        }

        return result;
    }

    private List<ChunkCoord> getChunks(final CommandSender sender, final String[] args) {
        final List<ChunkCoord> coords = new ArrayList<>();
        if (args.length == 0) {
            return coords;
        }
        final String[] parsedArgs = this.checkWorldArgument(sender, args); // worldName x z x z
        if (parsedArgs == null) {
            return null;
        } else if (parsedArgs.length == 3) {
            final String worldName = parsedArgs[0];
            final int x, z;
            try {
                x = Integer.parseInt(parsedArgs[1]);
                z = Integer.parseInt(parsedArgs[2]);
                coords.add(new ChunkCoord(x, z, worldName));
            } catch (final NumberFormatException ignored) {
                // Don't add anything
            }
        } else if (parsedArgs.length == 5) {
            final String worldName = parsedArgs[0];
            final int x1, z1, x2, z2;
            try {
                x1 = Integer.parseInt(parsedArgs[1]);
                z1 = Integer.parseInt(parsedArgs[2]);
                x2 = Integer.parseInt(parsedArgs[3]);
                z2 = Integer.parseInt(parsedArgs[4]);
            } catch (final NumberFormatException e) {
                return coords;
            }
            final int minX = Math.min(x1, x2);
            final int maxX = Math.max(x1, x2);
            final int minZ = Math.min(z1, z2);
            final int maxZ = Math.max(z1, z2);
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    coords.add(new ChunkCoord(x, z, worldName));
                }
            }
        }
        return coords;
    }
}
