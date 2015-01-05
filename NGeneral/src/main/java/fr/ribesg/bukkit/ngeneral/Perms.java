/***************************************************************************
 * Project file:    NPlugins - NGeneral - Perms.java                       *
 * Full Class name: fr.ribesg.bukkit.ngeneral.Perms                        *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ngeneral;

import fr.ribesg.bukkit.ncore.util.AsyncPermAccessor;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class Perms {

    private static final String PLUGIN_ADMIN          = "ngeneral.admin";
    private static final String PLUGIN_USER           = "ngeneral.user";
    private static final String CMD_RELOAD            = "ngeneral.cmd.reload";
    private static final String CMD_GOD               = "ngeneral.cmd.god";
    private static final String CMD_GOD_OTHERS        = "ngeneral.cmd.god.others";
    private static final String CMD_FLY               = "ngeneral.cmd.fly";
    private static final String CMD_FLY_OTHERS        = "ngeneral.cmd.fly.others";
    private static final String CMD_FLYSPEED          = "ngeneral.cmd.flyspeed";
    private static final String CMD_FLYSPEED_OTHERS   = "ngeneral.cmd.flyspeed.others";
    private static final String CMD_WALKSPEED         = "ngeneral.cmd.walkspeed";
    private static final String CMD_WALKSPEED_OTHERS  = "ngeneral.cmd.walkspeed.others";
    private static final String CMD_AFK               = "ngeneral.cmd.afk";
    private static final String CMD_BUSY              = "ngeneral.cmd.busy";
    private static final String CMD_TIME              = "ngeneral.cmd.time";
    private static final String CMD_WEATHER           = "ngeneral.cmd.weather";
    private static final String CMD_REPAIR            = "ngeneral.cmd.repair";
    private static final String CMD_NIGHT_VISION      = "ngeneral.cmd.nightvision";
    private static final String CMD_ITEMNETWORK       = "ngeneral.cmd.itemnetwork";
    private static final String CMD_ITEMNETWORK_ALL   = "ngeneral.cmd.itemnetwork.all";
    private static final String CMD_SPY               = "ngeneral.cmd.spy";
    private static final String CMD_TP                = "ngeneral.cmd.tp";
    private static final String CMD_TPPOS             = "ngeneral.cmd.tppos";
    private static final String CMD_TPHERE            = "ngeneral.cmd.tphere";
    private static final String CMD_TPTHERE           = "ngeneral.cmd.tpthere";
    private static final String CMD_TPWORLD           = "ngeneral.cmd.tpworld";
    private static final String CMD_TPBACK            = "ngeneral.cmd.tpback";
    private static final String CMD_HEAL              = "ngeneral.cmd.heal";
    private static final String CMD_HEALTH            = "ngeneral.cmd.health";
    private static final String CMD_FEED              = "ngeneral.cmd.feed";
    private static final String CMD_FOOD              = "ngeneral.cmd.food";
    private static final String PROTECTIONSIGN        = "ngeneral.protectionsign";
    private static final String PROTECTIONSIGN_BYPASS = "ngeneral.protectionsign.bypass";
    private static final String PROTECTIONSIGN_BREAK  = "ngeneral.protectionsign.break";
    private static final String SIGN_COLORS           = "ngeneral.signcolors";

    public static boolean isAdmin(final CommandSender permissible) {
        return has(permissible, PLUGIN_ADMIN);
    }

    public static boolean isUser(final CommandSender permissible) {
        return has(permissible, PLUGIN_USER);
    }

    public static boolean hasReload(final CommandSender permissible) {
        return has(permissible, CMD_RELOAD);
    }

    public static boolean hasGod(final CommandSender permissible) {
        return has(permissible, CMD_GOD);
    }

    public static boolean hasGodOthers(final CommandSender permissible) {
        return has(permissible, CMD_GOD_OTHERS);
    }

    public static boolean hasFly(final CommandSender permissible) {
        return has(permissible, CMD_FLY);
    }

    public static boolean hasFlyOthers(final CommandSender permissible) {
        return has(permissible, CMD_FLY_OTHERS);
    }

    public static boolean hasFlySpeed(final CommandSender permissible) {
        return has(permissible, CMD_FLYSPEED);
    }

    public static boolean hasFlySpeedOthers(final CommandSender permissible) {
        return has(permissible, CMD_FLYSPEED_OTHERS);
    }

    public static boolean hasWalkSpeed(final CommandSender permissible) {
        return has(permissible, CMD_WALKSPEED);
    }

    public static boolean hasWalkSpeedOthers(final CommandSender permissible) {
        return has(permissible, CMD_WALKSPEED_OTHERS);
    }

    public static boolean hasAfk(final CommandSender permissible) {
        return has(permissible, CMD_AFK);
    }

    public static boolean hasBusy(final CommandSender permissible) {
        return has(permissible, CMD_BUSY);
    }

    public static boolean hasTime(final CommandSender permissible) {
        return has(permissible, CMD_TIME);
    }

    public static boolean hasWeather(final CommandSender permissible) {
        return has(permissible, CMD_WEATHER);
    }

    public static boolean hasRepair(final CommandSender permissible) {
        return has(permissible, CMD_REPAIR);
    }

    public static boolean hasNightVision(final CommandSender permissible) {
        return has(permissible, CMD_NIGHT_VISION);
    }

    public static boolean hasItemNetwork(final CommandSender permissible) {
        return has(permissible, CMD_ITEMNETWORK);
    }

    public static boolean hasItemNetworkAll(final CommandSender permissible) {
        return has(permissible, CMD_ITEMNETWORK_ALL);
    }

    public static boolean hasSpy(final CommandSender permissible) {
        return has(permissible, CMD_SPY);
    }

    public static boolean hasTp(final CommandSender permissible) {
        return has(permissible, CMD_TP);
    }

    public static boolean hasTpPos(final CommandSender permissible) {
        return has(permissible, CMD_TPPOS);
    }

    public static boolean hasTpHere(final CommandSender permissible) {
        return has(permissible, CMD_TPHERE);
    }

    public static boolean hasTpThere(final CommandSender permissible) {
        return has(permissible, CMD_TPTHERE);
    }

    public static boolean hasTpWorld(final CommandSender permissible) {
        return has(permissible, CMD_TPWORLD);
    }

    public static boolean hasTpBack(final CommandSender permissible) {
        return has(permissible, CMD_TPBACK);
    }

    public static boolean hasHeal(final CommandSender permissible) {
        return has(permissible, CMD_HEAL);
    }

    public static boolean hasHealth(final CommandSender permissible) {
        return has(permissible, CMD_HEALTH);
    }

    public static boolean hasFeed(final CommandSender permissible) {
        return has(permissible, CMD_FEED);
    }

    public static boolean hasFood(final CommandSender permissible) {
        return has(permissible, CMD_FOOD);
    }

    public static boolean hasProtectionSign(final CommandSender permissible) {
        return has(permissible, PROTECTIONSIGN);
    }

    public static boolean hasProtectionSignBypass(final CommandSender permissible) {
        return has(permissible, PROTECTIONSIGN_BYPASS);
    }

    public static boolean hasProtectionSignBreak(final CommandSender permissible) {
        return has(permissible, PROTECTIONSIGN_BREAK);
    }

    public static boolean hasSignColors(final CommandSender permissible) {
        return has(permissible, SIGN_COLORS);
    }

    public static boolean has(final CommandSender permissible, final String permission) {
        if (Bukkit.isPrimaryThread()) {
            return permissible.hasPermission(permission);
        } else {
            return AsyncPermAccessor.has(permissible.getName(), permission);
        }
    }
}
