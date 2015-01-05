/***************************************************************************
 * Project file:    NPlugins - NPlayer - PunishmentCommandHandler.java     *
 * Full Class name: fr.ribesg.bukkit.nplayer.PunishmentCommandHandler      *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nplayer;

import fr.ribesg.bukkit.ncore.config.UuidDb;
import fr.ribesg.bukkit.ncore.lang.MessageId;
import fr.ribesg.bukkit.ncore.node.Node;
import fr.ribesg.bukkit.ncore.node.cuboid.CuboidNode;
import fr.ribesg.bukkit.ncore.util.IPValidator;
import fr.ribesg.bukkit.ncore.util.StringUtil;
import fr.ribesg.bukkit.ncore.util.TimeUtil;
import fr.ribesg.bukkit.nplayer.punishment.PunishmentDb;
import fr.ribesg.bukkit.nplayer.punishment.PunishmentType;
import fr.ribesg.bukkit.nplayer.user.User;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class PunishmentCommandHandler implements CommandExecutor, Listener {

    private final NPlayer      plugin;
    private final PunishmentDb db;

    public PunishmentCommandHandler(final NPlayer instance) {
        this.plugin = instance;
        this.db = this.plugin.getPunishmentDb();
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        this.plugin.entering(this.getClass(), "onCommand", "cmd=" + cmd.getName());
        final boolean result;

        switch (cmd.getName()) {
            case "ban":
                if (Perms.hasBan(sender)) {
                    result = this.cmdBan(sender, args);
                } else {
                    this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    result = true;
                }
                break;
            case "banip":
                if (Perms.hasBanIp(sender)) {
                    result = this.cmdBanIp(sender, args);
                } else {
                    this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    result = true;
                }
                break;
            case "jail":
                if (Perms.hasJail(sender)) {
                    result = this.cmdJail(sender, args);
                } else {
                    this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    result = true;
                }
                break;
            case "mute":
                if (Perms.hasMute(sender)) {
                    result = this.cmdMute(sender, args);
                } else {
                    this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    result = true;
                }
                break;
            case "unban":
                if (Perms.hasUnBan(sender)) {
                    result = this.cmdUnBan(sender, args);
                } else {
                    this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    result = true;
                }
                break;
            case "unbanip":
                if (Perms.hasUnBanIp(sender)) {
                    result = this.cmdUnBanIp(sender, args);
                } else {
                    this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    result = true;
                }
                break;
            case "unjail":
                if (Perms.hasUnJail(sender)) {
                    result = this.cmdUnJail(sender, args);
                } else {
                    this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    result = true;
                }
                break;
            case "unmute":
                if (Perms.hasUnMute(sender)) {
                    result = this.cmdUnMute(sender, args);
                } else {
                    this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    result = true;
                }
                break;
            case "kick":
                if (Perms.hasKick(sender)) {
                    result = this.cmdKick(sender, args);
                } else {
                    this.plugin.sendMessage(sender, MessageId.noPermissionForCommand);
                    result = true;
                }
                break;
            default:
                result = false;
        }

        this.plugin.exiting(this.getClass(), "onCommand");
        return result;
    }

    private boolean cmdBan(final CommandSender sender, final String[] args) {
        this.plugin.entering(this.getClass(), "cmdBan");

        final Result res = this.parsePunishmentDataFromArgs(PunishmentType.BAN, args);
        if (res == null) {
            this.plugin.exiting(this.getClass(), "cmdBan", "Invalid arguments");
            return false;
        }
        final UUID id = UuidDb.getId(Node.PLAYER, res.punished);
        if (!this.plugin.getUserDb().isUserKnown(id)) {
            this.plugin.sendMessage(sender, MessageId.player_unknownUser, res.punished);
        } else if (res.duration == -1) {
            if (Perms.hasBanPermanent(sender)) {
                if (!this.db.permBanId(id, res.reason)) {
                    this.plugin.sendMessage(sender, MessageId.player_alreadyBanned, res.punished);
                } else {
                    final Player target = Bukkit.getPlayer(id);
                    if (target != null) {
                        this.db.getLeaveMessages().put(target.getUniqueId(), this.plugin.getMessages().get(MessageId.player_permBannedBroadcast, target.getName(), res.reason)[0]);
                        target.kickPlayer(this.plugin.getMessages().get(MessageId.player_kickPermBanned, res.reason)[0]);
                    } else {
                        this.plugin.broadcastMessage(MessageId.player_permBannedBroadcast, res.punished, res.reason);
                    }
                }
            } else {
                this.plugin.sendMessage(sender, MessageId.player_noPermissionForPermanent, "ban");
            }
        } else {
            if (!this.db.tempBanId(id, res.duration, res.reason)) {
                this.plugin.sendMessage(sender, MessageId.player_alreadyBanned, res.punished);
            } else {
                final Player target = Bukkit.getPlayer(id);
                if (target != null) {
                    this.db.getLeaveMessages().put(target.getUniqueId(), this.plugin.getMessages().get(MessageId.player_tempBannedBroadcast, target.getName(), TimeUtil.toString(res.duration), res.reason)[0]);
                    target.kickPlayer(this.plugin.getMessages().get(MessageId.player_kickTempBanned, res.reason, TimeUtil.toString(res.duration))[0]);
                } else {
                    this.plugin.broadcastMessage(MessageId.player_tempBannedBroadcast, res.punished, TimeUtil.toString(res.duration), res.reason);
                }
            }
        }

        this.plugin.exiting(this.getClass(), "cmdBan");
        return true;
    }

    private boolean cmdBanIp(final CommandSender sender, final String[] args) {
        this.plugin.entering(this.getClass(), "cmdBanIp");

        final Result res = this.parsePunishmentDataFromArgs(PunishmentType.IPBAN, args);
        if (res == null) {
            this.plugin.exiting(this.getClass(), "cmdBanIp", "Invalid arguments");
            return false;
        }
        final boolean isIp = IPValidator.isValidIp(res.punished);
        final String ip;
        if (isIp) {
            ip = res.punished;
            if (!this.plugin.getUserDb().isIpKnown(ip)) {
                this.plugin.sendMessage(sender, MessageId.player_unknownIp, ip);
                this.plugin.exiting(this.getClass(), "cmdBanIp", "Unknown IP");
                return true;
            }
        } else {
            final UUID id = UuidDb.getId(Node.PLAYER, res.punished);
            final User user = this.plugin.getUserDb().get(id);
            if (user == null) {
                this.plugin.sendMessage(sender, MessageId.player_unknownUser, res.punished);
                this.plugin.exiting(this.getClass(), "cmdBanIp", "Unknown user");
                return true;
            } else {
                ip = user.getLastIp();
            }
        }
        if (res.duration == -1) {
            if (Perms.hasBanIpPermanent(sender)) {
                if (!this.db.permBanIp(ip, res.reason)) {
                    this.plugin.sendMessage(sender, MessageId.player_alreadyBannedIp, ip);
                } else {
                    final List<User> targets = this.plugin.getUserDb().getByIp(ip);
                    for (final User u : targets) {
                        final Player player = Bukkit.getPlayer(u.getUserId());
                        if (player != null) {
                            player.kickPlayer(this.plugin.getMessages().get(MessageId.player_kickPermIpBanned, res.reason)[0]);
                        }
                    }
                    this.plugin.broadcastMessage(MessageId.player_permIpBannedBroadcast, ip, res.reason);
                }
            } else {
                this.plugin.sendMessage(sender, MessageId.player_noPermissionForPermanent, "banip");
            }
        } else {
            if (!this.db.tempBanIp(res.punished, res.duration, res.reason)) {
                this.plugin.sendMessage(sender, MessageId.player_alreadyBannedIp, ip);
            } else {
                final List<User> targets = this.plugin.getUserDb().getByIp(ip);
                for (final User u : targets) {
                    final Player player = Bukkit.getPlayer(u.getUserId());
                    if (player != null) {
                        player.kickPlayer(this.plugin.getMessages().get(MessageId.player_kickTempIpBanned, res.reason, TimeUtil.toString(res.duration))[0]);
                    }
                }
                this.plugin.broadcastMessage(MessageId.player_tempIpBannedBroadcast, ip, TimeUtil.toString(res.duration), res.reason);
            }
        }

        this.plugin.exiting(this.getClass(), "cmdBanIp");
        return true;
    }

    private boolean cmdJail(final CommandSender sender, final String[] args) {
        this.plugin.entering(this.getClass(), "cmdJail");

        final CuboidNode cuboidNode = this.plugin.getCuboidNode();
        if (cuboidNode == null) {
            this.plugin.sendMessage(sender, MessageId.player_cuboidNodeRequired);
            this.plugin.exiting(this.getClass(), "cmdJail", "No Cuboid Node");
            return true;
        }

        final Result res = this.parsePunishmentDataFromArgs(PunishmentType.JAIL, args);
        if (res == null) {
            this.plugin.exiting(this.getClass(), "cmdJail", "Invalid arguments");
            return false;
        }
        final UUID id = UuidDb.getId(Node.PLAYER, res.punished);
        if (!this.plugin.getUserDb().isUserKnown(id)) {
            this.plugin.sendMessage(sender, MessageId.player_unknownUser, res.punished);
        } else if (!cuboidNode.getJailsSet().contains(res.jailPointName.toLowerCase())) {
            this.plugin.sendMessage(sender, MessageId.player_unknownJail, res.jailPointName);
        } else if (res.duration == -1) {
            if (Perms.hasJailPermanent(sender)) {
                if (!this.db.permJailId(id, res.reason, res.jailPointName)) {
                    this.plugin.sendMessage(sender, MessageId.player_alreadyJailed, res.punished);
                } else {
                    final Player target = Bukkit.getPlayer(id);
                    if (target != null) {
                        target.teleport(cuboidNode.getJailLocation(res.jailPointName).toBukkitLocation());
                        this.plugin.sendMessage(target, MessageId.player_permJailed, res.reason);
                    }
                    this.plugin.broadcastMessage(MessageId.player_permJailedBroadcast, target == null ? res.punished : target.getName(), res.reason);
                }
            } else {
                this.plugin.sendMessage(sender, MessageId.player_noPermissionForPermanent, "jail");
            }
        } else {
            if (!this.db.tempJailId(id, res.duration, res.reason, res.jailPointName)) {
                this.plugin.sendMessage(sender, MessageId.player_alreadyJailed, res.punished);
            } else {
                final Player target = Bukkit.getPlayer(id);
                if (target != null) {
                    target.teleport(cuboidNode.getJailLocation(res.jailPointName).toBukkitLocation());
                    this.plugin.sendMessage(target, MessageId.player_tempJailed, res.reason, TimeUtil.toString(res.duration));
                }
                this.plugin.broadcastMessage(MessageId.player_tempJailedBroadcast, target == null ? res.punished : target.getName(), TimeUtil.toString(res.duration), res.reason);
            }
        }

        this.plugin.exiting(this.getClass(), "cmdJail");
        return true;
    }

    private boolean cmdMute(final CommandSender sender, final String[] args) {
        this.plugin.entering(this.getClass(), "cmdMute");

        final Result res = this.parsePunishmentDataFromArgs(PunishmentType.MUTE, args);
        if (res == null) {
            this.plugin.exiting(this.getClass(), "cmdMute", "Invalid arguments");
            return false;
        }
        final UUID id = UuidDb.getId(Node.PLAYER, res.punished);
        if (!this.plugin.getUserDb().isUserKnown(id)) {
            this.plugin.sendMessage(sender, MessageId.player_unknownUser, res.punished);
        } else if (res.duration == -1) {
            if (Perms.hasMutePermanent(sender)) {
                if (!this.db.permMuteId(id, res.reason)) {
                    this.plugin.sendMessage(sender, MessageId.player_alreadyMuted, res.punished);
                } else {
                    final Player target = Bukkit.getPlayer(id);
                    if (target != null) {
                        this.plugin.sendMessage(target, MessageId.player_permMuted, res.reason);
                    }
                    this.plugin.broadcastMessage(MessageId.player_permMutedBroadcast, target == null ? res.punished : target.getName(), res.reason);
                }
            } else {
                this.plugin.sendMessage(sender, MessageId.player_noPermissionForPermanent, "mute");
            }
        } else {
            if (!this.db.tempMuteId(id, res.duration, res.reason)) {
                this.plugin.sendMessage(sender, MessageId.player_alreadyMuted, res.punished);
            } else {
                final Player target = Bukkit.getPlayer(id);
                if (target != null) {
                    this.plugin.sendMessage(target, MessageId.player_tempMuted, res.reason, TimeUtil.toString(res.duration));
                }
                this.plugin.broadcastMessage(MessageId.player_tempMutedBroadcast, target == null ? res.punished : target.getName(), TimeUtil.toString(res.duration), res.reason);
            }
        }

        this.plugin.exiting(this.getClass(), "cmdMute");
        return true;
    }

    private boolean cmdUnBan(final CommandSender sender, final String[] args) {
        this.plugin.entering(this.getClass(), "cmdUnBan");

        if (args.length != 1) {
            this.plugin.exiting(this.getClass(), "cmdUnBan", "Invalid arguments");
            return false;
        }
        final String userName = args[0];
        final UUID id = UuidDb.getId(Node.PLAYER, userName);
        if (!this.plugin.getUserDb().isUserKnown(id)) {
            this.plugin.sendMessage(sender, MessageId.player_unknownUser, userName);
        } else if (this.db.unBanId(id)) {
            this.plugin.broadcastMessage(MessageId.player_unBannedBroadcast, userName);
        } else {
            this.plugin.sendMessage(sender, MessageId.player_notBanned, userName);
        }

        this.plugin.exiting(this.getClass(), "cmdUnBan");
        return true;
    }

    private boolean cmdUnBanIp(final CommandSender sender, final String[] args) {
        this.plugin.entering(this.getClass(), "cmdUnBanIp");

        if (args.length != 1) {
            this.plugin.exiting(this.getClass(), "cmdUnBanIp", "Invalid arguments");
            return false;
        }
        final String input = args[0];
        final boolean isIp = IPValidator.isValidIp(input);
        final String ip;
        if (isIp) {
            ip = input;
            if (!this.plugin.getUserDb().isIpKnown(ip)) {
                this.plugin.sendMessage(sender, MessageId.player_unknownIp, ip);
                this.plugin.exiting(this.getClass(), "cmdBanIp", "Unknown IP");
                return true;
            }
        } else {
            final UUID id = UuidDb.getId(Node.PLAYER, input);
            final User user = this.plugin.getUserDb().get(id);
            if (user == null) {
                this.plugin.sendMessage(sender, MessageId.player_unknownUser, input);
                this.plugin.exiting(this.getClass(), "cmdBanIp", "Unknown user");
                return true;
            } else {
                ip = user.getLastIp();
            }
        }
        if (this.db.unBanIp(ip)) {
            this.plugin.broadcastMessage(MessageId.player_unBannedIpBroadcast, ip);
        } else {
            this.plugin.sendMessage(sender, MessageId.player_notBannedIp, ip);
        }

        this.plugin.exiting(this.getClass(), "cmdUnBanIp");
        return true;
    }

    private boolean cmdUnJail(final CommandSender sender, final String[] args) {
        this.plugin.entering(this.getClass(), "cmdUnJail");

        if (args.length != 1) {
            this.plugin.exiting(this.getClass(), "cmdUnJail", "Invalid arguments");
            return false;
        }
        String name = args[0];
        final Player player = Bukkit.getPlayer(name);
        if (player != null) {
            name = player.getName();
        }
        final UUID id = UuidDb.getId(Node.PLAYER, name);
        final String realName = UuidDb.getName(id);
        if (!this.plugin.getUserDb().isUserKnown(id)) {
            this.plugin.sendMessage(sender, MessageId.player_unknownUser, name);
        } else if (this.db.unJailId(id)) {
            this.plugin.broadcastMessage(MessageId.player_unJailedBroadcast, realName);
        } else {
            this.plugin.sendMessage(sender, MessageId.player_notJailed, realName);
        }

        this.plugin.exiting(this.getClass(), "cmdUnJail");
        return true;
    }

    private boolean cmdUnMute(final CommandSender sender, final String[] args) {
        this.plugin.entering(this.getClass(), "cmdUnMute");

        if (args.length != 1) {
            this.plugin.exiting(this.getClass(), "cmdUnMute", "Invalid arguments");
            return false;
        }
        String name = args[0];
        final Player player = Bukkit.getPlayer(name);
        if (player != null) {
            name = player.getName();
        }
        final UUID id = UuidDb.getId(Node.PLAYER, name);
        final String realName = UuidDb.getName(id);
        if (!this.plugin.getUserDb().isUserKnown(id)) {
            this.plugin.sendMessage(sender, MessageId.player_unknownUser, name);
        } else if (this.db.unMuteId(id)) {
            this.plugin.broadcastMessage(MessageId.player_unMutedBroadcast, realName);
        } else {
            this.plugin.sendMessage(sender, MessageId.player_notMuted, realName);
        }

        this.plugin.exiting(this.getClass(), "cmdUnMute");
        return true;
    }

    private boolean cmdKick(final CommandSender sender, final String[] args) {
        this.plugin.entering(this.getClass(), "cmdKick");

        if (args.length < 2) {
            this.plugin.exiting(this.getClass(), "cmdKick", "Invalid arguments");
            return false;
        }
        final String userName = args[0];
        final Player player = Bukkit.getPlayer(userName);
        if (player == null) {
            this.plugin.sendMessage(sender, MessageId.player_unknownUser, userName);
        } else {
            final String reason = StringUtil.joinStrings(args, 1);
            this.db.getLeaveMessages().put(player.getUniqueId(), this.plugin.getMessages().get(MessageId.player_broadcastedKickMessage, player.getName(), reason)[0]);
            player.kickPlayer(this.plugin.getMessages().get(MessageId.player_kickMessage, reason)[0]);
        }

        this.plugin.exiting(this.getClass(), "cmdKick");
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
        this.plugin.entering(this.getClass(), "parsePunishmentDataFromArgs");

        if (cmdArgs.length < 2) {
            this.plugin.exiting(this.getClass(), "parsePunishmentDataFromArgs", "Invalid arguments (No reason)");
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
                this.plugin.exiting(this.getClass(), "parsePunishmentDataFromArgs", "Invalid arguments (No reason for temporary)");
                return null;
            }
        } catch (final IllegalArgumentException e) {
            permanent = true;
        }
        final int reasonFirstWordIndex = 1 + (permanent ? 0 : 1) + (jail ? 1 : 0);
        final String reason = StringUtil.joinStrings(cmdArgs, reasonFirstWordIndex);
        final String jailPointName = jail ? permanent ? cmdArgs[1] : cmdArgs[2] : null;

        this.plugin.exiting(this.getClass(), "parsePunishmentDataFromArgs");
        return new Result(punished, reason, duration, jailPointName);
    }
}
