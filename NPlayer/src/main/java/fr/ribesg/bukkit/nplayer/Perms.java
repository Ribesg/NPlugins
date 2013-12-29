/***************************************************************************
 * Project file:    NPlugins - NPlayer - Perms.java                        *
 * Full Class name: fr.ribesg.bukkit.nplayer.Perms                         *
 *                                                                         *
 *                Copyright (c) 2013 Ribesg - www.ribesg.fr                *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nplayer;

import org.bukkit.command.CommandSender;

public class Perms {

	// Player node permissions
	private static final String ADMIN               = "nplayer.admin";
	private static final String USER                = "nplayer.user";
	private static final String CMD_FORCELOGIN      = "nplayer.cmd.forcelogin";
	private static final String CMD_LOGIN           = "nplayer.cmd.login";
	private static final String CMD_LOGOUT          = "nplayer.cmd.logout";
	private static final String CMD_REGISTER        = "nplayer.cmd.register";
	private static final String CMD_INFO            = "nplayer.cmd.info";
	private static final String CMD_INFO_ADMIN      = "nplayer.cmd.info.admin";
	private static final String CMD_HOME            = "nplayer.cmd.home";
	private static final String CMD_HOME_OTHERS     = "nplayer.cmd.home.others";
	private static final String CMD_SETHOME         = "nplayer.cmd.sethome";
	private static final String CMD_SETHOME_OTHERS  = "nplayer.cmd.sethome.others";
	private static final String CMD_BAN             = "nplayer.cmd.ban";
	private static final String CMD_BAN_PERMANENT   = "nplayer.cmd.ban.permanent";
	private static final String CMD_UNBAN           = "nplayer.cmd.unban";
	private static final String CMD_BANIP           = "nplayer.cmd.banip";
	private static final String CMD_BANIP_PERMANENT = "nplayer.cmd.banip.permanent";
	private static final String CMD_UNBANIP         = "nplayer.cmd.unbanip";
	private static final String CMD_JAIL            = "nplayer.cmd.jail";
	private static final String CMD_JAIL_PERMANENT  = "nplayer.cmd.jail.permanent";
	private static final String CMD_UNJAIL          = "nplayer.cmd.unjail";
	private static final String CMD_MUTE            = "nplayer.cmd.mute";
	private static final String CMD_MUTE_PERMANENT  = "nplayer.cmd.mute.permanent";
	private static final String CMD_UNMUTE          = "nplayer.cmd.unmute";
	private static final String CMD_KICK            = "nplayer.cmd.kick";

	public static boolean hasForceLogin(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_FORCELOGIN) || sender.hasPermission(ADMIN);
	}

	public static boolean hasLogin(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_LOGIN) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasLogout(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_LOGOUT) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasRegister(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_REGISTER) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasInfo(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_INFO) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasInfoAdmin(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_INFO_ADMIN) || sender.hasPermission(ADMIN);
	}

	public static boolean hasHome(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_HOME) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasHomeOthers(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_HOME_OTHERS) || sender.hasPermission(ADMIN);
	}

	public static boolean hasSetHome(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_SETHOME) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasSetHomeOthers(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_SETHOME_OTHERS) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasBan(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_BAN) || sender.hasPermission(ADMIN);
	}

	public static boolean hasBanPermanent(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_BAN_PERMANENT) || sender.hasPermission(ADMIN);
	}

	public static boolean hasUnBan(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_UNBAN) || sender.hasPermission(ADMIN);
	}

	public static boolean hasBanIp(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_BANIP) || sender.hasPermission(ADMIN);
	}

	public static boolean hasBanIpPermanent(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_BANIP_PERMANENT) || sender.hasPermission(ADMIN);
	}

	public static boolean hasUnBanIp(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_UNBANIP) || sender.hasPermission(ADMIN);
	}

	public static boolean hasJail(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_JAIL) || sender.hasPermission(ADMIN);
	}

	public static boolean hasJailPermanent(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_JAIL_PERMANENT) || sender.hasPermission(ADMIN);
	}

	public static boolean hasUnJail(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_UNJAIL) || sender.hasPermission(ADMIN);
	}

	public static boolean hasMute(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_MUTE) || sender.hasPermission(ADMIN);
	}

	public static boolean hasMutePermanent(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_MUTE_PERMANENT) || sender.hasPermission(ADMIN);
	}

	public static boolean hasUnMute(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_UNMUTE) || sender.hasPermission(ADMIN);
	}

	public static boolean hasKick(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_KICK) || sender.hasPermission(ADMIN);
	}
}
