/***************************************************************************
 * Project file:    NPlugins - NCuboid - WorldRegion.java                  *
 * Full Class name: fr.ribesg.bukkit.ncuboid.beans.WorldRegion             *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncuboid.beans;

import fr.ribesg.bukkit.ncore.common.NLocation;

// This is a really different Region type.
public class WorldRegion extends GeneralRegion {

    public WorldRegion(final String worldName) {
        super(worldName, RegionType.WORLD, 0);

        // Default flags are a little different for worlds
        // TODO Make this configurable
        this.setFlag(Flag.BUILD, false);
        this.setFlag(Flag.CHEST, false);
        this.setFlag(Flag.USE, false);
    }

    public WorldRegion(final String worldName, final Rights rights, final int priority, final Flags flags, final Attributes flagAtts) {
        super(worldName, RegionType.WORLD, rights, priority, flags, flagAtts);
    }

    @Override
    public boolean contains(final NLocation loc) {
        return this.getWorldName().equals(loc.getWorldName());
    }

    @Override
    public String getRegionName() {
        return "world_" + this.getWorldName();
    }

    @Override
    public long getTotalSize() {
        return Long.MAX_VALUE;
    }
}
