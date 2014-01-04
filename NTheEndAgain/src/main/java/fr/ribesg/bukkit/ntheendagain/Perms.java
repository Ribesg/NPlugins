/***************************************************************************
 * Project file:    NPlugins - NTheEndAgain - Perms.java                   *
 * Full Class name: fr.ribesg.bukkit.ntheendagain.Perms                    *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntheendagain;

import org.bukkit.command.CommandSender;

public class Perms {

	// TheEndAgain node permissions
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

	public static boolean hasHelp(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_HELP) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasReload(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_RELOAD) || sender.hasPermission(ADMIN);
	}

	public static boolean hasRegen(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_REGEN) || sender.hasPermission(ADMIN);
	}

	public static boolean hasRespawn(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_RESPAWN) || sender.hasPermission(ADMIN);
	}

	public static boolean hasNb(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_NB) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasChunkInfo(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_CHUNKINFO) || sender.hasPermission(USER) || sender.hasPermission(ADMIN);
	}

	public static boolean hasChunkProtect(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_CHUNKPROTECT) || sender.hasPermission(ADMIN);
	}

	public static boolean hasChunkUnprotect(final CommandSender sender) {
		return sender.isOp() || sender.hasPermission(CMD_CHUNKUNPROTECT) || sender.hasPermission(ADMIN);
	}
}
