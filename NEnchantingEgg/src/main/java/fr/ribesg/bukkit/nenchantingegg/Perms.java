/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - Perms.java                 *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.Perms                  *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg;

import fr.ribesg.bukkit.ncore.util.AsyncPermAccessor;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class Perms {

    private static final String PLUGIN_ADMIN = "nenchantingegg.admin";
    private static final String PLUGIN_USER  = "nenchantingegg.user";
    private static final String CMD_RELOAD   = "nenchantingegg.cmd.reload";

    public static boolean isAdmin(final CommandSender permissible) {
        return has(permissible, PLUGIN_ADMIN);
    }

    public static boolean isUser(final CommandSender permissible) {
        return has(permissible, PLUGIN_USER);
    }

    public static boolean hasReload(final CommandSender permissible) {
        return has(permissible, CMD_RELOAD);
    }

    public static boolean has(final CommandSender permissible, final String permission) {
        if (Bukkit.isPrimaryThread()) {
            return permissible.hasPermission(permission);
        } else {
            return AsyncPermAccessor.has(permissible.getName(), permission);
        }
    }
}
