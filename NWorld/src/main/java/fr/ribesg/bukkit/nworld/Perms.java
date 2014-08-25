/***************************************************************************
 * Project file:    NPlugins - NWorld - Perms.java                         *
 * Full Class name: fr.ribesg.bukkit.nworld.Perms                          *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.nworld;

import fr.ribesg.bukkit.ncore.util.AsyncPermAccessor;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class Perms {

    private static final String PLUGIN_ADMIN        = "nworld.admin";
    private static final String PLUGIN_USER         = "nworld.user";
    private static final String CMD_WORLD           = "nworld.cmd.world";
    private static final String CMD_WORLD_WARP_ALL  = "nworld.cmd.world.all";
    private static final String CMD_WORLD_CREATE    = "nworld.cmd.world.create";
    private static final String CMD_WORLD_LOAD      = "nworld.cmd.world.load";
    private static final String CMD_WORLD_UNLOAD    = "nworld.cmd.world.unload";
    private static final String CMD_WORLD_SETHIDDEN = "nworld.cmd.world.sethidden";
    private static final String CMD_WORLD_SETPERM   = "nworld.cmd.world.setperm";
    private static final String CMD_WORLD_SETNETHER = "nworld.cmd.world.setnether";
    private static final String CMD_WORLD_SETEND    = "nworld.cmd.world.setend";
    private static final String CMD_RELOAD          = "nworld.cmd.reload";
    private static final String CMD_SPAWN           = "nworld.cmd.spawn";
    private static final String CMD_SETSPAWN        = "nworld.cmd.setspawn";
    private static final String CMD_WARP            = "nworld.cmd.warp";
    private static final String CMD_WARP_ALL        = "nworld.cmd.warp.all";
    private static final String CMD_WARP_SETHIDDEN  = "nworld.cmd.warp.sethidden";
    private static final String CMD_WARP_SETPERM    = "nworld.cmd.warp.setperm";
    private static final String CMD_SETWARP         = "nworld.cmd.setwarp";
    private static final String CMD_DELWARP         = "nworld.cmd.delwarp";

    public static boolean isAdmin(final CommandSender permissible) {
        return has(permissible, PLUGIN_ADMIN);
    }

    public static boolean isUser(final CommandSender permissible) {
        return has(permissible, PLUGIN_USER);
    }

    public static boolean hasWorld(final CommandSender permissible) {
        return has(permissible, CMD_WORLD);
    }

    public static boolean hasWorldWarpAll(final CommandSender permissible) {
        return has(permissible, CMD_WORLD_WARP_ALL);
    }

    public static boolean hasWorldCreate(final CommandSender permissible) {
        return has(permissible, CMD_WORLD_CREATE);
    }

    public static boolean hasWorldLoad(final CommandSender permissible) {
        return has(permissible, CMD_WORLD_LOAD);
    }

    public static boolean hasWorldUnload(final CommandSender permissible) {
        return has(permissible, CMD_WORLD_UNLOAD);
    }

    public static boolean hasWorldSetHidden(final CommandSender permissible) {
        return has(permissible, CMD_WORLD_SETHIDDEN);
    }

    public static boolean hasWorldSetPerm(final CommandSender permissible) {
        return has(permissible, CMD_WORLD_SETPERM);
    }

    public static boolean hasWorldSetNether(final CommandSender permissible) {
        return has(permissible, CMD_WORLD_SETNETHER);
    }

    public static boolean hasWorldSetEnd(final CommandSender permissible) {
        return has(permissible, CMD_WORLD_SETEND);
    }

    public static boolean hasReload(final CommandSender permissible) {
        return has(permissible, CMD_RELOAD);
    }

    public static boolean hasSpawn(final CommandSender permissible) {
        return has(permissible, CMD_SPAWN);
    }

    public static boolean hasSetSpawn(final CommandSender permissible) {
        return has(permissible, CMD_SETSPAWN);
    }

    public static boolean hasWarp(final CommandSender permissible) {
        return has(permissible, CMD_WARP);
    }

    public static boolean hasWarpAll(final CommandSender permissible) {
        return has(permissible, CMD_WARP_ALL);
    }

    public static boolean hasWarpSetHidden(final CommandSender permissible) {
        return has(permissible, CMD_WARP_SETHIDDEN);
    }

    public static boolean hasWarpSetPerm(final CommandSender permissible) {
        return has(permissible, CMD_WARP_SETPERM);
    }

    public static boolean hasSetWarp(final CommandSender permissible) {
        return has(permissible, CMD_SETWARP);
    }

    public static boolean hasDelWarp(final CommandSender permissible) {
        return has(permissible, CMD_DELWARP);
    }

    public static boolean has(final CommandSender permissible, final String permission) {
        if (Bukkit.isPrimaryThread()) {
            return permissible.hasPermission(permission);
        } else {
            return AsyncPermAccessor.has(permissible.getName(), permission);
        }
    }
}
