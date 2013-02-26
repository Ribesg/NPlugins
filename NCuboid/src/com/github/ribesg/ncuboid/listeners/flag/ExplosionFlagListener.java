package com.github.ribesg.ncuboid.listeners.flag;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.github.ribesg.ncuboid.NCuboid;
import com.github.ribesg.ncuboid.beans.Flag;
import com.github.ribesg.ncuboid.events.extensions.ExtendedEntityExplodeEvent;
import com.github.ribesg.ncuboid.listeners.AbstractListener;

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
