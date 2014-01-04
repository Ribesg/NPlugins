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
		setFlag(Flag.BUILD, false);
		setFlag(Flag.CHEST, false);
		setFlag(Flag.USE, false);

		// Should change / remove this
		setIntFlagAtt(FlagAtt.EXPLOSION_BLOCK_DROP, 100);
	}

	public WorldRegion(final String worldName, final Rights rights, final int priority, final Flags flags, final FlagAttributes flagAtts) {
		super(worldName, RegionType.WORLD, rights, priority, flags, flagAtts);
	}

	@Override
	public boolean contains(final NLocation loc) {
		return getWorldName().equals(loc.getWorldName());
	}

	@Override
	public String getRegionName() {
		return "world_" + getWorldName();
	}

	@Override
	public long getTotalSize() {
		return Long.MAX_VALUE;
	}
}
