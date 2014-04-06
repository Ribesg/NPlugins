/***************************************************************************
 * Project file:    NPlugins - NPermissions - Perms.java                   *
 * Full Class name: fr.ribesg.bukkit.npermissions.Perms                    *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.npermissions;

import org.bukkit.permissions.Permissible;

public class Perms {

	private static final String ADMIN                  = "npermissions.admin";
	private static final String USER                   = "npermissions.user";
	private static final String CMD_RELOAD             = "npermissions.cmd.reload";
	private static final String CMD_RELOAD_MESSAGES    = "npermissions.cmd.reload.messages";
	private static final String CMD_RELOAD_PERMISSIONS = "npermissions.cmd.reload.permissions";
	private static final String CMD_SETGROUP           = "npermissions.cmd.setgroup";

	public static boolean isAdmin(final Permissible user) {
		return user.isOp() || user.hasPermission(ADMIN);
	}

	public static boolean isUser(final Permissible user) {
		return user.hasPermission(USER);
	}

	public static boolean hasReload(final Permissible user) {
		return isAdmin(user) || user.hasPermission(CMD_RELOAD);
	}

	public static boolean hasReloadMessages(final Permissible user) {
		return isAdmin(user) || user.hasPermission(CMD_RELOAD_MESSAGES);
	}

	public static boolean hasReloadPermissions(final Permissible user) {
		return isAdmin(user) || user.hasPermission(CMD_RELOAD_PERMISSIONS);
	}

	public static boolean hasSetGroup(final Permissible user) {
		return isAdmin(user) || user.hasPermission(CMD_SETGROUP);
	}
}
