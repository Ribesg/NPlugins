/***************************************************************************
 * Project file:    NPlugins - NGeneral - Perms.java                       *
 * Full Class name: fr.ribesg.bukkit.ngeneral.Perms                        *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

public class Perms {

	private static final String ADMIN                 = "ngeneral.admin";
	private static final String USER                  = "ngeneral.user";
	private static final String CMD_RELOAD            = "ngeneral.cmd.reload";
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
	private static final String CMD_REPAIR            = "ngeneral.cmd.repair";
	private static final String CMD_NIGHT_VISION      = "ngeneral.cmd.nightvision";
	private static final String CMD_ITEMNETWORK       = "ngeneral.cmd.itemnetwork";
	private static final String CMD_ITEMNETWORK_ALL   = "ngeneral.cmd.itemnetwork.all";
	private static final String CMD_TP                = "ngeneral.cmd.tp";
	private static final String CMD_TPPOS             = "ngeneral.cmd.tppos";
	private static final String CMD_TPHERE            = "ngeneral.cmd.tphere";
	private static final String CMD_TPTHERE           = "ngeneral.cmd.tpthere";
	private static final String CMD_TPWORLD           = "ngeneral.cmd.tpworld";
	private static final String CMD_TPBACK            = "ngeneral.cmd.tpback";
	private static final String PROTECTIONSIGN        = "ngeneral.protectionsign";
	private static final String PROTECTIONSIGN_BYPASS = "ngeneral.protectionsign.bypass";
	private static final String PROTECTIONSIGN_BREAK  = "ngeneral.protectionsign.break";
	private static final String SIGN_COLORS           = "ngeneral.signcolors";

	public static boolean isAdmin(final Permissible user) {
		return user.isOp() || user.hasPermission(ADMIN);
	}

	public static boolean hasReload(final Permissible user) {
		return isAdmin(user) || user.hasPermission(CMD_RELOAD);
	}

	public static boolean hasGod(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_GOD) || user.hasPermission(ADMIN);
	}

	public static boolean hasGodOthers(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_GOD_OTHERS) || user.hasPermission(ADMIN);
	}

	public static boolean hasFly(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_FLY) || user.hasPermission(ADMIN);
	}

	public static boolean hasFlyOthers(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_FLY_OTHERS) || user.hasPermission(ADMIN);
	}

	public static boolean hasFlySpeed(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_FLYSPEED) || user.hasPermission(ADMIN);
	}

	public static boolean hasFlySpeedOthers(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_FLYSPEED_OTHERS) || user.hasPermission(ADMIN);
	}

	public static boolean hasWalkSpeed(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_WALKSPEED) || user.hasPermission(ADMIN);
	}

	public static boolean hasWalkSpeedOthers(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_WALKSPEED_OTHERS) || user.hasPermission(ADMIN);
	}

	public static boolean hasAfk(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_AFK) || user.hasPermission(USER) || user.hasPermission(ADMIN);
	}

	public static boolean hasBusy(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_BUSY) || user.hasPermission(USER) || user.hasPermission(ADMIN);
	}

	public static boolean hasTime(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_TIME) || user.hasPermission(ADMIN);
	}

	public static boolean hasWeather(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_WEATHER) || user.hasPermission(ADMIN);
	}

	public static boolean hasRepair(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_REPAIR) || user.hasPermission(ADMIN);
	}

	public static boolean hasNightVision(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_NIGHT_VISION) || user.hasPermission(ADMIN);
	}

	public static boolean hasItemNetwork(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_ITEMNETWORK) || user.hasPermission(ADMIN);
	}

	public static boolean hasItemNetworkAll(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_ITEMNETWORK_ALL) || user.hasPermission(ADMIN);
	}

	public static boolean hasTp(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_TP) || user.hasPermission(ADMIN);
	}

	public static boolean hasTpPos(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_TPPOS) || user.hasPermission(ADMIN);
	}

	public static boolean hasTpHere(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_TPHERE) || user.hasPermission(ADMIN);
	}

	public static boolean hasTpThere(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_TPTHERE) || user.hasPermission(ADMIN);
	}

	public static boolean hasTpWorld(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_TPWORLD) || user.hasPermission(ADMIN);
	}

	public static boolean hasTpBack(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_TPBACK) || user.hasPermission(ADMIN);
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
