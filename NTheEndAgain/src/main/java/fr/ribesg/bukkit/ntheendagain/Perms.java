/***************************************************************************
 * Project file:    NPlugins - NTheEndAgain - Perms.java                   *
 * Full Class name: fr.ribesg.bukkit.ntheendagain.Perms                    *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntheendagain;

import org.bukkit.permissions.Permissible;

public class Perms {

	private static final String ADMIN              = "ntheendagain.admin";
	private static final String USER               = "ntheendagain.user";
	private static final String CMD_HELP           = "ntheendagain.cmd.help";
	private static final String CMD_RELOAD         = "ntheendagain.cmd.reload";
	private static final String CMD_REGEN          = "ntheendagain.cmd.regen";
	private static final String CMD_RESPAWN        = "ntheendagain.cmd.respawn";
	private static final String CMD_NB             = "ntheendagain.cmd.nb";
	private static final String CMD_CHUNKINFO      = "ntheendagain.cmd.chunkinfo";
	private static final String CMD_CHUNKPROTECT   = "ntheendagain.cmd.chunkprotect";
	private static final String CMD_CHUNKUNPROTECT = "ntheendagain.cmd.chunkunprotect";

	public static boolean hasHelp(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_HELP) || user.hasPermission(USER) || user.hasPermission(ADMIN);
	}

	public static boolean hasReload(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_RELOAD) || user.hasPermission(ADMIN);
	}

	public static boolean hasRegen(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_REGEN) || user.hasPermission(ADMIN);
	}

	public static boolean hasRespawn(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_RESPAWN) || user.hasPermission(ADMIN);
	}

	public static boolean hasNb(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_NB) || user.hasPermission(USER) || user.hasPermission(ADMIN);
	}

	public static boolean hasChunkInfo(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_CHUNKINFO) || user.hasPermission(USER) || user.hasPermission(ADMIN);
	}

	public static boolean hasChunkProtect(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_CHUNKPROTECT) || user.hasPermission(ADMIN);
	}

	public static boolean hasChunkUnprotect(final Permissible user) {
		return user.isOp() || user.hasPermission(CMD_CHUNKUNPROTECT) || user.hasPermission(ADMIN);
	}
}
