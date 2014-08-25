/***************************************************************************
 * Project file:    NPlugins - NPlayer - Perms.java                        *
 * Full Class name: fr.ribesg.bukkit.nplayer.Perms                         *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nplayer;

import fr.ribesg.bukkit.ncore.util.AsyncPermAccessor;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class Perms {

    private static final String PLUGIN_ADMIN        = "nplayer.admin";
    private static final String PLUGIN_USER         = "nplayer.user";
    private static final String CMD_RELOAD          = "nplayer.cmd.reload";
    private static final String CMD_FORCELOGIN      = "nplayer.cmd.forcelogin";
    private static final String CMD_LOGIN           = "nplayer.cmd.login";
    private static final String CMD_LOGOUT          = "nplayer.cmd.logout";
    private static final String CMD_REGISTER        = "nplayer.cmd.register";
    private static final String CMD_INFO            = "nplayer.cmd.info";
    private static final String CMD_INFO_ADMIN      = "nplayer.cmd.info.admin";
    private static final String CMD_HOME            = "nplayer.cmd.home";
    private static final String CMD_HOME_OTHERS     = "nplayer.cmd.home.others";
    private static final String CMD_SETHOME         = "nplayer.cmd.sethome";
    private static final String CMD_SETHOME_OTHERS  = "nplayer.cmd.sethome.others";
    private static final String CMD_BAN             = "nplayer.cmd.ban";
    private static final String CMD_BAN_PERMANENT   = "nplayer.cmd.ban.permanent";
    private static final String CMD_UNBAN           = "nplayer.cmd.unban";
    private static final String CMD_BANIP           = "nplayer.cmd.banip";
    private static final String CMD_BANIP_PERMANENT = "nplayer.cmd.banip.permanent";
    private static final String CMD_UNBANIP         = "nplayer.cmd.unbanip";
    private static final String CMD_JAIL            = "nplayer.cmd.jail";
    private static final String CMD_JAIL_PERMANENT  = "nplayer.cmd.jail.permanent";
    private static final String CMD_UNJAIL          = "nplayer.cmd.unjail";
    private static final String CMD_MUTE            = "nplayer.cmd.mute";
    private static final String CMD_MUTE_PERMANENT  = "nplayer.cmd.mute.permanent";
    private static final String CMD_UNMUTE          = "nplayer.cmd.unmute";
    private static final String CMD_KICK            = "nplayer.cmd.kick";

    public static boolean isAdmin(final CommandSender permissible) {
        return has(permissible, PLUGIN_ADMIN);
    }

    public static boolean isUser(final CommandSender permissible) {
        return has(permissible, PLUGIN_USER);
    }

    public static boolean hasReload(final CommandSender permissible) {
        return has(permissible, CMD_RELOAD);
    }

    public static boolean hasForceLogin(final CommandSender permissible) {
        return has(permissible, CMD_FORCELOGIN);
    }

    public static boolean hasLogin(final CommandSender permissible) {
        return has(permissible, CMD_LOGIN);
    }

    public static boolean hasLogout(final CommandSender permissible) {
        return has(permissible, CMD_LOGOUT);
    }

    public static boolean hasRegister(final CommandSender permissible) {
        return has(permissible, CMD_REGISTER);
    }

    public static boolean hasInfo(final CommandSender permissible) {
        return has(permissible, CMD_INFO);
    }

    public static boolean hasInfoAdmin(final CommandSender permissible) {
        return has(permissible, CMD_INFO_ADMIN);
    }

    public static boolean hasHome(final CommandSender permissible) {
        return has(permissible, CMD_HOME);
    }

    public static boolean hasHomeOthers(final CommandSender permissible) {
        return has(permissible, CMD_HOME_OTHERS);
    }

    public static boolean hasSetHome(final CommandSender permissible) {
        return has(permissible, CMD_SETHOME);
    }

    public static boolean hasSetHomeOthers(final CommandSender permissible) {
        return has(permissible, CMD_SETHOME_OTHERS);
    }

    public static boolean hasBan(final CommandSender permissible) {
        return has(permissible, CMD_BAN);
    }

    public static boolean hasBanPermanent(final CommandSender permissible) {
        return has(permissible, CMD_BAN_PERMANENT);
    }

    public static boolean hasUnBan(final CommandSender permissible) {
        return has(permissible, CMD_UNBAN);
    }

    public static boolean hasBanIp(final CommandSender permissible) {
        return has(permissible, CMD_BANIP);
    }

    public static boolean hasBanIpPermanent(final CommandSender permissible) {
        return has(permissible, CMD_BANIP_PERMANENT);
    }

    public static boolean hasUnBanIp(final CommandSender permissible) {
        return has(permissible, CMD_UNBANIP);
    }

    public static boolean hasJail(final CommandSender permissible) {
        return has(permissible, CMD_JAIL);
    }

    public static boolean hasJailPermanent(final CommandSender permissible) {
        return has(permissible, CMD_JAIL_PERMANENT);
    }

    public static boolean hasUnJail(final CommandSender permissible) {
        return has(permissible, CMD_UNJAIL);
    }

    public static boolean hasMute(final CommandSender permissible) {
        return has(permissible, CMD_MUTE);
    }

    public static boolean hasMutePermanent(final CommandSender permissible) {
        return has(permissible, CMD_MUTE_PERMANENT);
    }

    public static boolean hasUnMute(final CommandSender permissible) {
        return has(permissible, CMD_UNMUTE);
    }

    public static boolean hasKick(final CommandSender permissible) {
        return has(permissible, CMD_KICK);
    }

    public static boolean has(final CommandSender permissible, final String permission) {
        if (Bukkit.isPrimaryThread()) {
            return permissible.hasPermission(permission);
        } else {
            return AsyncPermAccessor.has(permissible.getName(), permission);
        }
    }
}
