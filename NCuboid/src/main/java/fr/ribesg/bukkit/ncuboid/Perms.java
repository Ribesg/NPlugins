package fr.ribesg.bukkit.ncuboid;

import fr.ribesg.bukkit.ncuboid.beans.Flag;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class Perms {

	// Cuboid node permissions
	private static final String ADMIN                = "ncuboid.admin";
	private static final String USER                 = "ncuboid.user";
	private static final String FLAG_BOOSTER         = "ncuboid.flag.booster";
	private static final String FLAG_BUILD           = "ncuboid.flag.build";
	private static final String FLAG_CHAT            = "ncuboid.flag.chat";
	private static final String FLAG_CHEST           = "ncuboid.flag.chest";
	private static final String FLAG_CLOSED          = "ncuboid.flag.closed";
	private static final String FLAG_CREATIVE        = "ncuboid.flag.creative";
	private static final String FLAG_DROP            = "ncuboid.flag.drop";
	private static final String FLAG_ENDERMANGRIEF   = "ncuboid.flag.endermangrief";
	private static final String FLAG_EXPLOSIONBLOCK  = "ncuboid.flag.explosionblock";
	private static final String FLAG_EXPLOSIONITEM   = "ncuboid.flag.explosionitem";
	private static final String FLAG_EXPLOSIONPLAYER = "ncuboid.flag.explosionplayer";
	private static final String FLAG_FARM            = "ncuboid.flag.farm";
	private static final String FLAG_FEED            = "ncuboid.flag.feed";
	private static final String FLAG_FIRE            = "ncuboid.flag.fire";
	private static final String FLAG_GOD             = "ncuboid.flag.god";
	private static final String FLAG_HEAL            = "ncuboid.flag.heal";
	private static final String FLAG_HIDDEN          = "ncuboid.flag.hidden";
	private static final String FLAG_INVISIBLE       = "ncuboid.flag.invisible";
	private static final String FLAG_JAIL            = "ncuboid.flag.jail";
	private static final String FLAG_MOB             = "ncuboid.flag.mob";
	private static final String FLAG_PASS            = "ncuboid.flag.pass";
	private static final String FLAG_PERMANENT       = "ncuboid.flag.permanent";
	private static final String FLAG_PICKUP          = "ncuboid.flag.pickup";
	private static final String FLAG_PVP             = "ncuboid.flag.pvp";
	private static final String FLAG_SNOW            = "ncuboid.flag.snow";
	private static final String FLAG_TELEPORT        = "ncuboid.flag.teleport";
	private static final String FLAG_USE             = "ncuboid.flag.use";
	private static final String FLAG_WARPGATE        = "ncuboid.flag.warpgate";
	private static final String SEE_INVISIBLE_CUBOID = "ncuboid.seeinvisible";
	private static final String CMD_GENERAL          = "ncuboid.cmd.cuboid";
	private static final String CMD_RELOAD           = "ncuboid.cmd.reload";
	private static final String CMD_CREATE           = "ncuboid.cmd.create";
	private static final String CMD_DELETE           = "ncuboid.cmd.delete";
	private static final String CMD_FLAG             = "ncuboid.cmd.flag";
	private static final String CMD_ALLOW            = "ncuboid.cmd.allow";
	private static final String CMD_DENY             = "ncuboid.cmd.deny";

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
			flagPermissions.put(Flag.SNOW, "ncuboid.flag.snow");
			flagPermissions.put(Flag.TELEPORT, "ncuboid.flag.teleport");
			flagPermissions.put(Flag.USE, "ncuboid.flag.use");
			flagPermissions.put(Flag.WARPGATE, "ncuboid.flag.warpgate");
		}
		return flagPermissions.get(f);
	}

	public static boolean isAdmin(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlag(CommandSender sender, Flag f) {
		final String perm = getFlagPermission(f);
		boolean user;
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

	public static boolean hasSeeInvisibleCuboid(CommandSender sender) {
		return isAdmin(sender) || sender.hasPermission(SEE_INVISIBLE_CUBOID);
	}

	public static boolean hasGeneral(CommandSender sender) {
		return isAdmin(sender) || sender.hasPermission(CMD_GENERAL) || sender.hasPermission(USER);
	}

	public static boolean hasReload(CommandSender sender) {
		return isAdmin(sender) || sender.hasPermission(CMD_RELOAD);
	}

	public static boolean hasCreate(CommandSender sender) {
		return isAdmin(sender) || sender.hasPermission(CMD_CREATE) || sender.hasPermission(USER);
	}

	public static boolean hasDelete(CommandSender sender) {
		return isAdmin(sender) || sender.hasPermission(CMD_DELETE) || sender.hasPermission(USER);
	}

	public static boolean hasFlag(CommandSender sender) {
		return isAdmin(sender) || sender.hasPermission(CMD_FLAG) || sender.hasPermission(USER);
	}

	public static boolean hasAdmin(CommandSender sender) {
		return isAdmin(sender) || sender.hasPermission(CMD_ALLOW) || sender.hasPermission(USER);
	}

	public static boolean hasUser(CommandSender sender) {
		return isAdmin(sender) || sender.hasPermission(CMD_DENY) || sender.hasPermission(USER);
	}
}
