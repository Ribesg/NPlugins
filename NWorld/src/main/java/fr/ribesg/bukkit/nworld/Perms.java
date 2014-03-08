/***************************************************************************
 * Project file:    NPlugins - NWorld - Perms.java                         *
 * Full Class name: fr.ribesg.bukkit.nworld.Perms                          *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nworld;

import org.bukkit.permissions.Permissible;

public class Perms {

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
	private static final String CMD_RELOAD          = "nworld.cmd.reload";
	private static final String CMD_SPAWN           = "nworld.cmd.spawn";
	private static final String CMD_SETSPAWN        = "nworld.cmd.setspawn";
	private static final String CMD_WARP            = "nworld.cmd.warp";
	private static final String CMD_WARP_ALL        = "nworld.cmd.warp.all";
	private static final String CMD_WARP_SETHIDDEN  = "nworld.cmd.warp.sethidden";
	private static final String CMD_WARP_SETPERM    = "nworld.cmd.warp.setperm";
	private static final String CMD_SETWARP         = "nworld.cmd.setwarp";
	private static final String CMD_DELWARP         = "nworld.cmd.delwarp";

	public static boolean hasAdmin(final Permissible user) {
		return user.isOp() || user.hasPermission(ADMIN);
	}

	public static boolean hasWorld(final Permissible user) {
		return hasAdmin(user) || user.hasPermission(CMD_WORLD) || user.hasPermission(USER);
	}

	public static boolean hasWorldWarpAll(final Permissible user) {
		return hasAdmin(user) || user.hasPermission(CMD_WORLD_WARP_ALL);
	}

	public static boolean hasWorldCreate(final Permissible user) {
		return hasAdmin(user) || user.hasPermission(CMD_WORLD_CREATE);
	}

	public static boolean hasWorldLoad(final Permissible user) {
		return hasAdmin(user) || user.hasPermission(CMD_WORLD_LOAD);
	}

	public static boolean hasWorldUnload(final Permissible user) {
		return hasAdmin(user) || user.hasPermission(CMD_WORLD_UNLOAD);
	}

	public static boolean hasWorldSetHidden(final Permissible user) {
		return hasAdmin(user) || user.hasPermission(CMD_WORLD_SETHIDDEN);
	}

	public static boolean hasWorldSetPerm(final Permissible user) {
		return hasAdmin(user) || user.hasPermission(CMD_WORLD_SETPERM);
	}

	public static boolean hasWorldSetNether(final Permissible user) {
		return hasAdmin(user) || user.hasPermission(CMD_WORLD_SETNETHER);
	}

	public static boolean hasWorldSetEnd(final Permissible user) {
		return hasAdmin(user) || user.hasPermission(CMD_WORLD_SETEND);
	}

	public static boolean hasReload(final Permissible user) {
		return hasAdmin(user) || user.hasPermission(CMD_RELOAD);
	}

	public static boolean hasSpawn(final Permissible user) {
		return hasAdmin(user) || user.hasPermission(CMD_SPAWN) || user.hasPermission(USER);
	}

	public static boolean hasSetSpawn(final Permissible user) {
		return hasAdmin(user) || user.hasPermission(CMD_SETSPAWN);
	}

	public static boolean hasRequiredPermission(final Permissible user, final String requiredPermission) {
		return hasAdmin(user) || user.hasPermission(requiredPermission);
	}

	public static boolean hasWarp(final Permissible user) {
		return hasAdmin(user) || user.hasPermission(CMD_WARP) || user.hasPermission(USER);
	}

	public static boolean hasWarpAll(final Permissible user) {
		return hasAdmin(user) || user.hasPermission(CMD_WARP_ALL);
	}

	public static boolean hasWarpSetHidden(final Permissible user) {
		return hasAdmin(user) || user.hasPermission(CMD_WARP_SETHIDDEN);
	}

	public static boolean hasWarpSetPerm(final Permissible user) {
		return hasAdmin(user) || user.hasPermission(CMD_WARP_SETPERM);
	}

	public static boolean hasSetWarp(final Permissible user) {
		return hasAdmin(user) || user.hasPermission(CMD_SETWARP);
	}

	public static boolean hasDelWarp(final Permissible user) {
		return hasAdmin(user) || user.hasPermission(CMD_DELWARP);
	}
}
