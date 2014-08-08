/***************************************************************************
 * Project file:    NPlugins - NEnchantingEgg - Perms.java                 *
 * Full Class name: fr.ribesg.bukkit.nenchantingegg.Perms                  *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nenchantingegg;

import org.bukkit.permissions.Permissible;

public class Perms {

    private static final String ADMIN      = "nenchantingegg.admin";
    private static final String USER       = "nenchantingegg.user";
    private static final String CMD_RELOAD = "nenchantingegg.cmd.reload";

    public static boolean isAdmin(final Permissible user) {
        return user.isOp() || user.hasPermission(ADMIN);
    }

    public static boolean isUser(final Permissible user) {
        return user.hasPermission(USER);
    }

    public static boolean hasReload(final Permissible user) {
        return isAdmin(user) || user.hasPermission(CMD_RELOAD);
    }
}
