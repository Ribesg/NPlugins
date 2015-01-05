/***************************************************************************
 * Project file:    NPlugins - NTheEndAgain - Perms.java                   *
 * Full Class name: fr.ribesg.bukkit.ntheendagain.Perms                    *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ntheendagain;

import fr.ribesg.bukkit.ncore.util.AsyncPermAccessor;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class Perms {

    private static final String PLUGIN_ADMIN       = "ntheendagain.admin";
    private static final String PLUGIN_USER        = "ntheendagain.user";
    private static final String CMD_HELP           = "ntheendagain.cmd.help";
    private static final String CMD_RELOAD         = "ntheendagain.cmd.reload";
    private static final String CMD_REGEN          = "ntheendagain.cmd.regen";
    private static final String CMD_RESPAWN        = "ntheendagain.cmd.respawn";
    private static final String CMD_NB             = "ntheendagain.cmd.nb";
    private static final String CMD_CHUNKINFO      = "ntheendagain.cmd.chunkinfo";
    private static final String CMD_CHUNKPROTECT   = "ntheendagain.cmd.chunkprotect";
    private static final String CMD_CHUNKUNPROTECT = "ntheendagain.cmd.chunkunprotect";

    public static boolean isAdmin(final CommandSender permissible) {
        return has(permissible, PLUGIN_ADMIN);
    }

    public static boolean isUser(final CommandSender permissible) {
        return has(permissible, PLUGIN_USER);
    }

    public static boolean hasHelp(final CommandSender permissible) {
        return has(permissible, CMD_HELP);
    }

    public static boolean hasReload(final CommandSender permissible) {
        return has(permissible, CMD_RELOAD);
    }

    public static boolean hasRegen(final CommandSender permissible) {
        return has(permissible, CMD_REGEN);
    }

    public static boolean hasRespawn(final CommandSender permissible) {
        return has(permissible, CMD_RESPAWN);
    }

    public static boolean hasNb(final CommandSender permissible) {
        return has(permissible, CMD_NB);
    }

    public static boolean hasChunkInfo(final CommandSender permissible) {
        return has(permissible, CMD_CHUNKINFO);
    }

    public static boolean hasChunkProtect(final CommandSender permissible) {
        return has(permissible, CMD_CHUNKPROTECT);
    }

    public static boolean hasChunkUnprotect(final CommandSender permissible) {
        return has(permissible, CMD_CHUNKUNPROTECT);
    }

    public static boolean has(final CommandSender permissible, final String permission) {
        if (Bukkit.isPrimaryThread()) {
            return permissible.hasPermission(permission);
        } else {
            return AsyncPermAccessor.has(permissible.getName(), permission);
        }
    }
}
