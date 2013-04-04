package fr.ribesg.bukkit.ncuboid.events.extensions;

import java.util.Set;

import lombok.Getter;

import org.bukkit.event.player.PlayerTeleportEvent;

import fr.ribesg.bukkit.ncuboid.beans.CuboidDB;
import fr.ribesg.bukkit.ncuboid.beans.PlayerCuboid;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;

public class ExtendedPlayerTeleportEvent extends AbstractExtendedEvent {

    @Getter private final PlayerCuboid      fromCuboid;
    @Getter private final Set<PlayerCuboid> fromCuboids;
    @Getter private final PlayerCuboid      toCuboid;
    @Getter private final Set<PlayerCuboid> toCuboids;

    public ExtendedPlayerTeleportEvent(final CuboidDB db, final PlayerTeleportEvent event) {
        super(event);
        fromCuboids = db.getAllByLoc(event.getFrom());
        fromCuboid = db.getPrior(fromCuboids);
        toCuboids = db.getAllByLoc(event.getTo());
        toCuboid = db.getPrior(toCuboids);
    }

}
