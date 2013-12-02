/***************************************************************************
 * Project file:    NPlugins - NPlayer - PunishmentCommandHandler.java     *
 * Full Class name: fr.ribesg.bukkit.nplayer.PunishmentCommandHandler      *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nplayer;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.utils.IPValidator;
import fr.ribesg.bukkit.ncore.utils.StringUtils;
import fr.ribesg.bukkit.ncore.utils.TimeUtils;
import fr.ribesg.bukkit.nplayer.punishment.PunishmentDb;
import fr.ribesg.bukkit.nplayer.punishment.PunishmentType;
import fr.ribesg.bukkit.nplayer.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.List;

public class PunishmentCommandHandler implements CommandExecutor, Listener {

	private final NPlayer      plugin;
	private final PunishmentDb db;

	public PunishmentCommandHandler(NPlayer instance) {
		this.plugin = instance;
		this.db = plugin.getPunishmentDb();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		switch (cmd.getName()) {
			case "ban":
				if (Perms.hasBan(sender)) {
					return cmdBan(sender, args);
				} else {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
					return true;
				}
			case "banip":
				if (Perms.hasBanIp(sender)) {
					return cmdBanIp(sender, args);
				} else {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
					return true;
				}
			case "jail":
				if (Perms.hasJail(sender)) {
					return cmdJail(sender, args);
				} else {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
					return true;
				}
			case "mute":
				if (Perms.hasMute(sender)) {
					return cmdMute(sender, args);
				} else {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
					return true;
				}
			case "unban":
				if (Perms.hasUnBan(sender)) {
					return cmdUnBan(sender, args);
				} else {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
					return true;
				}
			case "unbanip":
				if (Perms.hasUnBanIp(sender)) {
					return cmdUnBanIp(sender, args);
				} else {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
					return true;
				}
			case "unjail":
				if (Perms.hasUnJail(sender)) {
					return cmdUnJail(sender, args);
				} else {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
					return true;
				}
			case "unmute":
				if (Perms.hasUnMute(sender)) {
					return cmdUnMute(sender, args);
				} else {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
					return true;
				}
			case "kick":
				if (Perms.hasKick(sender)) {
					return cmdKick(sender, args);
				} else {
					plugin.sendMessage(sender, MessageId.noPermissionForCommand);
					return true;
				}
			default:
				return false;
		}
	}

	private boolean cmdBan(CommandSender sender, String[] args) {
		final Result res = get(PunishmentType.BAN, args);
		if (res == null) {
			return false;
		}
		if (!plugin.getUserDb().isUserKnown(res.punished)) {
			plugin.sendMessage(sender, MessageId.player_unknownUser, res.punished);
			return true;
		}
		if (res.duration == -1) {
			if (Perms.hasBanPermanent(sender)) {
				if (!db.permBanNick(res.punished, res.reason)) {
					plugin.sendMessage(sender, MessageId.player_alreadyBanned, res.punished);
				} else {
					final Player target = Bukkit.getPlayerExact(res.punished);
					if (target != null) {
						target.kickPlayer(plugin.getMessages().get(MessageId.player_kickPermBanned, res.reason)[0]);
						db.getLeaveMessages()
						  .put(target.getName(),
						       plugin.getMessages().get(MessageId.player_permBannedBroadcast, res.punished, res.reason)[0]);
					} else {
						plugin.broadcastMessage(MessageId.player_permBannedBroadcast, res.punished, res.reason);
					}
				}
				return true;
			} else {
				plugin.sendMessage(sender, MessageId.player_noPermissionForPermanent, "ban");
				return true;
			}
		} else {
			if (!db.tempBanNick(res.punished, res.duration, res.reason)) {
				plugin.sendMessage(sender, MessageId.player_alreadyBanned, res.punished);
			} else {
				final Player target = Bukkit.getPlayerExact(res.punished);
				if (target != null) {
					target.kickPlayer(plugin.getMessages()
					                        .get(MessageId.player_kickTempBanned, res.reason, TimeUtils.toString(res.duration))[0]);
					db.getLeaveMessages()
					  .put(target.getName(),
					       plugin.getMessages()
					             .get(MessageId.player_tempBannedBroadcast, res.punished, TimeUtils.toString(res.duration), res.reason)[0]);
				} else {
					plugin.broadcastMessage(MessageId.player_tempBannedBroadcast,
					                        res.punished,
					                        TimeUtils.toString(res.duration),
					                        res.reason);
				}
			}
			return true;
		}
	}

	private boolean cmdBanIp(CommandSender sender, String[] args) {
		final Result res = get(PunishmentType.IPBAN, args);
		if (res == null) {
			return false;
		}
		boolean isIp = IPValidator.isValidIp(res.punished);
		String ip;
		if (isIp) {
			ip = res.punished;
			if (!plugin.getUserDb().isIpKnown(ip)) {
				plugin.sendMessage(sender, MessageId.player_unknownIp, ip);
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
				return true;
			} else {
				plugin.sendMessage(sender, MessageId.player_noPermissionForPermanent, "banip");
				return true;
			}
		} else {
			if (!db.tempBanIp(res.punished, res.duration, res.reason)) {
				plugin.sendMessage(sender, MessageId.player_alreadyBannedIp, ip);
			} else {
				final List<User> targets = plugin.getUserDb().getByIp(ip);
				for (final User u : targets) {
					final Player player = Bukkit.getPlayerExact(u.getUserName());
					if (player != null) {
						player.kickPlayer(plugin.getMessages()
						                        .get(MessageId.player_kickTempIpBanned, res.reason, TimeUtils.toString(res.duration))[0]);
					}
				}
				plugin.broadcastMessage(MessageId.player_tempIpBannedBroadcast, ip, TimeUtils.toString(res.duration), res.reason);
			}
			return true;
		}
	}

	private boolean cmdJail(CommandSender sender, String[] args) {
		sender.sendMessage(ChatColor.RED + "Not yet implemented");
		return true;  // TODO Implement method
	}

	private boolean cmdMute(CommandSender sender, String[] args) {
		final Result res = get(PunishmentType.MUTE, args);
		if (res == null) {
			return false;
		}
		if (!plugin.getUserDb().isUserKnown(res.punished)) {
			plugin.sendMessage(sender, MessageId.player_unknownUser, res.punished);
			return true;
		}
		if (res.duration == -1) {
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
				return true;
			} else {
				plugin.sendMessage(sender, MessageId.player_noPermissionForPermanent, "mute");
				return true;
			}
		} else {
			if (!db.tempMuteNick(res.punished, res.duration, res.reason)) {
				plugin.sendMessage(sender, MessageId.player_alreadyMuted, res.punished);
			} else {
				final Player target = Bukkit.getPlayerExact(res.punished);
				if (target != null) {
					plugin.sendMessage(target, MessageId.player_tempMuted, res.reason, TimeUtils.toString(res.duration));
				}
				plugin.broadcastMessage(MessageId.player_tempMutedBroadcast, res.punished, TimeUtils.toString(res.duration), res.reason);
			}
			return true;
		}
	}

	private boolean cmdUnBan(CommandSender sender, String[] args) {
		if (args.length != 1) {
			return false;
		}
		final String userName = args[0];
		if (!plugin.getUserDb().isUserKnown(userName)) {
			plugin.sendMessage(sender, MessageId.player_unknownUser, userName);
			return true;
		}
		if (db.unbanNick(userName)) {
			plugin.broadcastMessage(MessageId.player_unBannedBroadcast, userName);
			return true;
		} else {
			plugin.sendMessage(sender, MessageId.player_notBanned, userName);
			return true;
		}
	}

	private boolean cmdUnBanIp(CommandSender sender, String[] args) {
		if (args.length != 1) {
			return false;
		}
		final String ip = args[0];
		if (!plugin.getUserDb().isIpKnown(ip)) {
			plugin.sendMessage(sender, MessageId.player_unknownUser, ip);
			return true;
		}
		if (db.unbanIp(ip)) {
			plugin.broadcastMessage(MessageId.player_unBannedIpBroadcast, ip);
			return true;
		} else {
			plugin.sendMessage(sender, MessageId.player_notBannedIp, ip);
			return true;
		}
	}

	private boolean cmdUnJail(CommandSender sender, String[] args) {
		return false; // TODO
	}

	private boolean cmdUnMute(CommandSender sender, String[] args) {
		if (args.length != 1) {
			return false;
		}
		String userName = args[0];
		final Player player = Bukkit.getPlayer(userName);
		if (player != null) {
			userName = player.getName();
		}
		if (!plugin.getUserDb().isUserKnown(userName)) {
			plugin.sendMessage(sender, MessageId.player_unknownUser, userName);
			return true;
		}
		if (db.unmuteNick(userName)) {
			plugin.broadcastMessage(MessageId.player_unMutedBroadcast, userName);
			return true;
		} else {
			plugin.sendMessage(sender, MessageId.player_notMuted, userName);
			return true;
		}
	}

	private boolean cmdKick(CommandSender sender, String[] args) {
		if (args.length < 2) {
			return false;
		}
		final String userName = args[0];
		final Player player = Bukkit.getPlayer(userName);
		if (player == null) {
			plugin.sendMessage(sender, MessageId.player_unknownUser, userName);
			return true;
		} else {
			if (args.length > 1) {
				final String reason = StringUtils.joinStrings(args, 1);
				player.kickPlayer(plugin.getMessages().get(MessageId.player_kickMessage, reason)[0]);
				db.getLeaveMessages()
				  .put(player.getName(), plugin.getMessages().get(MessageId.player_broadcastedKickMessage, userName, reason)[0]);
				return true;
			} else {
				return false;
			}
		}
	}

	private class Result {

		public final String punished;
		public final String reason;
		public final long   duration;
		public final String jailPointName;

		private Result(String punished, String reason, long duration, String jailPointName) {
			this.punished = punished;
			this.reason = reason;
			this.duration = duration;
			this.jailPointName = jailPointName;
		}
	}

	private Result get(final PunishmentType type, final String[] cmdArgs) {
		if (cmdArgs.length < 2) {
			return null;
		}
		final Player player = Bukkit.getPlayer(cmdArgs[0]);
		final String punished = player == null ? cmdArgs[0] : player.getName();
		boolean permanent = false, jail = type == PunishmentType.JAIL;
		long duration = -1;
		try {
			duration = TimeUtils.getInSeconds(cmdArgs[1]);
			if (cmdArgs.length < 3 || jail && cmdArgs.length < 4) {
				return null;
			}
		} catch (final IllegalArgumentException e) {
			permanent = true;
		}
		final int reasonFirstWordIndex = 1 + (permanent ? 0 : 1) + (jail ? 1 : 0);
		final String reason = StringUtils.joinStrings(cmdArgs, reasonFirstWordIndex);
		final String jailPointName = jail ? (permanent ? cmdArgs[1] : cmdArgs[2]) : null;
		return new Result(punished, reason, duration, jailPointName);
	}
}
