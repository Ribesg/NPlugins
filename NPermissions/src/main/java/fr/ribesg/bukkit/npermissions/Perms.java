/***************************************************************************
 * Project file:    NPlugins - NPermissions - Perms.java                   *
 * Full Class name: fr.ribesg.bukkit.npermissions.Perms                    *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.npermissions;

import fr.ribesg.bukkit.ncore.util.AsyncPermAccessor;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class Perms {

    private static final String PLUGIN_ADMIN           = "npermissions.admin";
    private static final String PLUGIN_USER            = "npermissions.user";
    private static final String CMD_RELOAD             = "npermissions.cmd.reload";
    private static final String CMD_RELOAD_MESSAGES    = "npermissions.cmd.reload.messages";
    private static final String CMD_RELOAD_PERMISSIONS = "npermissions.cmd.reload.permissions";
    private static final String CMD_SETGROUP           = "npermissions.cmd.setgroup";

    public static boolean isAdmin(final CommandSender permissible) {
        return has(permissible, PLUGIN_ADMIN);
    }

    public static boolean isUser(final CommandSender permissible) {
        return has(permissible, PLUGIN_USER);
    }

    public static boolean hasReload(final CommandSender permissible) {
        return has(permissible, CMD_RELOAD);
    }

    public static boolean hasReloadMessages(final CommandSender permissible) {
        return has(permissible, CMD_RELOAD_MESSAGES);
    }

    public static boolean hasReloadPermissions(final CommandSender permissible) {
        return has(permissible, CMD_RELOAD_PERMISSIONS);
    }

    public static boolean hasSetGroup(final CommandSender permissible) {
        return has(permissible, CMD_SETGROUP);
    }

    public static boolean has(final CommandSender permissible, final String permission) {
        if (Bukkit.isPrimaryThread()) {
            return permissible.hasPermission(permission);
        } else {
            return AsyncPermAccessor.has(permissible.getName(), permission);
        }
    }
}
