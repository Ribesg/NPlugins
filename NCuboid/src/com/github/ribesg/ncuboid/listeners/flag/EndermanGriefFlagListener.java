package com.github.ribesg.ncuboid.listeners.flag;

import org.bukkit.Material;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import com.github.ribesg.ncuboid.NCuboid;
import com.github.ribesg.ncuboid.beans.CuboidDB;
import com.github.ribesg.ncuboid.beans.Flag;
import com.github.ribesg.ncuboid.beans.PlayerCuboid;
import com.github.ribesg.ncuboid.listeners.AbstractListener;

public class EndermanGriefFlagListener extends AbstractListener {

    public EndermanGriefFlagListener(final NCuboid instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityChangeBlock(final EntityChangeBlockEvent event) {
        if (event.getEntityType() == EntityType.ENDERMAN) {
            final PlayerCuboid cuboid = CuboidDB.getInstance().getPriorByLoc(event.getBlock().getLocation());
            if (cuboid != null && cuboid.getFlag(Flag.ENDERMAN)) {
                ((Enderman) event.getEntity()).setCarriedMaterial(Material.RED_ROSE.getNewData((byte) 0));
                event.setCancelled(true);
            }
        }
    }
}
