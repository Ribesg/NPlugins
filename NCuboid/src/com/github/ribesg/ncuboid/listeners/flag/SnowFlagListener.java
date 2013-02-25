package com.github.ribesg.ncuboid.listeners.flag;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockFormEvent;

import com.github.ribesg.ncore.nodes.cuboid.beans.Flag;
import com.github.ribesg.ncuboid.NCuboid;
import com.github.ribesg.ncuboid.beans.CuboidDB;
import com.github.ribesg.ncuboid.beans.PlayerCuboid;
import com.github.ribesg.ncuboid.listeners.AbstractListener;

public class SnowFlagListener extends AbstractListener {

    public SnowFlagListener(final NCuboid instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockForm(final BlockFormEvent event) {
        final BlockState newState = event.getNewState();
        if (newState.getType() == Material.SNOW || newState.getType() == Material.ICE) {
            final PlayerCuboid c = CuboidDB.getInstance().getPriorByLoc(event.getBlock().getLocation());
            if (c != null && c.getFlag(Flag.SNOW)) {
                event.setCancelled(true);
            }
        }
    }
}
