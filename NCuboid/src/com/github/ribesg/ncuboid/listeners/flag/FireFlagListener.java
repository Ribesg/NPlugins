package com.github.ribesg.ncuboid.listeners.flag;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockSpreadEvent;

import com.github.ribesg.ncuboid.NCuboid;
import com.github.ribesg.ncuboid.beans.CuboidDB;
import com.github.ribesg.ncuboid.beans.Flag;
import com.github.ribesg.ncuboid.beans.PlayerCuboid;
import com.github.ribesg.ncuboid.listeners.AbstractListener;

public class FireFlagListener extends AbstractListener {

    public FireFlagListener(final NCuboid instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockBurn(final BlockBurnEvent event) {
        final PlayerCuboid cuboid = CuboidDB.getInstance().getPriorByLoc(event.getBlock().getLocation());
        if (cuboid != null && cuboid.getFlag(Flag.FIRE)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockSpread(final BlockSpreadEvent event) {
        if (event.getBlock().getType() == Material.FIRE) {
            final PlayerCuboid cuboid = CuboidDB.getInstance().getPriorByLoc(event.getBlock().getLocation());
            if (cuboid != null && cuboid.getFlag(Flag.FIRE)) {
                event.setCancelled(true);
            }
        }
    }
}
