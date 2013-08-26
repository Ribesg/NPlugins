package fr.ribesg.bukkit.nplayer;

import org.bukkit.command.CommandSender;

public class Perms {

	// Player node permissions
	private static final String ADMIN              = "nplayer.admin";
	private static final String USER               = "nplayer.user";
	private static final String CMD_LOGIN          = "nplayer.cmd.login";
	private static final String CMD_LOGOUT         = "nplayer.cmd.logout";
	private static final String CMD_REGISTER       = "nplayer.cmd.register";
	private static final String CMD_INFO           = "nplayer.cmd.info";
	private static final String CMD_INFO_ADMIN     = "nplayer.cmd.info.admin";
	private static final String CMD_HOME           = "nplayer.cmd.home";
	private static final String CMD_HOME_OTHERS    = "nplayer.cmd.home.others";
	private static final String CMD_SETHOME        = "nplayer.cmd.sethome";
	private static final String CMD_BAN            = "nplayer.cmd.ban";
	private static final String CMD_BAN_PERMANENT  = "nplayer.cmd.ban.permanent";
	private static final String CMD_JAIL           = "nplayer.cmd.jail";
	private static final String CMD_JAIL_PERMANENT = "nplayer.cmd.jail.permanent";
	private static final String CMD_KICK           = "nplayer.cmd.kick";
	private static final String CMD_MUTE           = "nplayer.cmd.mute";
	private static final String CMD_MUTE_PERMANENT = "nplayer.cmd.mute.permanent";

	public static boolean hasLogin(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_LOGIN) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasLogout(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_LOGOUT) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasRegister(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_REGISTER) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasInfo(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_INFO) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasInfoAdmin(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_INFO_ADMIN) || sender.hasPermission(ADMIN);
	}

	public static boolean hasHome(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_HOME) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasHomeOthers(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_HOME_OTHERS) || sender.hasPermission(ADMIN);
	}

	public static boolean hasSetHome(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_SETHOME) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasBan(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_BAN) || sender.hasPermission(ADMIN);
	}

	public static boolean hasBanPermanent(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_BAN_PERMANENT) || sender.hasPermission(ADMIN);
	}

	public static boolean hasJail(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_JAIL) || sender.hasPermission(ADMIN);
	}

	public static boolean hasJailPermanent(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_JAIL_PERMANENT) || sender.hasPermission(ADMIN);
	}

	public static boolean hasKick(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_KICK) || sender.hasPermission(ADMIN);
	}

	public static boolean hasMute(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_MUTE) || sender.hasPermission(ADMIN);
	}

	public static boolean hasMutePermanent(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_MUTE_PERMANENT) || sender.hasPermission(ADMIN);
	}
}
