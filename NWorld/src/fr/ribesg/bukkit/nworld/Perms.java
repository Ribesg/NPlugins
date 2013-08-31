package fr.ribesg.bukkit.nworld;

import org.bukkit.command.CommandSender;

public class Perms {

	// World node permissions
	private static final String ADMIN               = "nworld.admin";
	private static final String USER                = "nworld.user";
	private static final String CMD_WORLD           = "nworld.cmd.world";
	private static final String CMD_WORLD_WARP_ALL  = "nworld.cmd.world.all";
	private static final String CMD_WORLD_CREATE    = "nworld.cmd.world.create";
	private static final String CMD_WORLD_LOAD      = "nworld.cmd.world.load";
	private static final String CMD_WORLD_UNLOAD    = "nworld.cmd.world.unload";
	private static final String CMD_WORLD_SETHIDDEN = "nworld.cmd.world.sethidden";
	private static final String CMD_WORLD_SETPERM   = "nworld.cmd.world.setperm";
	private static final String CMD_WORLD_SETNETHER = "nworld.cmd.world.setnether";
	private static final String CMD_WORLD_SETEND    = "nworld.cmd.world.setend";
	private static final String CMD_SPAWN           = "nworld.cmd.spawn";
	private static final String CMD_SETSPAWN        = "nworld.cmd.setspawn";
	private static final String CMD_WARP            = "nworld.cmd.warp";
	private static final String CMD_WARP_ALL        = "nworld.cmd.warp.all";
	private static final String CMD_WARP_SETHIDDEN  = "nworld.cmd.warp.sethidden";
	private static final String CMD_WARP_SETPERM    = "nworld.cmd.warp.setperm";
	private static final String CMD_SETWARP         = "nworld.cmd.setwarp";
	private static final String CMD_DELWARP         = "nworld.cmd.delwarp";

	public static boolean hasAdmin(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(ADMIN);
	}

	public static boolean hasWorld(CommandSender sender) {
		return hasAdmin(sender) || sender.hasPermission(CMD_WORLD) || sender.hasPermission(USER);
	}

	public static boolean hasWorldWarpAll(CommandSender sender) {
		return hasAdmin(sender) || sender.hasPermission(CMD_WORLD_WARP_ALL);
	}

	public static boolean hasWorldCreate(CommandSender sender) {
		return hasAdmin(sender) || sender.hasPermission(CMD_WORLD_CREATE);
	}

	public static boolean hasWorldLoad(CommandSender sender) {
		return hasAdmin(sender) || sender.hasPermission(CMD_WORLD_LOAD);
	}

	public static boolean hasWorldUnload(CommandSender sender) {
		return hasAdmin(sender) || sender.hasPermission(CMD_WORLD_UNLOAD);
	}

	public static boolean hasWorldSetHidden(CommandSender sender) {
		return hasAdmin(sender) || sender.hasPermission(CMD_WORLD_SETHIDDEN);
	}

	public static boolean hasWorldSetPerm(CommandSender sender) {
		return hasAdmin(sender) || sender.hasPermission(CMD_WORLD_SETPERM);
	}

	public static boolean hasWorldSetNether(CommandSender sender) {
		return hasAdmin(sender) || sender.hasPermission(CMD_WORLD_SETNETHER);
	}

	public static boolean hasWorldSetEnd(CommandSender sender) {
		return hasAdmin(sender) || sender.hasPermission(CMD_WORLD_SETEND);
	}

	public static boolean hasSpawn(CommandSender sender) {
		return hasAdmin(sender) || sender.hasPermission(CMD_SPAWN) || sender.hasPermission(USER);
	}

	public static boolean hasSetSpawn(CommandSender sender) {
		return hasAdmin(sender) || sender.hasPermission(CMD_SETSPAWN);
	}

	public static boolean hasRequiredPermission(CommandSender sender, String requiredPermission) {
		return hasAdmin(sender) || sender.hasPermission(requiredPermission);
	}

	public static boolean hasWarp(CommandSender sender) {
		return hasAdmin(sender) || sender.hasPermission(CMD_WARP) || sender.hasPermission(USER);
	}

	public static boolean hasWarpAll(CommandSender sender) {
		return hasAdmin(sender) || sender.hasPermission(CMD_WARP_ALL);
	}

	public static boolean hasWarpSetHidden(CommandSender sender) {
		return hasAdmin(sender) || sender.hasPermission(CMD_WARP_SETHIDDEN);
	}

	public static boolean hasWarpSetPerm(CommandSender sender) {
		return hasAdmin(sender) || sender.hasPermission(CMD_WARP_SETPERM);
	}

	public static boolean hasSetWarp(CommandSender sender) {
		return hasAdmin(sender) || sender.hasPermission(CMD_SETWARP);
	}

	public static boolean hasDelWarp(CommandSender sender) {
		return hasAdmin(sender) || sender.hasPermission(CMD_DELWARP);
	}
}
