/***************************************************************************
 * Project file:    NPlugins - NTalk - Perms.java                          *
 * Full Class name: fr.ribesg.bukkit.ntalk.Perms                           *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntalk;

import fr.ribesg.bukkit.ncore.util.AsyncPermAccessor;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class Perms {

    private static final String PLUGIN_ADMIN = "ntalk.admin";
    private static final String PLUGIN_USER  = "ntalk.user";
    private static final String COLOR        = "ntalk.color";
    private static final String CMD_RELOAD   = "ntalk.cmd.reload";
    private static final String CMD_PM       = "ntalk.cmd.pm";
    private static final String CMD_PR       = "ntalk.cmd.pr";
    private static final String CMD_NICK     = "ntalk.cmd.nick";
    private static final String CMD_SPY      = "ntalk.cmd.spy";
    private static final String SEE_NICKS    = "ntalk.seenicks";

    public static boolean isAdmin(final CommandSender permissible) {
        return has(permissible, PLUGIN_ADMIN);
    }

    public static boolean isUser(final CommandSender permissible) {
        return has(permissible, PLUGIN_USER);
    }

    public static boolean hasReload(final CommandSender permissible) {
        return has(permissible, CMD_RELOAD);
    }

    public static boolean hasPrivateMessage(final CommandSender permissible) {
        return has(permissible, CMD_PM);
    }

    public static boolean hasPrivateResponse(final CommandSender permissible) {
        return has(permissible, CMD_PR);
    }

    public static boolean hasNick(final CommandSender permissible) {
        return has(permissible, CMD_NICK);
    }

    public static boolean hasColor(final CommandSender permissible) {
        return has(permissible, COLOR);
    }

    public static boolean hasSeeNicks(final CommandSender permissible) {
        return has(permissible, SEE_NICKS);
    }

    public static boolean hasSpy(final CommandSender permissible) {
        return has(permissible, CMD_SPY);
    }

    public static boolean has(final CommandSender permissible, final String permission) {
        if (Bukkit.isPrimaryThread()) {
            return permissible.hasPermission(permission);
        } else {
            return AsyncPermAccessor.has(permissible.getName(), permission);
        }
    }
}
