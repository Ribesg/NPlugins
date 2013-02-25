package com.github.ribesg.ncuboid.events.extensions;

import lombok.Getter;

import org.bukkit.event.player.PlayerDropItemEvent;

import com.github.ribesg.ncuboid.beans.CuboidDB;
import com.github.ribesg.ncuboid.beans.PlayerCuboid;
import com.github.ribesg.ncuboid.events.AbstractExtendedEvent;

public class ExtendedPlayerDropItemEvent extends AbstractExtendedEvent {

    @Getter PlayerCuboid playerCuboid;

    public ExtendedPlayerDropItemEvent(final PlayerDropItemEvent event) {
        super(event);
        playerCuboid = CuboidDB.getInstance().getPriorByLoc(event.getPlayer().getLocation());
    }

}
