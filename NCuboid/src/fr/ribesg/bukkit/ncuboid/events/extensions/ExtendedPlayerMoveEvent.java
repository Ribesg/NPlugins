package fr.ribesg.bukkit.ncuboid.events.extensions;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.event.player.PlayerMoveEvent;

import fr.ribesg.bukkit.ncuboid.beans.CuboidDB;
import fr.ribesg.bukkit.ncuboid.beans.PlayerCuboid;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;

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
