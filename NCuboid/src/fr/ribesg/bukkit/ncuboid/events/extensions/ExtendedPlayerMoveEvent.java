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

    public ExtendedPlayerMoveEvent(final CuboidDB db, final PlayerMoveEvent event) {
        super(event);
        fromCuboids = db.getAllByLoc(event.getFrom());
        fromCuboid = db.getPrior(fromCuboids);
        toCuboids = db.getAllByLoc(event.getTo());
        toCuboid = db.getPrior(toCuboids);
        customCancelled = false;
    }

    public boolean isCustomCancelled() {
        return customCancelled;
    }

}
