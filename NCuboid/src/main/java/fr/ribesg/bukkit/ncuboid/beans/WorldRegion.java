package fr.ribesg.bukkit.ncuboid.beans;

import fr.ribesg.bukkit.ncore.common.NLocation;

// This is a really different Region type.
public class WorldRegion extends GeneralRegion {

	public WorldRegion(final String worldName) {
		super(worldName, RegionType.WORLD);

		// Default flags are a little different for worlds
		// TODO Make this configurable
		setFlag(Flag.BUILD, false);
		setFlag(Flag.CHEST, false);
		setFlag(Flag.USE, false);

		// Should change / remove this
		setIntFlagAtt(FlagAtt.EXPLOSION_BLOCK_DROP, 100);
	}

	public WorldRegion(final String worldName, final Rights rights, final int priority, final Flags flags, final FlagAttributes flagAtts) {
		super(worldName, RegionType.WORLD, rights, priority, flags, flagAtts, false);
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
