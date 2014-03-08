/***************************************************************************
 * Project file:    NPlugins - NPlayer - Perms.java                        *
 * Full Class name: fr.ribesg.bukkit.nplayer.Perms                         *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nplayer;

import org.bukkit.permissions.Permissible;

public class Perms {

	private static final String ADMIN               = "nplayer.admin";
	private static final String USER                = "nplayer.user";
	private static final String CMD_RELOAD          = "nplayer.cmd.reload";
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

	public static boolean hasReload(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_RELOAD) || user.hasPermission(ADMIN);
	}

	public static boolean hasForceLogin(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_FORCELOGIN) || user.hasPermission(ADMIN);
	}

	public static boolean hasLogin(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_LOGIN) || user.hasPermission(USER) || user.hasPermission(ADMIN);
	}

	public static boolean hasLogout(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_LOGOUT) || user.hasPermission(USER) || user.hasPermission(ADMIN);
	}

	public static boolean hasRegister(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_REGISTER) || user.hasPermission(USER) || user.hasPermission(ADMIN);
	}

	public static boolean hasInfo(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_INFO) || user.hasPermission(USER) || user.hasPermission(ADMIN);
	}

	public static boolean hasInfoAdmin(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_INFO_ADMIN) || user.hasPermission(ADMIN);
	}

	public static boolean hasHome(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_HOME) || user.hasPermission(USER) || user.hasPermission(ADMIN);
	}

	public static boolean hasHomeOthers(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_HOME_OTHERS) || user.hasPermission(ADMIN);
	}

	public static boolean hasSetHome(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_SETHOME) || user.hasPermission(USER) || user.hasPermission(ADMIN);
	}

	public static boolean hasSetHomeOthers(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_SETHOME_OTHERS) || user.hasPermission(USER) || user.hasPermission(ADMIN);
	}

	public static boolean hasBan(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_BAN) || user.hasPermission(ADMIN);
	}

	public static boolean hasBanPermanent(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_BAN_PERMANENT) || user.hasPermission(ADMIN);
	}

	public static boolean hasUnBan(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_UNBAN) || user.hasPermission(ADMIN);
	}

	public static boolean hasBanIp(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_BANIP) || user.hasPermission(ADMIN);
	}

	public static boolean hasBanIpPermanent(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_BANIP_PERMANENT) || user.hasPermission(ADMIN);
	}

	public static boolean hasUnBanIp(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_UNBANIP) || user.hasPermission(ADMIN);
	}

	public static boolean hasJail(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_JAIL) || user.hasPermission(ADMIN);
	}

	public static boolean hasJailPermanent(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_JAIL_PERMANENT) || user.hasPermission(ADMIN);
	}

	public static boolean hasUnJail(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_UNJAIL) || user.hasPermission(ADMIN);
	}

	public static boolean hasMute(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_MUTE) || user.hasPermission(ADMIN);
	}

	public static boolean hasMutePermanent(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_MUTE_PERMANENT) || user.hasPermission(ADMIN);
	}

	public static boolean hasUnMute(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_UNMUTE) || user.hasPermission(ADMIN);
	}

	public static boolean hasKick(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_KICK) || user.hasPermission(ADMIN);
	}
}
