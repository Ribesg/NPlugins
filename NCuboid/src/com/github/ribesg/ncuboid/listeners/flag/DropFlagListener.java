package com.github.ribesg.ncuboid.listeners.flag;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerDropItemEvent;

import com.github.ribesg.ncore.nodes.cuboid.beans.Flag;
import com.github.ribesg.ncuboid.NCuboid;
import com.github.ribesg.ncuboid.events.extensions.ExtendedPlayerDropItemEvent;
import com.github.ribesg.ncuboid.listeners.AbstractListener;

public class DropFlagListener extends AbstractListener {

    public DropFlagListener(final NCuboid instance) {
        super(instance);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerDropItem(final ExtendedPlayerDropItemEvent ext) {
        final PlayerDropItemEvent event = (PlayerDropItemEvent) ext.getBaseEvent();
        if (ext.getPlayerCuboid() != null && ext.getPlayerCuboid().getFlag(Flag.DROP) && !ext.getPlayerCuboid().isAllowedPlayer(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
