/***************************************************************************
 * Project file:    NPlugins - NPlayer - PunishmentCommandHandler.java     *
 * Full Class name: fr.ribesg.bukkit.nplayer.PunishmentCommandHandler      *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nplayer;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.node.cuboid.CuboidNode;
import fr.ribesg.bukkit.ncore.util.IPValidator;
import fr.ribesg.bukkit.ncore.util.StringUtil;
import fr.ribesg.bukkit.ncore.util.TimeUtil;
import fr.ribesg.bukkit.nplayer.punishment.PunishmentDb;
import fr.ribesg.bukkit.nplayer.punishment.PunishmentType;
import fr.ribesg.bukkit.nplayer.user.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.List;

public class PunishmentCommandHandler implements CommandExecutor, Listener {

	private final NPlayer      plugin;
	private final PunishmentDb db;

	public PunishmentCommandHandler(final NPlayer instance) {
		this.plugin = instance;
		this.db = plugin.getPunishmentDb();
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
		plugin.entering(getClass(), "onCommand", "cmd=" + cmd.getName());
		final boolean result;

		switch (cmd.getName()) {
			case "ban":
				if (Perms.hasBan(sender)) {
					result = cmdBan(sender, args);
				} else {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
					result = true;
				}
				break;
			case "banip":
				if (Perms.hasBanIp(sender)) {
					result = cmdBanIp(sender, args);
				} else {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
					result = true;
				}
				break;
			case "jail":
				if (Perms.hasJail(sender)) {
					result = cmdJail(sender, args);
				} else {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
					result = true;
				}
				break;
			case "mute":
				if (Perms.hasMute(sender)) {
					result = cmdMute(sender, args);
				} else {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
					result = true;
				}
				break;
			case "unban":
				if (Perms.hasUnBan(sender)) {
					result = cmdUnBan(sender, args);
				} else {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
					result = true;
				}
				break;
			case "unbanip":
				if (Perms.hasUnBanIp(sender)) {
					result = cmdUnBanIp(sender, args);
				} else {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
					result = true;
				}
				break;
			case "unjail":
				if (Perms.hasUnJail(sender)) {
					result = cmdUnJail(sender, args);
				} else {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
					result = true;
				}
				break;
			case "unmute":
				if (Perms.hasUnMute(sender)) {
					result = cmdUnMute(sender, args);
				} else {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
					result = true;
				}
				break;
			case "kick":
				if (Perms.hasKick(sender)) {
					result = cmdKick(sender, args);
				} else {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
					result = true;
				}
				break;
			default:
				result = false;
		}

		plugin.exiting(getClass(), "onCommand");
		return result;
	}

	private boolean cmdBan(final CommandSender sender, final String[] args) {
		plugin.entering(getClass(), "cmdBan");

		final Result res = parsePunishmentDataFromArgs(PunishmentType.BAN, args);
		if (res == null) {
			plugin.exiting(getClass(), "cmdBan", "Invalid arguments");
			return false;
		}
		if (!plugin.getUserDb().isUserKnown(res.punished)) {
			plugin.sendMessage(sender, MessageId.player_unknownUser, res.punished);
		} else if (res.duration == -1) {
			if (Perms.hasBanPermanent(sender)) {
				if (!db.permBanNick(res.punished, res.reason)) {
					plugin.sendMessage(sender, MessageId.player_alreadyBanned, res.punished);
				} else {
					final Player target = Bukkit.getPlayerExact(res.punished);
					if (target != null) {
						db.getLeaveMessages().put(target.getName(), plugin.getMessages().get(MessageId.player_permBannedBroadcast, res.punished, res.reason)[0]);
						target.kickPlayer(plugin.getMessages().get(MessageId.player_kickPermBanned, res.reason)[0]);
					} else {
						plugin.broadcastMessage(MessageId.player_permBannedBroadcast, res.punished, res.reason);
					}
				}
			} else {
				plugin.sendMessage(sender, MessageId.player_noPermissionForPermanent, "ban");
			}
		} else {
			if (!db.tempBanNick(res.punished, res.duration, res.reason)) {
				plugin.sendMessage(sender, MessageId.player_alreadyBanned, res.punished);
			} else {
				final Player target = Bukkit.getPlayerExact(res.punished);
				if (target != null) {
					db.getLeaveMessages().put(target.getName(), plugin.getMessages().get(MessageId.player_tempBannedBroadcast, res.punished, TimeUtil.toString(res.duration), res.reason)[0]);
					target.kickPlayer(plugin.getMessages().get(MessageId.player_kickTempBanned, res.reason, TimeUtil.toString(res.duration))[0]);
				} else {
					plugin.broadcastMessage(MessageId.player_tempBannedBroadcast, res.punished, TimeUtil.toString(res.duration), res.reason);
				}
			}
		}

		plugin.exiting(getClass(), "cmdBan");
		return true;
	}

	private boolean cmdBanIp(final CommandSender sender, final String[] args) {
		plugin.entering(getClass(), "cmdBanIp");

		final Result res = parsePunishmentDataFromArgs(PunishmentType.IPBAN, args);
		if (res == null) {
			plugin.exiting(getClass(), "cmdBanIp", "Invalid arguments");
			return false;
		}
		final boolean isIp = IPValidator.isValidIp(res.punished);
		final String ip;
		if (isIp) {
			ip = res.punished;
			if (!plugin.getUserDb().isIpKnown(ip)) {
				plugin.sendMessage(sender, MessageId.player_unknownIp, ip);
				plugin.exiting(getClass(), "cmdBanIp", "Unknown IP");
				return true;
			}
		} else {
			String playerName = res.punished;
			final Player player = Bukkit.getPlayer(res.punished);
			if (player != null) {
				playerName = player.getName();
			}
			final User user = plugin.getUserDb().get(playerName);
			if (user == null) {
				plugin.sendMessage(sender, MessageId.player_unknownUser, res.punished);
				plugin.exiting(getClass(), "cmdBanIp", "Unknown user");
				return true;
			} else {
				ip = user.getLastIp();
			}
		}
		if (res.duration == -1) {
			if (Perms.hasBanIpPermanent(sender)) {
				if (!db.permBanIp(res.punished, res.reason)) {
					plugin.sendMessage(sender, MessageId.player_alreadyBannedIp, ip);
				} else {
					final List<User> targets = plugin.getUserDb().getByIp(ip);
					for (final User u : targets) {
						final Player player = Bukkit.getPlayerExact(u.getUserName());
						if (player != null) {
							player.kickPlayer(plugin.getMessages().get(MessageId.player_kickPermIpBanned, res.reason)[0]);
						}
					}
					plugin.broadcastMessage(MessageId.player_permIpBannedBroadcast, ip, res.reason);
				}
			} else {
				plugin.sendMessage(sender, MessageId.player_noPermissionForPermanent, "banip");
			}
		} else {
			if (!db.tempBanIp(res.punished, res.duration, res.reason)) {
				plugin.sendMessage(sender, MessageId.player_alreadyBannedIp, ip);
			} else {
				final List<User> targets = plugin.getUserDb().getByIp(ip);
				for (final User u : targets) {
					final Player player = Bukkit.getPlayerExact(u.getUserName());
					if (player != null) {
						player.kickPlayer(plugin.getMessages().get(MessageId.player_kickTempIpBanned, res.reason, TimeUtil.toString(res.duration))[0]);
					}
				}
				plugin.broadcastMessage(MessageId.player_tempIpBannedBroadcast, ip, TimeUtil.toString(res.duration), res.reason);
			}
		}

		plugin.exiting(getClass(), "cmdBanIp");
		return true;
	}

	private boolean cmdJail(final CommandSender sender, final String[] args) {
		plugin.entering(getClass(), "cmdJail");

		final CuboidNode cuboidNode = plugin.getCuboidNode();
		if (cuboidNode == null) {
			plugin.sendMessage(sender, MessageId.player_cuboidNodeRequired);
			plugin.exiting(getClass(), "cmdJail", "No Cuboid Node");
			return true;
		}

		final Result res = parsePunishmentDataFromArgs(PunishmentType.JAIL, args);
		if (res == null) {
			plugin.exiting(getClass(), "cmdJail", "Invalid arguments");
			return false;
		}
		if (!plugin.getUserDb().isUserKnown(res.punished)) {
			plugin.sendMessage(sender, MessageId.player_unknownUser, res.punished);
		} else if (!cuboidNode.getJailsSet().contains(res.jailPointName.toLowerCase())) {
			plugin.sendMessage(sender, MessageId.player_unknownJail, res.jailPointName);
		} else if (res.duration == -1) {
			if (Perms.hasJailPermanent(sender)) {
				if (!db.permJailNick(res.punished, res.reason, res.jailPointName)) {
					plugin.sendMessage(sender, MessageId.player_alreadyJailed, res.punished);
				} else {
					final Player target = Bukkit.getPlayerExact(res.punished);
					if (target != null) {
						target.teleport(cuboidNode.getJailLocation(res.jailPointName).toBukkitLocation());
						plugin.sendMessage(target, MessageId.player_permJailed, res.reason);
					}
					plugin.broadcastMessage(MessageId.player_permJailedBroadcast, res.punished, res.reason);
				}
			} else {
				plugin.sendMessage(sender, MessageId.player_noPermissionForPermanent, "jail");
			}
		} else {
			if (!db.tempJailNick(res.punished, res.duration, res.reason, res.jailPointName)) {
				plugin.sendMessage(sender, MessageId.player_alreadyJailed, res.punished);
			} else {
				final Player target = Bukkit.getPlayerExact(res.punished);
				if (target != null) {
					target.teleport(cuboidNode.getJailLocation(res.jailPointName).toBukkitLocation());
					plugin.sendMessage(target, MessageId.player_tempJailed, res.reason, TimeUtil.toString(res.duration));
				}
				plugin.broadcastMessage(MessageId.player_tempJailedBroadcast, res.punished, TimeUtil.toString(res.duration), res.reason);
			}
		}

		plugin.exiting(getClass(), "cmdJail");
		return true;
	}

	private boolean cmdMute(final CommandSender sender, final String[] args) {
		plugin.entering(getClass(), "cmdMute");

		final Result res = parsePunishmentDataFromArgs(PunishmentType.MUTE, args);
		if (res == null) {
			plugin.exiting(getClass(), "cmdMute", "Invalid arguments");
			return false;
		}
		if (!plugin.getUserDb().isUserKnown(res.punished)) {
			plugin.sendMessage(sender, MessageId.player_unknownUser, res.punished);
		} else if (res.duration == -1) {
			if (Perms.hasMutePermanent(sender)) {
				if (!db.permMuteNick(res.punished, res.reason)) {
					plugin.sendMessage(sender, MessageId.player_alreadyMuted, res.punished);
				} else {
					final Player target = Bukkit.getPlayerExact(res.punished);
					if (target != null) {
						plugin.sendMessage(target, MessageId.player_permMuted, res.reason);
					}
					plugin.broadcastMessage(MessageId.player_permMutedBroadcast, res.punished, res.reason);
				}
			} else {
				plugin.sendMessage(sender, MessageId.player_noPermissionForPermanent, "mute");
			}
		} else {
			if (!db.tempMuteNick(res.punished, res.duration, res.reason)) {
				plugin.sendMessage(sender, MessageId.player_alreadyMuted, res.punished);
			} else {
				final Player target = Bukkit.getPlayerExact(res.punished);
				if (target != null) {
					plugin.sendMessage(target, MessageId.player_tempMuted, res.reason, TimeUtil.toString(res.duration));
				}
				plugin.broadcastMessage(MessageId.player_tempMutedBroadcast, res.punished, TimeUtil.toString(res.duration), res.reason);
			}
		}

		plugin.exiting(getClass(), "cmdMute");
		return true;
	}

	private boolean cmdUnBan(final CommandSender sender, final String[] args) {
		plugin.entering(getClass(), "cmdUnBan");

		if (args.length != 1) {
			plugin.exiting(getClass(), "cmdUnBan", "Invalid arguments");
			return false;
		}
		final String userName = args[0];
		if (!plugin.getUserDb().isUserKnown(userName)) {
			plugin.sendMessage(sender, MessageId.player_unknownUser, userName);
		} else if (db.unBanNick(userName)) {
			plugin.broadcastMessage(MessageId.player_unBannedBroadcast, userName);
		} else {
			plugin.sendMessage(sender, MessageId.player_notBanned, userName);
		}

		plugin.exiting(getClass(), "cmdUnBan");
		return true;
	}

	private boolean cmdUnBanIp(final CommandSender sender, final String[] args) {
		plugin.entering(getClass(), "cmdUnBanIp");

		if (args.length != 1) {
			plugin.exiting(getClass(), "cmdUnBanIp", "Invalid arguments");
			return false;
		}
		final String ip = args[0];
		if (!plugin.getUserDb().isIpKnown(ip)) {
			plugin.sendMessage(sender, MessageId.player_unknownUser, ip);
		} else if (db.unBanIp(ip)) {
			plugin.broadcastMessage(MessageId.player_unBannedIpBroadcast, ip);
		} else {
			plugin.sendMessage(sender, MessageId.player_notBannedIp, ip);
		}

		plugin.exiting(getClass(), "cmdUnBanIp");
		return true;
	}

	private boolean cmdUnJail(final CommandSender sender, final String[] args) {
		plugin.entering(getClass(), "cmdUnJail");

		if (args.length != 1) {
			plugin.exiting(getClass(), "cmdUnJail", "Invalid arguments");
			return false;
		}
		String userName = args[0];
		final Player player = Bukkit.getPlayer(userName);
		if (player != null) {
			userName = player.getName();
		}
		if (!plugin.getUserDb().isUserKnown(userName)) {
			plugin.sendMessage(sender, MessageId.player_unknownUser, userName);
		} else if (db.unJailNick(userName)) {
			plugin.broadcastMessage(MessageId.player_unJailedBroadcast, userName);
		} else {
			plugin.sendMessage(sender, MessageId.player_notJailed, userName);
		}

		plugin.exiting(getClass(), "cmdUnJail");
		return true;
	}

	private boolean cmdUnMute(final CommandSender sender, final String[] args) {
		plugin.entering(getClass(), "cmdUnMute");

		if (args.length != 1) {
			plugin.exiting(getClass(), "cmdUnMute", "Invalid arguments");
			return false;
		}
		String userName = args[0];
		final Player player = Bukkit.getPlayer(userName);
		if (player != null) {
			userName = player.getName();
		}
		if (!plugin.getUserDb().isUserKnown(userName)) {
			plugin.sendMessage(sender, MessageId.player_unknownUser, userName);
		} else if (db.unMuteNick(userName)) {
			plugin.broadcastMessage(MessageId.player_unMutedBroadcast, userName);
		} else {
			plugin.sendMessage(sender, MessageId.player_notMuted, userName);
		}

		plugin.exiting(getClass(), "cmdUnMute");
		return true;
	}

	private boolean cmdKick(final CommandSender sender, final String[] args) {
		plugin.entering(getClass(), "cmdKick");

		if (args.length < 2) {
			plugin.exiting(getClass(), "cmdKick", "Invalid arguments");
			return false;
		}
		final String userName = args[0];
		final Player player = Bukkit.getPlayer(userName);
		if (player == null) {
			plugin.sendMessage(sender, MessageId.player_unknownUser, userName);
		} else {
			final String reason = StringUtil.joinStrings(args, 1);
			db.getLeaveMessages().put(player.getName(), plugin.getMessages().get(MessageId.player_broadcastedKickMessage, userName, reason)[0]);
			player.kickPlayer(plugin.getMessages().get(MessageId.player_kickMessage, reason)[0]);
		}

		plugin.exiting(getClass(), "cmdKick");
		return true;
	}

	private class Result {

		public final String punished;
		public final String reason;
		public final long   duration;
		public final String jailPointName;

		private Result(final String punished, final String reason, final long duration, final String jailPointName) {
			this.punished = punished;
			this.reason = reason;
			this.duration = duration;
			this.jailPointName = jailPointName;
		}
	}

	private Result parsePunishmentDataFromArgs(final PunishmentType type, final String[] cmdArgs) {
		plugin.entering(getClass(), "parsePunishmentDataFromArgs");

		if (cmdArgs.length < 2) {
			plugin.exiting(getClass(), "parsePunishmentDataFromArgs", "Invalid arguments (No reason)");
			return null;
		}
		final Player player = Bukkit.getPlayer(cmdArgs[0]);
		final String punished = player == null ? cmdArgs[0] : player.getName();
		boolean permanent = false;
		final boolean jail = type == PunishmentType.JAIL;
		long duration = -1;
		try {
			duration = TimeUtil.getInSeconds(cmdArgs[1]);
			if (cmdArgs.length < 3 || jail && cmdArgs.length < 4) {
				plugin.exiting(getClass(), "parsePunishmentDataFromArgs", "Invalid arguments (No reason for temporary)");
				return null;
			}
		} catch (final IllegalArgumentException e) {
			permanent = true;
		}
		final int reasonFirstWordIndex = 1 + (permanent ? 0 : 1) + (jail ? 1 : 0);
		final String reason = StringUtil.joinStrings(cmdArgs, reasonFirstWordIndex);
		final String jailPointName = jail ? (permanent ? cmdArgs[1] : cmdArgs[2]) : null;

		plugin.exiting(getClass(), "parsePunishmentDataFromArgs");
		return new Result(punished, reason, duration, jailPointName);
	}
}
