package com.github.ribesg.ncuboid.events.extensions;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.event.player.PlayerMoveEvent;

import com.github.ribesg.ncuboid.beans.CuboidDB;
import com.github.ribesg.ncuboid.beans.PlayerCuboid;
import com.github.ribesg.ncuboid.events.AbstractExtendedEvent;

public class ExtendedPlayerMoveEvent extends AbstractExtendedEvent {

    @Getter private final PlayerCuboid      fromCuboid;
    @Getter private final Set<PlayerCuboid> fromCuboids;
    @Getter private final PlayerCuboid      toCuboid;
    @Getter private final Set<PlayerCuboid> toCuboids;
    @Setter private boolean                 customCancelled;

    public ExtendedPlayerMoveEvent(final PlayerMoveEvent event) {
        super(event);
        fromCuboids = CuboidDB.getInstance().getAllByLoc(event.getFrom());
        fromCuboid = CuboidDB.getInstance().getPrior(fromCuboids);
        toCuboids = CuboidDB.getInstance().getAllByLoc(event.getTo());
        toCuboid = CuboidDB.getInstance().getPrior(toCuboids);
        customCancelled = false;
    }

    public boolean isCustomCancelled() {
        return customCancelled;
    }

}
