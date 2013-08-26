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

	public static boolean hasWorld(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_WORLD) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasWorldWarpAll(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_WORLD_WARP_ALL) || sender.hasPermission(ADMIN);
	}

	public static boolean hasWorldCreate(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_WORLD_CREATE) || sender.hasPermission(ADMIN);
	}

	public static boolean hasWorldLoad(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_WORLD_LOAD) || sender.hasPermission(ADMIN);
	}

	public static boolean hasWorldUnload(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_WORLD_UNLOAD) || sender.hasPermission(ADMIN);
	}

	public static boolean hasWorldSetHidden(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_WORLD_SETHIDDEN) || sender.hasPermission(ADMIN);
	}

	public static boolean hasWorldSetPerm(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_WORLD_SETPERM) || sender.hasPermission(ADMIN);
	}

	public static boolean hasWorldSetNether(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_WORLD_SETNETHER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasWorldSetEnd(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_WORLD_SETEND) || sender.hasPermission(ADMIN);
	}

	public static boolean hasSpawn(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_SPAWN) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasSetSpawn(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_SETSPAWN) || sender.hasPermission(ADMIN);
	}

	public static boolean hasRequiredPermission(CommandSender sender, String requiredPermission) {
		return sender.isOp() || sender.hasPermission(requiredPermission) || sender.hasPermission(ADMIN);
	}

	public static boolean hasWarp(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_WARP) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasWarpAll(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_WARP_ALL) || sender.hasPermission(ADMIN);
	}

	public static boolean hasWarpSetHidden(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_WARP_SETHIDDEN) || sender.hasPermission(ADMIN);
	}

	public static boolean hasWarpSetPerm(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_WARP_SETPERM) || sender.hasPermission(ADMIN);
	}

	public static boolean hasSetWarp(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_SETWARP) || sender.hasPermission(ADMIN);
	}

	public static boolean hasDelWarp(CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_DELWARP) || sender.hasPermission(ADMIN);
	}
}
