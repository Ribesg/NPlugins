package fr.ribesg.bukkit.ncuboid.listeners.flag;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.beans.GeneralCuboid;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;

public class FireFlagListener extends AbstractListener {

    public FireFlagListener(final NCuboid instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockBurn(final BlockBurnEvent event) {
        final GeneralCuboid cuboid = getPlugin().getDb().getPriorByLoc(event.getBlock().getLocation());
        if (cuboid != null && cuboid.getFlag(Flag.FIRE)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockIgnite(final BlockIgniteEvent event) {
        if (event.getCause() != IgniteCause.FLINT_AND_STEEL) {
            final GeneralCuboid cuboid = getPlugin().getDb().getPriorByLoc(event.getBlock().getLocation());
            if (cuboid != null && cuboid.getFlag(Flag.FIRE)) {
                event.setCancelled(true);
            }
        }
    }
}
