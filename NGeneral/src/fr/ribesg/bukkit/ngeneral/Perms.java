package fr.ribesg.bukkit.ngeneral;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Perms {

	// World node permissions
	private static final String ADMIN                 = "ngeneral.admin";
	private static final String USER                  = "ngeneral.user";
	private static final String CMD_GOD               = "ngeneral.cmd.god";
	private static final String CMD_GOD_OTHERS        = "ngeneral.cmd.god.others";
	private static final String CMD_FLY               = "ngeneral.cmd.fly";
	private static final String CMD_FLY_OTHERS        = "ngeneral.cmd.fly.others";
	private static final String CMD_FLYSPEED          = "ngeneral.cmd.flyspeed";
	private static final String CMD_FLYSPEED_OTHERS   = "ngeneral.cmd.flyspeed.others";
	private static final String CMD_WALKSPEED         = "ngeneral.cmd.walkspeed";
	private static final String CMD_WALKSPEED_OTHERS  = "ngeneral.cmd.walkspeed.others";
	private static final String CMD_AFK               = "ngeneral.cmd.afk";
	private static final String CMD_TIME              = "ngeneral.cmd.time";
	private static final String CMD_WEATHER           = "ngeneral.cmd.weather";
	private static final String PROTECTIONSIGN        = "ngeneral.protectionsign";
	private static final String PROTECTIONSIGN_BYPASS = "ngeneral.protectionsign.bypass";
	private static final String PROTECTIONSIGN_BREAK  = "ngeneral.protectionsign.break";
	private static final String SIGN_COLORS           = "ngeneral.signcolors";

	public static boolean hasGod(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_GOD) || sender.hasPermission(ADMIN);
	}

	public static boolean hasGodOthers(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_GOD_OTHERS) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFly(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_FLY) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlyOthers(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_FLY_OTHERS) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlySpeed(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_FLYSPEED) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlySpeedOthers(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_FLYSPEED_OTHERS) || sender.hasPermission(ADMIN);
	}

	public static boolean hasWalkSpeed(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_WALKSPEED) || sender.hasPermission(ADMIN);
	}

	public static boolean hasWalkSpeedOthers(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_WALKSPEED_OTHERS) || sender.hasPermission(ADMIN);
	}

	public static boolean hasAfk(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_AFK) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasTime(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_TIME) || sender.hasPermission(ADMIN);
	}

	public static boolean hasWeather(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_WEATHER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasProtectionSign(Player player) {
		return player.isOp() || player.hasPermission(PROTECTIONSIGN) || player.hasPermission(USER) || player.hasPermission(ADMIN);
	}

	public static boolean hasProtectionSignBypass(Player player) {
		return player.isOp() || player.hasPermission(PROTECTIONSIGN_BYPASS) || player.hasPermission(ADMIN);
	}

	public static boolean hasProtectionSignBreak(Player player) {
		return player.isOp() || player.hasPermission(PROTECTIONSIGN_BREAK) || player.hasPermission(ADMIN);
	}

	public static boolean hasSignColors(Player player) {
		return player.isOp() || player.hasPermission(SIGN_COLORS) || player.hasPermission(USER) || player.hasPermission(ADMIN);
	}
}
