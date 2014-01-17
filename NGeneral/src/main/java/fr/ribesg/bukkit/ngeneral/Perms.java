/***************************************************************************
 * Project file:    NPlugins - NGeneral - Perms.java                       *
 * Full Class name: fr.ribesg.bukkit.ngeneral.Perms                        *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

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
	private static final String CMD_BUSY              = "ngeneral.cmd.busy";
	private static final String CMD_TIME              = "ngeneral.cmd.time";
	private static final String CMD_WEATHER           = "ngeneral.cmd.weather";
	private static final String CMD_ITEMNETWORK       = "ngeneral.cmd.itemnetwork";
	private static final String CMD_ITEMNETWORK_ALL   = "ngeneral.cmd.itemnetwork.all";
	private static final String CMD_TP                = "ngeneral.cmd.tp";
	private static final String CMD_TPPOS             = "ngeneral.cmd.tppos";
	private static final String CMD_TPHERE            = "ngeneral.cmd.tphere";
	private static final String CMD_TPTHERE           = "ngeneral.cmd.tpthere";
	private static final String CMD_TPBACK            = "ngeneral.cmd.tpback";
	private static final String PROTECTIONSIGN        = "ngeneral.protectionsign";
	private static final String PROTECTIONSIGN_BYPASS = "ngeneral.protectionsign.bypass";
	private static final String PROTECTIONSIGN_BREAK  = "ngeneral.protectionsign.break";
	private static final String SIGN_COLORS           = "ngeneral.signcolors";

	public static boolean isAdmin(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(ADMIN);
	}

	public static boolean hasGod(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_GOD) || sender.hasPermission(ADMIN);
	}

	public static boolean hasGodOthers(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_GOD_OTHERS) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFly(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_FLY) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlyOthers(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_FLY_OTHERS) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlySpeed(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_FLYSPEED) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlySpeedOthers(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_FLYSPEED_OTHERS) || sender.hasPermission(ADMIN);
	}

	public static boolean hasWalkSpeed(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_WALKSPEED) || sender.hasPermission(ADMIN);
	}

	public static boolean hasWalkSpeedOthers(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_WALKSPEED_OTHERS) || sender.hasPermission(ADMIN);
	}

	public static boolean hasAfk(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_AFK) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasBusy(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_BUSY) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasTime(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_TIME) || sender.hasPermission(ADMIN);
	}

	public static boolean hasWeather(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_WEATHER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasItemNetwork(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_ITEMNETWORK) || sender.hasPermission(ADMIN);
	}

	public static boolean hasItemNetworkAll(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_ITEMNETWORK_ALL) || sender.hasPermission(ADMIN);
	}

	public static boolean hasTp(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_TP) || sender.hasPermission(ADMIN);
	}

	public static boolean hasTpPos(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_TPPOS) || sender.hasPermission(ADMIN);
	}

	public static boolean hasTpHere(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_TPHERE) || sender.hasPermission(ADMIN);
	}

	public static boolean hasTpThere(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_TPTHERE) || sender.hasPermission(ADMIN);
	}

	public static boolean hasTpBack(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_TPBACK) || sender.hasPermission(ADMIN);
	}

	public static boolean hasProtectionSign(final Player player) {
		return player.isOp() || player.hasPermission(PROTECTIONSIGN) || player.hasPermission(USER) || player.hasPermission(ADMIN);
	}

	public static boolean hasProtectionSignBypass(final Player player) {
		return player.isOp() || player.hasPermission(PROTECTIONSIGN_BYPASS) || player.hasPermission(ADMIN);
	}

	public static boolean hasProtectionSignBreak(final Player player) {
		return player.isOp() || player.hasPermission(PROTECTIONSIGN_BREAK) || player.hasPermission(ADMIN);
	}

	public static boolean hasSignColors(final Player player) {
		return player.isOp() || player.hasPermission(SIGN_COLORS) || player.hasPermission(USER) || player.hasPermission(ADMIN);
	}
}
