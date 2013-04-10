package fr.ribesg.bukkit.ncuboid.events.extensions;

import java.util.Set;

import lombok.Getter;

import org.bukkit.event.player.PlayerTeleportEvent;

import fr.ribesg.bukkit.ncuboid.beans.CuboidDB;
import fr.ribesg.bukkit.ncuboid.beans.GeneralCuboid;
import fr.ribesg.bukkit.ncuboid.events.AbstractExtendedEvent;

public class ExtendedPlayerTeleportEvent extends AbstractExtendedEvent {

    @Getter private final GeneralCuboid      fromCuboid;
    @Getter private final Set<GeneralCuboid> fromCuboids;
    @Getter private final GeneralCuboid      toCuboid;
    @Getter private final Set<GeneralCuboid> toCuboids;

    public ExtendedPlayerTeleportEvent(final CuboidDB db, final PlayerTeleportEvent event) {
        super(event);
        fromCuboids = db.getAllByLoc(event.getFrom());
        fromCuboid = db.getPrior(fromCuboids);
        toCuboids = db.getAllByLoc(event.getTo());
        toCuboid = db.getPrior(toCuboids);
    }

}
