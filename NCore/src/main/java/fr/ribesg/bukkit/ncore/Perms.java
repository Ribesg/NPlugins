/***************************************************************************
 * Project file:    NPlugins - NCore - Perms.java                          *
 * Full Class name: fr.ribesg.bukkit.ncore.Perms                           *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore;

import fr.ribesg.bukkit.ncore.util.AsyncPermAccessor;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class Perms {

    private static final String CMD_DEBUG   = "ncore.cmd.debug";
    private static final String CMD_UPDATER = "ncore.cmd.updater";

    private static final String UPDATER_NOTICE = "ncore.updater.notice";

    public static boolean hasDebug(final CommandSender permissible) {
        return has(permissible, CMD_DEBUG);
    }

    public static boolean hasUpdater(final CommandSender permissible) {
        return has(permissible, CMD_UPDATER);
    }

    public static boolean hasUpdaterNotice(final CommandSender permissible) {
        return has(permissible, UPDATER_NOTICE);
    }

    public static boolean has(final CommandSender permissible, final String permission) {
        if (Bukkit.isPrimaryThread()) {
            return permissible.hasPermission(permission);
        } else {
            return AsyncPermAccessor.has(permissible.getName(), permission);
        }
    }
}
