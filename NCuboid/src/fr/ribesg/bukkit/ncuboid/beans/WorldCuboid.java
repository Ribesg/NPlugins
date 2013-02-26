package fr.ribesg.bukkit.ncuboid.beans;

import org.bukkit.Location;
import org.bukkit.World;


// This is a really different Cuboid type.
public class WorldCuboid extends GeneralCuboid {

    public WorldCuboid(final World world) {
        super(world, CuboidType.WORLD);
    }

    public WorldCuboid(
            final World world,
            final Rights rights,
            final int priority,
            final Flags flags,
            final FlagAttributes flagAtts) {
        super(world,
                CuboidType.WORLD,
                rights,
                priority,
                flags,
                flagAtts);
    }

    @Override
    public boolean contains(final Location loc) {
        return getWorld().getName().equals(loc.getWorld().getName());
    }
}
