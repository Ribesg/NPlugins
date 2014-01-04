/***************************************************************************
 * Project file:    NPlugins - NTalk - Perms.java                          *
 * Full Class name: fr.ribesg.bukkit.ntalk.Perms                           *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntalk;

import fr.ribesg.bukkit.ncore.utils.AsyncPermAccessor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Perms {

	// Talk node permissions
	private static final String ADMIN    = "ntalk.admin";
	private static final String USER     = "ntalk.user";
	private static final String SPY      = "ntalk.spy";
	private static final String COLOR    = "ntalk.color";
	private static final String CMD_PM   = "ntalk.cmd.pm";
	private static final String CMD_PR   = "ntalk.cmd.pr";
	private static final String CMD_NICK = "ntalk.cmd.nick";

	public static boolean hasAdmin(final CommandSender sender, final boolean async) {
		if (async) {
			if (!(sender instanceof Player)) {
				throw new UnsupportedOperationException();
			}
			return AsyncPermAccessor.isOp(sender.getName()) || AsyncPermAccessor.has(sender.getName(), ADMIN);
		} else {
			return sender.isOp() || sender.hasPermission(ADMIN);
		}
	}

	public static boolean hasPrivateMessage(final CommandSender sender) {
		return hasAdmin(sender, false) || sender.hasPermission(CMD_PM) || sender.hasPermission(USER);
	}

	public static boolean hasPrivateResponse(final CommandSender sender) {
		return hasAdmin(sender, false) || sender.hasPermission(CMD_PR) || sender.hasPermission(USER);
	}

	public static boolean hasNick(final CommandSender sender) {
		return hasAdmin(sender, false) || sender.hasPermission(CMD_NICK);
	}

	public static boolean hasSpy(final CommandSender sender) {
		return hasAdmin(sender, false) || sender.hasPermission(SPY);
	}

	public static boolean hasColor(final CommandSender sender, final boolean async) {
		if (async) {
			if (!(sender instanceof Player)) {
				throw new UnsupportedOperationException();
			}
			return hasAdmin(sender, true) || AsyncPermAccessor.has(sender.getName(), COLOR);
		} else {
			return hasAdmin(sender, false) || sender.hasPermission(COLOR);
		}
	}
}
