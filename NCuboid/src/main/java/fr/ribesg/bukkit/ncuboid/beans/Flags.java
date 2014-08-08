/***************************************************************************
 * Project file:    NPlugins - NCuboid - Flags.java                        *
 * Full Class name: fr.ribesg.bukkit.ncuboid.beans.Flags                   *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.beans;

import java.util.EnumMap;

public class Flags {

    private final EnumMap<Flag, Boolean> flags;

    public Flags() {
        this.flags = Flags.getDefaultFlagMap();
    }

    public boolean getFlag(final Flag f) {
        return this.flags.get(f);
    }

    public void setFlag(final Flag f, final boolean b) {
        this.flags.put(f, b);
    }

    private static EnumMap<Flag, Boolean> getDefaultFlagMap() {
        final EnumMap<Flag, Boolean> defaultFlagMap = new EnumMap<>(Flag.class);
        // Hardcoded default values
        // TODO Make this configurable
        defaultFlagMap.put(Flag.BOOSTER, false);
        defaultFlagMap.put(Flag.BUILD, true);
        defaultFlagMap.put(Flag.CHAT, false);
        defaultFlagMap.put(Flag.CHEST, true);
        defaultFlagMap.put(Flag.CLOSED, false);
        defaultFlagMap.put(Flag.CREATIVE, false);
        defaultFlagMap.put(Flag.DROP, false);
        defaultFlagMap.put(Flag.ENDERMANGRIEF, true);
        defaultFlagMap.put(Flag.EXPLOSION_BLOCK, true);
        defaultFlagMap.put(Flag.EXPLOSION_PLAYER, false);
        defaultFlagMap.put(Flag.EXPLOSION_ITEM, true);
        defaultFlagMap.put(Flag.FARM, true);
        defaultFlagMap.put(Flag.FEED, false);
        defaultFlagMap.put(Flag.FIRE, true);
        defaultFlagMap.put(Flag.GOD, false);
        defaultFlagMap.put(Flag.HEAL, false);
        defaultFlagMap.put(Flag.HIDDEN, false);
        defaultFlagMap.put(Flag.INVISIBLE, false);
        defaultFlagMap.put(Flag.JAIL, false);
        defaultFlagMap.put(Flag.MOB, false);
        defaultFlagMap.put(Flag.PASS, false);
        defaultFlagMap.put(Flag.PERMANENT, false);
        defaultFlagMap.put(Flag.PICKUP, false);
        defaultFlagMap.put(Flag.PVP, true);
        defaultFlagMap.put(Flag.PVP_HIDE, false);
        defaultFlagMap.put(Flag.SNOW, false);
        defaultFlagMap.put(Flag.TELEPORT, false);
        defaultFlagMap.put(Flag.USE, true);
        defaultFlagMap.put(Flag.WARPGATE, false);
        return defaultFlagMap;
    }
}
