/***************************************************************************
 * Project file:    NPlugins - NCuboid - Perms.java                        *
 * Full Class name: fr.ribesg.bukkit.ncuboid.Perms                         *
 *                                                                         *
 *                Copyright (c) 2012-2015 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid;

import fr.ribesg.bukkit.ncore.util.AsyncPermAccessor;
import fr.ribesg.bukkit.ncuboid.beans.Attribute;
import fr.ribesg.bukkit.ncuboid.beans.Flag;

import java.util.EnumMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class Perms {

    // Cuboid node permissions
    private static final String PLUGIN_ADMIN         = "ncuboid.admin";
    private static final String PLUGIN_USER          = "ncuboid.user";
    private static final String SEE_INVISIBLE_CUBOID = "ncuboid.seeinvisible";
    private static final String CMD_GENERAL          = "ncuboid.cmd.cuboid";
    private static final String CMD_RELOAD           = "ncuboid.cmd.reload";
    private static final String CMD_CREATE           = "ncuboid.cmd.create";
    private static final String CMD_DELETE           = "ncuboid.cmd.delete";
    private static final String CMD_FLAG             = "ncuboid.cmd.flag";
    private static final String CMD_ATTRIBUTE        = "ncuboid.cmd.attribute";
    private static final String CMD_JAIL             = "ncuboid.cmd.jail";
    private static final String CMD_ADMIN            = "ncuboid.cmd.admin";
    private static final String CMD_USER             = "ncuboid.cmd.user";
    private static final String CMD_GROUP            = "ncuboid.cmd.group";

    // Flag permissions
    private static Map<Flag, String> flagPermissions;

    // Flag attributes permissions, linked to their related Flag permission
    private static Map<Attribute, String> attributesPermissions;

    public static boolean hasAdmin(final CommandSender permissible) {
        return has(permissible, CMD_ADMIN);
    }

    public static boolean hasAttribute(final CommandSender permissible) {
        return has(permissible, CMD_ATTRIBUTE);
    }

    public static boolean hasAttribute(final CommandSender permissible, final Attribute att) {
        return has(permissible, getAttributePermission(att));
    }

    public static boolean hasCreate(final CommandSender permissible) {
        return has(permissible, CMD_CREATE);
    }

    public static boolean hasDelete(final CommandSender permissible) {
        return has(permissible, CMD_DELETE);
    }

    public static boolean hasFlag(final CommandSender permissible) {
        return has(permissible, CMD_FLAG);
    }

    public static boolean hasFlag(final CommandSender permissible, final Flag f) {
        return has(permissible, getFlagPermission(f));
    }

    public static boolean hasGeneral(final CommandSender permissible) {
        return has(permissible, CMD_GENERAL);
    }

    public static boolean hasGroup(final CommandSender permissible) {
        return has(permissible, CMD_GROUP);
    }

    public static boolean hasJail(final CommandSender permissible) {
        return has(permissible, CMD_JAIL);
    }

    public static boolean hasReload(final CommandSender permissible) {
        return has(permissible, CMD_RELOAD);
    }

    public static boolean hasSeeInvisibleCuboid(final CommandSender permissible) {
        return has(permissible, SEE_INVISIBLE_CUBOID);
    }

    public static boolean hasUser(final CommandSender permissible) {
        return has(permissible, CMD_USER);
    }

    public static boolean isAdmin(final CommandSender permissible) {
        return has(permissible, PLUGIN_ADMIN);
    }

    public static boolean isUser(final CommandSender permissible) {
        return has(permissible, PLUGIN_USER);
    }

    public static boolean has(final CommandSender permissible, final String permission) {
        if (Bukkit.isPrimaryThread()) {
            return permissible.hasPermission(permission);
        } else {
            return AsyncPermAccessor.has(permissible.getName(), permission);
        }
    }

    private static String getFlagPermission(final Flag f) {
        if (flagPermissions == null) {
            flagPermissions = new EnumMap<>(Flag.class);
            flagPermissions.put(Flag.BOOSTER, "ncuboid.flag.booster");
            flagPermissions.put(Flag.BUILD, "ncuboid.flag.build");
            flagPermissions.put(Flag.CHAT, "ncuboid.flag.chat");
            flagPermissions.put(Flag.CHEST, "ncuboid.flag.chest");
            flagPermissions.put(Flag.CLOSED, "ncuboid.flag.closed");
            flagPermissions.put(Flag.CREATIVE, "ncuboid.flag.creative");
            flagPermissions.put(Flag.DROP, "ncuboid.flag.drop");
            flagPermissions.put(Flag.ENDERMANGRIEF, "ncuboid.flag.endermangrief");
            flagPermissions.put(Flag.EXPLOSION_BLOCK, "ncuboid.flag.explosionblock");
            flagPermissions.put(Flag.EXPLOSION_ITEM, "ncuboid.flag.explosionitem");
            flagPermissions.put(Flag.EXPLOSION_PLAYER, "ncuboid.flag.explosionplayer");
            flagPermissions.put(Flag.FARM, "ncuboid.flag.farm");
            flagPermissions.put(Flag.FEED, "ncuboid.flag.feed");
            flagPermissions.put(Flag.FIRE, "ncuboid.flag.fire");
            flagPermissions.put(Flag.GOD, "ncuboid.flag.god");
            flagPermissions.put(Flag.HEAL, "ncuboid.flag.heal");
            flagPermissions.put(Flag.HIDDEN, "ncuboid.flag.hidden");
            flagPermissions.put(Flag.INVISIBLE, "ncuboid.flag.invisible");
            flagPermissions.put(Flag.JAIL, "ncuboid.flag.jail");
            flagPermissions.put(Flag.MOB, "ncuboid.flag.mob");
            flagPermissions.put(Flag.PASS, "ncuboid.flag.pass");
            flagPermissions.put(Flag.PERMANENT, "ncuboid.flag.permanent");
            flagPermissions.put(Flag.PICKUP, "ncuboid.flag.pickup");
            flagPermissions.put(Flag.PVP, "ncuboid.flag.pvp");
            flagPermissions.put(Flag.PVP_HIDE, "ncuboid.flag.pvphide");
            flagPermissions.put(Flag.SNOW, "ncuboid.flag.snow");
            flagPermissions.put(Flag.TELEPORT, "ncuboid.flag.teleport");
            flagPermissions.put(Flag.USE, "ncuboid.flag.use");
            flagPermissions.put(Flag.WARPGATE, "ncuboid.flag.warpgate");
        }
        return flagPermissions.get(f);
    }

    private static String getAttributePermission(final Attribute att) {
        if (attributesPermissions == null) {
            attributesPermissions = new EnumMap<>(Attribute.class);

            // Strings
            attributesPermissions.put(Attribute.FAREWELL_MESSAGE, "ncuboid.attribute.farewellmessage");
            attributesPermissions.put(Attribute.WELCOME_MESSAGE, "ncuboid.attribute.welcomemessage");

            // Integers
            attributesPermissions.put(Attribute.EXPLOSION_BLOCK_DROP, "ncuboid.attribute.explosionblockdrop");
            attributesPermissions.put(Attribute.FEED_AMOUNT, "ncuboid.flag.feed");
            attributesPermissions.put(Attribute.FEED_MAX_FOOD, "ncuboid.flag.feed");
            attributesPermissions.put(Attribute.FEED_MIN_FOOD, "ncuboid.flag.feed");
            attributesPermissions.put(Attribute.FEED_TIMER, "ncuboid.flag.feed");
            attributesPermissions.put(Attribute.HEAL_AMOUNT, "ncuboid.flag.heal");
            attributesPermissions.put(Attribute.HEAL_MAX_HEALTH, "ncuboid.flag.heal");
            attributesPermissions.put(Attribute.HEAL_MIN_HEALTH, "ncuboid.flag.heal");
            attributesPermissions.put(Attribute.HEAL_TIMER, "ncuboid.flag.heal");

            // Locations
            attributesPermissions.put(Attribute.EXTERNAL_POINT, "ncuboid.attribute.externalpoint");
            attributesPermissions.put(Attribute.INTERNAL_POINT, "ncuboid.attribute.internalpoint");
            attributesPermissions.put(Attribute.RESPAWN_POINT, "ncuboid.attribute.respawnpoint");

            // Vectors
            attributesPermissions.put(Attribute.BOOSTER_VECTOR, "ncuboid.flag.booster");
        }
        return attributesPermissions.get(att);
    }
}
