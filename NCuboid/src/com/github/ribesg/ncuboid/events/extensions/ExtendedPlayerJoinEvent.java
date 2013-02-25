package com.github.ribesg.ncuboid.events.extensions;

import java.util.Set;

import lombok.Getter;

import org.bukkit.event.player.PlayerJoinEvent;

import com.github.ribesg.ncuboid.beans.CuboidDB;
import com.github.ribesg.ncuboid.beans.PlayerCuboid;
import com.github.ribesg.ncuboid.events.AbstractExtendedEvent;

public class ExtendedPlayerJoinEvent extends AbstractExtendedEvent {

    @Getter private final PlayerCuboid      cuboid;
    @Getter private final Set<PlayerCuboid> cuboids;

    public ExtendedPlayerJoinEvent(final PlayerJoinEvent event) {
        super(event);
        cuboids = CuboidDB.getInstance().getAllByLoc(event.getPlayer().getLocation());
        cuboid = CuboidDB.getInstance().getPrior(cuboids);
    }

}
