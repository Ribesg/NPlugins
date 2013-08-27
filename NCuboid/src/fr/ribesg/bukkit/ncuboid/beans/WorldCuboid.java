package fr.ribesg.bukkit.ncuboid.beans;

import fr.ribesg.bukkit.ncore.common.NLocation;

// This is a really different Cuboid type.
public class WorldCuboid extends GeneralCuboid {

	public WorldCuboid(final String worldName) {
		super(worldName, CuboidType.WORLD);

		// Default flags are a little different for worlds
		// TODO Make this configurable
		setFlag(Flag.BUILD, false);
		setFlag(Flag.CHEST, false);
		setFlag(Flag.USE, false);
		setIntFlagAtt(FlagAtt.EXPLOSION_BLOCK_DROP, 100);
	}

	public WorldCuboid(final String worldName, final Rights rights, final int priority, final Flags flags, final FlagAttributes flagAtts) {
		super(worldName, CuboidType.WORLD, rights, priority, flags, flagAtts);
	}

	@Override
	public boolean contains(final NLocation loc) {
		return getWorldName().equals(loc.getWorldName());
	}

	@Override
	public String getCuboidName() {
		return "world_" + getWorldName();
	}

	@Override
	public long getTotalSize() {
		return Long.MAX_VALUE;
	}
}
