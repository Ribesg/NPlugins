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

	private static final String ADMIN      = "ntalk.admin";
	private static final String USER       = "ntalk.user";
	private static final String SPY        = "ntalk.spy";
	private static final String COLOR      = "ntalk.color";
	private static final String CMD_RELOAD = "ntalk.cmd.reload";
	private static final String CMD_PM     = "ntalk.cmd.pm";
	private static final String CMD_PR     = "ntalk.cmd.pr";
	private static final String CMD_NICK   = "ntalk.cmd.nick";
	private static final String SEE_NICKS  = "ntalk.seenicks";

	public static boolean hasAdmin(final CommandSender user, final boolean async) {
		if (async) {
			if (!(user instanceof Player)) {
				throw new UnsupportedOperationException();
			}
			return AsyncPermAccessor.isOp(user.getName()) || AsyncPermAccessor.has(user.getName(), ADMIN);
		} else {
			return user.isOp() || user.hasPermission(ADMIN);
		}
	}

	public static boolean hasReload(final CommandSender user) {
		return hasAdmin(user, false) || user.hasPermission(CMD_RELOAD);
	}

	public static boolean hasPrivateMessage(final CommandSender user) {
		return hasAdmin(user, false) || user.hasPermission(CMD_PM) || user.hasPermission(USER);
	}

	public static boolean hasPrivateResponse(final CommandSender user) {
		return hasAdmin(user, false) || user.hasPermission(CMD_PR) || user.hasPermission(USER);
	}

	public static boolean hasNick(final CommandSender user) {
		return hasAdmin(user, false) || user.hasPermission(CMD_NICK);
	}

	public static boolean hasSpy(final CommandSender user) {
		return hasAdmin(user, false) || user.hasPermission(SPY);
	}

	public static boolean hasColor(final CommandSender user, final boolean async) {
		if (async) {
			if (!(user instanceof Player)) {
				throw new UnsupportedOperationException();
			}
			return hasAdmin(user, true) || AsyncPermAccessor.has(user.getName(), COLOR);
		} else {
			return hasAdmin(user, false) || user.hasPermission(COLOR);
		}
	}

	public static boolean hasSeeNicks(final CommandSender user, final boolean async) {
		if (async) {
			if (!(user instanceof Player)) {
				throw new UnsupportedOperationException();
			}
			return hasAdmin(user, true) || AsyncPermAccessor.has(user.getName(), SEE_NICKS);
		} else {
			return hasAdmin(user, false) || user.hasPermission(SEE_NICKS);
		}
	}
}
