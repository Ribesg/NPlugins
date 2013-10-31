package fr.ribesg.bukkit.ntheendagain;

import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.utils.StringUtils;
import fr.ribesg.bukkit.ncore.utils.WorldUtils;
import fr.ribesg.bukkit.ntheendagain.handler.EndWorldHandler;
import fr.ribesg.bukkit.ntheendagain.world.EndChunk;
import fr.ribesg.bukkit.ntheendagain.world.EndChunks;
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
		//      Or maybe we will just use the Bukkit /help command...
		sender.sendMessage("Available subcommands: help, regen, respawn, nb, chunk info, chunk protect, chunk unprotect");
		return true;
	}

	private boolean cmdRegen(final CommandSender sender, final String[] args) {
		final String[] parsedArgs = checkWorldArgument(sender, args);
		if (parsedArgs == null) {
			// The sender already received a message
			return true;
		} else {
			final EndWorldHandler handler = plugin.getHandler(StringUtils.toLowerCamelCase(parsedArgs[0]));
			if (handler != null) {
				plugin.sendMessage(sender, MessageId.theEndAgain_regenerating, handler.getEndWorld().getName());
				if (parsedArgs.length > 1 && parsedArgs[1].equalsIgnoreCase("hard")) {
					handler.getRegenHandler().regen(0);
				} else {
					handler.getRegenHandler().regen();
				}
			} else {
				plugin.sendMessage(sender, MessageId.unknownWorld, parsedArgs[0]);
			}
		}
		return true;
	}

	private boolean cmdRespawn(final CommandSender sender, final String[] args) {
		final String[] parsedArgs = checkWorldArgument(sender, args);
		if (parsedArgs == null) {
			// The sender already received a message
			return true;
		} else {
			final EndWorldHandler handler = plugin.getHandler(StringUtils.toLowerCamelCase(parsedArgs[0]));
			if (handler != null) {
				handler.getRespawnHandler().respawn();
			} else {
				plugin.sendMessage(sender, MessageId.unknownWorld, parsedArgs[0]);
			}
		}
		return true;
	}

	private boolean cmdNb(final CommandSender sender, final String[] args) {
		final String[] parsedArgs = checkWorldArgument(sender, args);
		if (parsedArgs == null) {
			// The sender already received a message
			return true;
		} else {
			final EndWorldHandler handler = plugin.getHandler(StringUtils.toLowerCamelCase(parsedArgs[0]));
			if (handler != null) {
				final Integer nb = handler.getNumberOfAliveEnderDragons();
				if (nb == 0 || nb == 1) {
					plugin.sendMessage(sender,
					                   nb == 0 ? MessageId.theEndAgain_nbAlive0 : MessageId.theEndAgain_nbAlive1,
					                   handler.getEndWorld().getName());
				} else {
					plugin.sendMessage(sender, MessageId.theEndAgain_nbAliveX, nb.toString(), handler.getEndWorld().getName());
				}
			} else {
				plugin.sendMessage(sender, MessageId.unknownWorld, parsedArgs[0]);
			}
		}
		return true;
	}

	private boolean cmdChunkInfo(final CommandSender sender) {
		// TODO Allow x, y, worldName parameters and so all CommandSender as sender
		if (!(sender instanceof Player)) {
			plugin.sendMessage(sender, MessageId.cmdOnlyAvailableForPlayers);
			return true;
		} else {
			final Player player = (Player) sender;
			final String worldName = player.getWorld().getName();
			final EndWorldHandler handler = plugin.getHandler(StringUtils.toLowerCamelCase(worldName));
			if (handler == null) {
				plugin.sendMessage(player, MessageId.theEndAgain_notInAnEndWorld);
				return true;
			} else {
				final EndChunks chunks = handler.getChunks();
				final EndChunk chunk = chunks.getChunk(worldName,
				                                       player.getLocation().getChunk().getX(),
				                                       player.getLocation().getChunk().getZ());
				final Integer x = chunk.getX();
				final Integer z = chunk.getZ();
				final MessageId id = chunk.isProtected()
				                     ? MessageId.theEndAgain_protectedChunkInfo
				                     : MessageId.theEndAgain_unprotectedChunkInfo;
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
			final EndWorldHandler handler = plugin.getHandler(StringUtils.toLowerCamelCase(worldName));
			if (handler == null) {
				plugin.sendMessage(player, MessageId.theEndAgain_notInAnEndWorld);
				return true;
			} else {
				final EndChunks chunks = handler.getChunks();
				final EndChunk chunk = chunks.getChunk(worldName,
				                                       player.getLocation().getChunk().getX(),
				                                       player.getLocation().getChunk().getZ());
				final Integer x = chunk.getX();
				final Integer z = chunk.getZ();
				final MessageId id = chunk.isProtected()
				                     ? MessageId.theEndAgain_protectedChunkProtect
				                     : MessageId.theEndAgain_unprotectedChunkProtect;
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
			final EndWorldHandler handler = plugin.getHandler(StringUtils.toLowerCamelCase(worldName));
			if (handler == null) {
				plugin.sendMessage(player, MessageId.theEndAgain_notInAnEndWorld);
				return true;
			} else {
				final EndChunks chunks = handler.getChunks();
				final EndChunk chunk = chunks.getChunk(worldName,
				                                       player.getLocation().getChunk().getX(),
				                                       player.getLocation().getChunk().getZ());
				final Integer x = chunk.getX();
				final Integer z = chunk.getZ();
				final MessageId id = chunk.isProtected()
				                     ? MessageId.theEndAgain_protectedChunkUnprotect
				                     : MessageId.theEndAgain_unprotectedChunkUnprotect;
				plugin.sendMessage(player, id, x.toString(), z.toString(), worldName);
				chunk.setProtected(true);
				return true;
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
	 *         sender did not provide a World (as a ConsoleSender)
	 */
	private String[] checkWorldArgument(final CommandSender sender, final String[] args) {
		String[] result;

		final boolean senderIsAPlayer = sender instanceof Player;
		if (args.length == 0) {
			if (!senderIsAPlayer) {
				plugin.sendMessage(sender, MessageId.missingWorldArg);
				return null;
			} else {
				result = new String[args.length + 1];
				result[0] = ((Player) sender).getWorld().getName();
				System.arraycopy(args, 0, result, 1, args.length);
			}
		} else {
			final String supposedWorldName = args[0];
			final String realWorldName = WorldUtils.getRealWorldName(supposedWorldName);
			if (realWorldName == null) { // No world argument provided, use Player's world
				if (senderIsAPlayer) {
					result = new String[args.length + 1];
					result[0] = ((Player) sender).getWorld().getName();
					System.arraycopy(args, 0, result, 1, args.length);
				} else {
					plugin.sendMessage(sender, MessageId.missingWorldArg);
					return null;
				}
			} else {
				result = args;
			}
		}

		return result;
	}
}
