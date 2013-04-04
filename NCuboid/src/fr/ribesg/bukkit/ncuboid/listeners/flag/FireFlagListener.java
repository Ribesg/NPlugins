package fr.ribesg.bukkit.ncuboid.listeners.flag;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockSpreadEvent;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.beans.PlayerCuboid;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;

public class FireFlagListener extends AbstractListener {

    public FireFlagListener(final NCuboid instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockBurn(final BlockBurnEvent event) {
        final PlayerCuboid cuboid = getPlugin().getDb().getPriorByLoc(event.getBlock().getLocation());
        if (cuboid != null && cuboid.getFlag(Flag.FIRE)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockSpread(final BlockSpreadEvent event) {
        if (event.getBlock().getType() == Material.FIRE) {
            final PlayerCuboid cuboid = getPlugin().getDb().getPriorByLoc(event.getBlock().getLocation());
            if (cuboid != null && cuboid.getFlag(Flag.FIRE)) {
                event.setCancelled(true);
            }
        }
    }
}
