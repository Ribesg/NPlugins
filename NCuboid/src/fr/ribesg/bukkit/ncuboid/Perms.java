package fr.ribesg.bukkit.ncuboid;

import org.bukkit.command.CommandSender;

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
	private static final String FLAG_EXPLOSION       = "ncuboid.flag.explosion";
	private static final String FLAG_FARM            = "ncuboid.flag.farm";
	private static final String FLAG_FEED            = "ncuboid.flag.feed";
	private static final String FLAG_FIRE            = "ncuboid.flag.fire";
	private static final String FLAG_GOD             = "ncuboid.flag.god";
	private static final String FLAG_HEAL            = "ncuboid.flag.heal";
	private static final String FLAG_INVISIBLE       = "ncuboid.flag.invisible";
	private static final String FLAG_MOB             = "ncuboid.flag.mob";
	private static final String FLAG_PASS            = "ncuboid.flag.pass";
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

	public static boolean isAdmin(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlagBooster(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(FLAG_BOOSTER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlagBuild(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(FLAG_BUILD) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlagChat(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(FLAG_CHAT) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlagChest(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(FLAG_CHEST) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlagClosed(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(FLAG_CLOSED) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlagCreative(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(FLAG_CREATIVE) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlagDrop(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(FLAG_DROP) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlagEndermanGrief(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(FLAG_ENDERMANGRIEF) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlagExplosion(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(FLAG_EXPLOSION) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlagFarm(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(FLAG_FARM) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlagFeed(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(FLAG_FEED) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlagFire(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(FLAG_FIRE) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlagGod(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(FLAG_GOD) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlagHeal(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(FLAG_HEAL) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlagInvisible(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(FLAG_INVISIBLE) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlagMob(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(FLAG_MOB) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlagPass(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(FLAG_PASS) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlagPvp(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(FLAG_PVP) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlagSnow(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(FLAG_SNOW) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlagTeleport(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(FLAG_TELEPORT) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlagUse(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(FLAG_USE) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasFlagWarpgate(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(FLAG_WARPGATE) || sender.hasPermission(ADMIN);
	}

	public static boolean hasSeeInvisibleCuboid(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(SEE_INVISIBLE_CUBOID) || sender.hasPermission(ADMIN);
	}

	public static boolean hasGeneral(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_GENERAL) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasReload(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_RELOAD) || sender.hasPermission(ADMIN);
	}

	public static boolean hasCreate(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_CREATE) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasDelete(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_DELETE) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}
}
