/***************************************************************************
 * Project file:    NPlugins - NCuboid - Perms.java                        *
 * Full Class name: fr.ribesg.bukkit.ncuboid.Perms                         *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid;

import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.beans.FlagAtt;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class Perms {

	// Cuboid node permissions
	private static final String ADMIN                = "ncuboid.admin";
	private static final String USER                 = "ncuboid.user";
	private static final String SEE_INVISIBLE_CUBOID = "ncuboid.seeinvisible";
	private static final String CMD_GENERAL          = "ncuboid.cmd.cuboid";
	private static final String CMD_RELOAD           = "ncuboid.cmd.reload";
	private static final String CMD_CREATE           = "ncuboid.cmd.create";
	private static final String CMD_DELETE           = "ncuboid.cmd.delete";
	private static final String CMD_FLAG             = "ncuboid.cmd.flag";
	private static final String CMD_FLAGATTRIBUTE    = "ncuboid.cmd.flagattribute";
	private static final String CMD_JAIL             = "ncuboid.cmd.jail";
	private static final String CMD_ADMIN            = "ncuboid.cmd.admin";
	private static final String CMD_USER             = "ncuboid.cmd.user";
	private static final String CMD_GROUP            = "ncuboid.cmd.group";

	// Flag permissions
	private static Map<Flag, String> flagPermissions;

	private static String getFlagPermission(final Flag f) {
		if (flagPermissions == null) {
			flagPermissions = new HashMap<>(Flag.values().length);
			flagPermissions.put(Flag.BOOSTER, "ncuboid.flag.booster");
			flagPermissions.put(Flag.BUILD, "ncuboid.flag.build");
			flagPermissions.put(Flag.CHAT, "ncuboid.flag.chat");
			flagPermissions.put(Flag.CHEST, "ncuboid.flag.chest");
			flagPermissions.put(Flag.CLOSED, "ncuboid.flag.closed");
			flagPermissions.put(Flag.CREATIVE, "ncuboid.flag.creative");
			flagPermissions.put(Flag.DROP, "ncuboid.flag.drop");
			flagPermissions.put(Flag.ENDERMANGRIEF, "ncuboid.flag.endermangrief");
			flagPermissions.put(Flag.EXPLOSION_BLOCK, "ncuboid.flag.explosionblock");
			flagPermissions.put(Flag.EXPLOSION_ITEM, "ncuboid.flag.explosionitem");
			flagPermissions.put(Flag.EXPLOSION_PLAYER, "ncuboid.flag.explosionplayer");
			flagPermissions.put(Flag.FARM, "ncuboid.flag.farm");
			flagPermissions.put(Flag.FEED, "ncuboid.flag.feed");
			flagPermissions.put(Flag.FIRE, "ncuboid.flag.fire");
			flagPermissions.put(Flag.GOD, "ncuboid.flag.god");
			flagPermissions.put(Flag.HEAL, "ncuboid.flag.heal");
			flagPermissions.put(Flag.HIDDEN, "ncuboid.flag.hidden");
			flagPermissions.put(Flag.INVISIBLE, "ncuboid.flag.invisible");
			flagPermissions.put(Flag.JAIL, "ncuboid.flag.jail");
			flagPermissions.put(Flag.MOB, "ncuboid.flag.mob");
			flagPermissions.put(Flag.PASS, "ncuboid.flag.pass");
			flagPermissions.put(Flag.PERMANENT, "ncuboid.flag.permanent");
			flagPermissions.put(Flag.PICKUP, "ncuboid.flag.pickup");
			flagPermissions.put(Flag.PVP, "ncuboid.flag.pvp");
			flagPermissions.put(Flag.PVP_HIDE, "ncuboid.flag.pvphide");
			flagPermissions.put(Flag.SNOW, "ncuboid.flag.snow");
			flagPermissions.put(Flag.TELEPORT, "ncuboid.flag.teleport");
			flagPermissions.put(Flag.USE, "ncuboid.flag.use");
			flagPermissions.put(Flag.WARPGATE, "ncuboid.flag.warpgate");
		}
		return flagPermissions.get(f);
	}

	// Flag attributes permissions, linked to their related Flag permission
	private static Map<FlagAtt, String> flagAttributesPermissions;

	private static String getFlagAttributePermission(final FlagAtt fa) {
		if (flagAttributesPermissions == null) {
			flagAttributesPermissions = new HashMap<>(FlagAtt.values().length);
			flagAttributesPermissions.put(FlagAtt.HEAL_AMOUNT, "ncuboid.flag.heal");
			flagAttributesPermissions.put(FlagAtt.HEAL_TIMER, "ncuboid.flag.heal");
			flagAttributesPermissions.put(FlagAtt.HEAL_MIN_HEALTH, "ncuboid.flag.heal");
			flagAttributesPermissions.put(FlagAtt.HEAL_MAX_HEALTH, "ncuboid.flag.heal");
			flagAttributesPermissions.put(FlagAtt.FEED_AMOUNT, "ncuboid.flag.feed");
			flagAttributesPermissions.put(FlagAtt.FEED_TIMER, "ncuboid.flag.feed");
			flagAttributesPermissions.put(FlagAtt.FEED_MIN_FOOD, "ncuboid.flag.feed");
			flagAttributesPermissions.put(FlagAtt.FEED_MAX_FOOD, "ncuboid.flag.feed");
			flagAttributesPermissions.put(FlagAtt.EXPLOSION_BLOCK_DROP, "ncuboid.flag.explosionblock");
			flagAttributesPermissions.put(FlagAtt.EXTERNAL_POINT, "ncuboid.flag.pass");
			flagAttributesPermissions.put(FlagAtt.INTERNAL_POINT, "ncuboid.flag.closed");
			flagAttributesPermissions.put(FlagAtt.BOOSTER_VECTOR, "ncuboid.flag.booster");
		}
		return flagAttributesPermissions.get(fa);
	}

	public static boolean isAdmin(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlag(final CommandSender sender, final Flag f) {
		final String perm = getFlagPermission(f);
		final boolean user;
		switch (f) {
			case BUILD:
			case CHEST:
			case ENDERMANGRIEF:
			case EXPLOSION_BLOCK:
			case EXPLOSION_ITEM:
			case EXPLOSION_PLAYER:
			case FARM:
			case FIRE:
			case MOB:
			case SNOW:
			case USE:
				user = true;
				break;
			default:
				user = false;
				break;
		}
		return isAdmin(sender) || sender.hasPermission(perm) || user && sender.hasPermission(USER);
	}

	public static boolean hasFlagAttribute(final CommandSender sender, final FlagAtt fa) {
		final String perm = getFlagAttributePermission(fa);
		final boolean user;
		switch (fa) {
			case EXPLOSION_BLOCK_DROP:
				user = true;
				break;
			default:
				user = false;
				break;
		}
		return isAdmin(sender) || sender.hasPermission(perm) || user && sender.hasPermission(USER);
	}

	public static boolean hasSeeInvisibleCuboid(final CommandSender sender) {
		return isAdmin(sender) || sender.hasPermission(SEE_INVISIBLE_CUBOID);
	}

	public static boolean hasGeneral(final CommandSender sender) {
		return isAdmin(sender) || sender.hasPermission(CMD_GENERAL) || sender.hasPermission(USER);
	}

	public static boolean hasReload(final CommandSender sender) {
		return isAdmin(sender) || sender.hasPermission(CMD_RELOAD);
	}

	public static boolean hasCreate(final CommandSender sender) {
		return isAdmin(sender) || sender.hasPermission(CMD_CREATE) || sender.hasPermission(USER);
	}

	public static boolean hasDelete(final CommandSender sender) {
		return isAdmin(sender) || sender.hasPermission(CMD_DELETE) || sender.hasPermission(USER);
	}

	public static boolean hasFlag(final CommandSender sender) {
		return isAdmin(sender) || sender.hasPermission(CMD_FLAG) || sender.hasPermission(USER);
	}

	public static boolean hasFlagAttribute(final CommandSender sender) {
		return isAdmin(sender) || sender.hasPermission(CMD_FLAGATTRIBUTE) || sender.hasPermission(USER);
	}

	public static boolean hasJail(final CommandSender sender) {
		return isAdmin(sender) || sender.hasPermission(CMD_JAIL) || sender.hasPermission(ADMIN);
	}

	public static boolean hasAdmin(final CommandSender sender) {
		return isAdmin(sender) || sender.hasPermission(CMD_ADMIN) || sender.hasPermission(USER);
	}

	public static boolean hasUser(final CommandSender sender) {
		return isAdmin(sender) || sender.hasPermission(CMD_USER) || sender.hasPermission(USER);
	}

	public static boolean hasGroup(final CommandSender sender) {
		return isAdmin(sender) || sender.hasPermission(CMD_GROUP) || sender.hasPermission(USER);
	}
}
