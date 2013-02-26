package fr.ribesg.bukkit.ncuboid.listeners.flag;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityExplodeEvent;

import fr.ribesg.bukkit.ncuboid.NCuboid;
import fr.ribesg.bukkit.ncuboid.beans.Flag;
import fr.ribesg.bukkit.ncuboid.events.extensions.ExtendedEntityExplodeEvent;
import fr.ribesg.bukkit.ncuboid.listeners.AbstractListener;

public class ExplosionFlagListener extends AbstractListener {

    public ExplosionFlagListener(final NCuboid instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityExplode(final ExtendedEntityExplodeEvent ext) {
        final EntityExplodeEvent event = (EntityExplodeEvent) ext.getBaseEvent();
        for (final Block b : ext.getBlockCuboidsMap().keySet()) {
            if (ext.getBlockCuboidsMap().get(b).getFlag(Flag.EXPLOSION)) {
                event.blockList().remove(b);
            }
        }
    }
}
